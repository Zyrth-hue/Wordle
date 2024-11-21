//Practica hecha por Limberth Poma Fernandez

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Score extends JPanel {
    final private Font mainFont = new Font("Times New Roman", Font.PLAIN, 26); // Fuente predeterminada
    final private Color bgColor = new Color(41, 51, 91); // color del fondo
    final private Color txColor = new Color(31, 61, 71); // color de la zona de partidas
    final private Color buttonColor = new Color(35, 154, 107); // fondo (Button)
    private HashMap<String, Integer> datos = new HashMap<>();
    
    public void initialize() {
        /* PANEL MAIN */
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BoxLayout(jpMain, BoxLayout.Y_AXIS));
        jpMain.setBackground(bgColor);

        /* PANEL CENTRAL DE POSIBLES PARTIDAS */
        JPanel jpCenter = new JPanel();
        jpCenter.setLayout(new GridBagLayout());
        jpCenter.setBackground(txColor);
        jpCenter.setPreferredSize(new Dimension(900, 500));

        jpMain.add(jpCenter);

        /* TEXTO DE CLASIFICACION */
        clasificacion(jpCenter, txColor, mainFont);

        /* PANEL BOTONeS */
        JPanel jpButton = new JPanel();
        jpButton.setLayout(new GridBagLayout());
        jpButton.setBackground(bgColor);

        GridBagConstraints gbc = new GridBagConstraints(); // contraints para el layaout
        gbc.insets = new Insets(50, 5, 10, 5); // Margen

        /* Boton salir */
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

        jpButton.add(jbExit, gbc);
        jpMain.add(jpButton);

        add(jpMain);
        setBackground(bgColor);
    }

    // Metodo encargado de la recuperacion y Mostrarlos en JLabel's EN ORDEN DESC
    private void clasificacion(JPanel jpMain, Color color, Font mainFont) {
        datos = getPuntuacion(); // se extraen los datos del fichero .csv
        // se extraen los valores del HashMap
        ArrayList<Integer> listaPuntos = new ArrayList<>(datos.values());

        // Se Ordena y se da la vuela(Queda de mayor a menor)
        Collections.sort(listaPuntos);
        Collections.reverse(listaPuntos);

        // Se Extraen las Claves del HashMap
        ArrayList<String> listaKeys = new ArrayList<>(datos.keySet());

        // COntador para que solo se muestren 10 Valores en la clasificacion
        int vacias = 10;

        GridBagConstraints gbc = new GridBagConstraints(); // contraints para el layaout
        gbc.insets = new Insets(10, 5, 10, 5); // Margen

        // Primer for para imprimir los Datos "Reales de Puntuacion"
        for (int i = 0; i < listaKeys.size(); i++) {
            if (i == 10) {// En caso existan mas de 10 entradas
                break;
            }
            String linea = valorPorKey(datos, listaPuntos.get(i)) + " - " + listaPuntos.get(i);
            JLabel jl = new JLabel(linea.toUpperCase());
            jl.setFont(mainFont);
            jl.setForeground(Color.white);
            jl.setBackground(color);
            gbc.gridx = 0;
            gbc.gridy = i;
            jpMain.add(jl, gbc);
        }

        // Se imprimen Entradas de relleno, se inicia a partir del tamño de las entradas
        // Reales
        for (int i = listaKeys.size(); i < vacias; i++) { // se detiene hasta que llegue al tope de 10
            JLabel jl = new JLabel("[ Vacia ] - " + 0);
            jl.setFont(mainFont);
            jl.setForeground(Color.white);
            jl.setBackground(color);
            gbc.gridx = 0; // columnas
            gbc.gridy = i; // filas
            jpMain.add(jl, gbc);
        }
    }

    /* Metodo especializado en la Carga en memoria del Fichero .csv */
    public HashMap<String, Integer> getPuntuacion() {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader("Puntuaciones.csv");
            br = new BufferedReader(fr);
            datos = new HashMap<>(); // Se termina de instanciar el Hashmap
            String l;
            while ((l = br.readLine()) != null) {
                String[] linea = l.split(","); // se saca un array por cada linea
                String nombre = linea[0]; // se extrae la primera pos(usuario)
                int cantidad = Integer.parseInt(linea[1]); // se extrea la segunda pos(Puntuacion)
                datos.put(nombre, cantidad); // Se ingresan dentro del HashMap
            }
            return datos;
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    fr.close();
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return datos;
    }

    public void insertarPuntuacion(String usuario, Integer puntuacion, HashMap<String, Integer> datos) {
        BufferedWriter bw = null;
        datos.put(usuario, puntuacion);
        try {
            FileWriter fw = new FileWriter("Puntuaciones.csv");
            bw = new BufferedWriter(fw);
            // Convertir los puntos del HashMap a un ArrayList
            ArrayList<Integer> listaPuntos = new ArrayList<>(datos.values());

            // ordenar lista por los puntos
            Collections.sort(listaPuntos);

            // Revertir el orden de la lista
            Collections.reverse(listaPuntos);

            // Insertar Puntuacion ordenada de mayor a menor
            for (int i = 0; i < listaPuntos.size(); i++) {
                String linea = valorPorKey(datos, listaPuntos.get(i)) + "," + listaPuntos.get(i);
                // se inserta mediante la puntuacion mayor al menor (Usaurio, Puntuacion)
                bw.write(linea.toUpperCase());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    //fw.close();
                    bw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // metodo para devolver la key por medio de un valor
    private String valorPorKey(HashMap<String, Integer> map, Integer value) {
        for (String key : map.keySet()) { // se extrae cada Key del "Map" ->(HashMap)
            if (map.get(key).equals(value)) { // Cuando encuentre Una coincidencia
                return key; // Devuelve la Key correspondiente ligado a ese valor
            }
        }
        return null; // Retorna null si el valor no se encuentra en el HashMap
    }
}