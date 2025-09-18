/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.ConductorInexistenteException;
import entradaDatos.TransporteDuplicadoException;
import entradaDatos.Validator;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import persistencia.*;

public abstract class Transporte implements Serializable, lCalculable, Comparable<Transporte>, Grabable {

    protected int codT;
    protected char Tipo; //segun la eleccion debe quedar en P o M
    protected int hora;
    protected long dniCond;
    protected double extra;
    protected boolean estado;

    private static int TAMARCHIVO = 100;
    private static int TAMAREG = 40;
    
    private static Archivo archivoConductores;
    private static Archivo archivoTransportes;
    private Archivo instanceArchivoConductores;
    private Archivo instanceArchivoTransportes;

    public Transporte() {
        this.estado = true;
        this.extra = 0.0;
    }

    public Transporte(char Tipo) {
        this.Tipo = Tipo;
        this.estado = true;
        this.extra = 0.0;
    }
    
    public static void setArchivoConductores(Archivo archivo) {
        archivoConductores = archivo;
    }
    
    public static void setArchivoTransportes(Archivo archivo) {
        archivoTransportes = archivo;
    }
    
    public void setInstanceArchivoConductores(Archivo archivo) {
        this.instanceArchivoConductores = archivo;
    }
    
    public void setInstanceArchivoTransportes(Archivo archivo) {
        this.instanceArchivoTransportes = archivo;
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

    public void setDniCond(long dniCond) {
        this.dniCond = dniCond;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void darDeBaja() {
        setEstado(false);
    }

    public void reActivar() {
        setEstado(true);
    }
    
    // Método para calcular y asignar el extra
    public void calcularYAsignarExtra() {
        this.extra = calcularExtra();
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
            a.writeInt(codT);
            a.writeChar(Tipo);
            a.writeInt(hora);
            a.writeLong(dniCond);
            a.writeDouble(extra);
            a.writeBoolean(estado);
        } catch (IOException e) {
            System.out.println("Error al grabar registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
     public void leer(RandomAccessFile a, int val) {
        try {
            codT = a.readInt();
            Tipo = a.readChar();
            hora = a.readInt();
            dniCond = a.readLong();
            extra = a.readDouble();
            estado = a.readBoolean();
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public int compareTo(Transporte o) {
        if (this.codT > o.codT) {
            return 1;
        }
        if (this.codT < o.codT) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public void mostrarRegistro(int val, boolean activo) {
        if (val==0||(activo && estado)||!activo) {
            System.out.println("Codigo de transporte: "+codT);
            System.out.println("Tipo: "+ Tipo);
            System.out.println("Horas: "+hora);
            System.out.println("DNI conductor: "+dniCond);
            System.out.println("Sueldo Extra: "+String.format("%.2f", extra));
            System.out.println("Estado: "+(estado?"Activo":"Inactivo"));
        }
    }

    @Override
    public void cargarDatos(int val) {
        try {
            if (val==0) {
                codT=Validator.validarInt("Ingrese el codigo del transporte: ");
                
                if (existeTransporte(codT)) {
                    throw new TransporteDuplicadoException("Transporte ya existe");
                }
            }
            
            hora=Validator.validarInt("Ingrese las horas conducidas: ");
            
            dniCond=Validator.validarDNI("Ingrese el dni del conductor: ");
            if(!existeConductor(dniCond)){
                throw new ConductorInexistenteException("Conductor con DNI " + dniCond + " no existe");
            }
            
            estado = true;
            
        } catch (Exception e) {
             System.out.println("Error en cargar los datos: "+e.getMessage());
             throw new RuntimeException(e);
        }
    }

    private boolean existeConductor(long dni) {
        Archivo archivoAUsar = instanceArchivoConductores != null ? instanceArchivoConductores : archivoConductores;
        
        if (archivoAUsar == null) {
            System.out.println("Archivo de conductores no configurado");
            return false;
        }
        
        if (!archivoAUsar.getFd().exists()) {
            System.out.println("Archivo de conductores no existe");
            return false;
        }
        
        archivoAUsar.abrirParaLectura();
        try {
            for (int i = 0; i < 100; i++) {
                archivoAUsar.buscarRegistro(i);
                if (!archivoAUsar.eof()) {
                    Registro reg = archivoAUsar.leerRegistro();
                    if (reg.getEstado()) {
                        Conductor conductor = (Conductor) reg.getDatos();
                        if (conductor.getDni() == dni) {
                            archivoAUsar.cerrarArchivo();
                            return true;
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar conductor: "+e.getMessage());
        }
        
        archivoAUsar.cerrarArchivo();
        return false;
    }
    
    protected boolean existeTransporte(int codT){
        Archivo archivoAUsar = instanceArchivoTransportes != null ? instanceArchivoTransportes : archivoTransportes;
        
        if (archivoAUsar == null || !archivoAUsar.getFd().exists()) {
            return false;
        }
        
        archivoAUsar.abrirParaLectura();
        try {
            for (int i = 0; i < 100; i++) {
                archivoAUsar.buscarRegistro(i);
                if (!archivoAUsar.eof()) {
                    Registro reg = archivoAUsar.leerRegistro();
                    if (reg.getEstado()) {
                        Transporte transporte = (Transporte) reg.getDatos();
                        if (transporte.getCodT()==codT) {
                            archivoAUsar.cerrarArchivo();
                            return true;
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al verificar transporte: "+e.getMessage());
        }
        archivoAUsar.cerrarArchivo();
        return false;
    }
}