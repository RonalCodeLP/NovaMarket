package com.upeu.pagoms.config;

import com.upeu.pagoms.dto.MedioPago;
import com.upeu.pagoms.entidad.Pago;
import com.upeu.pagoms.repositorio.PagoRepositorio;
import com.upeu.pagoms.seed.PagoVentaCatalog;
import com.upeu.pagoms.seed.PagoVentaCatalog.PagoVentaSeed;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagoDataSeeder implements CommandLineRunner {

    private static final String ESTADO_PAGADO = "PAGADO";

    private final PagoRepositorio pagoRepositorio;

    @Override
    @Transactional
    public void run(String... args) {
        if (pagoRepositorio.count() > 0) {
            log.info("Pagos ya existen ({}). Seeder omitido.", pagoRepositorio.count());
            return;
        }

        int count = 0;
        for (PagoVentaSeed seed : PagoVentaCatalog.PAGOS) {
            Instant fecha = Instant.now()
                    .minus(seed.diasAtras(), ChronoUnit.DAYS)
                    .minus(seed.horasAtras(), ChronoUnit.HOURS);

            pagoRepositorio.save(Pago.builder()
                    .ventaId(seed.ventaId())
                    .ordenId(seed.ventaId())
                    .monto(seed.monto())
                    .medioPago(seed.medioPago())
                    .montoRecibido(seed.montoRecibido())
                    .vuelto(seed.vuelto())
                    .estado(ESTADO_PAGADO)
                    .fechaPago(fecha)
                    .build());
            count++;
        }

        log.info("Seeder pagos: {} pagos vinculados a ventas de demostración.", count);
    }
}
