/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

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
    private Archivo archivoConductores;

    public MenuTransporte(Archivo archivoTransportes, Archivo archivoConductores) {
        this.archivoTransportes = archivoTransportes;
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
        archivoTransportes.abrirParaLectura();
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
    }

    private void bajaTransporte() {
    }

    private void ModificarTransporte() {
    }

    private void existeTranspote() {
    }

    private boolean existeConductor(long dniCond) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean existeTransporte(long dniCond) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
