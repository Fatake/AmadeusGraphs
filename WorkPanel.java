import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WorkPanel extends JPanel{
	//Variables del Panel
	ArrayList<Vertice> vertices = new ArrayList<>();
	ArrayList<Arista> aristas = new ArrayList<>();

	//Métodos
	//Constructor
	public WorkPanel() {
		super();
	}

	//Pintar en el Panel
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);

		for (Arista arista : aristas)
			arista.pintarElemento(g);

		for(Vertice vertice : vertices)
			vertice.pintarElemento(g);

	}

	//Adición de Vertces
	public void addVertice(Vertice vertice) {
		vertices.add(vertice);
		repaint();
	}
	//Adición de Aristas
	public void addArista(Arista arista) {
		aristas.add(arista);
		repaint();
	}

	//Obtención del Arreglo de Vertices
	public ArrayList<Vertice> getVertices() {
		return vertices;
	}
	//Obtención del Arreglo de Aristas
	public ArrayList<Arista> getAristas() {
		return aristas;
	}

	//Eliminación de Vértices
	public boolean delVertice(Vertice vertice) {
		if (vertices.remove(vertice)) {
			repaint();
			return true;
		}
		return false;
	}
	//Eliminación de Aristas
	public boolean delArista(Arista arista) {
		if (aristas.remove(arista)) {
			repaint();
			return true;
		}
		return false;
	}

	//Establecer arreglo de Vertices
	public void setVertices(ArrayList<Vertice> vertices) {
		this.vertices = vertices;
	}

	//Establecer arreglo de Aristas
	public void setAristas(ArrayList<Arista> aristas) {
		this.aristas = aristas;
	}

	//Establecer una nueva seleccion
	public void seleccionarElemento(ElementoGrafo elemento) {
		for(Arista arista : aristas)
			arista.setSeleccion(false);
		for(Vertice vertice : vertices)
			vertice.setSeleccion(false);

		if (elemento != null)
			elemento.setSeleccion(true);
		repaint();
	}

	//Reiniciar panel
	public void reiniciar() {
		vertices = new ArrayList<Vertice>();
		aristas = new ArrayList<Arista>();
		repaint();
	}
}
