/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import datos.Transporte;
import datos.TransporteMercaderías;
import datos.TransportePersonas;
import entradaDatos.TransporteDuplicadoException;
import entradaDatos.Validator;
import persistencia.Archivo;
import persistencia.Registro;

/**
 *
 * @author Lucas
 */
public class MenuTransporte {

    private Archivo archivoTransportes;
    private final Archivo archivoConductores;

    public MenuTransporte(Archivo archivoConductores) throws ClassNotFoundException {
        this.archivoConductores = archivoConductores;
        crearArchivo();
        
        Transporte.setArchivoConductores(this.archivoConductores);
        Transporte.setArchivoTransportes(this.archivoTransportes);
    }

    public void crearArchivo() throws ClassNotFoundException {
        this.archivoTransportes = new Archivo("Transportes.dat", new Transporte() {
            @Override
            public double calcularExtra() {
                return 0;
            }
        });
        
        // Si el archivo no existe, inicializarlo
        if (!archivoTransportes.getFd().exists()) {
            inicializarArchivo();
        }
    }

    private void inicializarArchivo() {
        archivoTransportes.abrirParaLeerEscribir();
        
        try {
            for (int i = 0; i < 100; i++) {
                Transporte transporteVacio = new TransportePersonas();
                transporteVacio.setCodT(0);
                transporteVacio.setTipo('X');
                transporteVacio.setHora(0);
                transporteVacio.setDniCond(0);
                transporteVacio.setExtra(0.0);
                transporteVacio.setEstado(false);
                
                Registro regVacio = new Registro(transporteVacio, i);
                regVacio.setEstado(false);
                
                archivoTransportes.buscarRegistro(i);
                regVacio.grabar(archivoTransportes.getMaestro());
            }
            
            archivoTransportes.cerrarArchivo();
            System.out.println("Archivo inicializado con 100 registros vacíos.");
            
        } catch (Exception e) {
            System.out.println("Error al inicializar archivo: " + e.getMessage());
            try {
                archivoTransportes.cerrarArchivo();
            } catch (Exception ex) {}
            System.out.println("Error al cerrar el archivo transportes: "+e.getMessage());
        }
    }

