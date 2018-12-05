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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Martin
 */
public class ParseAchivo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ArrayList<String[]> dataset = new ArrayList<>();
        String filePath = args[0];
        BufferedReader fileReader = null;
        String nombreArchivoSalida = "src/set_verbs_reg_3.arff";
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
            bufferEscritura.write(currentLineString + "\n");
            //seccion de datos
            currentLineString = fileReader.readLine();
            while (currentLineString != null) {
                inst = currentLineString.split(",");
                dataset.add(inst);
                currentLineString = fileReader.readLine();
            }
            //seleccionar caracteristicas del nuevo dataset.
            //deleteDuplicate(dataset);
            dataset = getColumn(dataset, new int[]{0,1,2,3,4,5,6});
            dataset = addColumn(dataset, new String[]{"0"});
            //dataset = getIrregulares(dataset);
            //dataset = addColumn(dataset, new String[]{"0"});
            LinkedList<String> datasetToString = new LinkedList<>();
            String lineToString;
            for (String[] line : dataset) {
                lineToString = "";
                for (int i = 0; i < line.length; i++) {
                    lineToString += line[i] + ",";
                }
                lineToString = lineToString.substring(0, lineToString.length() - 1);
                datasetToString.add(lineToString);
            }
            for (String line : datasetToString) {
                bufferEscritura.write(line + "\n");
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

    public static ArrayList getRegulares(ArrayList<String[]> dataset) {
        String[] element = dataset.get(0), element2, nextElement, nextElement2;
        while (element != null) {
            if (dataset.indexOf(element) + 1 < dataset.size()) {
                nextElement = dataset.get(dataset.indexOf(element) + 1);
            } else {
                nextElement = null;
            }
            if (!element[9].equalsIgnoreCase("ANY") || (!element[1].equalsIgnoreCase("ANY") && !element[7].startsWith(element[1]))) {
                dataset.remove(element);
//		System.out.print("elimina: ");
//		for (int i = 0; i < element.length; i++) {
//		    System.out.print(element[i] + " ");
//		}
//		System.out.println("");
                element2 = dataset.get(0);
                while (element2 != null) {
                    if (dataset.indexOf(element2) + 1 < dataset.size()) {
                        nextElement2 = dataset.get(dataset.indexOf(element2) + 1);
                    } else {
                        nextElement2 = null;
                    }
                    if (element2[0].equalsIgnoreCase(element[0]) && element2[1].equalsIgnoreCase(element[1]) && element2[2].equalsIgnoreCase(element[2])) {
                        if (nextElement == element2) {
                            nextElement = nextElement2;
                        }
                        dataset.remove(element2);
//			System.out.print("por ende elimina: ");
//			for (int i = 0; i < element2.length; i++) {
//			    System.out.print(element2[i] + " ");
//			}
//			System.out.println("");
                    }
                    element2 = nextElement2;
                }
            }
            element = nextElement;
        }

        return getColumn(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    }

    public static void getRegulares_2(ArrayList<String[]> dataset) {
        String[] element;
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            if ((!element[9].equalsIgnoreCase("ANY") || (!element[1].equalsIgnoreCase("ANY") && !element[7].startsWith(element[1])))) {
                dataset.remove(element);
            } else {
                i++;
            }
        }
    }

    public static ArrayList getColumn(ArrayList<String[]> dataset, int[] columns) {
        //se debe eliminar las columnas en el encabezado
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst;
        for (int i = 0; i < dataset.size(); i++) {
            inst = new String[columns.length];
            for (int j = 0; j < columns.length; j++) {
                inst[j] = dataset.get(i)[columns[j]];
            }
            newDataSet.add(inst);
        }
        return newDataSet;
    }

    /**
     * Recibe el dataset y una lista de columnas y agrega las columnas al final
     * del dataset.
     *
     * @param dataset
     * @param value
     * @return
     */
    public static ArrayList addColumn(ArrayList<String[]> dataset, String[] value) {
        //se debe eliminar las columnas en el encabezado
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst;
        int cantColumnNew = dataset.get(0).length + value.length;
        int cantColumn = dataset.get(0).length;
        for (int i = 0; i < dataset.size(); i++) {
            inst = new String[cantColumnNew];
            //valores del dataset pasan a la nueva instancia
            for (int j = 0; j < cantColumn; j++) {
                inst[j] = dataset.get(i)[j];
            }
            //se agregan los nuevos valores a la nueva instancia
            for (int j = 0; j < value.length; j++) {
                inst[cantColumn + j] = value[j];
            }
            newDataSet.add(inst);
        }
        return newDataSet;
    }

    public static ArrayList getIrregulares(ArrayList<String[]> dataset) {
        ArrayList<String[]> regularDataset = getRegulares((ArrayList<String[]>) dataset.clone()), newDataset = new ArrayList<>();
        boolean encontro;
        boolean igual;
        String[] line2;
        for (String[] line : dataset) {
            line2 = regularDataset.get(0);
            encontro = false;
            while (!encontro && line2 != null) {
                int i = 0;
                igual = true;
                while (i < line2.length && igual) {
                    igual = (line[i].equalsIgnoreCase(line2[i]));
                    i++;
                }
                encontro = igual;
                if (regularDataset.indexOf(line2) + 1 < regularDataset.size()) {
                    line2 = regularDataset.get(regularDataset.indexOf(line2) + 1);
                } else {
                    line2 = null;
                }
            }
            if (!encontro) {
                newDataset.add(line);
            }
        }
        return newDataset;
    }

    public static void getIrregulares_2(ArrayList<String[]> dataset) {
        String[] element;
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            if (!(!element[9].equalsIgnoreCase("ANY") || (!element[1].equalsIgnoreCase("ANY") && !element[7].startsWith(element[1])))) {
                dataset.remove(element);
            } else {
                i++;
            }
        }
    }

    public static ArrayList deleteDuplicate(ArrayList<String[]> dataset) {
        String[] element = dataset.get(0), element2, nextElement, nextElement2;
        while (element != null) {
            if (dataset.indexOf(element) + 1 < dataset.size()) {
                nextElement = dataset.get(dataset.indexOf(element) + 1);
            } else {
                nextElement = null;
            }
            element2 = nextElement;
            while (element2 != null) {
                if (dataset.indexOf(element2) + 1 < dataset.size()) {
                    nextElement2 = dataset.get(dataset.indexOf(element2) + 1);
                } else {
                    nextElement2 = null;
                }
                if (element2[0].equalsIgnoreCase(element[0]) && element2[1].equalsIgnoreCase(element[1]) && element2[2].equalsIgnoreCase(element[2]) && element2[3].equalsIgnoreCase(element[3]) && element2[4].equalsIgnoreCase(element[4]) && element2[5].equalsIgnoreCase(element[5]) && element2[6].equalsIgnoreCase(element[6])) {
                    if (nextElement == element2) {
                        nextElement = nextElement2;
                    }
                    dataset.remove(element2);
                    System.out.print("elimina duplicado: ");
                    for (int i = 0; i < element2.length; i++) {
                        System.out.print(element2[i] + " ");
                    }
                    System.out.println("");
                }
                element2 = nextElement2;
            }
            element = nextElement;
        }
        return dataset;
    }
}
