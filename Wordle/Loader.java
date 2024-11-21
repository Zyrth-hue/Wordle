//Practica hecha por Limberth Poma Fernandez

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Loader extends JPanel {
    final private Font mainFont = new Font("Sans-serif", Font.PLAIN, 26); // Fuente predeterminada
    private JButton jbSelect = new JButton(); // Boton para la seleccion de la partida
    final private Color bgColor = new Color(41,51,91); //color del fondo
    final private Color txColor = new Color(31,61,71); //color de la zona de partidas
    final private Color buttonColor = new Color(35,154,107); //color de los botones

    public void initialize() {
        /* PANEL MAIN */
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpMain.setBackground(bgColor);

        /* PANEL CENTRAL DE POSIBLES PARTIDAS */
        JPanel jpCenter = new JPanel();
        jpCenter.setLayout(new GridBagLayout());
        jpCenter.setBackground(txColor);
        jpCenter.setPreferredSize(new Dimension(600, 600));

        /* GENERAR LAS PARTIDAS DISPONIBLES */
        partidas("Save", jpCenter, Color.GRAY, mainFont);

        /* PANEL DE BOTONES */
        JPanel jpButton = new JPanel();
        jpButton.setLayout(new GridBagLayout()); // orientacion de izquirda a derecha
        jpButton.setBackground(bgColor);

        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(10, 150, 10, 150); // Margen

        /* BOTON SALIR */
        JButton jbExit = new JButton("Salir");
        jbExit.setFont(mainFont);
        jbExit.setForeground(Color.white);
        jbExit.setFocusPainted(false);
        jbExit.setBackground(buttonColor);
        jbExit.addActionListener(e -> {
            MainFrame f = new MainFrame();
            f.initialize();
            f.setSize(1200, 750);
            f.setResizable(false);
            f.setLocationRelativeTo(null);

            jpMain.removeAll();
            jpMain.add(f);
            jpMain.revalidate();
            jpMain.repaint();
        });
        

        gbcButton.gridx = 1; // columna 1
        jpButton.add(jbExit, gbcButton);

        /* BOTON Cargar Partida */
        jbSelect = new JButton("Elegir partida");
        jbSelect.setFont(mainFont);
        jbSelect.setForeground(Color.white);
        jbSelect.setFocusPainted(false);
        jbSelect.setBackground(buttonColor);
        jbSelect.addActionListener(e -> {
                JFrame jf = new JFrame();
                jf.setVisible(true);
                jf.setSize(620, 220);
                jf.setResizable(false);
                jf.setLocationRelativeTo(null);
                jf.setTitle("Cargar Partida ");
                
                /* PANEL PRINCIPAL */
                JPanel jpPrincipal = new JPanel();
                jpPrincipal.setLayout(new BoxLayout(jpPrincipal, BoxLayout.Y_AXIS));
                jpPrincipal.setBackground(getBackground());

                /* PANEL Texto */
                JPanel jpText = new JPanel();
                jpText.setLayout(new GridBagLayout());
                jpText.setPreferredSize(new Dimension(620, 60));
                jpText.setBackground(getBackground());

                JTextField tlText = new JTextField();
                tlText.setBackground(Color.GRAY);
                tlText.setFont(mainFont);
                tlText.setForeground(Color.white);
                tlText.setPreferredSize(new Dimension(300, 30));

                jpText.add(tlText); // añadiendo el textfield al panel
                jpPrincipal.add(jpText); // añadiendo el panel de texto al principal

                /* PANEL BOTONEs */
                JPanel jpBotones = new JPanel();
                jpBotones.setLayout(new GridBagLayout());
                jpBotones.setBackground(getBackground());
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 30, 5, 30); // Margen

                /* BOTON ACEPTAR */
                JButton jbCargar = new JButton("Cargar Partida");
                jbCargar.setFont(mainFont);
                jbCargar.setForeground(Color.white);
                jbCargar.setFocusPainted(false);
                jbCargar.setBackground(buttonColor);
                jbCargar.addActionListener(ev -> {
                    // añadir parametros para empezar el juego, un booleano?

                    Partida p = seleccionarPartida(tlText.getText());
                    if (p != null) {
                    Juego i = new Juego();
                    i.setPartida(p);
                    i.setSize(1200, 750);
                    i.setLocation(100, 100);

                    jpMain.removeAll();
                    jpMain.add(i);
                    jpMain.revalidate();
                    jpMain.repaint();
                    i.initialize();
                    } else {
                        System.out.println("La partida no fue encontrada");
                    }
                    // Dentro del ActionListener del botón jbNewGame
                    Window parentWindow = SwingUtilities.windowForComponent(jbCargar);
                    parentWindow.dispose(); // Cierra la ventana actual
                });
                gbc.gridx = 1;
                jpBotones.add(jbCargar, gbc);

                /* BOTON CANCELAR */
                JButton jbCancelar = new JButton("Cancelar");
                jbCancelar.setFont(mainFont);
                jbCancelar.setForeground(Color.white);
                jbCancelar.setFocusPainted(false);
                jbCancelar.setBackground(buttonColor);
                jbCancelar.addActionListener(ev -> {

                    // Dentro del ActionListener del botón jbNewGame
                    Window parentWindow = SwingUtilities.windowForComponent(jbCancelar);
                    parentWindow.dispose(); // Cierra la ventana actual
                });
                gbc.gridx = 2;
                jpBotones.add(jbCancelar, gbc);

                jpPrincipal.add(jpBotones);// añadiendo el panel de botones al principal
                jf.add(jpPrincipal); // añadiendo el panel principal al frame
        });
        gbcButton.gridx = 2;// columna 2
        jpButton.add(jbSelect, gbcButton);

        jpMain.add(jpCenter);
        jpMain.add(jpButton);
        add(jpMain);
        setBackground(bgColor);
    }

    // Metodo Usado especialmente para el metodo guardarPartida() de Juego.java
    public int getNumPartidas() {
        File carpeta = new File("Save");
        File archivos[] = carpeta.listFiles();
        int numPartidas = (int) archivos.length;
        return numPartidas; // Devuelve el numero de partidas existentes en la ruta
    }

    /* Metodo especiazado en Mostrar por pantalla las partidas DIsponibles */
    public void partidas(String ruta, JPanel panel, Color color, Font main) {
        File carpeta = new File(ruta);
        File[] archivos = carpeta.listFiles(); //fichero file con todos los archivos en la ruta preterminada
        int y = 0; //contador para el gridy
        int x = 0; //contador para el gridy
        GridBagConstraints gbc = new GridBagConstraints(); //contraints para el layaout
        gbc.insets = new Insets(10, 25, 10, 25); // Margen
        if (archivos.length > 0) { //se ejecuta mientras haya al menos un archivo
            for (File archivo : archivos) {
                if (y == 10) {//no sobrepasar los 10
                    x++;
                    y= 0;
                    if (x == 4) {
                        return; //cuando se cumplan 4 columnas, ya no se mostraran mas
                    }
                }
                JLabel jl = new JLabel(archivo.getName()); // Creamos un nuevo JLabel para cada archivo
                jl.setFont(main);
                jl.setForeground(Color.white);
                jl.setBackground(color);
                gbc.gridx = x; // Establecer la posición en la columna x progresivamente
                gbc.gridy = y; // Establecer la posición en la fila i
                panel.add(jl, gbc); //se añaden tantos Jlabel como partidas disponibles hayan
                y++;
            }
        } else { //caso contrario se presenta un mensaje
            JLabel jl = new JLabel("No hay partidas disponibles");
            jl.setFont(main);
            jl.setBackground(color);
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(jl,gbc);
        }
    }

    public Partida seleccionarPartida(String partida) {
        Partida p = null; //se crea una partida en null para almacenar la partida a cargar

        //a travez de una ruta predeterminada, se carga el fichero que contiene la partida 
        try (FileInputStream fileIn = new FileInputStream("Save\\"+partida);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)) { 
            p = (Partida) objectIn.readObject(); //con un cast se carga el fichero binario y se actualiza el objeto Partida
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            //se puede mostrar un aviso para que escriba e indiqe una partida existente
        }
        return p; //se develve la partida ya cargada
    }
}