    void menu() throws TransporteDuplicadoException {
        int opcion;
        do {
            System.out.println("---Menu Transporte---");
            System.out.println("1. Alta Transporte");
            System.out.println("2. Baja Transporte");
            System.out.println("3. Modificar Transporte");
            System.out.println("0. Salir");

            opcion = Validator.validarInt("Ingrese una opcion: ");

            switch (opcion) {
                case 1:
                    altaTransporte();
                    break;
                case 2:
                    bajaTransporte();
                    break;
                case 3:
                    ModificarTransporte();
                    break;
                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void altaTransporte() throws TransporteDuplicadoException {
        try {
            char tipo = Validator.validarTexto("Ingrese el tipo de transporte que desea ingresar: \nP = Personas\nM = Mercaderias\nIngrese una opcion: ").charAt(0);

            Transporte transporte = null;
            if (tipo == 'P' || tipo == 'p') {
                transporte = new TransportePersonas();
            } else if (tipo == 'M' || tipo == 'm') {
                transporte = new TransporteMercaderías();
            } else {
                System.out.println("Tipo de transporte inválido");
                return;
            }

            transporte.setInstanceArchivoConductores(archivoConductores);
            transporte.setInstanceArchivoTransportes(archivoTransportes);

            transporte.cargarDatos(0);

            if (existeTransporte(transporte.getCodT())) {
                throw new TransporteDuplicadoException("Transporte ya existe");
            }

            transporte.calcularYAsignarExtra();

            int posicionLibre = buscarPosicionLibre();
            if (posicionLibre == -1) {
                System.out.println("Archivo lleno. No se puede agregar más transportes.");
                return;
            }

            // Crear registro con la posición libre encontrada
            Registro reg = new Registro(transporte, posicionLibre);

            grabarTransporteEnPosicion(reg, posicionLibre);

            System.out.println("Transporte cargado con éxito");
            System.out.println("Código: " + transporte.getCodT() + " en posición: " + posicionLibre);
            System.out.println("DNI Conductor: " + transporte.getDniCond());
            System.out.println("Extra calculado: $" + String.format("%.2f", transporte.getExtra()));

        } catch (Exception e) {
            System.out.println("Error al dar de alta: " + e.getMessage());
        }
    }

    private int buscarPosicionLibre() {
        archivoTransportes.abrirParaLectura();
        try {
            for (int i = 0; i < 100; i++) {
                archivoTransportes.buscarRegistro(i);
                if (!archivoTransportes.eof()) {
                    Registro reg = archivoTransportes.leerRegistro();
                    if (!reg.getEstado()) {
                        archivoTransportes.cerrarArchivo();
                        return i;
                    }
                } else {
                    archivoTransportes.cerrarArchivo();
                    return i;
                }
            }
            archivoTransportes.cerrarArchivo();
            return -1; // Archivo lleno
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            try {
                archivoTransportes.cerrarArchivo();
            } catch (Exception ex) {
                System.out.println("Error al cerrar el archivo: "+e.getMessage());
            }
            return -1;
        }
    }

    private void grabarTransporteEnPosicion(Registro reg, int posicion) {
        archivoTransportes.abrirParaLeerEscribir();
        try {
            archivoTransportes.buscarRegistro(posicion);
            reg.setEstado(true); 
            reg.grabar(archivoTransportes.getMaestro());
            System.out.println("Debug: Grabado en posición " + posicion + " con estado " + reg.getEstado());
        } catch (Exception e) {
            System.out.println("Error al grabar en posición " + posicion + ": " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
    }

    private void bajaTransporte() {
        try {
            int codigo = Validator.validarInt("Ingrese el codigo de transporte: ");

            Registro reg = buscarTransporte(codigo);
            if (reg == null) {
                System.out.println("Transporte no encontrado");
                return;
            }

            System.out.println("Datos del transporte: ");
            reg.mostrarRegistro(1, true);

            // Marcar como inactivo y grabar
            reg.setEstado(false);
            Transporte t = (Transporte) reg.getDatos();
            t.setEstado(false);
            grabarTransporteEnPosicion(reg, reg.getNroOrden());

            System.out.println("Transporte dado de baja exitosamente");

        } catch (Exception e) {
            System.out.println("Error al dar de baja el transporte: " + e.getMessage());
        }
    }

    private void ModificarTransporte() {
        try {
            int codigo = Validator.validarInt("Ingrese el codigo de transporte: ");

            Registro reg = buscarTransporte(codigo);
            if (reg == null) {
                System.out.println("Transporte no encontrado");
                return;
            }

            System.out.println("\nDatos actuales:");
            reg.mostrarRegistro(1, true);

            System.out.println("\nIngrese nuevos datos: ");

            Transporte transporte = (Transporte) reg.getDatos();
            transporte.setInstanceArchivoConductores(archivoConductores);
            transporte.setInstanceArchivoTransportes(archivoTransportes);

            int codigoOriginal = transporte.getCodT();
            transporte.cargarDatos(1);
            transporte.setCodT(codigoOriginal);

            transporte.calcularYAsignarExtra();

            grabarTransporteEnPosicion(reg, reg.getNroOrden());
            System.out.println("Transporte modificado exitosamente");

        } catch (Exception e) {
            System.out.println("Error al modificar transporte: " + e.getMessage());
        }
    }

    private boolean existeTransporte(int codT) {
        if (!archivoTransportes.getFd().exists()) {
            return false;
        }

        archivoTransportes.abrirParaLectura();
        try {
            for (int i = 0; i < 100; i++) {
                archivoTransportes.buscarRegistro(i);
                if (!archivoTransportes.eof()) {
                    Registro reg = archivoTransportes.leerRegistro();
                    if (reg.getEstado()) {
                        Transporte transporte = (Transporte) reg.getDatos();
                        if (transporte.getCodT() == codT) {
                            archivoTransportes.cerrarArchivo();
                            return true;
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar transporte: " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
        return false;
    }

    private Registro buscarTransporte(int codigo) {
        if (!archivoTransportes.getFd().exists()) {
            return null;
        }

        archivoTransportes.abrirParaLectura();
        try {
            for (int i = 0; i < 100; i++) {
                archivoTransportes.buscarRegistro(i);
                if (!archivoTransportes.eof()) {
                    Registro reg = archivoTransportes.leerRegistro();
                    if (reg.getEstado()) {
                        Transporte transporte = (Transporte) reg.getDatos();
                        if (transporte.getCodT() == codigo) {
                            Registro resultado = new Registro(transporte, i);
                            resultado.setEstado(true);
                            archivoTransportes.cerrarArchivo();
                            return resultado;
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transporte: " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
        return null;
    }

}
