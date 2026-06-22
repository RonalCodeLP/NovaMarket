package com.upeu.pagoms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DineroUtil {

    private DineroUtil() {}

    public static BigDecimal de(Double valor) {
        if (valor == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public static double aDouble(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static BigDecimal vuelto(BigDecimal recibido, BigDecimal total) {
        return recibido.subtract(total).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}
