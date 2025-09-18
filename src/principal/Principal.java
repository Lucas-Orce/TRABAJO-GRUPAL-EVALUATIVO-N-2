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
        try {
            Conductor conductor = new Conductor();
            conductor.cargarDatos(0);

            if (existeConductor(conductor.getDni())) {
                System.out.println("Conductor Existente");
                return;
            }

            int numOrd = buscarProximoNumeroOrden();
            conductor.setNumOrd(numOrd);

            System.out.println("Debug - Asignando número de orden: " + numOrd);
            System.out.println("Debug - DNI del conductor: " + conductor.getDni());
            System.out.println("Debug - Nombre del conductor: " + conductor.getApe_Nom());

            Registro regConductor = new Registro(conductor, conductor.getNumOrd());
            archivoConductores.cargarUnRegistro(regConductor);

            System.out.println("Conductor grabado con éxito en la posición: " + numOrd);

        } catch (Exception e) {
            System.out.println("Error al cargar un conductor: " + e.getMessage());
        }
    }

    private void menuTransporte() throws TransporteDuplicadoException, ConductorInexistenteException, ClassNotFoundException {
        MenuTransporte menuT = new MenuTransporte(archivoConductores);
        menuT.menu();
    }

    private void listadoSueldos() {
        
    if (!archivoConductores.getFd().exists()) {
        System.out.println("El archivo Conductores.dat no existe.");
        return;
    }

    archivoConductores.abrirParaLectura();
    archivoConductores.irPrincipioArchivo();

    try {
        boolean hayConductores = false;
        for (int i = 0; i < 100; i++) {
            archivoConductores.buscarRegistro(i);
            if (!archivoConductores.eof()) {
                Registro reg = archivoConductores.leerRegistro();

                if (reg.getEstado()) {
                    Conductor conductor = (Conductor) reg.getDatos();
                    double sueldo = calcularSueldoConductor(conductor.getDni());

                    System.out.println("Conductor: " + conductor.getApe_Nom());
                    System.out.println("DNI: " + conductor.getDni());
                    System.out.println("Número de Orden: " + conductor.getNumOrd());
                    System.out.println("Sueldo Total: $" + String.format("%.2f", sueldo));
                    System.out.println("------------------------");
                    hayConductores = true;
                }
            } else {
                break; // Si llegamos al final del archivo, salir del bucle
            }
        }

        if (!hayConductores) {
            System.out.println("No hay conductores activos registrados en el archivo.");
        }

    } catch (Exception e) {
        System.out.println("Error al listar los sueldos: " + e.getMessage());
    }

    archivoConductores.cerrarArchivo();
}

    private void listadoTransporte() {
    long dni = Validator.validarDNI("");

    if (!existeConductor(dni)) {
        System.out.println("Conductor inexistente");
        return;
    }

    Conductor conductor = obtenerConductorPorDNI(dni);
    System.out.println("\nConductor: " + conductor.getApe_Nom());
    System.out.println("DNI: " + conductor.getDni());
    
    try {
        if (archivoTransportes == null) {
            archivoTransportes = new Archivo("Transportes.dat", new Transporte() {
                @Override
                public double calcularExtra() {
                    return 0;
                }
            });
        }
        
        Transporte.setArchivoConductores(archivoConductores);
        Transporte.setArchivoTransportes(archivoTransportes);
        
        if (!archivoTransportes.getFd().exists()) {
            System.out.println("No hay archivo de transportes. No hay transportes registrados para este conductor.");
            return;
        }
        
        archivoTransportes.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();

        boolean hayTransporte = false;
        for (int i = 0; i < 100; i++) {
            archivoTransportes.buscarRegistro(i);
            if (!archivoTransportes.eof()) {
                Registro reg = archivoTransportes.leerRegistro();
                if (reg.getEstado()) {
                    Transporte transporte = (Transporte) reg.getDatos();
                    if (transporte.getDniCond() == dni) {
                        System.out.println("Codigo Transporte: " + transporte.getCodT());
                        System.out.println("Tipo: " + transporte.getTipo());
                        System.out.println("Horas: " + transporte.getHora());
                        System.out.println("Monto extra: $" + String.format("%.2f", transporte.getExtra()));
                        System.out.println("------------------------");
                        hayTransporte = true;
                    }
                }
            } else {
                break; // Fin del archivo
            }
        }
        if (!hayTransporte) {
            System.out.println("No hay transportes asignados a este conductor.");
        }
        archivoTransportes.cerrarArchivo();
        
    } catch (Exception e) {
        System.out.println("Error al acceder al archivo de transportes: " + e.getMessage());
    }
}

    private boolean existeConductor(long dni) {
        if (!archivoConductores.getFd().exists()) {
            return false;
        }

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
                } else {
                    break; // Fin del archivo
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el conductor: " + e.getMessage());
        }
        archivoConductores.cerrarArchivo();
        return false;
    }

    private double calcularSueldoConductor(long dni) {
    double sueldoFijo = 400000;
    double totalExtra = 0.0;

    try {
        if (archivoTransportes == null) {
            archivoTransportes = new Archivo("Transportes.dat", new Transporte() {
                @Override
                public double calcularExtra() {
                    return 0;
                }
            });
        }
        
        if (!archivoTransportes.getFd().exists()) {
            return sueldoFijo;
        }
        
        archivoTransportes.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();
        
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
            } else {
                break; // Salir si llegamos al final del archivo
            }
        }
        
        archivoTransportes.cerrarArchivo();
        
    } catch (Exception e) {
        System.out.println("Error al calcular el sueldo: "+e.getMessage());
        return sueldoFijo;
    }
    
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

    private int buscarProximoNumeroOrden() {
        // Si el archivo no existe, empezar desde 0
        if (!archivoConductores.getFd().exists()) {
            return 0;
        }

        archivoConductores.abrirParaLectura();
        archivoConductores.irPrincipioArchivo();

        try {
            for (int i = 0; i < 100; i++) {
                archivoConductores.buscarRegistro(i);
                if (archivoConductores.eof()) {
                    archivoConductores.cerrarArchivo();
                    return i;
                }

                Registro reg = archivoConductores.leerRegistro();
                if (!reg.getEstado()) {
                    archivoConductores.cerrarArchivo();
                    return i;
                }
            }

            archivoConductores.cerrarArchivo();
            return 99; // 

        } catch (Exception e) {
            System.out.println("Error al buscar próximo número de orden: " + e.getMessage());
            try {
                archivoConductores.cerrarArchivo();
            } catch (Exception ex) {
            }
            return 0;
        }
    }
}
