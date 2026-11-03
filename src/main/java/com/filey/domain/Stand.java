package com.filey.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stand {
    private final String identificador;
    private final BigDecimal ancho;
    private final BigDecimal largo;
    private final BigDecimal precio;
    private EstadoStand estado;
    private Reserva reserva;

    public Stand(
            String identificador,
            BigDecimal ancho,
            BigDecimal largo,
            BigDecimal precioPorMetroCuadrado) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del stand es obligatorio.");
        }
        validarDimension(ancho, "El ancho");
        validarDimension(largo, "El largo");
        if (precioPorMetroCuadrado == null || precioPorMetroCuadrado.signum() <= 0) {
            throw new IllegalArgumentException("El precio por metro cuadrado debe ser mayor que cero.");
        }

        this.identificador = identificador.trim();
        this.ancho = escala(ancho);
        this.largo = escala(largo);
        precio = metrosCuadrados()
                .multiply(precioPorMetroCuadrado)
                .setScale(2, RoundingMode.HALF_UP);
        estado = EstadoStand.DISPONIBLE;
    }

    private static void validarDimension(BigDecimal valor, String nombre) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException(nombre + " debe ser mayor que cero.");
        }
    }

    private static BigDecimal escala(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal metrosCuadrados() {
        return ancho.multiply(largo).setScale(2, RoundingMode.HALF_UP);
    }

    public void bloquear() {
        if (estado != EstadoStand.DISPONIBLE) {
            throw new IllegalStateException("Solo se puede bloquear un stand disponible.");
        }
        estado = EstadoStand.BLOQUEADO;
    }

    public void desbloquear() {
        if (estado != EstadoStand.BLOQUEADO) {
            throw new IllegalStateException("El stand no está bloqueado.");
        }
        estado = EstadoStand.DISPONIBLE;
    }

    public void reservar(Reserva reserva) {
        if (estado != EstadoStand.DISPONIBLE && estado != EstadoStand.BLOQUEADO) {
            throw new IllegalStateException("El stand no está disponible para reservar.");
        }
        this.reserva = reserva;
        estado = EstadoStand.RESERVADO;
    }

    public void marcarOcupado() {
        if (reserva == null) {
            throw new IllegalStateException("No se puede ocupar un stand sin reserva.");
        }
        estado = EstadoStand.OCUPADO;
    }

    public String getIdentificador() {
        return identificador;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public BigDecimal getLargo() {
        return largo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public EstadoStand getEstado() {
        return estado;
    }

    public Reserva getReserva() {
        return reserva;
    }
}
