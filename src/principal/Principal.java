/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import datos.Conductor;
import datos.Transporte;
import entradaDatos.*;
import persistencia.Archivo;
import persistencia.Registro;

/**
 *
 * @author Lucas
 */
public class Principal {

    private Archivo archivoConductores;
    private Archivo archivoTransportes;

    public void menuPrincipal() throws TransporteDuplicadoException, ConductorInexistenteException, ClassNotFoundException {
        int opcion;
        archivoConductores = new Archivo("Conductores.dat", new Conductor());
        do {
            System.out.println("---Menu Principal---");
            System.out.println("1- Alta conductores");
            System.out.println("2- Actualizcion de Transportes");
            System.out.println("3- Listado de sueldos");
            System.out.println("4- Listado de Transporte por Conductor");
            System.out.println("0- Salir");

            opcion = Validator.validarInt("Ingrse una opcion: ");

            switch (opcion) {
                case 1:
                    cargaConductor();
                    break;
                case 2:
                    menuTransporte();
                    break;
                case 3:
                    listadoSueldos();
                    break;
                case 4:
                    listadoTransporte();
                    break;
                case 0:
                    System.out.println("Saliendo del programa");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);

    }

    public static void main(String[] args) throws TransporteDuplicadoException, ConductorInexistenteException, ClassNotFoundException {
        Principal main = new Principal();
        main.menuPrincipal();
    }

    private void cargaConductor() {
        archivoConductores.abrirParaLeerEscribir();
        try {
            Conductor conductor = new Conductor();
            conductor.cargarDatos(0);

            if (existeConductor(conductor.getDni())) {
                System.out.println("Conductor Existente");
                return;
            }

            int numOrd = (int) conductor.tamRegistro();
            conductor.setNumOrd(numOrd);
            
            Registro regConductor = new Registro(conductor, conductor.getNumOrd());
            archivoConductores.cargarUnRegistro(regConductor);

            System.out.println("Conductor grabado con exito");

        } catch (Exception e) {
            System.out.println("Error al cargar un conductor" + e.getMessage());
        }
        archivoConductores.cerrarArchivo();
    }

    private void menuTransporte() throws TransporteDuplicadoException, ConductorInexistenteException, ClassNotFoundException {
        MenuTransporte menuT = new MenuTransporte(archivoConductores);
        menuT.menu();
    }

    private void listadoSueldos() {
        archivoConductores.abrirParaLectura();
        archivoConductores.irPrincipioArchivo();

        try {
            while (!archivoConductores.eof()) {
                Registro reg = archivoConductores.leerRegistro();
                reg.mostrarRegistro(1, true);
                if (reg.getEstado()) {
                    reg.mostrarRegistro(1, true);
                    Conductor conductor = (Conductor) reg.getDatos();
                    System.out.println("Conductor: " + conductor.getApe_Nom());
                    System.out.println("DNI: " + conductor.getDni());
                }
            }

        } catch (Exception e) {
            System.out.println("Error al listar los sueldos" + e.getMessage());
        }

    }

    private void listadoTransporte() {

        long dni = Validator.validarDNI("Ingrese el dni del conductor a listar: ");

        if (!existeConductor(dni)) {
            System.out.println("Conductor inexistente");
            return;
        }

        Conductor conductor = obtenerConductorPorDNI(dni);
        System.out.println("\nConductor; " + conductor.getApe_Nom());
        System.out.println("DNI: " + conductor.getDni());
        if (archivoTransportes != null) {
            archivoTransportes.abrirParaLectura();
            archivoTransportes.irPrincipioArchivo();

            try {
                boolean hayTransporte = false;
                for (int i = 0; i < 100; i++) {
                    archivoTransportes.buscarRegistro(i);
                    if (!archivoTransportes.eof()) {
                        Registro reg = archivoTransportes.leerRegistro();
                        if (reg.getEstado()) {
                            Transporte transporte = (Transporte) reg.getDatos();
                            if (transporte.getDniCond() == dni) {
                                System.out.println("Monto extra: " + String.format("%.2f", transporte.getExtra()));
                                hayTransporte = true;
                            }
                        }
                    }
                }
                if (!hayTransporte) {
                    System.out.println("No hay transportes ingresados en el archvo TRANSPORTES");
                }
            } catch (Exception e) {
                System.out.println("Error al listar los transportes: " + e.getMessage());
            }
            archivoTransportes.cerrarArchivo();
        } else {
            System.out.println("Archivo Transporte no inicializado!!!!");
        }

    }

    private boolean existeConductor(long dni) {
        archivoConductores.abrirParaLectura();
        archivoConductores.irPrincipioArchivo();

        try {
            for (int i = 0; i < 100; i++) {
                archivoConductores.buscarRegistro(i);
                if (!archivoConductores.eof()) {
                    Registro reg = archivoConductores.leerRegistro();
                    Conductor c = (Conductor) reg.getDatos();
                    if (reg.getEstado() && c.getDni() == dni) {
                        archivoConductores.cerrarArchivo();
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el conductor: " + e.getMessage());
        }
        archivoConductores.cerrarArchivo();;
        return false;
    }

    private double calcularSueldoConductor(long dni) {

        double sueldoFijo = 400000;
        double totalExtra = 0.0;

        archivoTransportes.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();

        try {
            for (int i = 0; i < 100; i++) {
                archivoTransportes.buscarRegistro(i);
                if (!archivoTransportes.eof()) {
                    Registro reg = archivoTransportes.leerRegistro();
                    if (reg.getEstado()) {
                        Transporte transporte = (Transporte) reg.getDatos();
                        if (transporte.getDniCond() == dni) {
                            totalExtra += transporte.getExtra();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al calcular el sueldo: " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
        return sueldoFijo + totalExtra;
    }

    private Conductor obtenerConductorPorDNI(long dni) {
        archivoConductores.abrirParaLectura();
        archivoConductores.irPrincipioArchivo();

        try {
            for (int i = 0; i < 100; i++) {
                archivoConductores.buscarRegistro(i);
                if (!archivoConductores.eof()) {
                    Registro reg = archivoConductores.leerRegistro();
                    if (reg.getEstado()) {
                        Conductor conductor = (Conductor) reg.getDatos();
                        if (conductor.getDni() == dni) {
                            archivoConductores.cerrarArchivo();
                            return conductor;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener el conductor deseado: " + e.getMessage());
        }
        archivoConductores.cerrarArchivo();
        return null;
    }

    /*
     if (archivoTransportes == null) {
                        double sueldo = calcularSueldoConductor(conductor.getDni());
                        System.out.println("Sueldo: " + String.format("%.2f", sueldo));

                    } else {
                        System.out.println("Archivo Transporte no inicializado!!");
                    }
    
     */
}
