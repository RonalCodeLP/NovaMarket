package com.upeu.pagoms.entidad;

import com.upeu.pagoms.dto.MedioPago;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "venta_id")
    private Long ventaId;

    /** @deprecated use ventaId */
    private Long ordenId;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    private Double montoRecibido;

    private Double vuelto;

    private String estado;

    /** Código de autorización datáfono (tarjeta) o operación Yape */
    private String codigoAutorizacion;

    private String referenciaTransaccion;

    @Enumerated(EnumType.STRING)
    private com.upeu.pagoms.dto.TipoTarjeta tipoTarjeta;

    /** Código de operación Yape (7 dígitos) */
    private String codigoOperacion;

    private String moneda;

    private Instant fechaPago;
}
