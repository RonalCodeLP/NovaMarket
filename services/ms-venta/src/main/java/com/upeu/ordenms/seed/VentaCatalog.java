package com.upeu.ordenms.seed;

import com.upeu.ordenms.dto.MedioPago;
import java.util.List;

public final class VentaCatalog {

    public record VentaLineSeed(int productoId, String nombre, double precioUnitario, int cantidad) {}

    public record VentaSeed(
            int diasAtras,
            int horasAtras,
            MedioPago medioPago,
            double descuento,
            List<VentaLineSeed> lineas) {

        public double subtotal() {
            return lineas.stream().mapToDouble(l -> l.precioUnitario() * l.cantidad()).sum();
        }

        public double total() {
            return Math.max(0, subtotal() - descuento);
        }
    }

    /** Artículo id = (rubro - 1) * 12 + índice (1..12), alineado con ArticuloDataSeeder. */
    public static int pid(int rubro, int indice) {
        return (rubro - 1) * 12 + indice;
    }

    public static final List<VentaSeed> VENTAS = List.of(
            venta(0, 2, MedioPago.EFECTIVO, 0,
                    line(pid(13, 11), "Inca Kola 500 ml", 2.50, 2),
                    line(pid(19, 1), "Papitas Lay's clásicas 140 g", 4.90, 1)),
            venta(0, 5, MedioPago.YAPE, 0,
                    line(pid(1, 1), "Azúcar blanca Cartavio 1 kg", 4.90, 1),
                    line(pid(2, 1), "Arroz Costeño Extra 1 kg", 4.50, 2)),
            venta(1, 1, MedioPago.TARJETA, 0,
                    line(pid(7, 1), "Leche Gloria UHT entera 1 L", 4.80, 2),
                    line(pid(12, 3), "Pan de molde Bimbo blanco", 7.90, 1)),
            venta(1, 4, MedioPago.EFECTIVO, 0.50,
                    line(pid(4, 1), "Fideos spaghetti Don Vittorio 400 g", 3.50, 3),
                    line(pid(6, 5), "Ketchup Hellmann's 397 g", 9.50, 1)),
            venta(2, 0, MedioPago.EFECTIVO, 0,
                    line(pid(24, 1), "Carne molida res kg", 24.90, 1),
                    line(pid(23, 4), "Tomate kg", 4.50, 1)),
            venta(2, 3, MedioPago.YAPE, 0,
                    line(pid(25, 2), "Pechuga de pollo kg", 16.90, 1),
                    line(pid(5, 1), "Aceite Primor vegetal 1 L", 9.90, 1)),
            venta(3, 2, MedioPago.TARJETA, 0,
                    line(pid(16, 2), "Cerveza Pilsen 630 ml", 6.50, 6)),
            venta(3, 6, MedioPago.EFECTIVO, 0,
                    line(pid(20, 10), "Chocolate Sublime clásico", 2.20, 5),
                    line(pid(21, 1), "Galletas Oreo 108 g", 4.50, 2)),
            venta(4, 1, MedioPago.YAPE, 0,
                    line(pid(14, 1), "Agua San Luis sin gas 625 ml", 1.50, 6),
                    line(pid(15, 1), "Jugo Gloria naranja 1 L", 5.90, 1)),
            venta(4, 4, MedioPago.EFECTIVO, 0,
                    line(pid(11, 1), "Jamón del país San Fernando 250 g", 9.90, 1),
                    line(pid(10, 1), "Huevos rojos x12", 11.90, 1)),
            venta(5, 0, MedioPago.TARJETA, 1.00,
                    line(pid(29, 1), "Detergente Ariel polvo 1 kg", 14.90, 1),
                    line(pid(30, 6), "Pasta dental Colgate 75 ml", 5.90, 2)),
            venta(5, 3, MedioPago.EFECTIVO, 0,
                    line(pid(22, 1), "Atún Florida aceite 170 g", 6.90, 3),
                    line(pid(3, 1), "Lentejas Costeño 500 g", 4.20, 1)),
            venta(6, 2, MedioPago.YAPE, 0,
                    line(pid(27, 4), "Nuggets San Fernando 400 g", 14.90, 1),
                    line(pid(28, 1), "Helado D'Onofrio vainilla 1 L", 18.90, 1)),
            venta(7, 1, MedioPago.EFECTIVO, 0,
                    line(pid(23, 1), "Papa amarilla kg", 3.50, 2),
                    line(pid(23, 7), "Lechuga americana unidad", 3.50, 1),
                    line(pid(23, 11), "Banano kg", 3.90, 1)),
            venta(7, 5, MedioPago.TARJETA, 0,
                    line(pid(17, 6), "Vino tinto Tabernero 750 ml", 22.90, 1),
                    line(pid(18, 5), "Pisco Queirolo 750 ml", 32.90, 1)),
            venta(8, 0, MedioPago.EFECTIVO, 0,
                    line(pid(8, 1), "Yogurt Gloria fresa 1 kg", 8.90, 1),
                    line(pid(9, 1), "Queso fresco Laive 500 g", 12.90, 1)),
            venta(8, 4, MedioPago.YAPE, 0,
                    line(pid(32, 1), "Alimento perro Ringo adulto 2 kg", 18.90, 1)),
            venta(9, 2, MedioPago.EFECTIVO, 0,
                    line(pid(33, 1), "Vasos descartables 50 u", 4.90, 2),
                    line(pid(33, 4), "Servilletas Elite 100 u", 3.90, 1)),
            venta(10, 1, MedioPago.TARJETA, 0,
                    line(pid(34, 1), "Paracetamol 500 mg x20", 4.90, 2),
                    line(pid(34, 7), "Suero oral Electrolit 625 ml", 3.90, 3)),
            venta(11, 3, MedioPago.EFECTIVO, 0,
                    line(pid(13, 1), "Inca Kola 2.25 L", 7.90, 1),
                    line(pid(19, 4), "Doritos nacho 140 g", 5.20, 2),
                    line(pid(19, 7), "Maní salado 200 g", 3.50, 1)),
            venta(12, 0, MedioPago.YAPE, 0,
                    line(pid(26, 3), "Filete de tilapia kg", 19.90, 1),
                    line(pid(6, 1), "Mayonesa Alacena 475 g", 8.90, 1)),
            venta(13, 2, MedioPago.EFECTIVO, 0,
                    line(pid(31, 1), "Pañal Huggies RN 34 u", 32.90, 1)),
            venta(14, 4, MedioPago.TARJETA, 2.00,
                    line(pid(35, 1), "Pilas AA Duracell x4", 9.90, 1),
                    line(pid(35, 4), "Foco LED 9W", 6.90, 2)),
            venta(15, 1, MedioPago.EFECTIVO, 0,
                    line(pid(2, 2), "Arroz Paisana Superior 5 kg", 19.90, 1),
                    line(pid(1, 3), "Harina Blanca Flor 1 kg", 4.20, 1)),
            venta(20, 0, MedioPago.YAPE, 0,
                    line(pid(21, 5), "Cereal Zucaritas 300 g", 9.90, 1),
                    line(pid(7, 5), "Leche evaporada Gloria 410 ml", 4.20, 2),
                    line(pid(12, 1), "Pan francés unidad", 0.50, 8))
    );

    private VentaCatalog() {}

    private static VentaLineSeed line(int productoId, String nombre, double precio, int cantidad) {
        return new VentaLineSeed(productoId, nombre, precio, cantidad);
    }

    private static VentaSeed venta(int dias, int horas, MedioPago medio, double descuento, VentaLineSeed... lineas) {
        return new VentaSeed(dias, horas, medio, descuento, List.of(lineas));
    }
}
