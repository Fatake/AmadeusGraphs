/*Clase Grafo
  Almacena la matriz de adyacencia generada por la relación que implica
  el grafo dibujado por el usuario.
  No contiene vértices ni aristas.

  Determina las propiedades del grafo que genera el usuario.
*/
import java.util.ArrayList;

public final class Grafo {
  //Atributos
  private final boolean[][] matAdy;

  //Métodos
  //Constructor
  public Grafo(ArrayList<Vertice> vertices, ArrayList<Arista> aristas){
    //Creación de matriz adyacencia
    matAdy = new boolean[vertices.size()][vertices.size()]; //Se inicializa en false

    for (Arista arista : aristas) {
      matAdy[vertices.indexOf(arista.getOrigen())][vertices.indexOf(arista.getTerminal())] = true;
      matAdy[vertices.indexOf(arista.getTerminal())][vertices.indexOf(arista.getOrigen())] = true;
    }
  }

  //Obtener el grado del grafo
  public boolean esTrivial() {
    if (matAdy.length == 1) return true; //Si solo hay un vértice es trivial
    return false;
  }

  //Recorrer la matriz en busca de las relaciones
  private boolean[] recorridoAnchura(){
    //Array visitados inicializados en false
    boolean[] visitados = new boolean[matAdy.length];
    //El nodo inicial ya esta visitado
    visitados[0] = true;

    //Cola de visitas de los nodos adyacentes
    ArrayList<Integer> cola = new ArrayList<>();

    //Se agrega el nodo a la cola de visitas
    cola.add(0);

    while(!cola.isEmpty()){
      int j = cola.remove(0); //Se saca el primero de la cola

      //Se busca en la matriz que representa el grafo los nodos adyacentes
      for (int i = 0 ; i < matAdy.length ; i++)
        //Si un nodo es adyacente y no está visitado entonces
        if(matAdy[j][i] == true && !visitados[i]){
          cola.add(i); //se agrega a la cola de visitas
          visitados[i] = true;
        }
    }

    return visitados;
  }

  //Determinar si el grafo es conexo
  public boolean esConexo(){
    if(matAdy.length <= 1) return true;

    boolean[] recorrido = recorridoAnchura();
    for (boolean bool : recorrido)
      if(!bool) return false;

    return true;
  }

  //Determinar si el grafo es disconexo
  public boolean esCompleto(){
    for(int i = 0 ; i < matAdy.length ; i++)
      for (int j = 0 ; j < matAdy.length ; j++)
        if(i != j && !matAdy[i][j]) return false;
    return true;
  }

  //Determinar si el grafo tiene Circuito de Euler
  public boolean tieneCircuitoEuler(){
    for(int i = 0 ; i < matAdy.length ; i++){
      int cont = 0;
      for (int j = 0 ; j < matAdy.length ; j++)
        if(matAdy[i][j]) cont++;

      if (cont % 2 != 0) return false;
    }
    return true;
  }

  //Determinar si el grafo tiene Recorrido de Euler
  public boolean tieneRecorridoEuler(){
    int contImp = 0;

    for (int i = 0 ; i < matAdy.length ; i++){
      int cont=0;
      for (int j = 0 ; j < matAdy.length ; j++)
        if(matAdy[i][j]) cont++;

      if (cont % 2 != 0) contImp++;
    }
    return (contImp == 2);
  }

  //Mostrar Matriz Advertencia
  public String mostrarMatrizAdyacencia(){
        StringBuilder s = new StringBuilder();
        s.append("   ");
        for(int i = 0 ; i < matAdy.length ; i++) s.append("  |").append(i+1).append("|"); s.append('\n');
        for(int i = 0 ; i < matAdy.length ; i++){
            s.append("|").append(i+1).append("|");
            for (int j = 0 ; j < matAdy.length ; j++){
              s.append("  ");
              if(matAdy[i][j]) s.append(1);
              else s.append(0);
              if(j < (matAdy[0].length -1)) s.append("   ");
            }
            s.append('\n');
        }
        return s.toString();
    }
}
