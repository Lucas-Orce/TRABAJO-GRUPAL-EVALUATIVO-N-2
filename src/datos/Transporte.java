/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.Validator;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import persistencia.*;

/**
 *
 * @author Mariano
 */
abstract public class Transporte implements Serializable, lCalculable, Comparable<Transporte> {

    protected int codT;
    protected char Tipo; //segun la eleccion debe quedar en P o M
    protected int hora;
    protected long dniCond;
    protected double extra;
    protected boolean estado;

    private static int TAMARCHIVO = 100;
    private static int TAMAREG = 39;

    public Transporte() {
    }

    public Transporte(int codT, char Tipo, int hora, int dniCond, boolean estado) {
        this.codT = codT;
        this.Tipo = Tipo;
        this.hora = hora;
        this.dniCond = dniCond;
        this.extra = calcularExtra();
        this.estado = true;
    }

    public int getCodT() {
        return codT;
    }

    public void setCodT(int codT) {
        this.codT = codT;
    }

    public char getTipo() {
        return Tipo;
    }

    public void setTipo(char Tipo) {
        this.Tipo = Tipo;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public long getDniCond() {
        return dniCond;
    }

    public void setDniCond(int dniCond) {
        this.dniCond = dniCond;
    }

    public double getExtra() {
        return extra;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void cargaCompleta() {
        leerCodigo();
        leerhoras();
        leerDniCon();
    }

    public void leerCodigo() {
        setCodT(Validator.validarInt("Ingrese el codito del transporte: "));
    }

    public void leerhoras() {
        setHora(Validator.validarInt("Ingrese las horas conducidas del "));
    }

    public void leerDniCon() {
        setDniCond(Validator.validarDNI("Ingrese el dni del conductor: "));
    }

    public void darDeBaja() {
        setEstado(false);
    }

    public void reActivar() {
        setEstado(true);
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }

        Transporte a = (Transporte) x;
        return (codT == a.codT);
    }

    @Override
    public int hashCode() {
        return codT;
    }

    @Override
    public String toString() {
        String condicion = estado ? "Existe" : "No existe";
        return "Transporte" + "codT=" + codT + ", Tipo=" + Tipo + ", hora=" + hora + ", dniCond=" + dniCond + ", extra=" + extra + ", estado=" + condicion;
    }

    public int tamRegistro() {
        return TAMAREG;
    }

    public int tamArchivo() {
        return TAMARCHIVO;
    }

    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(codT);
            Registro.writeString(a, "", 20);
        } catch (IOException e) {
            System.out.println("Error al grabar registro: " + e.getMessage());
            System.exit(1);
        }
    }

    public void leer(RandomAccessFile a, int val) {
        try {
            codT = a.readInt();
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }

}
