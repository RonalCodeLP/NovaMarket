package com.upeu.ordenms.servicio;

import com.upeu.ordenms.cliente.PagoVentaClient;
import com.upeu.ordenms.cliente.ProductoVentaClient;
import com.upeu.ordenms.dto.CrearVentaRequest;
import com.upeu.ordenms.dto.MedioPago;
import com.upeu.ordenms.dto.PagoRegistradoDto;
import com.upeu.ordenms.dto.ProductoVentaDto;
import com.upeu.ordenms.dto.VentaDetalleResponse;
import com.upeu.ordenms.dto.VentaItemRequest;
import com.upeu.ordenms.dto.TipoTarjeta;
import com.upeu.ordenms.dto.VentaResponse;
import com.upeu.ordenms.entidad.Orden;
import com.upeu.ordenms.entidad.OrdenDetalle;
import com.upeu.ordenms.evento.EventoOrden;
import com.upeu.ordenms.repositorio.OrdenRepositorio;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaServicio {

    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String TIPO_EVENTO_ORDEN_CREADA = "orden.creada";

    private final OrdenRepositorio ordenRepositorio;
    private final ProductoVentaClient productoVentaClient;
    private final PagoVentaClient pagoVentaClient;
    private final ProductorOrden productorOrden;

    @Value("${spring.application.name}")
    private String applicationName;

    @Transactional(readOnly = true)
    public List<VentaResponse> listarVentas() {
        return ordenRepositorio.findAllByOrderByFechaVentaDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse obtenerVenta(Long id) {
        Orden orden = ordenRepositorio.findDetalleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        return toResponse(orden);
    }

    @Transactional
    public VentaResponse crearVenta(CrearVentaRequest request) {
        validarPago(request);

        double subtotal = 0;
        List<OrdenDetalle> detalles = new ArrayList<>();

        for (VentaItemRequest item : request.getItems()) {
            ProductoVentaDto producto = productoVentaClient.obtenerProducto(item.getProductoId());
            if (producto.getStock() == null || producto.getStock() < item.getCantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para " + producto.getNombre());
            }
            double precio = producto.getPrecio() != null ? producto.getPrecio().doubleValue() : 0;
            double linea = precio * item.getCantidad();
            subtotal += linea;

            detalles.add(OrdenDetalle.builder()
                    .productoId(producto.getId())
                    .productoNombre(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(precio)
                    .subtotal(linea)
                    .build());
        }

        double descuento = request.getDescuento() != null ? request.getDescuento() : 0;
        double total = Math.max(0, subtotal - descuento);

        Orden orden = Orden.builder()
                .cajeroUsername(request.getCajeroUsername())
                .subtotal(subtotal)
                .descuento(descuento)
                .total(total)
                .estado(ESTADO_PAGADO)
                .medioPago(request.getMedioPago().name())
                .montoRecibido(request.getMontoRecibido())
                .fechaVenta(Instant.now())
                .build();

        for (OrdenDetalle detalle : detalles) {
            detalle.setOrden(orden);
            orden.getDetalles().add(detalle);
        }

        Orden guardada = ordenRepositorio.save(orden);
        guardada.setNumeroBoleta(generarNumeroBoleta(guardada.getId()));
        if (request.getMedioPago() == MedioPago.EFECTIVO && request.getMontoRecibido() != null) {
            guardada.setVuelto(Math.max(0, request.getMontoRecibido() - total));
        }
        guardada = ordenRepositorio.save(guardada);

        PagoRegistradoDto pago = pagoVentaClient.registrar(
                new PagoVentaClient.RegistrarPagoFeignRequest(
                        guardada.getId(),
                        total,
                        request.getMedioPago(),
                        request.getMontoRecibido(),
                        request.getTipoTarjeta(),
                        request.getCodigoAutorizacion(),
                        request.getCodigoOperacion()
                )
        );

        for (VentaItemRequest item : request.getItems()) {
            productoVentaClient.descontarStock(item.getProductoId(), item.getCantidad());
        }

        guardada.setCodigoAutorizacion(pago.getCodigoAutorizacion());
        guardada.setReferenciaTransaccion(pago.getReferenciaTransaccion());
        if (pago.getTipoTarjeta() != null) {
            guardada.setTipoTarjeta(pago.getTipoTarjeta().name());
        }
        guardada.setCodigoOperacion(pago.getCodigoOperacion());
        guardada.setMonedaPago(pago.getMoneda());
        guardada = ordenRepositorio.save(guardada);

        publicarEventoOpcional(guardada);

        VentaResponse response = toResponse(guardada);
        response.setPagoId(pago.getId());
        response.setCodigoAutorizacion(pago.getCodigoAutorizacion());
        response.setReferenciaTransaccion(pago.getReferenciaTransaccion());
        response.setTipoTarjeta(pago.getTipoTarjeta());
        response.setCodigoOperacion(pago.getCodigoOperacion());
        response.setMonedaPago(pago.getMoneda());
        if (pago.getVuelto() != null) {
            response.setVuelto(pago.getVuelto());
        }
        if (pago.getMontoRecibido() != null && request.getMedioPago() != MedioPago.EFECTIVO) {
            response.setMontoRecibido(pago.getMontoRecibido());
        }
        return response;
    }

    /** Kafka es opcional en dev: la venta no debe fallar si el broker no está activo. */
    private void publicarEventoOpcional(Orden orden) {
        try {
            productorOrden.publicarOrdenCreada(EventoOrden.builder()
                    .tipoEvento(TIPO_EVENTO_ORDEN_CREADA)
                    .ordenId(orden.getId())
                    .total(orden.getTotal())
                    .estado(orden.getEstado())
                    .origen(applicationName)
                    .timestamp(Instant.now().toEpochMilli())
                    .build());
        } catch (Exception ex) {
            log.warn("Venta {} guardada; Kafka no disponible: {}", orden.getId(), ex.getMessage());
        }
    }

    private void validarPago(CrearVentaRequest request) {
        switch (request.getMedioPago()) {
            case EFECTIVO -> {
                if (request.getMontoRecibido() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Indique el monto recibido en efectivo");
                }
            }
            case TARJETA -> {
                if (request.getTipoTarjeta() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Seleccione débito o crédito");
                }
            }
            case YAPE -> {
                if (request.getCodigoOperacion() == null
                        || !request.getCodigoOperacion().replaceAll("\\D", "").matches("\\d{7}")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ingrese el código de operación Yape (7 dígitos)");
                }
            }
        }
    }

    private String generarNumeroBoleta(Long id) {
        return String.format("NM-%08d", id);
    }

    private VentaResponse toResponse(Orden orden) {
        List<VentaDetalleResponse> items = orden.getDetalles() == null ? List.of()
                : orden.getDetalles().stream()
                .map(d -> VentaDetalleResponse.builder()
                        .id(d.getId())
                        .productoId(d.getProductoId())
                        .productoNombre(d.getProductoNombre())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        MedioPago medioPago = null;
        if (orden.getMedioPago() != null) {
            try {
                medioPago = MedioPago.valueOf(orden.getMedioPago());
            } catch (IllegalArgumentException ignored) {
                // legacy rows
            }
        }

        TipoTarjeta tipoTarjeta = null;
        if (orden.getTipoTarjeta() != null) {
            try {
                tipoTarjeta = TipoTarjeta.valueOf(orden.getTipoTarjeta());
            } catch (IllegalArgumentException ignored) {
                // legacy rows
            }
        }

        return VentaResponse.builder()
                .id(orden.getId())
                .cajeroUsername(orden.getCajeroUsername())
                .subtotal(orden.getSubtotal())
                .descuento(orden.getDescuento())
                .total(orden.getTotal())
                .estado(orden.getEstado())
                .medioPago(medioPago)
                .montoRecibido(orden.getMontoRecibido())
                .vuelto(orden.getVuelto())
                .numeroBoleta(orden.getNumeroBoleta())
                .fechaVenta(orden.getFechaVenta())
                .codigoAutorizacion(orden.getCodigoAutorizacion())
                .referenciaTransaccion(orden.getReferenciaTransaccion())
                .tipoTarjeta(tipoTarjeta)
                .codigoOperacion(orden.getCodigoOperacion())
                .monedaPago(orden.getMonedaPago())
                .items(items)
                .build();
    }
}
