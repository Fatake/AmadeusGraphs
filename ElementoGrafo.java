/*Elemento del Grafo
  Abstración de un elemento perteneciente al grafo.
  Hereda a: Vertice, Arista
  Un elemento del grafo tiene ID, etiqueta y un estado de selección.
*/
import java.awt.Graphics;
import java.io.Serializable;

public abstract class ElementoGrafo implements Serializable{
	//Atributos de Instancia
	protected int id;
	protected String etiqueta;
	protected boolean estadoSeleccion;

	//Métodos
	//Constructor
	public ElementoGrafo() {
		estadoSeleccion = false; //Al crearse, se selecciona el objeto
	}

	//Obtener el ID
	public int getID() {
		return id;
	}

	//Establecer selección
	public void setSeleccion(boolean valor) {
		estadoSeleccion = valor;
	}

	//Obtener Estado de Selección
	public boolean getSeleccion() {
		return estadoSeleccion;
	}

	//Establecer etiqueta
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	//Obtener Etiqueta
	public String getEtiqueta() {
		return etiqueta;
	}

	//Funciones Abstractas
	//Pintar elemento en Panel (Abstracto)
	public abstract void pintarElemento(Graphics g);
	//Retorno de información
	public abstract String toString();
}
