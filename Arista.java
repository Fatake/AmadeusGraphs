/*Arista
  La Arista es la unión de dos vértices, representa la relación entre
  dos elementos del conjunto.
  Hereda de ElementoGrafo.
  La Arista es un Elemento del Grafo que tiene: Vértice Origen, Vértice Terminal.

  Se define el contador de Identificadores.
*/

import java.awt.*;

public final class Arista extends ElementoGrafo{
  //Atributos de Clase
  private static int idCont = 0;

  //Atributos de Instancia
  private final Vertice org; //Origen
  private final Vertice term; //Terminal

  //Métodos
  //Constructor
  public Arista(Vertice org, Vertice term) {
    this.org = org;
    this.term = term;
    id = ++idCont;
    etiqueta = org.getEtiqueta() + " - " + term.getEtiqueta();
  }

  //Obtener Origen
  public Vertice getOrigen() {
    return org;
  }

  //Obtener Terminal
  public Vertice getTerminal() {
    return term;
  }

  //Establecer conteo de ID
  public static void setContID(int id) {
    idCont = id;
  }

  //Obtener Pendiente
  //m = (y2 - y1) / (x2 - x1)
  public double getPendiente() {
    double dY = term.getY() - org.getY();
    double dX = term.getX() - org.getX();
    return (dY / dX);
  }

  //Obtener cruce con el eje Y
  //b = y - mx
  public double getCorteY() {
    return (org.getY() - getPendiente() * org.getX());
  }

  //Pintar sobre el Contenedor
  public void pintarElemento(Graphics g) {
    ((Graphics2D)g).setStroke(new BasicStroke(3));
    if (estadoSeleccion) g.setColor(new Color(247,94,34));
    else g.setColor(new Color(68,224,33));
    if (org == term) g.drawOval(org.getX(), org.getY(), 20, 20);
    else g.drawLine(org.getX(), org.getY(), term.getX(), term.getY());
  }

  //Retorno de Información
  public String toString() {
    return ("Origen 1: " + org.getEtiqueta()
          + "\nOrigen 2: " + term.getEtiqueta());
  }
}
