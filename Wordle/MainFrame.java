//Practica hecha por Limberth Poma Fernandez
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Sans-serif", Font.PLAIN, 46); // Fuente predeterminada
    final private Color bgColor = new Color(41,51,91);
    final private Color buttonColor = new Color(35,154,107);

    public void initialize() {
        /* PANEL MAIN */
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS)); //orientacion de arriba a abajo
        jpMain.setBackground(bgColor);

        /*->  PANEL CENTER */
        JPanel jpCenter = new JPanel();
        jpCenter.setLayout(new GridBagLayout());  //orientacion mediante una "cuadricula X & Y"
        jpCenter.setBackground(bgColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 20, 10); // Margen

        /* BOTON INICIO */
        JButton jbStart = new JButton("Inicio");
        jbStart.setFont(mainFont);
        jbStart.setForeground(Color.white);
        jbStart.setBackground(buttonColor);
        jbStart.setFocusPainted(false);
        jbStart.addActionListener(e -> {
            Juego i = new Juego(); //se inicializa la pantalla del Juego
            i.initialize();
            Juego.setGuardad(true); //necesario??
            i.setSize(1200,750);

            jpMain.removeAll();
            jpMain.add(i);
            jpMain.revalidate();
            jpMain.repaint();
        });

        gbc.gridy = 0; // Fila 0
        jpCenter.add(jbStart, gbc);

        /* BOTON CARGAR PARTIDA */
        JButton jbReload = new JButton("Cargar Partida");
        jbReload.setFont(mainFont);
        jbReload.setForeground(Color.white);
        jbStart.setFocusPainted(false);
        jbReload.setBackground(buttonColor);
        jbReload.addActionListener(e -> {
            Loader gl = new Loader();
            gl.initialize();
            gl.setSize(1200,750);
            gl.setLocation(100,100);

            jpMain.removeAll();
            jpMain.add(gl);
            jpMain.revalidate();
            jpMain.repaint();
        });

        gbc.gridy = 1; // Fila 1
        jpCenter.add(jbReload, gbc);

        /*BOTON PUNTUACION */
        JButton jbScore = new JButton("Puntuacion");
        jbScore.setFont(mainFont);
        jbScore.setForeground(Color.white);
        jbStart.setFocusPainted(false);
        jbScore.setBackground(buttonColor);
        jbScore.addActionListener(e -> {
            Score sc = new Score();
            sc.setSize(1200,750);
            sc.setLocation(100,100);
            sc.initialize();

            jpMain.removeAll();
            jpMain.add(sc);
            jpMain.revalidate();
            jpMain.repaint();
        });

        gbc.gridy = 2; //fila2
        jpCenter.add(jbScore, gbc);


        /*BOTON SALIR */
        JButton jbExit = new JButton("Salir");
        jbExit.setFont(mainFont);
        jbExit.setForeground(Color.white);
        jbStart.setFocusPainted(false);
        jbExit.setBackground(buttonColor);
        jbExit.addActionListener(e -> {
            System.exit(1);
        });
        gbc.gridy = 3; //fila3
        jpCenter.add(jbExit,gbc);

        jpMain.add(jpCenter); // Añadiendo el panel central al Main

        /* PANEL MAIN CONFIGURACION */
        add(jpMain);
        setTitle("Juego Wordle");
        setSize(new Dimension(1200, 750));
        setResizable(false); // Evita que la ventana sea redimensionable
        setDefaultCloseOperation(EXIT_ON_CLOSE); //operacion para cerrar y "apagar" la ejecucion del programa
        setVisible(true);
        setLocationRelativeTo(null); // Coloca la ventana en el centro del monitor
    }
}