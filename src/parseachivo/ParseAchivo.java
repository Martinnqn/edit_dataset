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
        String filePath2 = args[1];
        BufferedReader bfReader = null;
        BufferedReader bfReader2 = null;
        String nombreArchivoSalida = "src/dataset_completo_2.csv";
        //FileWriter escritorArchivo = new FileWriter(nombreArchivoSalida);
        BufferedWriter bufferEscritura = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivoSalida), "windows-1252"));
        //BufferedWriter bufferEscritura = new BufferedWriter(escritorArchivo);
        String[] inst;
        try {
            FileInputStream is = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(is, "windows-1252");
            bfReader = new BufferedReader(isr);
            String currentLineString = bfReader.readLine();
            FileInputStream is2 = new FileInputStream(filePath2);
            InputStreamReader isr2 = new InputStreamReader(is2, "windows-1252");
            bfReader2 = new BufferedReader(isr2);
            //preambulo
            /*while (!currentLineString.contains("@data")) {
                bufferEscritura.write(currentLineString + "\n");
                currentLineString = fileReader.readLine();
            }
            bufferEscritura.write(currentLineString + "\n");*/
            //seccion de datos
            String currentLineString2 = bfReader2.readLine();
            String aux;
            while (currentLineString != null) {
                aux = currentLineString + "," + currentLineString2;
//                aux = currentLineString;
                inst = aux.split(",");
                dataset.add(inst);
                currentLineString = bfReader.readLine();
                currentLineString2 = bfReader2.readLine();
            }
            //seleccionar caracteristicas del nuevo dataset.
            //dataset = checkDuplicados(dataset);
            //dataset = getColumn(dataset, new int[]{0});
            //dataset = addColumn(dataset, new String[]{"1"});
            //dataset = getIrregulares(dataset);
            //dataset = getStemAndPensyl(dataset);
//            dataset = separarStem_2(dataset);
//            dataset = getColumn(dataset, new int[]{11});
            //dataset = getSuff(dataset);
            //dataset = getClassPensyl(dataset);
            //dataset = getPensyl(dataset);
            //dataset = addPensylStemEnding(dataset, bfReader2);
            //dataset = delete11(dataset);
            //dataset = filterByEnding(dataset, "ir");
            //dataset = replacePersonNumber(dataset);
            //dataset = deletStemFromSuff(dataset);
            //dataset = getPrePensyl(dataset);
//            dataset = getEnding(dataset);
            //dataset = fixPrePosPensyl(dataset);
//            dataset = filterByCol(dataset, 12, "1");
//            dataset = parche(dataset);
//            dataset = verbosUsados(dataset);
            dataset = makeTypeOfVerb(dataset);

            LinkedList< String> datasetToString = new LinkedList<>();
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
                if (bfReader != null) {
                    // close reader, good practice
                    bufferEscritura.close();
                    bfReader.close();
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
                if (element2[0].equalsIgnoreCase(element[0]) && element2[1].equalsIgnoreCase(element[1])
                        && element2[2].equalsIgnoreCase(element[2])
                        && element2[3].equalsIgnoreCase(element[3])
                        && element2[4].equalsIgnoreCase(element[4])
                        && element2[5].equalsIgnoreCase(element[5])
                        && element2[6].equalsIgnoreCase(element[6])
                        && element2[7].equalsIgnoreCase(element[7])
                        && element2[8].equalsIgnoreCase(element[8])) {
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

    public static void filtrarConj(ArrayList<String[]> dataset) {
        String[] element;
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            if (!element[0].endsWith("ar") && !element[0].endsWith("er") && !element[0].endsWith("ir")) {
                dataset.remove(element);
            } else {
                i++;
            }
        }
    }

    public static ArrayList getStemAndPensyl(ArrayList<String[]> dataset) {
        String[] element;
        String[] verbo;
        String[] newElement;
        ArrayList<String[]> newDataset = new ArrayList<>();
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            verbo = element[0].split("-");
            newElement = new String[2];
            if (verbo.length >= 2) {
                newElement[0] = verbo[verbo.length - 2];
                newElement[1] = verbo[verbo.length - 1];
                newDataset.add(newElement);
            } else {
                newElement[0] = "NULL";
                newElement[1] = verbo[0];
                newDataset.add(newElement);
            }
            i++;
        }
        return newDataset;
    }

    public static ArrayList separarStem(ArrayList<String[]> dataset) {
        String[] element;
        String endingS;
        String newEndingS;
        String stem;
        String[] newElement;
        ArrayList<String[]> newDataset = new ArrayList<>();
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            endingS = element[element.length - 1];
            System.out.println("end " + endingS + " i " + i);
            newEndingS = endingS.substring(endingS.length() - 2, endingS.length());
            if (endingS.charAt(0) == '\'') {
                if (endingS.length() > 2) {
                    stem = endingS.substring(1, endingS.length() - 2);
                } else {
                    stem = "NULL";
                }
            } else {
                stem = endingS.substring(0, endingS.length() - 2);
            }
            System.out.println("new " + newEndingS);
            newElement = new String[2];
            newElement[0] = stem;
            newElement[1] = newEndingS;
            newDataset.add(newElement);
            i++;
        }
        return newDataset;
    }

    /**
     * Requiere los verbos infinitivos y conjugados separados en silabas con
     * guiones.
     *
     * @param dataset
     * @return
     */
    public static ArrayList separarStem_2(ArrayList<String[]> dataset) {
        String[] element;
        String suff;
        String inf;
        String ending;
        String conj;
        String newSuff;
        String stem;
        String[] newElement;
        ArrayList<String[]> newDataset = new ArrayList<>();
        int i = 0;
        while (i < dataset.size()) {
            element = dataset.get(i);
            inf = element[0];
            conj = element[1];
            ending = inf.split("-")[inf.split("-").length - 1];
            if (ending.length() > 2) {
                stem = ending.substring(0, ending.length() - 2);
            } else {
                stem = "NULL";
            }
            suff = "";
            for (int j = inf.split("-").length - 1; j < conj.split("-").length; j++) {
                suff += conj.split("-")[j];
            }
            System.out.println("suff " + suff + " i " + i + " stem " + stem + " de " + element[0]);
            if (suff.startsWith(stem)) {
                newSuff = suff.substring(stem.length(), suff.length());
            } else {
                newSuff = suff;
            }
            System.out.println("new " + newSuff);
            newElement = new String[2];
            newElement[0] = stem;
            newElement[1] = newSuff;
            newDataset.add(newElement);
            i++;
        }
        return newDataset;
    }

    public static ArrayList getSuff(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] verboConj;
        String[] verboInf;
        String suff = "";
        for (String[] str : dataset) {
            verboInf = str[0].split("-");
            verboConj = str[1].split("-");
            if (verboInf.length - 1 < verboConj.length) {
                for (int i = verboInf.length - 1; i < verboConj.length; i++) {
                    suff += verboConj[i];
                }
            } else {
                suff = "-";
            }
            if (!suff.equals("-")) {
                suff = suff.replace("-", "");
            }
            System.out.println("news " + suff);
            newDataSet.add(suff.split(" "));
            suff = "";
        }
        return newDataSet;
    }

    public static ArrayList getClassPensyl(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] verboConj;
        String[] verboInf;
        String pensyl = "";
        for (String[] str : dataset) {
            verboInf = str[0].split("-");
            verboConj = str[1].split("-");
            if (verboInf.length - 2 < verboConj.length && verboInf.length - 2 >= 0) {
                if (verboConj[verboInf.length - 2].endsWith(verboInf[verboInf.length - 2])) {
                    pensyl = "0";
                } else {
                    pensyl = verboConj[verboInf.length - 2];
                }
            } else {
                pensyl = "-";
            }
            newDataSet.add(pensyl.split(" "));
        }
        return newDataSet;
    }

    public static ArrayList getPensyl(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] verboInf;
        String pensyl = "";
        for (String[] str : dataset) {
            verboInf = str[0].split("-");
            if (verboInf.length > 1) {
                pensyl = verboInf[verboInf.length - 2];
            } else {
                pensyl = "NULL";
            }
            newDataSet.add(pensyl.split(" "));
        }
        return newDataSet;
    }

    public static ArrayList getEnding(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] verboInf;
        String ending;
        for (String[] str : dataset) {
            verboInf = str[0].split("-");
            ending = verboInf[verboInf.length - 1].substring(verboInf[verboInf.length - 1].length() - 2, verboInf[verboInf.length - 1].length());
            System.out.println("ending " + ending);
            newDataSet.add(ending.split(" "));
        }
        return newDataSet;
    }

    public static ArrayList getPrePensyl(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String pensyl;
        String prePensyl;
        for (String[] str : dataset) {
            pensyl = str[2];
            if (pensyl.length() > 2) {
                prePensyl = pensyl.substring(0, 1);
            } else {
                prePensyl = "NULL";
            }
            newDataSet.add(prePensyl.split(" "));
        }
        return newDataSet;
    }

    public static ArrayList getPosPensyl(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String pensyl;
        String posPensyl;
        for (String[] str : dataset) {
            pensyl = str[2];
            if (pensyl.length() > 2) {
                posPensyl = pensyl.substring(1, pensyl.length());
            } else {
                posPensyl = pensyl;
            }
            newDataSet.add(posPensyl.split(" "));
        }
        return newDataSet;
    }

    public static ArrayList fixPrePosPensyl(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String pensyl;
        String prePensyl;
        for (String[] str : dataset) {
            System.out.println("str " + str.length);
            if (str[11].equals("-")) {
                str[12] = "-";
                str[13] = "-";
            }
        }
        return dataset;
    }

    public static ArrayList addPensylStemEnding(ArrayList<String[]> dataset, BufferedReader buff) throws IOException {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String str = buff.readLine();
        String inst;
        int k = 1;
        int fin = 55;
        while (str != null) {
            for (int i = 0; i < fin; i++) {
                inst = dataset.get(k)[0] + "," + str;
                for (int j = 1; j < dataset.get(k).length; j++) {
                    inst += "," + dataset.get(k)[j];
                }
                newDataSet.add(inst.split(","));
                System.out.println(inst);
                k++;
                fin = 54;
            }
            str = buff.readLine();
        }

        return newDataSet;
    }

    public static ArrayList delete11(ArrayList<String[]> dataset) throws IOException {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst = new String[11];
        int j;
        for (String[] strings : dataset) {
            if (strings.length == 12) {
                System.out.println("delet " + strings[0]);
                j = 0;
                for (int i = 0; i < strings.length; i++) {
                    if (i != 10) {
                        inst[j] = strings[i];
                        j++;
                    }
                }
                newDataSet.add(inst);
            } else {
                newDataSet.add(strings);
            }

        }
        return newDataSet;
    }

    public static ArrayList deletStemFromSuff(ArrayList<String[]> dataset) throws IOException {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst;
        int j;
        int p;
        int k;
        for (String[] strings : dataset) {
            inst = new String[2];
            String newSuff = null;
            if (strings[1].startsWith(strings[0])) {
                newSuff = strings[1].substring(strings[0].length());
            } else if (strings[1].startsWith("*" + strings[0])) {
                newSuff = strings[1].substring(strings[0].length() + 1);
            }
            System.out.println("newss " + newSuff + " de " + strings[1] + " " + strings[0]);
            k = 0;
            for (int i = 0; i < strings.length; i++) {
                if (i != 1) {
                    inst[k] = strings[i];
                } else {
                    if (newSuff != null) {
                        inst[k] = newSuff;
                    } else {
                        inst[k] = strings[i];
                    }
                }
                k++;
            }
            newDataSet.add(inst);
        }
        return newDataSet;
    }

    public static ArrayList filterByEnding(ArrayList<String[]> dataset, String ending) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        for (String[] strings : dataset) {
            if (strings[0].endsWith(ending)) {
                newDataSet.add(strings);
            }
        }
        return newDataSet;
    }

    public static ArrayList filterByCol(ArrayList<String[]> dataset, int col, String val) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        for (String[] strings : dataset) {
            if (strings[col].equals(val)) {
                newDataSet.add(strings);
            }
        }
        return newDataSet;
    }

    public static ArrayList replacePersonNumber(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst;
        for (String[] strings : dataset) {
            inst = new String[12];
            inst[0] = strings[0];
            inst[1] = strings[1];
            inst[2] = strings[2];
            inst[3] = strings[3];
            inst[4] = strings[4];
            inst[5] = strings[5];
            String pron = getPronombre(strings[6], strings[7]);
            inst[6] = pron;
            inst[7] = strings[8];
            inst[8] = strings[9];
            inst[9] = strings[10];
            inst[10] = strings[11];
            inst[11] = strings[12];
            newDataSet.add(inst);
        }
        return newDataSet;
    }

    public static ArrayList checkDuplicados(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] inst;
        String[] inst2;
        int i = 0;
        int k = 0;
        int j = 0;
        while (i < dataset.size()) {
            inst = dataset.get(i);
            k = 0;
            j = 0;
            while (k < newDataSet.size() && j != -1) {
                inst2 = dataset.get(k);
                if (inst[0].equals(inst2[0])) {
                    j = -1;
                }
                k++;
            }
            if (j != -1) {
                newDataSet.add(inst);
            } else {
                System.out.println("no agrega " + inst[0]);
            }
            i++;
        }
        return newDataSet;
    }

    public static ArrayList verbosUsados(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] newStr;
        int j = 0;
        boolean encontro;
        String v;
        for (String[] str : dataset) {
            v = str[0];
            encontro = false;
            j = 0;
            while (j < newDataSet.size() && !encontro) {
                encontro = newDataSet.get(j)[0].equals(v);
                System.out.println("compara " + newDataSet.get(j)[0] + " " + v);
                j++;
            }
            if (!encontro) {
                System.out.println("agrega " + v);
                newDataSet.add(v.split(" "));
            } else {
                System.out.println("no agrega " + v);
            }
        }
        return newDataSet;
    }

    public static ArrayList parche(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] newStr;
        boolean sing = true;
        for (String[] str : dataset) {
            newStr = new String[13];
            newStr[0] = str[0];
            newStr[1] = str[1];
            newStr[2] = str[2];
            newStr[3] = str[3];
            newStr[4] = str[4];
            newStr[5] = str[5];
            newStr[8] = str[8];
            newStr[9] = str[9];
            newStr[10] = str[10];
            newStr[11] = str[11];
            newStr[12] = str[12];
            if (str[6].equals("0") && str[7].equals("0") && str[8].equals("0") && str[9].equals("2") && !str[12].equals("-")) {
                newStr[6] = "3";
                if (sing) {
                    newStr[7] = "0";
                    sing = false;
                } else {
                    newStr[7] = "1";
                    sing = true;
                }
            } else {
                newStr[6] = str[6];
                newStr[7] = str[7];
            }
            newDataSet.add(newStr);
        }
        return newDataSet;
    }

    /**
     * Hace el dataset para entrenar los pensyl y suff regulares e irregulares
     *
     * @param dataset
     * @return
     */
    public static ArrayList makeTypeOfVerb(ArrayList<String[]> dataset) {
        ArrayList<String[]> newDataSet = new ArrayList<>();
        String[] newStr;
        SeparaSilabas ss = new SeparaSilabas();
        String[] inf;
        String[] conj;
        for (String[] str : dataset) {
            newStr = new String[15];
            newStr[0] = str[0];
            newStr[1] = str[1];
            newStr[2] = str[2];
            newStr[3] = str[3];
            newStr[4] = str[4];
            newStr[5] = str[5];
            newStr[8] = str[8];
            newStr[9] = str[9];
            newStr[10] = str[10];
            newStr[11] = str[11];
            newStr[14] = str[13];
            ss.setString(str[0]);
            inf = ss.silabear().split("-");
            ss.setString(str[13]);
            conj = ss.silabear().split("-");
            //si el pensyl es regular o irregular
            if (str[10].equals("0")) {
                newStr[12] = "0"; //es regular
                //si es regular, pero el verbo esta marcado como irregular, 
                //entonces el suff no se adapta a la conjugacion regular, por lo que es irregular
                if (str[12].equals("0")) {
                    newStr[13] = "0";
                } else {
                    newStr[13] = "1";
                }
            } else {
                newStr[12] = "1"; //es irregular
                //si el sufijo de la conjugacion es igual a algun class_suff del dataset de verbos regulares, entonces es regular
                boolean isReg = false;
                int j = 0;
                while (j < dataset.size() && !isReg) {
                    isReg = dataset.get(j)[14].equals(str[11]);
                    j++;
                }
                if (isReg) {
                    newStr[13] = "0";
                } else {
                    newStr[13] = "1";
                }
            }

            newDataSet.add(newStr);
        }
        return newDataSet;
    }

    private static String getPronombre(String person, String num) {
        String pron = "-";
        if (num.equals("0")) {
            switch (person) {
                case "1":
                    pron = "yo";
                    break;
                case "2":
                    pron = "tu";
                    break;
                case "3":
                    pron = "el";
                    break;
                default:
                    break;
            }
        } else if (num.equals("1")) {
            switch (person) {
                case "1":
                    pron = "nosotros";
                    break;
                case "2":
                    pron = "vosotros";
                    break;
                case "3":
                    pron = "ellos";
                    break;
                default:
                    break;
            }
        }
        return pron;
    }

}
