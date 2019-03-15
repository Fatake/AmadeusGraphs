/*Vértice
  El vértice es un punto sobre el plano, que representa un elemento
  de un conjunto.
  Hereda de ElementoGrafo.
  Un vértice es un Elemento del Grafo que tiene: coordenadas en X y en Y, grado.

  Se definen el diámetro para dibujar un vértice y el contador de
  identificadores.
*/

import java.awt.*;

public final class Vertice extends ElementoGrafo{
	//Atributos de Clase
	private static int idCont = 0;
	private static int diam = 18;

	//Atributos de Instancia
	private final int x;
	private final int y;
	private int grado;

	//Métodos
	//Constructor
	public Vertice (int x, int y) {
		this.x = x;
		this.y = y;
		id = ++idCont;
		etiqueta = "Vertice " + id;
	}

	//Establecer conteo de ID
	public static void setContID(int id) {
		idCont = id;
	}

	//Obtener coordenada X
	public int getX() {
		return x;
	}

	//Obtener coordenada y
	public int getY() {
		return y;
	}

	//Establecer el grado del Vértice
	public void setGrado(int grado) {
		this.grado = grado;
	}

	//Obtener el grado del Vértice
	public int getGrado() {
		return grado;
	}

	//Pintar el punto sobre el Contenedor
	public void pintarElemento(Graphics g) {
		g.setColor(new Color(33,106,224));
		g.fillOval(x - diam/2, y - diam/2, diam, diam);
		if (estadoSeleccion) g.setColor(new Color(247,94,34));
		else g.setColor(new Color(15,47,99));
		((Graphics2D)g).setStroke(new BasicStroke(2));
		g.drawOval(x - diam/2, y - diam/2, diam, diam);
	}

	//Información del Punto
	public String toString() {
		return ("[" + x + "," + y + "]");
	}

	//Obtención del Diametro general de un Punto
	public static int getDiametro() {
		return diam;
	}
}
