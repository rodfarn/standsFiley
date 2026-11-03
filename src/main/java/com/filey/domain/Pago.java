package com.filey.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pago {
    private BigDecimal importe;
    private LocalDate fecha;

    public Pago(BigDecimal importe, LocalDate fecha) {
        if (importe == null || importe.signum() <= 0) {
            throw new IllegalArgumentException("El importe del pago debe ser mayor que cero.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del pago es obligatoria.");
        }
        this.importe = importe;
        this.fecha = fecha;
    }

    public BigDecimal getImporte() { return importe; }
    public LocalDate getFecha() { return fecha; }
}
