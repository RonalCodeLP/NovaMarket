package com.upeu.ordenms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CrearVentaRequest {

    @NotNull
    private String cajeroUsername;

    private Double descuento;

    @NotNull
    private MedioPago medioPago;

    /** Efectivo: monto entregado por el cliente */
    private Double montoRecibido;

    /** Tarjeta: débito o crédito */
    private TipoTarjeta tipoTarjeta;

    /** Tarjeta: autorización del datáfono (6 dígitos) */
    private String codigoAutorizacion;

    /** Yape: código de operación del cliente (7 dígitos) */
    private String codigoOperacion;

    @NotEmpty
    @Valid
    private List<VentaItemRequest> items;
}
