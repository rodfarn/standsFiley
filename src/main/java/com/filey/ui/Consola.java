package com.filey.ui;

import com.filey.domain.Configuracion;
import com.filey.domain.Editorial;
import com.filey.domain.Reserva;
import com.filey.domain.Stand;
import com.filey.notification.Notificacion;
import com.filey.service.SistemaFiley;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Consola {
    private final Scanner entrada = new Scanner(System.in);
    private final SistemaFiley sistema = new SistemaFiley();

    public void iniciar() {
        System.out.println("=== Sistema Administrativo de Stands FILEY ===");
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            try {
                continuar = ejecutar(entrada.nextLine().trim());
            } catch (IllegalArgumentException | IllegalStateException error) {
                System.out.println("Error: " + error.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("1. Configurar costos y plazos");
        System.out.println("2. Registrar stand");
        System.out.println("3. Mostrar stands");
        System.out.println("4. Reservar stand");
        System.out.println("5. Bloquear stand");
        System.out.println("6. Desbloquear stand");
        System.out.println("7. Transferir bloqueado a reserva");
        System.out.println("8. Registrar pago");
        System.out.println("9. Revisar notificaciones");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private boolean ejecutar(String opcion) {
        switch (opcion) {
            case "1": configurar(); break;
            case "2": registrarStand(); break;
            case "3": mostrarStands(); break;
            case "4": reservar(false); break;
            case "5": sistema.bloquear(leerTexto("Identificador del stand: ")); break;
            case "6": sistema.desbloquear(leerTexto("Identificador del stand: ")); break;
            case "7": reservar(true); break;
            case "8": registrarPago(); break;
            case "9": mostrarNotificaciones(); break;
            case "0": return false;
            default: System.out.println("Opción no válida.");
        }
        return true;
    }

    private void configurar() {
        sistema.getConfiguracion().actualizar(
                leerDecimal("Precio por m²: "),
                leerEntero("Días de vigencia de reserva: "),
                leerEntero("Días máximos para liquidar: "),
                leerEntero("Días de pronto pago: "),
                leerDecimal("Porcentaje mínimo para ocupar: "),
                leerDecimal("Porcentaje de descuento: "),
                leerEnteroNoNegativo("Días de anticipación de avisos: "));
        System.out.println("Configuración guardada.");
    }

    private void registrarStand() {
        Stand stand = sistema.registrarStand(
                leerTexto("Identificador: "),
                leerDecimal("Ancho en metros: "),
                leerDecimal("Largo en metros: "));
        System.out.printf(
                "Stand registrado: %s | Área: %s m² | Precio: $%s%n",
                stand.getIdentificador(),
                stand.metrosCuadrados(),
                stand.getPrecio());
    }

    private void mostrarStands() {
        if (sistema.getStands().isEmpty()) {
            System.out.println("No hay stands registrados.");
            return;
        }
        for (Stand stand : sistema.getStands()) {
            System.out.printf(
                    "%s | %s | %s m² | $%s%n",
                    stand.getIdentificador(),
                    stand.getEstado(),
                    stand.metrosCuadrados(),
                    stand.getPrecio());
            if (stand.getReserva() != null) {
                Reserva reserva = stand.getReserva();
                System.out.printf(
                        "  Editorial: %s | Pagado: $%s | Saldo: $%s%n",
                        reserva.getEditorial().getNombre(),
                        reserva.pagado(),
                        reserva.saldo(LocalDate.now(), sistema.getConfiguracion()));
            }
        }
    }

    private void reservar(boolean transferencia) {
        String identificador = leerTexto("Identificador del stand: ");
        Editorial editorial = new Editorial(
                leerTexto("Editorial: "),
                leerTexto("Contacto: "));
        LocalDate fecha = leerFecha("Fecha de reserva (AAAA-MM-DD): ");
        Reserva reserva = transferencia
                ? sistema.transferirBloqueadoAReserva(identificador, editorial, fecha)
                : sistema.reservar(identificador, editorial, fecha);
        System.out.println("Reserva creada. Vence: " + reserva.getFechaVencimiento());
    }

    private void registrarPago() {
        sistema.registrarPago(
                leerTexto("Identificador del stand: "),
                leerDecimal("Importe: "),
                leerFecha("Fecha del pago (AAAA-MM-DD): "));
        System.out.println("Pago registrado correctamente.");
    }

    private void mostrarNotificaciones() {
        java.util.List<Notificacion> notificaciones = sistema.revisarNotificaciones(LocalDate.now());
        if (notificaciones.isEmpty()) {
            System.out.println("No hay notificaciones nuevas.");
            return;
        }
        for (Notificacion notificacion : notificaciones) {
            System.out.println("NOTIFICACIÓN: " + notificacion.getMensaje());
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String valor = entrada.nextLine().trim();
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor es obligatorio.");
        }
        return valor;
    }

    private BigDecimal leerDecimal(String mensaje) {
        try {
            return new BigDecimal(leerTexto(mensaje));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Ingrese un número válido.");
        }
    }

    private int leerEntero(String mensaje) {
        int valor = leerEnteroNoNegativo(mensaje);
        if (valor <= 0) {
            throw new IllegalArgumentException("Ingrese un entero mayor que cero.");
        }
        return valor;
    }

    private int leerEnteroNoNegativo(String mensaje) {
        try {
            int valor = Integer.parseInt(leerTexto(mensaje));
            if (valor < 0) {
                throw new IllegalArgumentException("Ingrese un entero no negativo.");
            }
            return valor;
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Ingrese un entero válido.");
        }
    }

    private LocalDate leerFecha(String mensaje) {
        try {
            return LocalDate.parse(leerTexto(mensaje));
        } catch (DateTimeParseException error) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD.");
        }
    }
}
