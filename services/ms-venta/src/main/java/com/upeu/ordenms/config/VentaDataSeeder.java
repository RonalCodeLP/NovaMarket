package com.upeu.ordenms.config;

import com.upeu.ordenms.dto.MedioPago;
import com.upeu.ordenms.entidad.Orden;
import com.upeu.ordenms.entidad.OrdenDetalle;
import com.upeu.ordenms.repositorio.OrdenRepositorio;
import com.upeu.ordenms.seed.VentaCatalog;
import com.upeu.ordenms.seed.VentaCatalog.VentaLineSeed;
import com.upeu.ordenms.seed.VentaCatalog.VentaSeed;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class VentaDataSeeder implements CommandLineRunner {

    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String CAJERO = "cajero";

    private final OrdenRepositorio ordenRepositorio;

    @Override
    @Transactional
    public void run(String... args) {
        if (ordenRepositorio.count() > 0) {
            log.info("Ventas ya existen ({}). Seeder omitido.", ordenRepositorio.count());
            return;
        }

        int count = 0;
        for (VentaSeed seed : VentaCatalog.VENTAS) {
            guardarVenta(seed);
            count++;
        }

        log.info("Seeder ventas: {} ventas de demostración cargadas.", count);
    }

    private void guardarVenta(VentaSeed seed) {
        List<OrdenDetalle> detalles = new ArrayList<>();
        for (VentaLineSeed linea : seed.lineas()) {
            double subtotalLinea = linea.precioUnitario() * linea.cantidad();
            detalles.add(OrdenDetalle.builder()
                    .productoId(linea.productoId())
                    .productoNombre(linea.nombre())
                    .cantidad(linea.cantidad())
                    .precioUnitario(linea.precioUnitario())
                    .subtotal(subtotalLinea)
                    .build());
        }

        double total = seed.total();
        Double montoRecibido = null;
        Double vuelto = null;
        if (seed.medioPago() == MedioPago.EFECTIVO) {
            montoRecibido = Math.ceil(total);
            vuelto = montoRecibido - total;
        }

        Instant fecha = Instant.now()
                .minus(seed.diasAtras(), ChronoUnit.DAYS)
                .minus(seed.horasAtras(), ChronoUnit.HOURS);

        Orden orden = Orden.builder()
                .cajeroUsername(CAJERO)
                .subtotal(seed.subtotal())
                .descuento(seed.descuento())
                .total(total)
                .estado(ESTADO_PAGADO)
                .medioPago(seed.medioPago().name())
                .montoRecibido(montoRecibido)
                .vuelto(vuelto)
                .fechaVenta(fecha)
                .build();

        for (OrdenDetalle detalle : detalles) {
            detalle.setOrden(orden);
            orden.getDetalles().add(detalle);
        }

        Orden guardada = ordenRepositorio.save(orden);
        guardada.setNumeroBoleta(String.format("NM-%08d", guardada.getId()));
        ordenRepositorio.save(guardada);
    }
}
