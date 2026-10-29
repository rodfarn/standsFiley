 # Sistema Administrativo de Stands FILEY

## 1. Descripción del proyecto

Sistema administrativo para gestionar la venta de stands de la Feria Internacional de la Lectura Yucatán (FILEY). Permitirá administrar espacios, reservas, pagos, descuentos y notificaciones sobre fechas próximas a vencer.

El sistema se desarrollará en Java utilizando programación orientada a objetos (POO) y podrá ejecutarse desde la línea de comandos.

## 2. Requisitos funcionales (RF)

Los requisitos funcionales describen las acciones que debe realizar el sistema.

| ID | Requisito funcional | Prioridad |
|---|---|---|
| RF-01 | El sistema debe permitir registrar stands con identificador, precio y dimensiones. | Alta |
| RF-02 | El sistema debe calcular el precio del stand según sus metros cuadrados. | Alta |
| RF-03 | El sistema debe mostrar los stands disponibles, reservados, ocupados y bloqueados. | Alta |
| RF-04 | El sistema debe permitir reservar un stand para una editorial y establecer una fecha de vencimiento. | Alta |
| RF-05 | El sistema debe permitir bloquear y desbloquear stands desde la administración. | Alta |
| RF-06 | El sistema debe permitir configurar los costos, plazos y porcentajes de pago. | Alta |
| RF-07 | El sistema debe registrar los pagos de las editoriales y calcular el saldo pendiente. | Alta 
| RF-08 | El sistema debe aplicar descuentos por pronto pago cuando corresponda. | Alta |
| RF-09 | El sistema debe cambiar una reserva a ocupada cuando se alcance el porcentaje mínimo de pago establecido. | Alta |
| RF-10 | El sistema debe controlar la fecha límite para liquidar el costo del stand. | Alta |
| RF-11 | El sistema debe notificar a las editoriales cuando sus reservas estén próximas a vencer. | Alta |
| RF-12 | El sistema debe notificar cuando esté por vencer el plazo del descuento por pronto pago. | Alta |
| RF-13 | El sistema debe notificar cuando esté próxima la fecha límite para liquidar el stand. | Alta |
| RF-14 | El sistema debe validar los datos ingresados para evitar valores incorrectos. | Alta |
| RF-15 | El sistema debe mostrar las notificaciones en la consola. | Alta |

## 3. Requisitos no funcionales (RNF)

Los requisitos no funcionales describen las condiciones que debe cumplir el sistema.

| ID | Requisito no funcional|
|---|---|
| RNF-01 | El sistema debe desarrollarse en Java utilizando programación orientada a objetos.
| RNF-02 | El sistema debe funcionar desde la línea de comandos. 
| RNF-03 | El código debe organizarse en paquetes para separar las clases de negocio y las notificaciones.
| RNF-04 | El sistema debe evitar generar repetidamente la misma notificación. 
| RNF-05 | El código debe ser fácil de modificar y mantener. 
| RNF-06 | El sistema debe mostrar menús y opciones con nombres claros y fáciles de entender. |
| RNF-07 | El sistema debe presentar mensajes de error comprensibles para facilitar la corrección de datos. |
| RNF-08 | El sistema debe permitir navegar por sus opciones mediante el teclado. |

