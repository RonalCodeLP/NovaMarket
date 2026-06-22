package com.upeu.pagoms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegistrarPagoRequest {

    @NotNull
    private Long ventaId;

    @NotNull
    @Positive
    private Double monto;

    @NotNull
    private MedioPago medioPago;

    /** Efectivo: monto entregado por el cliente */
    private Double montoRecibido;

    /** Tarjeta: débito o crédito */
    private TipoTarjeta tipoTarjeta;

    /** Tarjeta: código del datáfono (6 dígitos). Si no se envía, se genera en demo */
    private String codigoAutorizacion;

    /** Yape: código de operación del comprobante del cliente (7 dígitos) */
    private String codigoOperacion;
}
