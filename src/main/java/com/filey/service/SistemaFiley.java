package com.filey.service;

import com.filey.domain.Configuracion;
import com.filey.domain.Editorial;
import com.filey.domain.EstadoStand;
import com.filey.domain.Reserva;
import com.filey.domain.Stand;
import com.filey.notification.Notificacion;
import com.filey.notification.ServicioNotificaciones;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SistemaFiley {
    private final Configuracion configuracion = new Configuracion();
    private final Map<String, Stand> stands = new LinkedHashMap<>();
    private final List<Reserva> reservas = new ArrayList<>();
    private final ServicioNotificaciones notificaciones = new ServicioNotificaciones();

    public Stand registrarStand(String identificador, BigDecimal ancho, BigDecimal largo) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del stand es obligatorio.");
        }
        String clave = identificador.trim();
        if (stands.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe un stand con ese identificador.");
        }
        Stand stand = new Stand(
                clave,
                ancho,
                largo,
                configuracion.getPrecioPorMetroCuadrado());
        stands.put(clave, stand);
        return stand;
    }

    public Reserva reservar(String identificador, Editorial editorial, LocalDate fecha) {
        Stand stand = obtener(identificador);
        if (stand.getEstado() != EstadoStand.DISPONIBLE) {
            throw new IllegalStateException("El stand no está disponible para reservar.");
        }
        Reserva reserva = new Reserva(stand, editorial, fecha, configuracion);
        reservas.add(reserva);
        stand.reservar(reserva);
        return reserva;
    }

    public Reserva transferirBloqueadoAReserva(
            String identificador,
            Editorial editorial,
            LocalDate fecha) {
        Stand stand = obtener(identificador);
        if (stand.getEstado() != EstadoStand.BLOQUEADO) {
            throw new IllegalStateException("Solo se puede transferir un stand bloqueado.");
        }
        Reserva reserva = new Reserva(stand, editorial, fecha, configuracion);
        reservas.add(reserva);
        stand.reservar(reserva);
        return reserva;
    }

    public void bloquear(String identificador) {
        obtener(identificador).bloquear();
    }

    public void desbloquear(String identificador) {
        obtener(identificador).desbloquear();
    }

    public void registrarPago(String identificador, BigDecimal importe, LocalDate fecha) {
        Stand stand = obtener(identificador);
        if (stand.getReserva() == null) {
            throw new IllegalStateException("El stand no tiene una reserva.");
        }
        Reserva reserva = stand.getReserva();
        reserva.registrarPago(importe, fecha, configuracion);
        if (reserva.alcanzaPorcentajeMinimo(fecha, configuracion)) {
            stand.marcarOcupado();
        }
    }

    public List<Notificacion> revisarNotificaciones(LocalDate fecha) {
        return notificaciones.revisar(reservas, configuracion, fecha);
    }

    private Stand obtener(String identificador) {
        if (identificador == null || !stands.containsKey(identificador.trim())) {
            throw new IllegalArgumentException("No existe un stand con ese identificador.");
        }
        return stands.get(identificador.trim());
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public Collection<Stand> getStands() {
        return new ArrayList<Stand>(stands.values());
    }

    public List<Reserva> getReservas() {
        return new ArrayList<Reserva>(reservas);
    }
}
