package com.filey.domain;

public class Editorial {
    private String nombre;
    private String contacto;

    public Editorial(String nombre, String contacto) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la editorial es obligatorio.");
        }
        if (contacto == null || contacto.trim().isEmpty()) {
            throw new IllegalArgumentException("El contacto de la editorial es obligatorio.");
        }
        this.nombre = nombre.trim();
        this.contacto = contacto.trim();
    }

    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
}
