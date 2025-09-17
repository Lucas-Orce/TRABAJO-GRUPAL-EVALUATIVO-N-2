/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.Validator;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Mariano
 */
public class TransportePersonas extends Transporte implements lCalculable {

    private int Personas;

    public TransportePersonas() {
        super.Tipo = 'P';
        cargarDatos(0);
        super.estado = true;
    }

    public int getPersonas() {
        return Personas;
    }

    public void setPersonas(int Personas) {
        this.Personas = Personas;
    }

    public void leerPersonas() {
        this.setPersonas(Validator.validarInt("Ingrese la cantidad de personas que llevara el transporte: "));
    }

    @Override
    public double calcularExtra() {
        if (Personas > 9) {
            return 5500 * this.hora;
        } else {
            return 3000 * this.hora;
        }
    }

    @Override
    public int compareTo(Transporte Tr) {
        if (this.codT > Tr.codT) {
            return 1;
        }
        if (this.codT < Tr.codT) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public void cargarDatos(int val) {
        super.cargarDatos(val);
        leerPersonas();
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            super.leer(a, val);
            Personas=a.readInt();
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
        }
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            super.grabar(a);
            a.write(Personas);
        } catch (Exception e) {
        }

    }

}
