package com.filey.notification;

import com.filey.domain.Configuracion;
import com.filey.domain.Reserva;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServicioNotificaciones {
    private final Set<String> eventos = new HashSet<>();

    public List<Notificacion> revisar(
            List<Reserva> reservas,
            Configuracion configuracion,
            LocalDate fecha) {
        List<Notificacion> resultado = new ArrayList<>();
        for (Reserva reserva : reservas) {
            revisarFecha(
                    reserva,
                    "RESERVA",
                    reserva.getFechaVencimiento(),
                    "La reserva del stand %s de %s está próxima a vencer el %s.",
                    configuracion,
                    fecha,
                    resultado);
            revisarFecha(
                    reserva,
                    "DESCUENTO",
                    reserva.getFechaLimiteProntoPago(),
                    "El plazo de descuento por pronto pago del stand %s vence el %s.",
                    configuracion,
                    fecha,
                    resultado);
            revisarFecha(
                    reserva,
                    "LIQUIDACION",
                    reserva.getFechaLimiteLiquidacion(),
                    "La fecha máxima para liquidar el stand %s vence el %s.",
                    configuracion,
                    fecha,
                    resultado);
        }
        return resultado;
    }

    private void revisarFecha(
            Reserva reserva,
            String tipo,
            LocalDate vencimiento,
            String formato,
            Configuracion configuracion,
            LocalDate fecha,
            List<Notificacion> resultado) {
        long dias = ChronoUnit.DAYS.between(fecha, vencimiento);
        String clave = reserva.getStand().getIdentificador() + "-" + tipo + "-" + vencimiento;
        if (dias >= 0
                && dias <= configuracion.getDiasAnticipacionNotificacion()
                && eventos.add(clave)) {
            String mensaje = tipo.equals("RESERVA")
                    ? String.format(
                            formato,
                            reserva.getStand().getIdentificador(),
                            reserva.getEditorial().getNombre(),
                            vencimiento)
                    : String.format(formato, reserva.getStand().getIdentificador(), vencimiento);
            resultado.add(new Notificacion(clave, mensaje, fecha));
        }
    }
}
