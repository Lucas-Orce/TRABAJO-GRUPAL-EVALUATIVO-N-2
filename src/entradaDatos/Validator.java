package entradaDatos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {

    public static int validarInt(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        while (!ingreso.hasNextInt()) {
            System.out.print("Debes ingresar un numero valido: ");
            ingreso.next();
        }
        return ingreso.nextInt();
    }

    public static String validarSN(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        String entrada;

        while (true) {
            System.out.print("Ingrese 's' para si o 'n' para no: ");
            entrada = ingreso.nextLine().trim().toLowerCase();

            if (entrada.equals("s") || entrada.equals("n")) {
                return entrada;
            } else {
                System.out.println("Entrada invalida. Solo se permite 's' o 'n'.");
            }
        }
    }

    public static int validarIntRango(int min, int max, String mensaje) {
        int num;
        do {
            num = validarInt(mensaje);
            if (num < min || num > max) {
                System.out.print("Ingrese un valor entre " + min + " y " + max + ": ");
            }
        } while (num < min || num > max);
        return num;
    }

    public static double validarDouble(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        while (!ingreso.hasNextDouble()) {
            System.out.print("Ingrese un numero decimal valido: ");
            ingreso.next();
        }
        return ingreso.nextDouble();
    }

    public static double validarDoublePos(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        double numero = -1;

        while (true) {
            if (ingreso.hasNextDouble()) {
                numero = ingreso.nextDouble();
                if (numero > 0) {
                    return numero;
                } else {
                    System.out.print("Ingrese un numero decimal positivo: ");
                }
            } else {
                System.out.print("Ingrese un numero decimal valido: ");
                ingreso.next();
            }
        }
    }

    public static float validarFloat(String mensaje) {
        System.out.println(mensaje);       
        Scanner ingreso = new Scanner(System.in);
        while (!ingreso.hasNextFloat()) {
            System.out.print("Ingrese un numero decimal valido: ");
            ingreso.next();
        }
        return ingreso.nextFloat();
    }

    public static float validarFloatPos(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        float numero = -1;

        while (true) {
            if (ingreso.hasNextFloat()) {
                numero = ingreso.nextFloat();
                if (numero > 0) {
                    return numero;
                } else {
                    System.out.print("Ingrese un numero decimal positivo: ");
                }
            } else {
                System.out.print("Ingrese un numero decimal valido: ");
                ingreso.next();
            }
        }
    }

    public static String validarTexto(String mensaje) {
        Scanner ingreso = new Scanner(System.in);
        String texto;

        do {
            System.out.print(mensaje);
            texto = ingreso.nextLine().trim(); // elimina espacios adelante y atrás
            if (texto.isEmpty()) {
                System.out.println("Entrada inválida. No puede estar vacía ni contener solo espacios.");
            }
        } while (texto.isEmpty());

        return texto;
    }


    public static String validarPatente(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);

        String formatoViejo = "^[A-Z]{3}[0-9]{3}$";
        String formatoNuevo = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$";
        String patente = "";

        while (true) {
            System.out.print("Ingresa una patente [Formato: 'ABC123' - 'AB123CD']:  ");
            patente = ingreso.nextLine().toUpperCase().replaceAll("\\s+", "");

            if (Pattern.matches(formatoViejo, patente) || Pattern.matches(formatoNuevo, patente)) {
                System.out.println("Patente valida.");
                break;
            } else {
                System.out.println("Patente invalida [Formato: 'ABC123' - 'AB123CD']");
            }

        }
        return patente;
    }

    public static LocalDate validarFormatoFecha() {
        Scanner ingreso = new Scanner(System.in);
        LocalDate fecha = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.print("Ingrese la fecha (formato: aaaa-mm-dd): ");
            String input = ingreso.nextLine();
            try {
                fecha = LocalDate.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha invalido. Intente nuevamente.");
            }
        }
        return fecha;
    }

    public static LocalTime validarFormatoHora() {
        Scanner ingreso = new Scanner(System.in);
        LocalTime hora = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (true) {
            System.out.print("Ingrese la hora (formato 24hs: HH:mm): ");
            String input = ingreso.nextLine();
            try {
                hora = LocalTime.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora invalido. Intente nuevamente.");
            }
        }
        return hora;
    }

    public static LocalDate validarFecha() {
        Scanner scanner = new Scanner(System.in);
        LocalDate fecha = null;

        while (true) {
            try {
                System.out.print("Ingrese la fecha (YYYY-MM-DD): ");
                String entrada = scanner.nextLine();
                fecha = LocalDate.parse(entrada);

                if (!fecha.isBefore(LocalDate.now())) {
                    return fecha;
                } else {
                    System.out.println("La fecha no puede ser anterior a hoy.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Ingrese una fecha con formato YYYY-MM-DD.");
            }
        }
    }

    public static String validarCUIL() {
        Scanner ingreso = new Scanner(System.in);
        String cuil;
        String[] tiposValidos = {"20", "23", "24", "25", "26", "27", "30", "33", "34"};

        while (true) {
            System.out.print("Ingrese el CUIL/CUIT (solo numeros, sin guiones ni espacios - ej: 20123456783): ");
            cuil = ingreso.nextLine().trim();

            // Verifica que tenga exactamente 11 caracteres numericos
            if (!cuil.matches("\\d{11}")) {
                System.out.println("El CUIL/CUIT debe contener exactamente 11 digitos numericos.");
                continue;
            }

            // Verifica que los primeros 2 digitos esten dentro de los tipos validos
            String tipo = cuil.substring(0, 2);
            boolean tipoEsValido = false;
            for (String valido : tiposValidos) {
                if (tipo.equals(valido)) {
                    tipoEsValido = true;
                    break;
                }
            }

            if (!tipoEsValido) {
                System.out.println("Tipo de CUIL/CUIT invalido. Debe comenzar con: 20, 23, 24, 25, 26, 27, 30, 33 o 34.");
                continue;
            }
            return cuil;
        }
    }

    public static int validarDNI(String mensaje) {
        System.out.println(mensaje);
        Scanner ingreso = new Scanner(System.in);
        int dni = -1;

        while (true) {
            System.out.print("Ingrese el numero de DNI [sin puntos, entre 1.000.000 y 99.999.999]: ");

            if (ingreso.hasNextInt()) {
                dni = ingreso.nextInt();

                if (dni >= 1000000 && dni <= 99999999) {
                    return dni;
                } else {
                    System.out.println("El DNI debe estar entre 1.000.000 y 99.999.999.");
                }
            } else {
                System.out.println("Entrada invalida. Debe ingresar solo numeros.");
                ingreso.next(); // limpiar entrada invalida
            }
        }
    }

}
