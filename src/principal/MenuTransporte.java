/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import datos.Conductor;
import datos.Transporte;
import datos.TransporteMercaderías;
import datos.TransportePersonas;
import entradaDatos.ConductorInexistenteException;
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
       crearArchivo();
        this.archivoConductores = archivoConductores;
    }

    void menu() throws TransporteDuplicadoException, ConductorInexistenteException {
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
                default:
                    throw new AssertionError();
            }

        } while (opcion != 0);

    }

    private void altaTransporte() throws TransporteDuplicadoException, ConductorInexistenteException {
        archivoTransportes.abrirParaLeerEscribir();
        Transporte transporte = null;
        try {
            char tipo = Validator.validarTexto("Ingrese el tipo de transporte que desea ingresar: \nP = Personas\nM = Mercaderias").charAt(0);

            if (tipo == 'P') {
                transporte = new TransportePersonas();
            } else if (tipo == 'M') {
                transporte = new TransporteMercaderías();
            }

            if (!existeConductor(transporte.getDniCond())) {
                throw new ConductorInexistenteException("Conductor Inexistente");
            }

            if (existeTransporte(transporte.getCodT())) {
                throw new TransporteDuplicadoException("Alta Existente");
            }

            Registro reg = new Registro(transporte, transporte.getCodT());
            archivoTransportes.cargarUnRegistro(reg);
            System.out.println("Transporte cargado con exito");
        } catch (ConductorInexistenteException | TransporteDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al dar de alta: " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
    }
    
    public void crearArchivo() throws ClassNotFoundException{
         this.archivoTransportes = new Archivo("Transportes.dat",new Transporte() {
            @Override
            public double calcularExtra() {
                return 0;
            }
        });
    }

    private void bajaTransporte() {
        try {
            int codigo = Validator.validarInt("Ingrese el codigo de transporte: ");

            Registro posicion = buscarTransporte(codigo);
            if (posicion == null) {
                System.out.println("Transporte no encontrado");
                return;
            }
            archivoTransportes.abrirParaLeerEscribir();
            archivoTransportes.irPrincipioArchivo();

            System.out.println("Datos del transporte: ");
            posicion.mostrarRegistro(1, true);

            archivoTransportes.bajaRegistro(posicion);
            archivoTransportes.cerrarArchivo();

        } catch (Exception e) {
            System.out.println("Error al dar de baja el transporte: " + e.getMessage());
        }
    }

    private void ModificarTransporte() {
        try {
            archivoTransportes.abrirParaLeerEscribir();
            archivoTransportes.irPrincipioArchivo();
            
            int codigo = Validator.validarInt("Ingrese el codigo de transporte: ");

            Registro posicion = buscarTransporte(codigo);
            if (posicion == null) {
                System.out.println("Transporte no encontrado");
                return;
            }
            
            System.out.println("\nDatos actuales:");
            posicion.mostrarRegistro(1, true);
            
            System.out.println("\nIngrese nuevos datos: ");
            posicion.cargarDatos(1);
            
            Transporte transporte = (Transporte) posicion.getDatos();
            if (!existeConductor(transporte.getDniCond())) {
                throw new ConductorInexistenteException("Conductor inexistente");
            }
            
            archivoTransportes.cargarUnRegistro(posicion);
            System.out.println("Transporte Modificado");
            
            archivoTransportes.cerrarArchivo();
            
        } catch (ConductorInexistenteException e) {
            System.out.println("Error: "+e.getMessage());
        }catch(Exception e){
            System.out.println("Error al modificar transporte: "+e.getMessage());
        }

    }

    private boolean existeConductor(long dniCond) {
        archivoConductores.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();
        
        try {
            for (int i = 0; i < 100; i++) {
                archivoConductores.buscarRegistro(i);
                if (!archivoConductores.eof()) {
                    Registro reg = archivoConductores.leerRegistro();
                    if (reg.getEstado()) {
                        Conductor conductor = (Conductor) reg.getDatos();
                        if (conductor.getDni() == dniCond) {
                            archivoConductores.cerrarArchivo();
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el conductor: " + e.getMessage());
        }
        archivoConductores.cerrarArchivo();
        return false;
    }

    private boolean existeTransporte(int codT) {
        archivoTransportes.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();
        
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
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el transporte: " + e.getMessage());
        }

        archivoTransportes.cerrarArchivo();
        return false;
    }

    private Registro buscarTransporte(int codigo) {
        archivoTransportes.abrirParaLectura();
        archivoTransportes.irPrincipioArchivo();
        
        try {
            for (int i = 0; i < 100; i++) {
                archivoTransportes.buscarRegistro(i);
                if (!archivoTransportes.eof()) {
                    Registro reg = archivoTransportes.leerRegistro();
                    if (reg.getEstado()) {
                        Transporte transporte = (Transporte) reg.getDatos();
                        if (transporte.getCodT() == codigo) {
                            archivoTransportes.cerrarArchivo();
                            return reg;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transporte: " + e.getMessage());
        }
        archivoTransportes.cerrarArchivo();
        return null;
    }

}
