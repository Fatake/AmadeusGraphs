/*Panel de Detalles de un Elemento del Grafo
  Muestra:
  -Etiqueta del Elemento
  -Información (Arista: Origen y Terminal, Vërtice: Coordenadas)
  -Botón de Eliminar
*/
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PanelDetalles extends JPanel {
  //Atributos
  private final ElementoGrafo elemento;
  //Elementos Gráficos
  private final JPanel panelEtiqueta; //Panel superior
  private final JLabel etiquetaElemento; //Etiqueta del Elemento
  private JTextField edicionEtiqueta; //Campo para la edición de la etiqueta
  private final JTextArea infoElemento; //Campo de la información del Elemento
  private final JButton botonAplicarCambios; //Botón de Aplicación de Cambios a la Etiqueta
  private final JButton botonEliminar; //Botón de Eliminar Elemento

  //Métodos
  //Constructor
  public PanelDetalles(ElementoGrafo elemento) {
    super();
    panelSeleccionado(true);
    setLayout(new BorderLayout());
    setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51,51,51), null, null));

    this.elemento = elemento;

    if (elemento instanceof Vertice) {
      setMaximumSize(new Dimension(150,100));
      setMinimumSize(new Dimension(150,100));
    }
    else {
      setMaximumSize(new Dimension(150,125));
      setMinimumSize(new Dimension(150,125));
    }

    etiquetaElemento = new JLabel(elemento.getEtiqueta());

    //Adición de la Etiqueta al panel superior
    panelEtiqueta = new JPanel();
    panelEtiqueta.setLayout(new BorderLayout());
    panelEtiqueta.add(etiquetaElemento, BorderLayout.CENTER);
    panelEtiqueta.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        cambiarEtiqueta(evt);
      }
    });
    add(panelEtiqueta, BorderLayout.NORTH);

    //Adición de la información del Elemento
    infoElemento = new JTextArea();
    infoElemento.setText(elemento.toString());
    infoElemento.setEditable(false);
    add(infoElemento, BorderLayout.CENTER);

    //Adición del Botón de Eliminar
    botonEliminar = new JButton("Eliminar");
    botonEliminar.setFont(new Font("Arial", Font.BOLD, 14));
    botonEliminar.setBackground(new Color(237, 26, 26));
    botonEliminar.setForeground(Color.WHITE);
    add(botonEliminar, BorderLayout.SOUTH);

    //Creación del Boton de Cambios
    botonAplicarCambios = new JButton("Aplicar");
    botonAplicarCambios.setFont(new Font("Arial", Font.BOLD, 14));
    botonAplicarCambios.setBackground(new Color(49, 204, 38));
    botonAplicarCambios.setForeground(Color.WHITE);
    botonAplicarCambios.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        aplicarCambio(evt);
      }
    });
  }

  //Obtener el elemento asociado al panel
  public ElementoGrafo getElemento() {
    return elemento;
  }

  //Obtener el botón de Aplicar Cambios
  public JButton getBotonAplicarCambios() {
    return botonAplicarCambios;
  }
  //Obtener el botón de Eliminar
  public JButton getBotonEliminar() {
    return botonEliminar;
  }


  //Edición de la etiqueta
  public void cambiarEtiqueta(MouseEvent evt) {
    edicionEtiqueta = new JTextField(8);

    panelEtiqueta.removeAll();
    panelEtiqueta.add(edicionEtiqueta);
    edicionEtiqueta.requestFocusInWindow(); //Se coloca la entrada en el TextField
    edicionEtiqueta.setText(etiquetaElemento.getText()); //Se conserva el nombre por defecto

    remove(botonEliminar);
    add(botonAplicarCambios, BorderLayout.SOUTH);

    updateUI();
  }

  //Aplicacion del cambio de la etiqueta
  public void aplicarCambio(MouseEvent evt) {
    if (edicionEtiqueta.getText().length() != 0) {
      elemento.setEtiqueta(edicionEtiqueta.getText());
      etiquetaElemento.setText(elemento.getEtiqueta());

      panelEtiqueta.removeAll();
      panelEtiqueta.add(etiquetaElemento);

      remove(botonAplicarCambios);
      add(botonEliminar, BorderLayout.SOUTH);

      updateUI();
    }
  }

  //Comprobar pertenencia de elemento de grafo
  public boolean esPanelDe(ElementoGrafo elemento) {
    if (elemento == null) return false;
    return this.elemento.equals(elemento);
  }

  //Seleccionar Panel
  public void panelSeleccionado(boolean seleccionado) {
    if (seleccionado) {
      setBackground(new Color(99, 255, 174));
      requestFocusInWindow();
    }
    else setBackground(new Color(255, 255, 175));
  }

  //Deshabilitar boton de Eliminacion
  public void setBotonEliminarHabilitado(boolean valor) {
    botonEliminar.setEnabled(valor);
  }

  //Mostrar Información más detallada
  public void setInformacionDetallada(boolean valor) {
    if (valor && elemento instanceof Vertice)
      infoElemento.setText(elemento.toString() + "\nGrado: " + ((Vertice) elemento).getGrado());
    else
      infoElemento.setText(elemento.toString());
  }
}
