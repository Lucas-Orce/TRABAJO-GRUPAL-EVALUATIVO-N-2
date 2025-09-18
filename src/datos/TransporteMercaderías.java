/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.Validator;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TransporteMercaderías extends Transporte implements lCalculable {

    private double Toneladas;
    private boolean esPeligroso;

    public TransporteMercaderías() {
        super('M');
    }

    public double getToneladas() {
        return Toneladas;
    }

    public void setToneladas(double Toneladas) {
        this.Toneladas = Toneladas;
    }

    public boolean isEsPeligroso() {
        return esPeligroso;
    }

    public void setEsPeligroso(boolean esPeligroso) {
        this.esPeligroso = esPeligroso;
    }

    public void leerToneladas() {
        this.setToneladas(Validator.validarDouble("Ingrese la cantidad de toneladas que llevara: "));
    }

    public void Peligroso() {
        int des = Validator.validarInt("Indique si la carga es peligrosa: (1-SI || 2-NO)");
        if (des == 1) {
            this.esPeligroso = true;
        } else {
            this.esPeligroso = false;
        }
    }

    @Override
    public double calcularExtra() {
        if (esPeligroso) {
            return (7000 * Toneladas) + 20000;
        } else {
            return (7000 * Toneladas);
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
        leerToneladas();
        Peligroso();
        this.extra = calcularExtra();
    }
    
    @Override
    public void grabar(RandomAccessFile a) {
        try {
            super.grabar(a);
            a.writeDouble(Toneladas);
            a.writeBoolean(esPeligroso);
            a.writeInt(0); // 4 bytes
        } catch (IOException e) {
            System.out.println("Error al grabar el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            super.leer(a, val);
            Toneladas = a.readDouble();
            esPeligroso = a.readBoolean();
            a.readInt(); // Leer los datos dummy
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public void mostrarRegistro(int val, boolean activo) {
        if (val==0||(activo && estado)||!activo) {
            super.mostrarRegistro(val, activo);
            System.out.println("Toneladas: " + Toneladas);
            System.out.println("Carga peligrosa: " + (esPeligroso ? "Sí" : "No"));
            System.out.println("------------------------");
        }
    }
}