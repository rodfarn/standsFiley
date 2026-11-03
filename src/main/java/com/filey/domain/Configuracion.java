package com.filey.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Configuracion {
    private BigDecimal precioPorMetroCuadrado = dinero("1500");
    private int diasVigenciaReserva = 15;
    private int diasMaximosLiquidacion = 60;
    private int diasProntoPago = 7;
    private int diasAnticipacionNotificacion = 3;
    private BigDecimal porcentajeMinimoOcupado = porcentaje("50");
    private BigDecimal porcentajeDescuentoProntoPago = porcentaje("10");

    public void actualizar(
            BigDecimal precio,
            int vigencia,
            int liquidacion,
            int prontoPago,
            BigDecimal minimo,
            BigDecimal descuento,
            int anticipacion) {
        validarPositivo(precio, "El precio por metro cuadrado");
        validarEnteroPositivo(vigencia, "Los días de vigencia");
        validarEnteroPositivo(liquidacion, "Los días máximos de liquidación");
        validarEnteroPositivo(prontoPago, "Los días de pronto pago");
        validarPorcentaje(minimo, "El porcentaje mínimo de pago");
        validarPorcentaje(descuento, "El porcentaje de descuento");
        if (anticipacion < 0) {
            throw new IllegalArgumentException("Los días de anticipación no pueden ser negativos.");
        }
        if (liquidacion < vigencia) {
            throw new IllegalArgumentException(
                    "El plazo de liquidación debe cubrir la vigencia de la reserva.");
        }

        precioPorMetroCuadrado = precio.setScale(2, RoundingMode.HALF_UP);
        diasVigenciaReserva = vigencia;
        diasMaximosLiquidacion = liquidacion;
        diasProntoPago = prontoPago;
        diasAnticipacionNotificacion = anticipacion;
        porcentajeMinimoOcupado = minimo.setScale(2, RoundingMode.HALF_UP);
        porcentajeDescuentoProntoPago = descuento.setScale(2, RoundingMode.HALF_UP);
    }

    private static void validarPositivo(BigDecimal valor, String nombre) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException(nombre + " debe ser mayor que cero.");
        }
    }

    private static void validarEnteroPositivo(int valor, String nombre) {
        if (valor <= 0) {
            throw new IllegalArgumentException(nombre + " debe ser mayor que cero.");
        }
    }

    private static void validarPorcentaje(BigDecimal valor, String nombre) {
        if (valor == null || valor.signum() < 0 || valor.compareTo(porcentaje("100")) > 0) {
            throw new IllegalArgumentException(nombre + " debe estar entre 0 y 100.");
        }
    }

    private static BigDecimal dinero(String valor) {
        return new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal porcentaje(String valor) {
        return new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPrecioPorMetroCuadrado() {
        return precioPorMetroCuadrado;
    }

    public int getDiasVigenciaReserva() {
        return diasVigenciaReserva;
    }

    public int getDiasMaximosLiquidacion() {
        return diasMaximosLiquidacion;
    }

    public int getDiasProntoPago() {
        return diasProntoPago;
    }

    public BigDecimal getPorcentajeMinimoOcupado() {
        return porcentajeMinimoOcupado;
    }

    public BigDecimal getPorcentajeDescuentoProntoPago() {
        return porcentajeDescuentoProntoPago;
    }

    public int getDiasAnticipacionNotificacion() {
        return diasAnticipacionNotificacion;
    }
}
