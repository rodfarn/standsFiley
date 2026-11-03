package com.filey.notification;

import java.time.LocalDate;

public class Notificacion {
    private String clave;
    private String mensaje;
    private LocalDate fecha;

    public Notificacion(String clave, String mensaje, LocalDate fecha) {
        this.clave = clave;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getClave() { return clave; }
    public String getMensaje() { return mensaje; }
    public LocalDate getFecha() { return fecha; }
}
