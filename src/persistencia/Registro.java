package persistencia;

/**
 * Una clase para describir en forma genérica un registro capaz de ser
 * almacenado en un Archivo. Contiene un atributo "activo" que indica si el
 * registro es válido o no dentro del archivo. Si el registro está marcado como
 * borrado, el atributo "activo" vale false. Si el registro no está borrado,
 * "activo" vale true.
 *
 */
//import entradaDatos.Consola;
import java.io.*;

public class Registro implements Grabable {

    private int nroOrden; // indica la posicion del registro en el archivo 4 bytes
    private boolean estado; // marca de registro activo. Ocupa 1 byte en disco 
    private Grabable datos; // los datos que seran grabados en el registro
    // para utilizar la clase Grabable sera necesario que la clase de los datos herede de la interface

    /**
     * Crea un Registro sin datos, marcándolo como inactivo
     */
    public Registro() {
        estado = false;
        datos = null;
    }

    /**
     * Crea un Registro con datos, marcándolo como activo
     * @param d
     * @param orden
     */
    public Registro(Grabable d, int orden) {
        nroOrden = orden;
        estado = true;
        datos = d;
    }

     /**
     * Accede al valor del nroOrden
     *
     * @return el valor del atributo nroOrden
     */
    public int getNroOrden() {
        return nroOrden;
    }

    /**
     * Accede al valor del nroOrden
     *
     * @param nroOrden
     */
    public void setNroOrden(int nroOrden) {
        this.nroOrden = nroOrden;
    }    

    /**
     * Determina si el registro es activo o no
     *
     * @return true si es activo, false si no.
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Cambia el estado del registro, en memoria
     *
     * @param x el nuevo estado
     */
    public void setEstado(boolean x) {
        estado = x;
    }

    /**
     * Cambia los datos del registro en memoria
     *
     * @param d los nuevos datos
     */
    public void setDatos(Grabable d) {
        datos = d;
    }

    /**
     * Accede a los datos del registro en memoria
     * devuelve el tipo Grabable ya que en el archivo se tratan a los datos con ese tipo
     * @return una referencia a los datos del registro
     */
    public Grabable getDatos() {
        return datos;
    }
    

    /**
     * Carga un Nro.Orden de Articulo por teclado
     * @param orden
     */
    public void cargarNroOrden(int orden) {
        setNroOrden(orden);
    }
    

    /**
     * Calcula el tamaño en bytes del objeto, tal como será grabado en disco.
     * Pedido por Grabable.
     *
     * @return el tamaño en bytes del objeto como será grabado.
     */
    @Override
    public int tamRegistro() {
        return datos.tamRegistro() + 5; // suma 5, cantidad de bytes de activo y nroOrden 
    }

    /**
     * Devuelve cantidad de registros.
     * Pedido por Grabable.
     *
     * @return cantidad de registros.
     */
    @Override
    public int tamArchivo() {
        return datos.tamArchivo();
    }

    /**
     * Especifica cómo se graba un Registro en disco. Pedido por Grabable.
     *
     * @param a el manejador del archivo de disco donde se hará la grabación
     */
    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(nroOrden);
            a.writeBoolean(estado);
            datos.grabar(a);
        } catch (IOException e) {
            System.out.println("Error al grabar los datos del registro: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Especifica cómo se lee un Registro desde disco. Pedido por Grabable.
     *
     * @param a el manejador del archivo de disco desde donde se hará la lectura
     * @param val
     */
    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            nroOrden = a.readInt();
            estado = a.readBoolean();
            datos.leer(a, val); // convoca al metodo de lectura especifico a los datos del registro
        } catch (IOException e) {
            System.out.println("Error al leer los datos del registro: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Lee desde un archivo un String de "tam" caracteres. Se declara static
     * para que pueda ser usado en forma global por cualquier clase que requiera
     * leer una cadena de longitud fija desde un archivo.
     *
     * @param arch el archivo desde el cual se lee
     * @param tam la cantidad de caracteres a leer
     * @return el String leido
     */
    public static final String leerString(RandomAccessFile arch, int tam) {
        String cad = "";

        try {
            char vector[] = new char[tam];
            for (int i = 0; i < tam; i++) {
                vector[i] = arch.readChar();
            }
            cad = new String(vector, 0, tam);
        } catch (IOException e) {
            System.out.println("Error al leer una cadena: " + e.getMessage());
            System.exit(1);
        }
        return cad;
    }

    /**
     * Graba en un archivo un String de "tam" caracteres. Se declara static y publico para
     * que pueda ser usado en forma global por cualquier clase que requiera
     * grabar una cadena de longitud fija en un archivo.
     *
     * @param arch el archivo en el cual se graba
     * @param cad la cadena a a grabar
     * @param tam la cantidad de caracteres a grabar
     */
    public static void writeString(RandomAccessFile arch, String cad, int tam) {
        try {
            int i;
            char vector[] = new char[tam];
            for (i = 0; i < tam; i++) {
                vector[i] = ' ';
            }
            cad.getChars(0, cad.length(), vector, 0);
            for (i = 0; i < tam; i++) {
                arch.writeChar(vector[i]);
            }
        } catch (IOException e) {
            System.out.println("Error al grabar una cadena: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Muestra un registro por consola estandar
     * @param val
     * @param activos
     */
    @Override
    public void mostrarRegistro(int val, boolean activos) {
        if(val == 0)
            getDatos().mostrarRegistro(val, activos); 
        if(activos && val != 0 && getEstado())
            getDatos().mostrarRegistro(val, activos); // que sería lo mismo que System.out.print(reg.getData())
        if(!activos && val != 0)
            getDatos().mostrarRegistro(val, activos); // que sería lo mismo que System.out.print(reg.getData())
    }

    /**
     * Muestra un registro por consola estandar
     * @param val
     */
    @Override
    public void cargarDatos(int val) {
        datos.cargarDatos(val);
    }

}