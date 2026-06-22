package com.upeu.producto.util;

public final class CodigoBarrasUtil {

    private CodigoBarrasUtil() {
    }

    public static String normalizar(String codigoBarras) {
        if (codigoBarras == null) {
            return null;
        }
        String limpio = codigoBarras.replaceAll("[^0-9]", "");
        return limpio.isEmpty() ? null : limpio;
    }

    public static boolean esValido(String codigoBarras) {
        String limpio = normalizar(codigoBarras);
        if (limpio == null) {
            return true;
        }
        int len = limpio.length();
        return len >= 8 && len <= 13;
    }

    /** EAN-13 Perú (prefijo 775) para datos iniciales de inventario. */
    public static String generarEan13(int rubroId, int secuencia) {
        String base12 = String.format("775%02d%07d", rubroId, secuencia);
        int total = 0;
        for (int i = 0; i < 12; i++) {
            int digito = base12.charAt(i) - '0';
            total += digito * (i % 2 == 0 ? 1 : 3);
        }
        int check = (10 - (total % 10)) % 10;
        return base12 + check;
    }
}
