import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class VentanaPrincipal extends JFrame {
	//Constantes
	private final int WINDOW_LENGTHT = 1280; //Tamaño de la Vetana (frame)
	private final int WINDOW_HEIGTH = 720;
	private final Color PANEL_BACKGROUND = new Color(161,175,224);
	private final Color SUBPANEL_BACKGROUND = new Color(140,154,206);

	//Variables
	private Grafo grafo;
	private String ruta = "Sin Titulo.acm"; //Ruta del archivo abierto
	private boolean cambios = false; //Variable de cambios en el archivo actual
	private boolean botonCrearVerticeActivado = false; //Si fue activado el botón de Creación de Vertices
	private boolean botonCrearAristaActivado = false; //Si fue activado el botón de Creación de Aristas
	private boolean modoEditor;
	private Vertice newOrg = null;
	private Vertice newTerm = null;

	//Componentes Graficos
	//Contenedor Principal
	private Container contenedorPrincipal;

	//Paneles
	private WorkPanel workPanel;
	private JPanel mainPanel;
	private JPanel panelA;
	private JPanel panelB;

	private JPanel panelEditorGrafo;
	private JPanel panelEditorCamino;
	private JPanel panelInformacionGrafo;
	private JPanel panelPropiedadesGrafo;
	private JPanel panelInformacionArista;
	private JPanel panelInformacionVertice;
	private JPanel panelInfoGeneral;

	//Etiquetas
	private JLabel labelCrear;
	private JLabel labelBorrar;
	private JLabel infoLabel;
	private JLabel infoPuntosPanelLabel;
	private JLabel infoAristasPanelLabel;

	//Botones
	//Sección Editor de Grafo
	private JButton botonSeleccionarEditor;
	private JButton botonCrearVertice;
	private JButton botonCrearArista;
	//Seccion Propiedades del Grafo
	private JButton botonSeleccionarProp;
	private JButton	botonMatrizAdyacencia;
	private JButton	botonTrivial;
	private JButton botonCompleto;
	private JButton botonConexo;
	private JButton botonRecorridoEuler;
	private JButton botonCircuitoEuler;

	private JPanel infoPuntosPanel;
	private JPanel infoAristasPanel;

	private ArrayList<PanelDetalles> detallesVertices = new ArrayList<>();
	private ArrayList<PanelDetalles> detallesAristas = new ArrayList<>();

	private JScrollPane infoPuntosPanelScrollPane;
	private JScrollPane infoAristasPanelScrollPane;

	//Menus
	private JMenuBar menuPrincipal;

	private JMenu menuArchivo;
	private JMenuItem menuArchivoNuevo;
	private JMenuItem menuArchivoAbrir;
	private JMenuItem menuArchivoGuardar;
	private JMenuItem menuArchivoGuardarComo;

	private JMenu menuGrafo;
	private JMenuItem menuGrafoBorrar;
	private JMenuItem menuGrafoEditor;

	private JMenu menuCamino;
	private JMenuItem menuCaminoEditor;

	//Constructor
	public VentanaPrincipal() {
		Image icon = new ImageIcon(System.getProperty("user.dir") + "\\lib\\icon.png").getImage();
		setIconImage(icon);
		setTitle("AmadeusGraphs - " + ruta);
		setSize(WINDOW_LENGTHT, WINDOW_HEIGTH);
		setResizable(false);
		setLocationRelativeTo(null);
		initComponents();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //Evita el cierre automático de la ventana
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if (cambios) { //Ya se realizaron cambios
		      int jop = mostrarSeleccion("El archivo tiene cambios sin guardar, ¿Deseas guardar los cambios?");
		      if( jop == JOptionPane.YES_OPTION) {
						if(ruta.equals("Sin Titulo.acm")) guardarComoArchivo();
						else guardarArchivo();
						salir();
					}
					else if (jop == JOptionPane.NO_OPTION) salir();
		    }
				else salir();
			}
		});
		setVisible(true);
	}

	//
	//Inicialización de Componentes
	//
	public void initComponents(){
		//Contenedor
		Container contenedorPrincipal = getContentPane();
		contenedorPrincipal.setLayout(new BorderLayout());

		// Menu
		initMenus();

		// Panel de Trabajo
		workPanel = new WorkPanel();
		workPanel.setLayout(null);
		workPanel.setBackground(new Color(225,225,225));
		workPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51, 51, 51), null, null));
		workPanel.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent evt){
				workPanelMouseMoved(evt);
			}
		});
		workPanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt){
				workPanelMouseClicked(evt);
			}
		});
		contenedorPrincipal.add(workPanel,BorderLayout.CENTER);

		//Panel InfoGeneral Panel de abajo
		panelInfoGeneral = new JPanel();
		panelInfoGeneral.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51, 51, 51), null, null));

		//Etiqueta de Estado
		infoLabel = new JLabel("Posicion...");
		infoLabel.setSize(700,50);
		panelInfoGeneral.add(infoLabel);
		contenedorPrincipal.add(panelInfoGeneral,BorderLayout.SOUTH);

		//Panel Izquierdo (a)
		panelA = new JPanel();
		panelA.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51, 51, 51), null, null));
		initPanelA();
		contenedorPrincipal.add(panelA,BorderLayout.WEST);

		//Panel Derecho (b)
		panelB = new JPanel();
		panelB.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51, 51, 51), null, null));
		initPanelB();
		contenedorPrincipal.add(panelB,BorderLayout.EAST);
	}

	//
	//Inicialización de los Menús
	//
	private void initMenus(){
		//Fuentes
		Font MENU_FONT = new Font("Arial", Font.PLAIN, 14);
		Font MENUITEM_FONT = new Font("Arial", Font.PLAIN, 12);

		//Menu Principal
		menuPrincipal = new JMenuBar();

		//Menu Archivo
		menuArchivo = new JMenu("Archivo");
		menuArchivo.setFont(MENU_FONT);

		//Componentes Menu Archivo
		//Submenu Nuevo
		menuArchivoNuevo = new JMenuItem("Nuevo");
		menuArchivoNuevo.setFont(MENUITEM_FONT);
		menuArchivoNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menuArchivoNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menuArchivoNuevoMouseClicked(evt);
			}
		});
		menuArchivo.add(menuArchivoNuevo);

		//Submenu Arbrir
		menuArchivoAbrir = new JMenuItem ("Abrir");
		menuArchivoAbrir.setFont(MENUITEM_FONT);
		menuArchivoAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuArchivoAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menuArchivoAbrirMouseClicked(evt);
			}
		});
		menuArchivo.add(menuArchivoAbrir);
		menuArchivo.add(new JPopupMenu.Separator());

		//Submenu Guardar
		menuArchivoGuardar = new JMenuItem ("Guardar");
		menuArchivoGuardar.setFont(MENUITEM_FONT);
		menuArchivoGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuArchivoGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menuArchivoGuardarMouseClicked(evt);
			}
		});
		menuArchivo.add (menuArchivoGuardar);

		//Submenu Guardar Como
		menuArchivoGuardarComo = new JMenuItem ("Guardar como");
		menuArchivoGuardarComo.setFont(MENUITEM_FONT);
		menuArchivoGuardarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		menuArchivoGuardarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menuArchivoGuardarComoMouseClicked(evt);
			}
		});
		menuArchivo.add (menuArchivoGuardarComo);

		//Menu Grafo
		menuGrafo = new JMenu("Grafo");
		menuGrafo.setFont(MENU_FONT);

		//Componentes Menu Grafo
		//Submenu Borrar Grafo
		menuGrafoBorrar = new JMenuItem("Borrar Grafo");
		menuGrafoBorrar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		menuGrafoBorrar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				menuGrafoBorrarMouseClicked(evt);
			}
		});
		menuGrafo.add(menuGrafoBorrar);
		menuGrafo.add(new JPopupMenu.Separator());

		//Submenu Editor de Grafos
		menuGrafoEditor = new JMenuItem("Editor de Grafos");
		menuGrafoEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		menuGrafoEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Panel A
				panelEditorGrafo.setVisible(true);
				panelEditorCamino.setVisible(false);
				//Panel B
				panelPropiedadesGrafo.setVisible(false);
				panelInformacionGrafo.setVisible(true);

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //Establece el cursor del sistema como el DEFAULT_CURSOR
				modoEditor = true;
				botonCrearAristaActivado = false;
				botonCrearVerticeActivado = false;
				newOrg = newTerm = null;

				seleccionarElemento(null);
			}
		});
		menuGrafo.add(menuGrafoEditor);
		menuGrafo.add(new JPopupMenu.Separator());

		modoEditor = true;

		//Menu Caminos
		menuCamino = new JMenu("Propiedades");
		menuCamino.setFont(MENU_FONT);

		//Componentes menu Caminos
		//Submenu Editor de Caminos
		menuCaminoEditor = new JMenuItem("Propiedades del Grafo");
		menuCaminoEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		menuCaminoEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Panel A
				panelEditorGrafo.setVisible(false);
				panelEditorCamino.setVisible(true);
				//Panel B
				panelPropiedadesGrafo.setVisible(true);
				panelInformacionGrafo.setVisible(false);

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //Establece el cursor del sistema como el DEFAULT_CURSOR
				modoEditor = false;
				botonCrearAristaActivado = false;
				botonCrearVerticeActivado = false;
				newOrg = newTerm = null;

				seleccionarElemento(null);

				//Se crea el grafo correspondiente
				grafo = new Grafo(workPanel.getVertices(), workPanel.getAristas());
			}
		});
		menuCamino.add(menuCaminoEditor);

		//Se añaden todos los menus
		menuPrincipal.add(menuArchivo);
		menuPrincipal.add(menuGrafo);
		menuPrincipal.add(menuCamino);
		this.setJMenuBar(menuPrincipal);
	}

	//
	//Panel de la Izquierda (a)
	//
	private void initPanelA(){
		//Panel Editor Grafo
		panelEditorGrafo = new JPanel();
		panelEditorGrafo.setPreferredSize(new Dimension(120,200));
		panelEditorGrafo.setLayout(new GridLayout(4,1));

		//Boton de Selección 1
		botonSeleccionarEditor = new JButton("Seleccionar");
		botonSeleccionarEditor.setSize(80,30);
		botonSeleccionarEditor.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonSeleccionarMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorGrafo.add(botonSeleccionarEditor);

		//Etiqueta Crear: 2
		labelCrear = new JLabel("Herrramientas");
		labelCrear.setSize(80,30);
		labelCrear.setBackground(new Color(37,39,41));
		panelEditorGrafo.add(labelCrear);

		//Boton de Creación de Vertices 3
		botonCrearVertice = new JButton("Crear Vertice");
		botonCrearVertice.setSize(80,30);
		botonCrearVertice.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt){
				botonCrearVerticeMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt){
				botonMouseEntered(evt);
			}
		});
		panelEditorGrafo.add(botonCrearVertice);

		//Botón de Creación de Aristas 4
		botonCrearArista = new JButton("Crear Arista");
		botonCrearArista.setSize(80,30);
		botonCrearArista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt){
				botonCrearAristaMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt){
				botonMouseEntered(evt);
			}
		});
		panelEditorGrafo.add(botonCrearArista);

		//Panel Editor De Caminos
		panelEditorCamino = new JPanel();
		panelEditorCamino.setPreferredSize(new Dimension(120,400));
		panelEditorCamino.setLayout(new GridLayout(9,1));

		//Se pone invisible para no estorbar
		//Solo se cambia cuando se da clic en editor de caminos o ctl q
		panelEditorCamino.setVisible(false);

		//Boton de Selección
		botonSeleccionarProp = new JButton("Seleccionar");
		botonSeleccionarProp.setSize(80,30);
		botonSeleccionarProp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonSeleccionarMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonSeleccionarProp);

		panelEditorCamino.add(new JLabel("Propiedades"));

		//Botón de Grado del Grafo
		botonMatrizAdyacencia = new JButton("M. Adyacencia");
		botonMatrizAdyacencia.setSize(80,30);
		botonMatrizAdyacencia.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonMatrizAdyacenciaMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonMatrizAdyacencia);

		//Botón de determinación trivial
		botonTrivial = new JButton("Trivialidad");
		botonTrivial.setSize(80,30);
		botonTrivial.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonTrivialMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonTrivial);

		//Botón de determinación de Grafo Completo
		botonCompleto = new JButton("Completo");
		botonCompleto.setSize(80,30);
		botonCompleto.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonCompletoMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonCompleto);

		//Botón de Conexidad
		botonConexo = new JButton("Conexidad");
		botonConexo.setSize(80,30);
		botonConexo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonConexoMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonConexo);

		panelEditorCamino.add(new JLabel("Eulerianas"));

		//Botón de determinación de Recorrido Euleriano
		botonRecorridoEuler = new JButton("Recorrido");
		botonRecorridoEuler.setSize(80,30);
		botonRecorridoEuler.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonRecorridoEulerMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonRecorridoEuler);

		//Botón de
		botonCircuitoEuler = new JButton("Circuito");
		botonCircuitoEuler.setSize(80,30);
		botonCircuitoEuler.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				botonCircuitoEulerMouseClicked(evt);
			}
			public void mouseEntered(MouseEvent evt) {
				botonMouseEntered(evt);
			}
		});
		panelEditorCamino.add(botonCircuitoEuler);

		//Se Agregan los dos paneles al panel A
		panelA.add(panelEditorGrafo);
		panelA.add(panelEditorCamino);
	}

	//
	//Panel Derecho (b)
	//
	private void initPanelB(){
		//Panel de Informacion del Grafo
		panelInformacionGrafo = new JPanel();
		panelInformacionGrafo.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, new Color(51, 51, 51), null, null));
		panelInformacionGrafo.setLayout(new BoxLayout(panelInformacionGrafo, BoxLayout.Y_AXIS));

		//Etiqueta Area de Infomación de Puntos
		infoPuntosPanelLabel = new JLabel("Vertices: 0");
		infoPuntosPanelLabel.setForeground(new Color(37,39,41));
		panelInformacionGrafo.add(infoPuntosPanelLabel);

		//Area de Texto para Información de Puntos
		infoPuntosPanel = new JPanel();
		infoPuntosPanel.setLayout(new BoxLayout(infoPuntosPanel, BoxLayout.Y_AXIS));
		infoPuntosPanelScrollPane = new JScrollPane(infoPuntosPanel);
		infoPuntosPanelScrollPane.setPreferredSize(new Dimension(155,280));
		infoPuntosPanelScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		infoPuntosPanelScrollPane.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				infoPuntosPanelScrollPaneMouseEntered(evt);
			}
		});
		panelInformacionGrafo.add(infoPuntosPanelScrollPane);

		//Etiqueta Area de Infomación de Aristas
		infoAristasPanelLabel = new JLabel("Aristas: 0");
		infoAristasPanelLabel.setForeground(new Color(37,39,41));
		panelInformacionGrafo.add(infoAristasPanelLabel);

		//Area de Texto para Información de Aristas
		infoAristasPanel = new JPanel();
		infoAristasPanel.setLayout(new BoxLayout(infoAristasPanel, BoxLayout.Y_AXIS));
		infoAristasPanelScrollPane = new JScrollPane(infoAristasPanel);
		infoAristasPanelScrollPane.setPreferredSize(new Dimension(155,280));
		infoAristasPanelScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		infoAristasPanelScrollPane.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				infoAristasPanelScrollPaneMouseEntered(evt);
			}
		});
		panelInformacionGrafo.add(infoAristasPanelScrollPane);

		//Area de Información de un Camino
		panelPropiedadesGrafo = new JPanel();
		panelPropiedadesGrafo.setLayout(new BoxLayout(panelPropiedadesGrafo, BoxLayout.Y_AXIS));
		panelPropiedadesGrafo.setVisible(false);
		panelPropiedadesGrafo.setPreferredSize(new Dimension(155,280));
		panelPropiedadesGrafo.add(new JLabel("Propiedades"));

		//Se añaden los paneles al Panel B
		panelB.add(panelInformacionGrafo);
		panelB.add(panelPropiedadesGrafo);
	}

	//Actualización del Ambiente en modo Editor Grafo
	public void actualizarInfoEditorGrafo() {
		//Actualización del TextField de Vértices
		infoPuntosPanelLabel.setText("Vertices: " + workPanel.getVertices().size());
		infoPuntosPanel.removeAll();
		for(PanelDetalles panelDetalle : detallesVertices) {
			panelDetalle.setBotonEliminarHabilitado(true); //Se rehabilita el boton de eliminar
			panelDetalle.setInformacionDetallada(false);
			infoPuntosPanel.add(panelDetalle);
		}
		infoPuntosPanel.updateUI();

		//Actualización del TextField de Aristas
		infoAristasPanelLabel.setText("Aristas: " + workPanel.getAristas().size());
		infoAristasPanel.removeAll();
		for(PanelDetalles panelDetalle : detallesAristas) {
			panelDetalle.setBotonEliminarHabilitado(true); //Se rehabilita el boton de eliminar
			infoAristasPanel.add(panelDetalle);
		}
		infoAristasPanel.updateUI();
	}

	//Guardar archivo Como
  public void guardarComoArchivo(){
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle("Guardar como: ");
    jfc.setAcceptAllFileFilterUsed(false);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("ACM", "acm");
    jfc.addChoosableFileFilter(filter);

    do {
      int returnValue = jfc.showSaveDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION){
        ruta = jfc.getSelectedFile().getAbsolutePath().concat(".acm");
        File file = new File(ruta);

        if(file.exists()) { //El archivo existe?
          int jop = this.mostrarSeleccion("Desea sobreescribir el archivo existente");

          //Verificar si desea sobre escribir
          if (jop == JOptionPane.YES_OPTION) {
						guardarArchivo();
						break;
					}
        }
				else { //No existe el archivo se crea y se guarda
					guardarArchivo();
					break;
				}
      }
			else break;
    } while (true);
  }

	//Guardar Archivo
	public void guardarArchivo(){
	  //Abrir Archivo
	  if(Archivo.fopen(ruta, 'w')){
			ArrayList<ElementoGrafo> auxArray = new ArrayList<>();

			auxArray.addAll(workPanel.getVertices());
			auxArray.addAll(workPanel.getAristas());

	    //Escribir Archivo
	    if(!Archivo.fwrite((Object) auxArray))
	      mostrarAdvertencia("ERROR: No se puedo guardar el archivo");

	    //Cerrar Archivo
	    Archivo.fclose('w');

			cambios(false);
	  }
	  else mostrarAdvertencia("ERROR: No se puedo salvar el archivo ");
	}

	//Abrir Archivo
	public void abrirArchivo(){
	  JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	  jfc.setDialogTitle("Seleecione un archivo .acm");
	  jfc.setAcceptAllFileFilterUsed(false);
	  FileNameExtensionFilter filter = new FileNameExtensionFilter("ACM", "acm");
	  jfc.addChoosableFileFilter(filter);

	  int returnValue = jfc.showOpenDialog(null);
	  if (returnValue == JFileChooser.APPROVE_OPTION) {
	    reiniciarFrame(); //Reiniciar componentes
	    ruta = jfc.getSelectedFile().getPath();

	    //Abrir Archivo
	    if(Archivo.fopen(ruta, 'r')){
				//ArrayList's Auxiliares
	      ArrayList<Arista> auxAristas = new ArrayList<>();
	      ArrayList<Vertice> auxVertices = new ArrayList<>();;

				ArrayList<Object> auxArray = (ArrayList<Object>) Archivo.fread();

				for (Object elemento : auxArray) {
					if (elemento instanceof Vertice)
						auxVertices.add((Vertice) elemento);
					else
						auxAristas.add((Arista) elemento);
				}

				if (auxVertices == null) System.out.println("Vertices");
				if (auxAristas == null) System.out.println("Aristas");

        workPanel.setVertices(auxVertices);
        workPanel.setAristas(auxAristas);

        //Cerrar archivo
        Archivo.fclose('r');

        //Inicializar Vertices y Aristas
        for(Vertice vertice : workPanel.getVertices()){
          PanelDetalles nuevoPanelDetalle = new PanelDetalles(vertice);
          detallesVertices.add(nuevoPanelDetalle);

          //Implementacion del boton eliminar
          nuevoPanelDetalle.getBotonEliminar().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
              botonEliminarMouseClicked(evt, nuevoPanelDetalle);
            }
          });
        }

        for(Arista arista : workPanel.getAristas()){
          PanelDetalles nuevoPanelDetalle = new PanelDetalles(arista);
          detallesAristas.add(nuevoPanelDetalle);

          //Implementacion del boton eliminar
          nuevoPanelDetalle.getBotonEliminar().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
              botonEliminarMouseClicked(evt, nuevoPanelDetalle);
            }
          });
        }
        actualizarInfoEditorGrafo();
        seleccionarElemento(null);

        if (auxAristas.size() != 0) Arista.setContID(workPanel.getAristas().get(workPanel.getAristas().size()-1).getID()); //ID SE INICIALIZA EN LA ULTIMA ARISTA
        if (auxVertices.size() != 0) Vertice.setContID(workPanel.getVertices().get(workPanel.getVertices().size()-1).getID()); //ID SE INICIALIZA EN EL ULTIMO VERTICE

				cambios(false);
	    }
	  }
	}

	//Abrir Archivo nuevo
	public void reiniciarFrame() {
		workPanel.reiniciar();
		detallesAristas = new ArrayList<>();
		detallesVertices = new ArrayList<>();
		actualizarInfoEditorGrafo();

		ruta = "Sin Titulo.acm";
		cambios = false;
		setTitle("AmadeusGraphs - " + ruta);
	}

	//Salida del Programa
	private void salir() {
		setVisible(false); //Esconde la ventana
		dispose(); //Elimina los componentes gráficos y retorna la memoria al SO

		System.exit(0); //Termina el Proceso
	}

	//Establecer cambios
	public void cambios(boolean valor) {
		if (valor) setTitle("* AmadeusGraphs - " + ruta);
		else setTitle("AmadeusGraphs - " + ruta);

		cambios = valor;
	}

	//Mensaje de Advertencia
	public int mostrarSeleccion(String control) {
		return JOptionPane.showConfirmDialog(null, control, ruta, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	}

	//Mensaje de Error
	public void mostrarAdvertencia(String control) {
		JOptionPane.showMessageDialog(this, control, "Error", JOptionPane.ERROR_MESSAGE);
	}

	//
	//Eventos
	//
	//Eventos de workPanel
	public void workPanelMouseMoved(MouseEvent evt){
		infoLabel.setText("(" + evt.getX() + "," + evt.getY() + ")");
	}
	public void workPanelMouseClicked(MouseEvent evt){
		infoLabel.setText("Click en (" + evt.getX() + "," + evt.getY() + ")");
		if (!botonCrearVerticeActivado && !botonCrearAristaActivado) {
			//Se busca primero un vértice
			boolean esVertice = false;
			boolean esArista = false;
			for(Vertice vertice : workPanel.getVertices()){
				double dist = Math.sqrt(Math.pow(vertice.getX() - evt.getX(), 2) + Math.pow(vertice.getY() - evt.getY(), 2)); //Distancia entre ambos centros (vertice y newVertex)
				if(dist <= Vertice.getDiametro()/2){ //La distancia debe maxima de un radio
					esVertice = true;
					seleccionarElemento(vertice);
					break;
				}
			}

			//Si no, se busca una arista, se toma la primera ocurrencia
			if (!esVertice) {
				for (Arista arista : workPanel.getAristas()) {
					int x1 = arista.getOrigen().getX();
					int y1 = arista.getOrigen().getY();
					int x = evt.getX();
					int y = evt.getY();
					int x2 = arista.getTerminal().getX();
					int y2 = arista.getTerminal().getY();
					//Se busca el click dentro del rectángulo que forman los vértices de la arista como esquinas opuestas
					if (x1 < x && x < x2){
						if ((y1 < y && y < y2) || (y2 < y && y < y1)) {
							seleccionarElemento(arista);
							esArista = true;
							break;
						}
					}
					else if (x2 < x && x < x1)
						if ((y1 < y && y < y2) || (y2 < y && y < y1)) {
							seleccionarElemento(arista);
							esArista = true;
							break;
						}
				}

				if (!esArista) seleccionarElemento(null); //Deselecciona del panel
			}
		}
		if (modoEditor) {
			procesamientoEditor(evt);
		}
		else {
			procesamientoPropiedades(evt);
		}
	}

	//Procesamiento en modo Editor
	public void procesamientoEditor(MouseEvent evt) {
		if (botonCrearVerticeActivado) { //Creación de Vértices
			boolean correctVertex = true;

			for (Vertice vertice : workPanel.getVertices()) {
				double dist = Math.sqrt(Math.pow(vertice.getX() - evt.getX(), 2) + Math.pow(vertice.getY() - evt.getY(), 2)); //Distancia entre ambos centros (vertice y newVertex)
				if (dist < Vertice.getDiametro()) {
					correctVertex = false;
					break;
				} //La distancia debe ser de al menos dos diametros
			}
			if (!correctVertex) infoLabel.setText("No es posible encimar Vertices. Intente colocando el Vertice en otro punto.");
			else {
				//Adición al registro de Vertices
				Vertice newVertex = new Vertice(evt.getX(), evt.getY());
				workPanel.addVertice(newVertex);

				//Se crea el panel de Detalles y se añade al sistema
				PanelDetalles nuevoElementoPanel = new PanelDetalles(newVertex);
				nuevoElementoPanel.getBotonEliminar().addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						botonEliminarMouseClicked(evt, nuevoElementoPanel);
					}
				});
				detallesVertices.add(nuevoElementoPanel);

				//Se deja solo seleccionado el nuevo vértice
				seleccionarElemento(newVertex);

				//Actualización del Ambiente Gráfico
				actualizarInfoEditorGrafo();

				cambios(true);
			}
		}
		else if(botonCrearAristaActivado){ //Creación de Aristas
			boolean correctVertex = false;
			Vertice vertex = null;
			Arista edge = null;
			for(Vertice vertice : workPanel.getVertices()){
				double dist = Math.sqrt(Math.pow(vertice.getX() - evt.getX(), 2) + Math.pow(vertice.getY() - evt.getY(), 2)); //Distancia entre ambos centros (vertice y newVertex)
				if(dist <= Vertice.getDiametro()/2){ //La distancia debe maxima de un radio
					correctVertex = true;
					vertex = vertice; //Se guarda la referencia del vértice
					break;
				}
			}
			if(correctVertex){
				//Primera vez, se genera un Origen
				if (newOrg == null) newOrg = vertex;
				//Segunda vez, se genera un Terminal, se construye la arista y se resetean el Orgien y el Terminal
				else if (newOrg != null && newTerm == null && vertex != newOrg) {
					newTerm = vertex;
					edge = new Arista(newOrg, newTerm); //Creación de la Arista

					newOrg.setGrado(newOrg.getGrado() + 1);
					newTerm.setGrado(newTerm.getGrado() + 1);

					newOrg = newTerm = null;

					//Adición al Arreglo de Aristas
					workPanel.addArista(edge);

					//Se crea el panel de Detalles y se añade al sistema
					PanelDetalles nuevoElementoPanel = new PanelDetalles(edge);
					nuevoElementoPanel.getBotonEliminar().addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							botonEliminarMouseClicked(evt, nuevoElementoPanel);
						}
					});
					detallesAristas.add(nuevoElementoPanel);

					//Se deja solo seleccionada la nueva Arista
					seleccionarElemento(edge);

					//Actualización del Ambiente Gráfico
					actualizarInfoEditorGrafo();

					cambios(true);
				}
			}
			else infoLabel.setText("No se selecciono ningun punto.");
		}// fin de las funciones de workPanel
	}

	//Procesamiento en modo Propiedades
	public void procesamientoPropiedades(MouseEvent evt) {

	}

	//Atencion sobre el panel del elemento seleccionado
	private void seleccionarElemento(ElementoGrafo elemento) {
		workPanel.seleccionarElemento(elemento);

		if (modoEditor) {
			//Se actualizan los paneles de detalles
			for (PanelDetalles detalle : detallesAristas) {
				if (detalle.esPanelDe(elemento)) detalle.panelSeleccionado(true);
				else detalle.panelSeleccionado(false);
			}
			for (PanelDetalles detalle : detallesVertices) {
				if (detalle.esPanelDe(elemento)) detalle.panelSeleccionado(true);
				else detalle.panelSeleccionado(false);
			}

			actualizarInfoEditorGrafo();
		}
		else {
			//Se muestra el panel de detalles del elemento seleccionado
			panelPropiedadesGrafo.removeAll();

			if (elemento == null) {
				panelPropiedadesGrafo.add(new JLabel("Propiedades"));
			}
			else {
				for (PanelDetalles detalle : detallesAristas)
					if (detalle.esPanelDe(elemento)) {
						detalle.panelSeleccionado(true);
						detalle.setBotonEliminarHabilitado(false); //Se impide la eliminación
						panelPropiedadesGrafo.add(detalle);
					}
				for (PanelDetalles detalle : detallesVertices)
					if (detalle.esPanelDe(elemento)) {
						detalle.panelSeleccionado(true);
						detalle.setBotonEliminarHabilitado(false); //Se impide la eliminación
						detalle.setInformacionDetallada(true);
						panelPropiedadesGrafo.add(detalle);
					}
			}

			panelPropiedadesGrafo.updateUI();
		}
	}

	//Eventos de Menú
	//Click sobre menuArchivoNuevo
	public void menuArchivoNuevoMouseClicked(ActionEvent evt) {
		if (cambios) {
			int jop = mostrarSeleccion("El archivo tiene cambios sin guardar, ¿Deseas guardar los cambios?");
      if (jop == JOptionPane.YES_OPTION) {
				guardarArchivo();
				reiniciarFrame();
			}
			else if (jop == JOptionPane.NO_OPTION) reiniciarFrame();
		}
		else reiniciarFrame();
	}

	//Click sobre menuArchivoAbrir
	public void menuArchivoAbrirMouseClicked(ActionEvent evt){
    if (cambios) { //Ya se realizaron cambios
      int jop = mostrarSeleccion("El archivo tiene cambios sin guardar, ¿Deseas guardar los cambios?");
      if( jop == JOptionPane.YES_OPTION) {
				guardarArchivo();
				abrirArchivo();
			}
			else if (jop == JOptionPane.NO_OPTION) abrirArchivo();
    }
		else abrirArchivo();
	}

	//Click sobre menuArchivoGuardar
	public void menuArchivoGuardarMouseClicked(ActionEvent evt){
    if (ruta.equals("Sin Titulo.acm")) guardarComoArchivo();
    else guardarArchivo();
	}

	//Click sobre menuArchivoGuardarComo
	public void menuArchivoGuardarComoMouseClicked(ActionEvent evt){
    guardarComoArchivo();
  }

	//Click sobre menuGrafoBorrar
	public void menuGrafoBorrarMouseClicked(ActionEvent evt) {
		reiniciarFrame();
	}

	//Eventos de botonSeleccionar
	public void botonSeleccionarMouseClicked(MouseEvent evt) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //Establece el cursor del sistema como el DEFAULT_CURSOR
		((JButton) evt.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		botonCrearAristaActivado = false;
		botonCrearVerticeActivado = false;
		newOrg = newTerm = null;
	}

	//Eventos de botonCrearVertice
	public void botonCrearVerticeMouseClicked(MouseEvent evt) {
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); //Establece el cursor del sistema como el CROSSHAIR_CURSOR
		botonCrearVertice.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		botonCrearAristaActivado = false;
		botonCrearVerticeActivado = true;
		newOrg = newTerm = null;
	}

	//Eventos de botonCrearArista
	public void botonCrearAristaMouseClicked(MouseEvent evt) {
		setCursor(new Cursor(Cursor.MOVE_CURSOR)); //Establece el cursor del sistema como el MOVE_CURSOR
		botonCrearArista.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		botonCrearAristaActivado = true;
		botonCrearVerticeActivado = false;
		newOrg = newTerm = null;
	}

	//Eventos de infoPuntosPanelScrollPane
	public void infoPuntosPanelScrollPaneMouseEntered(MouseEvent evt) {
		infoPuntosPanelScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	//Eventos de infoAristasPanelScrollPane
	public void infoAristasPanelScrollPaneMouseEntered(MouseEvent evt) {
		infoAristasPanelScrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	//Eventos de botonEliminar de los paneles de detalles
	public void botonEliminarMouseClicked(MouseEvent evt, PanelDetalles detalleElemento) {
		if (((JButton) evt.getSource()).isEnabled()) {
			if(detalleElemento.getElemento() instanceof Vertice) {
				//Se remueven las aristas asociadas al vertice
				ArrayList<Arista> aristasPorEliminar = new ArrayList<>();
				for (Arista arista : workPanel.getAristas())
					if (arista.getOrigen() == detalleElemento.getElemento() || arista.getTerminal() == detalleElemento.getElemento())
						aristasPorEliminar.add(arista);

				ArrayList<PanelDetalles> panelesPorEliminar = new ArrayList<>();
				for (Arista arista : aristasPorEliminar) {
					arista.getOrigen().setGrado(arista.getOrigen().getGrado() - 1);
					arista.getTerminal().setGrado(arista.getTerminal().getGrado() - 1);
					workPanel.delArista(arista);

					for (PanelDetalles panelArista : detallesAristas)
						if (panelArista.esPanelDe(arista)) panelesPorEliminar.add(panelArista);
				}

				for (PanelDetalles panelArista : panelesPorEliminar)
					detallesAristas.remove(panelArista);

				workPanel.delVertice((Vertice) detalleElemento.getElemento());
				detallesVertices.remove(detalleElemento);
			}
			else {
				workPanel.delArista((Arista) detalleElemento.getElemento());
				detallesAristas.remove(detalleElemento);
			}

			actualizarInfoEditorGrafo();
			seleccionarElemento(null);

			cambios(true);
		}
	}

	//Eventos de botonMatrizAdyacencia
	public void botonMatrizAdyacenciaMouseClicked(MouseEvent evt) {
		JTextArea textArea = new JTextArea(grafo.mostrarMatrizAdyacencia());
		textArea.setEditable(false);
		JOptionPane optionPane = new JOptionPane(textArea);
		JDialog dialog = optionPane.createDialog(this,"Matriz Adyacencia");
		dialog.setVisible(true); dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	//Eventos de botonTrivial
	public void botonTrivialMouseClicked(MouseEvent evt) {
		if (grafo.esTrivial())
			JOptionPane.showMessageDialog(this, "El grafo es Trivial", "Trivialidad", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "El grafo no es Trivial", "Trivialidad", JOptionPane.INFORMATION_MESSAGE);
	}

	//Eventos de botonCompleto
	public void botonCompletoMouseClicked(MouseEvent evt) {
		if (grafo.esCompleto())
			JOptionPane.showMessageDialog(this, "El grafo es Completo", "Grafo Completo", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "El grafo no es Completo", "Grafo Completo", JOptionPane.INFORMATION_MESSAGE);
	}

	//Eventos de botonConexo
	public void botonConexoMouseClicked(MouseEvent evt) {
		if (grafo.esConexo())
			JOptionPane.showMessageDialog(this, "El grafo es Conexo", "Conexidad", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "El grafo no es Conexo", "Conexidad", JOptionPane.INFORMATION_MESSAGE);
	}

	//Eventos de botonRecorridoEuler
	public void botonRecorridoEulerMouseClicked(MouseEvent evt) {
		if (grafo.tieneRecorridoEuler())
			JOptionPane.showMessageDialog(this, "El grafo tiene al menos un Recorrido Euleriano", "Recorrido Euleriano", JOptionPane.INFORMATION_MESSAGE);
		else
				JOptionPane.showMessageDialog(this, "El grafo no tiene ningun Recorrido Euleriano", "Recorrido Euleriano", JOptionPane.INFORMATION_MESSAGE);
	}

	//Eventos de botonCircuitoEuler
	public void botonCircuitoEulerMouseClicked(MouseEvent evt) {
		if (grafo.tieneCircuitoEuler())
			JOptionPane.showMessageDialog(this, "El grafo tiene al menos un Circuito Euleriano", "Circuito Euleriano", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "El grafo no tiene ningun Circuito Euleriano", "Circuito Euleriano", JOptionPane.INFORMATION_MESSAGE);
	}

	//Evento general: Cursor entro en Boton
	public void botonMouseEntered(MouseEvent evt) {
		((JButton) evt.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
	}


	//Programa Principal
	public static void main (String[] args) {
		VentanaPrincipal AmadeusGraphs = new VentanaPrincipal();
	}
}
