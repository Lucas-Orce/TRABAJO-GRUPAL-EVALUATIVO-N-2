/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import entradaDatos.Validator;

/**
 *
 * @author Mariano
 */
public class TransporteMercaderías extends Transporte implements lCalculable {

    private double Toneladas;
    private boolean esPeligroso;

    public TransporteMercaderías() {
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

    public void Cargar() {
        super.cargaCompleta();
        leerToneladas();
        Peligroso();
    }

    public void leerToneladas() {
        this.setToneladas(Validator.validarDouble("Ingrese la cantidad de toneladas que llevara: "));
    }

    public void Peligroso() {
        int des = Validator.validarInt("Indique si la carga es peligrosa: (1-SI || 2-NO)");
        if (des == 1) {
            this.esPeligroso = true;
        } else if (des != 1) {
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

}
