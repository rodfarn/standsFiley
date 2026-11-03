package com.filey.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private final Stand stand;
    private final Editorial editorial;
    private final LocalDate fechaReserva;
    private final LocalDate fechaVencimiento;
    private final LocalDate fechaLimiteProntoPago;
    private final LocalDate fechaLimiteLiquidacion;
    private final List<Pago> pagos = new ArrayList<>();
    private boolean descuentoAplicado;

    public Reserva(Stand stand, Editorial editorial, LocalDate fecha, Configuracion configuracion) {
        if (stand == null || editorial == null || fecha == null || configuracion == null) {
            throw new IllegalArgumentException("Los datos de la reserva son obligatorios.");
        }
        this.stand = stand;
        this.editorial = editorial;
        fechaReserva = fecha;
        fechaVencimiento = fecha.plusDays(configuracion.getDiasVigenciaReserva());
        fechaLimiteProntoPago = fecha.plusDays(configuracion.getDiasProntoPago());
        fechaLimiteLiquidacion = fecha.plusDays(configuracion.getDiasMaximosLiquidacion());
    }

    public void registrarPago(BigDecimal importe, LocalDate fecha, Configuracion configuracion) {
        if (importe == null || importe.signum() <= 0) {
            throw new IllegalArgumentException("El importe del pago debe ser mayor que cero.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del pago es obligatoria.");
        }
        if (fecha.isAfter(fechaLimiteLiquidacion)) {
            throw new IllegalStateException("La fecha máxima para liquidar el stand ya venció.");
        }
        if (pagado().add(importe).compareTo(total(fecha, configuracion)) > 0) {
            throw new IllegalArgumentException("El pago supera el saldo pendiente.");
        }
        pagos.add(new Pago(importe.setScale(2, RoundingMode.HALF_UP), fecha));
        if (!fecha.isAfter(fechaLimiteProntoPago)) {
            descuentoAplicado = true;
        }
    }

    public BigDecimal total(LocalDate fecha, Configuracion configuracion) {
        BigDecimal precio = stand.getPrecio();
        if (descuentoAplicado || !fecha.isAfter(fechaLimiteProntoPago)) {
            BigDecimal descuento = precio
                    .multiply(configuracion.getPorcentajeDescuentoProntoPago())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            return precio.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
        }
        return precio;
    }

    public BigDecimal pagado() {
        BigDecimal totalPagado = BigDecimal.ZERO;
        for (int i = 0; i < pagos.size(); i++) {
            totalPagado = totalPagado.add(pagos.get(i).getImporte());
        }
        return totalPagado.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal saldo(LocalDate fecha, Configuracion configuracion) {
        return total(fecha, configuracion).subtract(pagado()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal porcentajePagado(LocalDate fecha, Configuracion configuracion) {
        return pagado()
                .multiply(new BigDecimal("100"))
                .divide(total(fecha, configuracion), 2, RoundingMode.HALF_UP);
    }

    public boolean alcanzaPorcentajeMinimo(LocalDate fecha, Configuracion configuracion) {
        return porcentajePagado(fecha, configuracion)
                .compareTo(configuracion.getPorcentajeMinimoOcupado()) >= 0;
    }

    public Stand getStand() {
        return stand;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDate getFechaLimiteProntoPago() {
        return fechaLimiteProntoPago;
    }

    public LocalDate getFechaLimiteLiquidacion() {
        return fechaLimiteLiquidacion;
    }

    public List<Pago> getPagos() {
        return new ArrayList<Pago>(pagos);
    }
}
