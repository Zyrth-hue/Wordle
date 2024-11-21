//Practica hecha por Limberth Poma Fernandez

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

public class Partida implements Serializable {
    private int puntuacion = 0;
    private String usuario = "";
    private int palabrasAdivinadas = 0;
    private String ultimaPalabraAdvi = "";
    private int vidas= 6;
    private JLabel[][] jlCampos = new JLabel[6][5]; // array para contener los campos personales de la partida
    private String palabraSecreta; // se guarda la palabra secreta actual
    private int filaActual = 0; // se guarda la fila actual
    private ArrayList<String> palabras = new ArrayList<>(); // array de las posibles palabras secretas
    private ArrayList<String> PalabrasUsables = new ArrayList<>(); //array con palabras que el usuario pueda ingresar

    private void extraerPalabras() {
        BufferedReader br = null;
        BufferedReader br1 = null;
        if (palabras.size() == 0) { // solo se ejecuta cuando el arraylist este vacio
            try {
                br = new BufferedReader(new FileReader("Palabras5L"));
                String linea1 = ""; // linea para guardar todas las palabras en una sola linea
                linea1 = br.readLine();
                String palabra[] = linea1.split(",");// creando array temporal para separar por comas
                for (int i = 0; i < palabra.length; i++) {
                    palabras.add(palabra[i]); // añadiendo las palabras al ArrayList
                }

                /*Recoger un listado ( 8000+ ) de posibles palabras que pueda aceptar el sistema */
                br1 = new BufferedReader(new FileReader("PalabrasUsables"));
                String linea2 = "";
                linea2 = br1.readLine();
                String usables[] = linea2.split(",");
                for (int i = 0; i < usables.length; i++) {
                    PalabrasUsables.add(usables[i]);//se añaden todas las posibles palabras que puedan ingresarse
                }
                
                System.out.println(PalabrasUsables.size());
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                        br1.close();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
            }
        }
    }

    //Funcion encargadad de Generar una palabra aleatoria
    public String palabraAleatoria() {
        Random r = new Random();
        extraerPalabras(); // se llama al metodo encargado de extraer las palabras
        palabraSecreta = palabras.get(r.nextInt(palabras.size())); // se guarda la palabra secreta
        return palabraSecreta;
    }

    // funcion para deconstruir una palabra en letras
    public ArrayList<Character> letras(String palabra) {
        ArrayList<Character> letra = new ArrayList<>();
        for (int i = 0; i < palabra.length(); i++) {
            letra.add(palabra.charAt(i)); // deconstruir y guardar letra por letra
        }
        return letra;
    }

    // FUncion encargada de verificar que la palabra del usaurio es valida
    public boolean palabraValida(String palabra) {
        for (int i = 0; i < palabras.size(); i++) {
            if (PalabrasUsables.contains(palabra)) { // se encuentra dentro del arrayList de palabras
                return true; // si se encuentra una coincidencia, la palabra es valida
            }
        }
        return false;
    }

    // Metodo encargado de Dar fondos Verdes o Amarillos en Juego.java -> escribirPanel(. . )
    public ArrayList<Integer> intento(String palabraUsaurio, String palabraSistema) {
        ArrayList<Integer> indice = new ArrayList<>();

        //Se separan las palabras por letras
        ArrayList<Character> letraUsuario = letras(palabraUsaurio);
        ArrayList<Character> letraSistema = letras(palabraSistema);

        for (int i = 0; i < letraUsuario.size(); i++) {
            // primero se verifica que la letra del usuario este en la posicion correcta
            if (letraUsuario.get(i).equals(letraSistema.get(i))) {
                indice.add(0); //Bien (Verde)
                continue;
            }
             // se comprueba que la palabra secreta contenta alguna letra del usuario
            if (palabraSistema.contains(letraUsuario.get(i).toString())) {  //if (palabraSecreta.contains(letraUsuario.get(i).toString())) {
                indice.add(1);// casi (Amarillo)
            } else { // en caso de no haber ninguna coincidencia se marca -1 para evitar un null
                indice.add(-1); // mal (Blanco)
            }
        }
        return indice;
    }

    /*METODOS GET/SET/AUMENTO */

    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    public void setPalabraSecreta(String palabraSecreta) {
        this.palabraSecreta = palabraSecreta;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public void setFilaActual(int filaActual) {
        this.filaActual = filaActual;
    }

    public JLabel[][] getCampos() {
        return jlCampos;
    }

    public void setCampos(JLabel[][] campos) {
        jlCampos = campos;
    }


    public void aumentarPuntos(int puntos) {
        puntuacion = puntuacion + puntos;
    }

    public void setUltimaPalabraAdi(String palabra) {
        ultimaPalabraAdvi = palabra; // guardar la ultima palabra adivinada del usuario

    }

    public void addPalabrasAdivinada() {
        palabrasAdivinadas++; // aumentar las palabras adivinadas por el usuario
    }

    public String getUltimaPalabraAdi() {
        return ultimaPalabraAdvi; // devuleve la ultima palabra adivinada del usuario
    }

    public int getPalabrasAdivinadas() {
        return palabrasAdivinadas;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setVidas(int vidas){
        this.vidas=vidas;
    }

    public int getVidas(){
        return vidas;
    }

}