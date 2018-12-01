/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parseachivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Martin
 */
public class ParseAchivo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        BufferedReader fileReader = null;
        String nombreArchivoSalida = "src/set_verbs_sin_null_suff_reg.arff";
        //FileWriter escritorArchivo = new FileWriter(nombreArchivoSalida);
        BufferedWriter bufferEscritura = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivoSalida), "Windows-1252"));
        //BufferedWriter bufferEscritura = new BufferedWriter(escritorArchivo);
        String[] inst;
        try {
            FileInputStream is = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(is, "Windows-1252");
            fileReader = new BufferedReader(isr);
            String currentLineString = fileReader.readLine();
            //preambulo
            while (!currentLineString.contains("@data")) {
                bufferEscritura.write(currentLineString + "\n");
                currentLineString = fileReader.readLine();
            }
            bufferEscritura.write("instancia[1].equalsIgnoreCase(\"ANY\") \n");
            bufferEscritura.write(currentLineString + "\n");
            //seccion de datos
            currentLineString = fileReader.readLine();
            while (currentLineString != null) {
                inst = currentLineString.split(",");
                if (condicion(inst)) {
                    bufferEscritura.write(currentLineString + "\n");
                } else {
                    System.out.println("elimina: " + currentLineString);
                }
                currentLineString = fileReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    // close reader, good practice
                    bufferEscritura.close();
                    fileReader.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Condicion devuelve true si esa instancia debe ir en el nuevo dataset.
     * False en caso contrario.
     *
     * @param instancia
     * @return
     */
    public static boolean condicion(String[] instancia) {
        boolean res = (instancia[1].equalsIgnoreCase("ANY"));
        return res;
    }
}
