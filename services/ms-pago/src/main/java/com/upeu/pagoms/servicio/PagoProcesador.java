package com.upeu.pagoms.servicio;

import com.upeu.pagoms.dto.MedioPago;
import com.upeu.pagoms.dto.RegistrarPagoRequest;
import com.upeu.pagoms.dto.TipoTarjeta;
import com.upeu.pagoms.util.DineroUtil;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Validaciones y datos de comprobante al estilo POS retail (Perú).
 */
@Component
public class PagoProcesador {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter REF_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("America/Lima"));

    public record ResultadoPago(
            BigDecimal monto,
            BigDecimal montoRecibido,
            BigDecimal vuelto,
            String codigoAutorizacion,
            String referenciaTransaccion,
            TipoTarjeta tipoTarjeta,
            String codigoOperacion) {}

    public ResultadoPago procesar(RegistrarPagoRequest request) {
        BigDecimal total = DineroUtil.de(request.getMonto());
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto de la venta debe ser mayor a cero");
        }

        return switch (request.getMedioPago()) {
            case EFECTIVO -> procesarEfectivo(request, total);
            case TARJETA -> procesarTarjeta(request, total);
            case YAPE -> procesarYape(request, total);
        };
    }

    private ResultadoPago procesarEfectivo(RegistrarPagoRequest request, BigDecimal total) {
        if (request.getMontoRecibido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indique el monto recibido en efectivo");
        }
        BigDecimal recibido = DineroUtil.de(request.getMontoRecibido());
        if (recibido.compareTo(total) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(Locale.US,
                            "Monto insuficiente. Total S/ %.2f, recibido S/ %.2f",
                            DineroUtil.aDouble(total), DineroUtil.aDouble(recibido)));
        }
        BigDecimal vuelto = DineroUtil.vuelto(recibido, total);
        String referencia = referenciaEfectivo(request.getVentaId());
        return new ResultadoPago(total, recibido, vuelto, null, referencia, null, null);
    }

    private ResultadoPago procesarTarjeta(RegistrarPagoRequest request, BigDecimal total) {
        TipoTarjeta tipo = request.getTipoTarjeta();
        if (tipo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleccione débito o crédito para el cobro con tarjeta");
        }
        String auth = request.getCodigoAutorizacion();
        if (auth == null || !auth.matches("\\d{6}")) {
            auth = generarCodigoAutorizacion();
        }
        String referencia = referenciaTarjeta(request.getVentaId(), tipo);
        return new ResultadoPago(total, total, BigDecimal.ZERO.setScale(2), auth, referencia, tipo, null);
    }

    private ResultadoPago procesarYape(RegistrarPagoRequest request, BigDecimal total) {
        String codigo = normalizarCodigoYape(request.getCodigoOperacion());
        if (codigo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ingrese el código de operación Yape (6 dígitos del comprobante del cliente)");
        }
        String referencia = "YAPE-" + REF_FMT.format(Instant.now()) + "-" + request.getVentaId();
        return new ResultadoPago(total, total, BigDecimal.ZERO.setScale(2), codigo, referencia, null, codigo);
    }

    public static String normalizarCodigoYape(String codigo) {
        if (codigo == null) {
            return null;
        }
        String limpio = codigo.replaceAll("\\D", "");
        return limpio.length() == 6 ? limpio : null;
    }

    public static String generarCodigoAutorizacion() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    private String referenciaEfectivo(Long ventaId) {
        return "EFE-" + REF_FMT.format(Instant.now()) + "-" + ventaId;
    }

    private String referenciaTarjeta(Long ventaId, TipoTarjeta tipo) {
        String prefijo = tipo == TipoTarjeta.DEBITO ? "TDD" : "TDC";
        return prefijo + "-" + REF_FMT.format(Instant.now()) + "-" + ventaId;
    }
}
