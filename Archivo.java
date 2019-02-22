//Clase Archivo para Escritura y Lectura de Objetos en Archivos
/*Métodos proporcionados:
  (static) fopen - Apertura de Archivo
  (static) fclose - Cierre de Archivo
  (static) fread - Lectura de un Objeto
  (static) fwrite - Escritura de un Objeto*/

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.lang.Exception;
import java.lang.NullPointerException;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Archivo {
  private static ObjectOutputStream write; //Flujo de Escitura
  private static ObjectInputStream read; //Flujo de Lectura

  //Apertura de Archivo
  /*Recibe: String con el Nombre del Archivo a abrir, y un Comando de Control
    para la apertura, siguiendo la sintaxis de C:
    r - Archivo para lectura
    w - Archivo para Escritura
    Retorna <true> si el archivo se abrió, <false> si no.*/
  public static boolean fopen(String nombreArchivo, char tipo) {
    switch(tipo){
      case 'r':
        try {
          read = new ObjectInputStream(new FileInputStream(nombreArchivo)); //Abre el archivo para lectura
        }
        catch (IOException fnfe){
          return false; //El archivo no se pudo abrir
        }

        return true;
      case 'w':
        try {
          write = new ObjectOutputStream(new FileOutputStream(nombreArchivo)); //Abre el archivo para Escritura
        }
        catch (IOException fnfe){
          return false; //El archivo no se pudo abrir
        }

        return true;
      default: return false;
    }
  }

  //Cierre de Archivos
  /*Recibe: Comando de Control con el que fue abierto el archivo.
    Retorna <true> si el archivo se cerró, <false> si no.*/
  public static boolean fclose(char tipo) {
    switch(tipo){
      case 'r':
        try {
          read.close(); //Cierra el archivo
        }
        catch (IOException | NullPointerException ioe){
          return false; //Si no se pudo cerrar o si el archivo no estaba abierto para lectura
        }

        return true;
      case 'w':
        try {
          write.close(); //Cierra el archivo
        }
        catch (IOException | NullPointerException ioe){
          return false; //Si no se pudo cerrar o si el archivo no estaba abierto para escritura
        }

        return true;
      default: return false;
    }
  }

  //Escritura al Archivo abierto
  /*Recibe: Objeto a escribir en el archivo.
    El archivo recibido puede ser de cualquier tipo.
    Retorna <true> si se escribió el archivo, o <false> si hubo algún problema.*/
  public static boolean fwrite(Object wObject) {
    try {
      write.writeObject(wObject); //Escribe el objeto
    }
    catch (Exception e) {
      return false; //Si no se pudo escribir el objeto en el archivo
    }

    return true;
  }

  //Lectura del Archivo abierto
  /*Recibe: Recipiente del Objeto a leer desde el archivo.
    El archivo leído puede ser de cualquier tipo.
    (Precaución de Uso: Asegurar que el recipiente es del mismo tipo de lo que
    se guardó previamente el archivo, y en el mismo orden configurado).
    Retorna un Objeto si se leyó el objeto, o <null> si hubo algún problema.*/
  public static Object fread() {
    Object rObject;
    try {
      rObject = read.readObject(); //Lee el objeto
    }
    catch (Exception e){
      return null; //Si no se pudo leer el objeto del archivo
    }

    return rObject;
  }
}
