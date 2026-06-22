package com.upeu.pagoms.seed;

import com.upeu.pagoms.dto.MedioPago;
import java.util.List;

/**
 * Pagos alineados con {@code VentaCatalog} de ms-venta (mismos totales y fechas).
 */
public final class PagoVentaCatalog {

    public record PagoVentaSeed(
            long ventaId,
            int diasAtras,
            int horasAtras,
            MedioPago medioPago,
            double monto,
            Double montoRecibido,
            Double vuelto) {}

    public static final List<PagoVentaSeed> PAGOS = List.of(
            pago(1, 0, 2, MedioPago.EFECTIVO, 9.90, 10.0, 0.10),
            pago(2, 0, 5, MedioPago.YAPE, 13.90, null, null),
            pago(3, 1, 1, MedioPago.TARJETA, 17.50, null, null),
            pago(4, 1, 4, MedioPago.EFECTIVO, 19.50, 20.0, 0.50),
            pago(5, 2, 0, MedioPago.EFECTIVO, 29.40, 30.0, 0.60),
            pago(6, 2, 3, MedioPago.YAPE, 26.80, null, null),
            pago(7, 3, 2, MedioPago.TARJETA, 39.00, null, null),
            pago(8, 3, 6, MedioPago.EFECTIVO, 20.00, 20.0, 0.0),
            pago(9, 4, 1, MedioPago.YAPE, 14.90, null, null),
            pago(10, 4, 4, MedioPago.EFECTIVO, 21.80, 22.0, 0.20),
            pago(11, 5, 0, MedioPago.TARJETA, 25.70, null, null),
            pago(12, 5, 3, MedioPago.EFECTIVO, 24.90, 25.0, 0.10),
            pago(13, 6, 2, MedioPago.YAPE, 33.80, null, null),
            pago(14, 7, 1, MedioPago.EFECTIVO, 14.40, 15.0, 0.60),
            pago(15, 7, 5, MedioPago.TARJETA, 55.80, null, null),
            pago(16, 8, 0, MedioPago.EFECTIVO, 21.80, 22.0, 0.20),
            pago(17, 8, 4, MedioPago.YAPE, 18.90, null, null),
            pago(18, 9, 2, MedioPago.EFECTIVO, 13.70, 14.0, 0.30),
            pago(19, 10, 1, MedioPago.TARJETA, 21.50, null, null),
            pago(20, 11, 3, MedioPago.EFECTIVO, 21.80, 22.0, 0.20),
            pago(21, 12, 0, MedioPago.YAPE, 28.80, null, null),
            pago(22, 13, 2, MedioPago.EFECTIVO, 32.90, 33.0, 0.10),
            pago(23, 14, 4, MedioPago.TARJETA, 21.70, null, null),
            pago(24, 15, 1, MedioPago.EFECTIVO, 24.10, 25.0, 0.90),
            pago(25, 20, 0, MedioPago.YAPE, 22.30, null, null)
    );

    private PagoVentaCatalog() {}

    private static PagoVentaSeed pago(long ventaId, int dias, int horas, MedioPago medio,
                                      double monto, Double recibido, Double vuelto) {
        return new PagoVentaSeed(ventaId, dias, horas, medio, monto, recibido, vuelto);
    }
}
