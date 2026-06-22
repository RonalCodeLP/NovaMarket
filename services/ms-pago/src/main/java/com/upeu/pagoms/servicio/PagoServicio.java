package com.upeu.pagoms.servicio;

import com.upeu.pagoms.dto.MedioPago;
import com.upeu.pagoms.dto.PagoResponse;
import com.upeu.pagoms.dto.RegistrarPagoRequest;
import com.upeu.pagoms.entidad.Pago;
import com.upeu.pagoms.repositorio.PagoRepositorio;
import com.upeu.pagoms.servicio.PagoProcesador.ResultadoPago;
import com.upeu.pagoms.util.DineroUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoServicio {

    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String MONEDA_PEN = "PEN";

    private final PagoRepositorio pagoRepositorio;
    private final PagoProcesador pagoProcesador;

    public List<Pago> listarPagos() {
        return pagoRepositorio.findAll();
    }

    public Optional<Pago> buscarPagoPorId(Long id) {
        return pagoRepositorio.findById(id);
    }

    public Optional<Pago> buscarPorVentaId(Long ventaId) {
        return pagoRepositorio.findByVentaId(ventaId);
    }

    @Transactional
    public PagoResponse registrar(RegistrarPagoRequest request) {
        if (pagoRepositorio.findByVentaId(request.getVentaId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La venta #" + request.getVentaId() + " ya tiene un pago registrado");
        }

        ResultadoPago resultado = pagoProcesador.procesar(request);

        Pago pago = Pago.builder()
                .ventaId(request.getVentaId())
                .ordenId(request.getVentaId())
                .monto(DineroUtil.aDouble(resultado.monto()))
                .medioPago(request.getMedioPago())
                .montoRecibido(DineroUtil.aDouble(resultado.montoRecibido()))
                .vuelto(DineroUtil.aDouble(resultado.vuelto()))
                .estado(ESTADO_PAGADO)
                .codigoAutorizacion(resultado.codigoAutorizacion())
                .referenciaTransaccion(resultado.referenciaTransaccion())
                .tipoTarjeta(resultado.tipoTarjeta())
                .codigoOperacion(resultado.codigoOperacion())
                .moneda(MONEDA_PEN)
                .fechaPago(Instant.now())
                .build();

        Pago guardado = pagoRepositorio.save(pago);
        log.info("Pago {} registrado venta={} medio={} ref={}",
                guardado.getId(), guardado.getVentaId(), guardado.getMedioPago(), guardado.getReferenciaTransaccion());
        return toResponse(guardado);
    }

    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .ventaId(pago.getVentaId() != null ? pago.getVentaId() : pago.getOrdenId())
                .monto(pago.getMonto())
                .medioPago(pago.getMedioPago())
                .montoRecibido(pago.getMontoRecibido())
                .vuelto(pago.getVuelto())
                .estado(pago.getEstado())
                .codigoAutorizacion(pago.getCodigoAutorizacion())
                .referenciaTransaccion(pago.getReferenciaTransaccion())
                .tipoTarjeta(pago.getTipoTarjeta())
                .codigoOperacion(pago.getCodigoOperacion())
                .moneda(pago.getMoneda())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
