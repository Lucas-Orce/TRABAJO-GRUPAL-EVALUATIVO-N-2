/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;


import entradaDatos.Validator;
import java.io.IOException;
import java.io.RandomAccessFile;
import persistencia.*;

/**
 *
 * @author Mariano
 */

public class Conductor {

    private int NumOrd;  //4 
    private int dni;  //4
    private String Ape_Nom; //40
    private boolean estado; //1

    private static int TAMARCHIVO = 100;
    private static int TAMAREG = 49;

    //Constructores
    public Conductor() {
    }

    public Conductor(int NumOrd, int dni, String Ape_Nom, boolean estado) {
        this.NumOrd = NumOrd; //para que se incremente cada vez que se cree un nuevo objeto
        this.dni = dni;
        this.Ape_Nom = Ape_Nom;
        this.estado = true;
    }
    //Fin Constructores

    //*****Getters & setters******
    public int getNumOrd() {
        return NumOrd;
    }

    public void setNumOrd(int NumOrd) {
        this.NumOrd = NumOrd;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
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

    //*****Cargas*****
    public void carga() {
        leerdni();
        leerNombre();
    }

    public void leerNum(){
        int min=0;
        int max=100;
        setNumOrd(Validator.validarIntRango(min, max, "Ingrese el numero de orden: "));
    }
    
    public void leerdni() {
        setDni(Validator.validarDNI("Ingrese el dni del Conductor: "));
    }

    public void leerNombre() {
        this.setApe_Nom(Validator.validarTexto("Ingrese el nombre del conductor: "));
    }
    //*****Fin cargas*****

    //***Funciones***
    public int tamRegistro() {
        return TAMAREG;
    }

    public int tamArchivo() {
        return TAMARCHIVO;
    }

    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(NumOrd);
            Registro.writeString(a, Ape_Nom, 20);
        } catch (IOException e) {
            System.out.println("Error al grabar registro: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void leer(RandomAccessFile a, int val){
        try{
            NumOrd = a.readInt();
            Ape_Nom = Registro.leerString(a, 20).trim();
        }catch(IOException e){
            System.out.println("Error al leer el registro: " +e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean equals(Object x) {
        if(x==null){
            return false;
        }
        
        Conductor a = (Conductor) x;
        return (NumOrd == a.NumOrd);
    }

    @Override
    public int hashCode() {
        return NumOrd;
    }

    public void mostrarRegistro(int val, boolean activo) {
        if(val == 0){
            System.out.println("Numero de orden --  Nombre y apellido  --  Estado  --  DNI ");
        }
        if(val == 1 && activo){
            System.out.println(toString());
        }
        if(val == 2 && activo){
            String condicion = estado ? "Existe" : "No existe";
            System.out.println("Numero de destino: " +getNumOrd()+ "Nombre&Apellido: " +this.getApe_Nom()+ "Dni: "+this.getDni() + "Estado: "+estado); 
        }
    }
    
    public boolean compararConductor(int x) {
        return dni == x;
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
        return "Conductor{" + "NumOrd=" + NumOrd + ", dni=" + dni + ", Ape_Nom=" + Ape_Nom + ", estado=" + condicion + '}';
    }
    //***Fin funciones***

}