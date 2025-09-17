/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import persistencia.*;

/**
 *
 * @author Mariano
 */
public class Conductor implements Serializable, Grabable, Comparable<Conductor> {

    private int NumOrd;  //4 
    private long dni;  //8
    private String Ape_Nom; //40
    private boolean estado; //1

    private static int TAMARCHIVO = 100;
    private static int TAMAREG = 53;

    //Constructores
    public Conductor() {
        estado=true;
    }

    //Fin Constructores

    //*****Getters & setters******
    public int getNumOrd() {
        return NumOrd;
    }
    public void setNumOrd(int n){
        NumOrd=n;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getApe_Nom() {
        return Ape_Nom;
    }

    public void setApe_Nom(String Ape_Nom) {
        this.Ape_Nom = Ape_Nom;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    //*****Getters & setters******

    //***Funciones***
    @Override
    public int tamRegistro() {
        return TAMAREG;
    }

    @Override
    public int tamArchivo() {
        return TAMARCHIVO;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(NumOrd);
            a.writeLong(dni);
            Registro.writeString(a, Ape_Nom, 20);
        } catch (IOException e) {
            System.out.println("Error al grabar registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            NumOrd = a.readInt();
            dni = a.readLong();
            Ape_Nom = Registro.leerString(a, 20).trim();
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }

        Conductor a = (Conductor) x;
        return (NumOrd == a.NumOrd);
    }

    @Override
    public int hashCode() {
        return NumOrd;
    }

    @Override
    public void mostrarRegistro(int val, boolean activo) {
        if (val == 0) {
            System.out.println("Numero de orden --  Nombre y apellido  --  Estado  --  DNI ");
        }
        if (val == 1 && activo) {
            System.out.println(toString());
        }
        if (val == 2 && activo) {
            String condicion = estado ? "Existe" : "No existe";
            System.out.println("Numero de destino: " + getNumOrd() + "Nombre&Apellido: " + this.getApe_Nom() + "Dni: " + this.getDni() + "Estado: " + condicion);
        }
    }

    public void darDeBaja() {
        setEstado(false);
    }

    public void reActivar() {
        setEstado(true);
    }

    @Override
    public String toString() {
        String condicion = estado ? "Existe" : "No existe";
        return "Conductor" + "NumOrd=" + NumOrd + ", dni=" + dni + ", Ape_Nom=" + Ape_Nom + ", estado=" + condicion + '}';
    }

    @Override
    public int compareTo(Conductor o) {
        if (this.NumOrd > o.NumOrd) {
            return 1;
        }
        if (this.NumOrd < o.NumOrd) {
            return -1;
        } else {
            return 0;
        }
    }

    //***Fin funciones***
    @Override
    public void cargarDatos(int val) {
        try {
            dni = Validator.validarDNI("Ingrese el DNI del conductor: ");
            Ape_Nom = Validator.validarTexto("Ingrese el nombre y apellido del conductor");
            estado = true;
        } catch (Exception e) {
            System.out.println("Error al ingresar datos: " + e.getMessage());
        }

    }
    
    
    
}
