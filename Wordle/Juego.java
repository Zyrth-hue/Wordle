//Practica hecha por Limberth Poma Fernandez

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Juego extends JPanel {
    final private Font mainFont = new Font("Sans-serif", Font.PLAIN, 26); // Fuente predeterminada
    final private Font mainFontJuego = new Font("Times New Roman", Font.PLAIN, 65); // Fuente predeterminada para el
                                                                                    // juego
    final private Color bgcolor = new Color(41, 51, 91);// fondo (Background)
    final private Color buttonColor = new Color(35, 154, 107); // fondo (Button)
    private Partida p = null;
    private boolean iniciarioJuego = false; // comprobante si seJuego el juego o no
    private JLabel[][] jlCampos = new JLabel[6][5]; // array para contener los campos
    private JLabel jlAviso = new JLabel(); // jlabel para contener los avisos
    private JLabel jlScore = new JLabel(); // jLabel para mostrar la puntuacion
    private JLabel jlPalAdi = new JLabel(); // jlabel para mostrar las palabras adivinadas
    private JLabel jlUltiPal = new JLabel(); // jlabel para mostrar la ultima pal. adivinada
    private JButton jbSave = new JButton(); // jbutton para guardar
    private JTextField jtTexto = new JTextField(); // campo del usuario para ingresar texto
    private int filaActual = 0; // guardar la fila actual del juego
    private int vidas = 6; // guardar las vidas restantes
    private static boolean guardado = false; // se pone en true cuando se guarda el juego

    static public void setGuardad(boolean guardar) {
        guardado = guardar;
    }

    public static boolean getGuardado() {
        return guardado;
    }

    public void setPartida(Partida p) { // traer una partida cargada de GameLoader.java
        this.p = p;
    }

    public boolean partidaVacia() { // verificacion si el juego inicia o no con una partida nueva o cargada
        if (p == null) {
            return true; // si es null es porque es nueva
        }
        return false; // si no, false porque es una partida cargada (usada)
    }

    public boolean panelValido(JLabel[][] campo) { // verificacion para un panel vacio
        for (int i = 0; i < campo.length; i++) { // se recorre todos los campos
            for (int j = 0; j < campo[i].length; j++) {// buscando una sola ocurrecia
                if (campo[i][j] == null) { // en caso sea nula, es porque el panel no es valido
                    return true; // se devuelve true para crear un panel nuevo
                }
            }
        }
        return false; // si no hay nulos, el panel es valido para usarse y no es necesario crear uno
                      // nuevo
    }

    public void initialize() {
        JPanel jpMain = createMainPanel();
        add(jpMain);
        setBackground(bgcolor);
    }

    private JPanel createMainPanel() { // funcion "padre" que construye los distintos paneles que lo conforma
        JPanel jpMain = new JPanel(); // JPMain principal
        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpMain.setBackground(bgcolor);

        JPanel jpScore = scorePanel();
        jpMain.add(jpScore);

        JPanel jpCenter = centerPanel();
        jpMain.add(jpCenter);

        JPanel jpText = textPanel();
        jpMain.add(jpText);

        JPanel jpButton = buttonPanel(jpMain);
        jpMain.add(jpButton);

        return jpMain;
    }

    /* Funcion generica para cualquier Jlabel usado margen X y Y */
    private JLabel addJlabel(JPanel panel, Font main, int gridx, int gridy) {
        JLabel jl = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50); // Margen
        jl.setFont(mainFont);
        jl.setForeground(Color.white);
        // jl.setBackground(bgcolor);
        gbc.gridx = gridx; // posicion X = Columna
        gbc.gridy = gridy; // posicion Y = fila
        panel.add(jl, gbc);

        return jl;
    }

    /* Funcion generica para cualquier JLabel usando solo Margen X */
    private JLabel addJlabel(JPanel panel, Font main, int gridx) {
        JLabel jl = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50); // Margen
        jl.setFont(mainFont);
        jl.setForeground(Color.white);
        // jl.setBackground(bgcolor);
        gbc.gridx = gridx; // posicion X = Columna
        panel.add(jl, gbc);

        return jl;
    }

    /* Funcion generica para cualquier JTextField, siempre se usan margenes X & Y */
    private JTextField addTextField(JPanel panel, Font main, Color color, int gridx, int gridy, int ancho, int alto) {
        JTextField jt = new JTextField();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margen
        jt.setFont(mainFont);
        jt.setBackground(color);
        jt.setPreferredSize(new Dimension(ancho, alto));
        gbc.gridx = gridx; // posicion X = Columna
        gbc.gridy = gridy; // posicion Y = fila
        panel.add(jt, gbc);

        return jt;
    }

    /*
     * Creacion del primer panel, JPScore, alberga los distintos campos como la
     * puntuacion,
     * las palabras adivinadas y la ultima palabra adivinada
     */
    private JPanel scorePanel() {
        if (partidaVacia()) { // en caso de que la partida sea null
            p = new Partida(); // se crea una nueva partida
            p.palabraAleatoria();
        } else {
            jlCampos = p.getCampos(); // se cargan los campos de la partida
            filaActual = p.getFilaActual(); // o se inicia en la ultima fila actual de la partida
        }
        JPanel jpScore = new JPanel();
        jpScore.setLayout(new GridBagLayout());
        jpScore.setBackground(bgcolor);

        jlScore = addJlabel(jpScore, mainFont, 1); // se usan las funciones genericas Jlabel
        jlScore.setText("Puntuacion: " + p.getPuntuacion()); // se añade un texto junto a su campo

        jlPalAdi = addJlabel(jpScore, mainFont, 2);
        jlPalAdi.setText("Palabras_Adivinadas " + p.getPalabrasAdivinadas());

        jlUltiPal = addJlabel(jpScore, mainFont, 3);
        jlUltiPal.setText("Ultima Palabra: " + p.getUltimaPalabraAdi());
        return jpScore;
    }

    /*
     * Creacion del segundo Jpanel, alberga todo lo que seria el "Tablero" del juego
     */
    private JPanel centerPanel() {
        JPanel jpCenter = new JPanel();
        jpCenter.setLayout(new GridBagLayout());
        jpCenter.setBackground(bgcolor);
        if (panelValido(jlCampos)) { // se verifica si el array de JLabel es apto para insertarse
            invocarCentral(jpCenter); // en caso que no sea valido, se crea un nuevo campo
        } else { // caso de ser uno ya usado, se carga este mismo y no se crea uno nuevo
            invocarCentral(jpCenter, p.getCampos());
        }
        return jpCenter;
    }

    /*
     * Creacion del Tercer JPanel, donde se alberga algunos campos como el Jlabel y
     * TextField para recoger el text
     * y el JLAviso
     */
    private JPanel textPanel() {
        JPanel jpText = new JPanel();
        jpText.setLayout(new GridBagLayout());
        jpText.setBackground(bgcolor);

        JLabel jlAyuda = addJlabel(jpText, mainFont, 1, 1);
        jlAyuda.setText("Ingrese Su texto Aqui: ");
        jtTexto = addTextField(jpText, mainFont, Color.WHITE, 2, 1, 120, 30);
        jtTexto.setEditable(iniciarioJuego); // se crea Desactivado hasta que se inicie el juego
        jlAviso = addJlabel(jpText, mainFont, 1, 2);

        return jpText;
    }
    /* Creacion del Cuarto y Ultimo JPanel, todos los botones disponibles. . . */

    private JPanel buttonPanel(JPanel jpMain) {
        JPanel jpButton = new JPanel();
        jpButton.setLayout(new GridBagLayout()); // orientacion de izquirda a derecha
        jpButton.setBackground(bgcolor);
        GridBagConstraints gbcButton = new GridBagConstraints(); // se crea un nuevo GBC con constraints especificas
        gbcButton.insets = new Insets(10, 50, 10, 50); // Margen
        /* BOTON SALIR */
        JButton jbExit = new JButton("Salir");
        jbExit.setFont(mainFont);
        jbExit.setForeground(Color.white);
        jbExit.setBackground(buttonColor);
        jbExit.setFocusPainted(false);
        jbExit.addActionListener(e -> {
            if (guardado) { // solamente se puede regresar si el usuario ha guardado la partida

                MainFrame f = new MainFrame(); // este boton inicializa nuevamente el MainFrame
                f.initialize(); // dando la apariencia de que se ha "retrocedido"
                f.setSize(1200, 750);
                f.setResizable(false);
                f.setLocationRelativeTo(null);

                jpMain.removeAll();// se remueven todos los componentes existentes
                jpMain.add(f); // se añade el objeto MainFrame
                jpMain.revalidate();
                jpMain.repaint();
            } else { // caso contrario se mostrara el texto hasta que se guarde
                jlAviso.setText("La partida no se ha guardado");
            }
        });
        gbcButton.gridy = 2; // fila 2
        gbcButton.gridx = 1; // columna 1
        jpButton.add(jbExit, gbcButton);

        /* BOTON GUARDAR */
        jbSave = new JButton("Guardar");
        jbSave.setFont(mainFont);
        jbSave.setForeground(Color.white);
        jbSave.setBackground(buttonColor);
        jbSave.setFocusPainted(false);
        jbSave.addActionListener(e -> {
            if (existeUsuario()) { // verificar si la partida tiene un usuario registrado
                // en caso lo tenga, se guarda la partida directamente
                guardarPartida(); // se gestiona el sistema de guardado de partidas
                guardado = true; // con esto en true, ya es posible abandonar la partida
            } else {
                // en caso no, se pide el usaurio y se guarda
                if (pedirUsuario()) {
                    guardarPartida();
                    guardado = true;
                }
            }
        });
        gbcButton.gridy = 2; // fila 2
        gbcButton.gridx = 2; // columna 2
        jbSave.setEnabled(iniciarioJuego); // se desactiva hasta que se inicie el juego
        jpButton.add(jbSave, gbcButton);

        /* BOTON ACEPTAR TEXTO */
        JButton jbAceptar = new JButton("Aceptar");
        jbAceptar.setFont(mainFont);
        jbAceptar.setForeground(Color.white);
        jbAceptar.setBackground(buttonColor);
        jbAceptar.setFocusPainted(false);
        jbAceptar.addActionListener(e -> {
            //se convierte la palabar a minuscula para evitar cualquier problema
            String textoMin = jtTexto.getText().toLowerCase();
            if (esTexto(textoMin)) { // se revisa que se ingresen solo letras
                if (textoMin.length() == 5) { // se verifica que el texto contenga solo 5 letras

                    // se verifica que la palabta introducida este en el fichero de palabras
                    if (p.palabraValida(textoMin)) {
                        jlAviso.setText(""); // se restablece el aviso
                        // se ejecuta el proceso para escribir las palabras en el panel
                        escribirPanel(p.getCampos(), textoMin);
                        jtTexto.setText(""); // se restablece el campo donde se ingresa el texto
                    } else {
                        jlAviso.setText("La palabra ingresada no es valida, ingrese otra");
                        jtTexto.setText("");
                    }
                } else {
                    jlAviso.setText("El texto no tiene 5 letras");
                }
            } else {
                jlAviso.setText("ingrese solo texto");
            }
            System.out.println(p.getPalabraSecreta());
        });
        gbcButton.gridy = 2; // fila 2
        gbcButton.gridx = 3; // columna 3
        jbAceptar.setEnabled(iniciarioJuego); // inicia descativado hasta que el usuario inicie el juego
        jpButton.add(jbAceptar, gbcButton);

        /* BOTON EMPEZAR JUEGO */
        JButton jbEmpezarJuego = new JButton("Empezar partida");
        jbEmpezarJuego.setFont(mainFont);
        jbEmpezarJuego.setForeground(Color.white);
        jbEmpezarJuego.setBackground(buttonColor);
        jbEmpezarJuego.setFocusPainted(false);
        jbEmpezarJuego.addActionListener(e -> {
            // se crea una nueva ventana TEMPORAL para la confirmacion
            JFrame jf = new JFrame();
            jf.setVisible(true);
            jf.setSize(620, 220);
            jf.setResizable(false); // Evita que la ventana sea redimensionable
            jf.setLocationRelativeTo(null); // Coloca la ventana en el centro del monitor
            jf.setTitle("ESTAS SEGURO?. . . ");

            /* JPANEL DE AVISOS */
            JPanel jpAviso = new JPanel();
            jpAviso.setBackground(bgcolor);
            jpAviso.setLayout(new GridBagLayout());

            /* MARGENEs */
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 90, 5, 90); // Margen

            /* BOTON ACEPTAR */
            JButton jbNewGame = new JButton("Empezar");
            jbNewGame.setFont(mainFont);
            jbNewGame.setBackground(Color.GREEN);
            jbNewGame.setFocusPainted(false);
            jbNewGame.addActionListener(ev -> {
                
                iniciarioJuego = true; // Se marca que empezo el juego
                guardado = false; // ahora no se podra salir hasta que se guarde

                Window parentWindow = SwingUtilities.windowForComponent(jbNewGame);
                parentWindow.dispose(); // necesario para que solo se cierre la ventana temporal

                jbAceptar.setEnabled(iniciarioJuego); // se activa el boton de aceptar Texto
                jbSave.setEnabled(iniciarioJuego); // Se activa el boton solo si se ha iniciado el juego
                jtTexto.setEditable(iniciarioJuego); // se activa la entrada de texto

                jpButton.remove(jbEmpezarJuego); // quitar el boton una vez se inicia el juego
                jpButton.revalidate();
                jpButton.repaint();
                System.out.println(p.getPalabraSecreta());

            });

            gbc.gridx = 1;
            jpAviso.add(jbNewGame, gbc);

            /* BOTON CANCELAR */
            JButton jbCancelar = new JButton("Cancelar");
            jbCancelar.setFont(mainFont);
            jbCancelar.setForeground(Color.white);
            jbCancelar.setBackground(new Color(255, 0, 0));
            jbCancelar.setFocusPainted(false);
            jbCancelar.addActionListener(ev -> {
                Window parentWindow = SwingUtilities.windowForComponent(jbCancelar);
                parentWindow.dispose(); // Cierra la ventana actual y no todo el juego
            });
            gbc.gridx = 2;
            jpAviso.add(jbCancelar, gbc);

            jf.add(jpAviso);

        });
        gbcButton.gridy = 3; // fila 3
        gbcButton.gridx = 2; // columna 2 (medio)
        jpButton.add(jbEmpezarJuego, gbcButton);

        return jpButton;
    }

    private boolean esTexto(String palabra) { // Verificacion para que solo se ingresen letras
        for (int i = 0; i < palabra.length(); i++) {
            if (!Character.isLetter(palabra.charAt(i))) { // se verifica letra por letra
                return false; // si no es una letra, devuelve false
            }
        }
        return true; // si la palabra completa son letras, true
    }

    private boolean existeUsuario() {
        if (p.getUsuario().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean pedirUsuario() {
        /* Frame para pedir Nombre de Usaurio */
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(620, 220);
        jf.setResizable(false); // Evita que la ventana sea redimensionable
        jf.setLocationRelativeTo(null); // Coloca la ventana en el centro del monitor
        jf.setTitle("Ingrese el Usuario");

        /* Panel Principal */
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new GridBagLayout());
        jpMain.setBackground(bgcolor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 20, 5); // Margen

        /* TEXTField Para ingreso del usaurio */
        JTextField jtUsuario = addTextField(jpMain, mainFont, bgcolor, 0, 1, 120, 30);
        jtUsuario.setForeground(Color.white);

        /* BOTON PARA CONTINUAR */
        JButton jbAceptar = new JButton("Ingresar Usaurio");
        jbAceptar.setFont(mainFont);
        jbAceptar.setFocusPainted(false);
        jbAceptar.setForeground(Color.white);
        jbAceptar.setBackground(new Color(255, 0, 0));
        jbAceptar.addActionListener(e -> {
            if (!jtUsuario.getText().isEmpty()) { // no se hara nada hasta que el usuario ingrese algo
                p.setUsuario(jtUsuario.getText());
                escribirPuntuacion();
                Window parentWindow = SwingUtilities.windowForComponent(jbAceptar);
                parentWindow.dispose(); // Cierra la ventana actual y no todo el juego
                return;
            }
        });
        gbc.gridy = 2;
        jpMain.add(jbAceptar, gbc);

        jf.add(jpMain);
        return true;
    }

    private void escribirPuntuacion() {
        Score sc = new Score();
        sc.insertarPuntuacion(p.getUsuario(), p.getPuntuacion(), sc.getPuntuacion());
    }

    private void guardarPartida() { // funcion encargada para guardar partidas
        Loader gm = new Loader(); // se instancia un objeto de la clase especializada
        /*
         * Se guarda la partida en base a un conteo, este es la cantidad del Save
         * existentes(Save 1, Save 2 . . .)
         */
        try (FileOutputStream fileOut = new FileOutputStream(
                "Save\\Save " + (gm.getNumPartidas()));
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void nuevaRonda() { // Funcion encargada para una nueva ronda
        for (int i = 0; i < jlCampos.length; i++) {
            for (int j = 0; j < jlCampos[i].length; j++) {
                jlCampos[i][j].setText(""); // se restablece el texto
                jlCampos[i][j].setBackground(Color.WHITE); // se restablece el fondo
            }
        }
        filaActual = 0; // la fila se inicializa en -1 (averiguar razon de que se suma 1)
        vidas = 6; // se restablecen las vidas
        p.setFilaActual(filaActual); // se guarda la fila actual nueva en la partida
        p.palabraAleatoria(); // se saca una nueva palabra
    }

    private void invocarCentral(JPanel jpCenter, JLabel[][] campo) { // para cargar los campos de una partida ya
                                                                     // iniciada
        for (int i = 0; i < campo.length; i++) { // recorriendo todos los campos
            for (int j = 0; j < campo[i].length; j++) {
                añadirPanelCentral(jpCenter, campo[i][j], i + 1, j); // se pasan directaemente a añadirse al panel
                                                                     // //sospecha del +1
            }
        }
    }

    private void invocarCentral(JPanel jpCenter) { // Funcion encargadad de crear un panel vacio
        for (int i = 0; i < jlCampos.length; i++) {
            for (int j = 0; j < jlCampos[i].length; j++) {
                JLabel label = new JLabel(""); // se crea un nuevo JLabel para cada iteracion
                label.setFont(mainFontJuego); // se añade el Font especial
                label.setBackground(Color.white); // necesario??
                label.setOpaque(true); // Para que el fondo sea visible
                añadirPanelCentral(jpCenter, label, i + 1, j);
                jlCampos[i][j] = label;
            }
        }
        // se guardan los campos creados pero vacios en el JLabel[][] personal de cada
        // partida
        p.setCampos(jlCampos);
    }

    private void añadirPanelCentral(JPanel panel, JLabel textField, int fila, int columna) {
        textField.setPreferredSize(new Dimension(65, 55)); // se coloca un tamaño definido

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margen
        gbc.gridy = fila;
        gbc.gridx = columna;
        panel.add(textField, gbc);
    }

    private void escribirPanel(JLabel[][] jlabel, String palabra) { // funcion encargada de la escritura del panel
        String PALABRA = palabra.toUpperCase(); // se convierte las palabras a MAYUSCULA
        int vic = 0; // se registran las letras correctas

        /*
         * Segun lo que devuelva el metodo intento() de la clase Partida.java, se va
         * cambiando
         * el background segun sea correcta, casi o simplemente nada
         */
        ArrayList<Integer> intento = p.intento(palabra, p.getPalabraSecreta());
        for (int i = 0; i < PALABRA.length(); i++) {
            jlabel[filaActual][i].setText(String.valueOf(PALABRA.charAt(i))); // se escribe primeramente
            if (intento.get(i) == 0) { // codigo 0
                jlabel[filaActual][i].setBackground(Color.GREEN); // se añade un fondo verde
                vic++;
            }
            if (intento.get(i) == 1) { // codigo 1
                jlabel[filaActual][i].setBackground(Color.YELLOW); // se añade un fondo blanco
            }
            
        }
        jlAviso.setText("Le quedan un total de: " + (vidas-1)+ " vidas");
        if (filaActual >= 5 && vic != 5) { // caso no de no adivinar y no le queden vidas //usar la variable de vidas
            jlAviso.setText("Se le han acabado los turnos, has perdido. . .");
            jtTexto.setEditable(false); // se desactiva el campo JTextFieldp
            jbSave.setEnabled(false); // se desactiva el Boton de guardar
            if (p.getPalabrasAdivinadas() > 0) {
                if (existeUsuario()) {
                    escribirPuntuacion();
                } else {
                    pedirUsuario();
                }
            }
            guardado = true; // se permite salir
        }
        p.setCampos(jlabel); // se actualiza los campos ya Marcados de verde, amarillo o blanco
        filaActual++; // se aumenta la fila actual
        p.setFilaActual(filaActual);
        if (victoria(vic)) {
            ventanaFelicitacion();
        }
        vidas--; // se resta una vida por el intento hecho
        p.setVidas(vidas); //
    }

    public boolean victoria(int letrasAdi) { // se verifica si las letras correctas que se pasan son 5
        if (letrasAdi == 5) {
            p.addPalabrasAdivinada(); // aumenta el contador de palabras
            jlPalAdi.setText("Palabras_Adivinadas " + p.getPalabrasAdivinadas()); // se actualiza el Jlabel
            p.aumentarPuntos(vidas * 100); // se añaden sus puntos correspondientes
            jlScore.setText("Puntuacion: " + p.getPuntuacion()); // se actualiza el Jlabel
            p.setFilaActual(filaActual); // necesario???
            jlUltiPal.setText("Ultima Palabra: " + p.getPalabraSecreta()); // se actualiza el Jlabel
            p.setUltimaPalabraAdi(p.getPalabraSecreta());
            return true;
        }
        return false;
    }

    public void ventanaFelicitacion() {
        /* Frame para felicitar al usuario */
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(620, 220);
        jf.setResizable(false); // Evita que la ventana sea redimensionable
        jf.setLocationRelativeTo(null); // Coloca la ventana en el centro del monitor
        jf.setTitle("Has Ganado");

        /* Panel Principal */
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new GridBagLayout());
        jpMain.setBackground(bgcolor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 20, 5); // Margen

        /* TEXTO DE FELICITACION */
        JLabel jlTexto = addJlabel(jpMain, mainFont, 0, 1);
        jlTexto.setText("Has Adivinado La Palabra Secreta");

        /* BOTON PARA CONTINUAR */
        JButton jbAceptar = new JButton("Siguiente ronda");
        jbAceptar.setFont(mainFont);
        jbAceptar.setFocusPainted(false);
        jbAceptar.setForeground(Color.white);
        jbAceptar.setBackground(new Color(255, 0, 0));
        jbAceptar.addActionListener(e -> {

            Window parentWindow = SwingUtilities.windowForComponent(jbAceptar);
            parentWindow.dispose(); // Cierra la ventana actual y no todo el juego
            jlAviso.setText("");
            vidas = 6;
            nuevaRonda();
        });
        gbc.gridy = 2;
        jpMain.add(jbAceptar, gbc);

        jf.add(jpMain);
    }
}