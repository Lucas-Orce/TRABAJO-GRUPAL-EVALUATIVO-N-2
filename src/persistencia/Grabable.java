package persistencia;

/**
 * Indica el comportamiento que debe ser capaz de mostrar un objeto que vaya a
 * ser grabado en un Archivo.
 *
 */
import java.io.*;

public interface Grabable {

    /**
     * Calcula el tamaño en bytes del objeto, tal como será grabado
     *
     * @return el tamaño en bytes del objeto
     */
    int tamRegistro();

    /**
     * Obtiene la cantidad de registros que se desea en el archivo
     *
     * @return la cantidad de registros
     */
    int tamArchivo();

    /**
     * Indica cómo grabar un objeto
     *
     * @param a
     */
    void grabar(RandomAccessFile a);

    /**
     * Indica cómo leer un objeto
     *
     * @param a el archivo donde se hará la lectura
     * @param val
     */
    void leer(RandomAccessFile a, int val);
    
    /**
     * Muestra un registro por consola estandar
     * @param val
     * @param activo
     */
    void mostrarRegistro(int val, boolean activo);
    
    /**
     * Carga datos de un registro por consola estandar
     * @param val
     */
    void cargarDatos(int val);
    // si val esta en 0 no ingresaremos la clave del registro
    // ya que estamos modificando y no modificamos la clave que determina su ubicacion

    
}
