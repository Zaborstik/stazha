package src;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> files = new ArrayList<>();
        boolean discend = false;
        boolean isNum = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")){
                discend = false;
            } else if (args[i].equals("-d")){
                discend = true;
            } else if (args[i].equals("-s")){
                isNum = false;
            } else if (args[i].equals("-i")){
                isNum = true;
            } else {
                if (!args[i].equals("out.txt")){
                    files.add(args[i]);
                }
            }
        }

        if (files.size() == 0){
            System.out.println("Не передан ни один файл");
            return;
        }

        if (!Files.exists(Path.of("out.txt"))){
            System.out.println("Выходящий файл не существует, создайте файл, затем запустите программу заново");
            return;
        }

        if (!Files.exists(Path.of(files.get(0)))){
            System.out.println("Файл " + files.get(0) + " не существует");
        }

        if (files.size() > 1){
            mergeSort(files.get(0), files.get(1), discend, isNum);
            for (int i = 2; i < files.size(); i++) {
                if (!Files.exists(Path.of(files.get(i)))){
                    System.out.println("Файл " + files.get(i) + " не существует");
                    continue;
                }
                mergeSort("out.txt", files.get(i), discend, isNum);
            }
        } else {
            mergeSort("out.txt", files.get(0), discend, isNum);
        }
    }

    public static void mergeSort(String pathFile1, String pathFile2, boolean discend, boolean isNum) {
        //discend по алфавиту - false (по возрастанию), против true (по убыванию)

        Path pathOut = Path.of("/Users/zaborstik/Desktop/testsstr/out.txt");
        try (Scanner file1 = new Scanner(new FileInputStream(pathFile1));
             Scanner file2 = new Scanner(new FileInputStream(pathFile2));
             FileWriter fileWriter = new FileWriter(pathOut.toFile())) {
            if (isNum) {
                mergeInnerInt(discend, file1, file2, fileWriter);
            } else {
                mergeInnerString(discend, file1, file2, fileWriter);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (FileSystemException e) {
            System.out.println("Файл поврежден, отусвет сортировка");

            int numOfFile = Integer.parseInt(e.getMessage());
            try (Scanner scanner = new Scanner(new FileInputStream(numOfFile == 1 ? pathFile1 : pathFile2))) {
                while (scanner.hasNext()) {
                    String pathToTmp = null;
                    if (isNum){
                        pathToTmp = sortInt(discend, scanner);
                    } else {
                        pathToTmp = sortStr(discend, scanner);
                    }
                    mergeSort(numOfFile == 1 ? pathFile2 : pathFile1, pathToTmp, discend, isNum);
                }
            } catch (IOException exception) {
                System.out.println("Не удалось создать временный файл, разрешите доступ к папке, или переместите в другое место, затем запустите повторно");
            }
        } catch (IOException e) {
            System.out.println("Файл не читается");
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка");
        }
    }

    private static String replaceChar(String str){
        if (str.contains("Ё")){
            str = str.replace("Ё", "Е");
        } else if (str.contains("ё")){
            str = str.replace("ё", "е");
        }
        return str;
    }

    private static void mergeInnerString(boolean discend, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, String> hashMap = new HashMap<>();

        int negativeOrPositive = discend ? -1 : 1;
        String tmp1;
        String tmp2;
        if (!file1.hasNext()){
            if (!file2.hasNext()){
                System.out.println("Файлы не содержат элементов");
            } else {
                tmp1 = "";
            }
        } else if (!file2.hasNext()){
            tmp2 = "";
        }
        tmp1 = replaceChar(file1.nextLine());
        tmp2 = replaceChar(file2.nextLine());

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, "");
        hashMap.put(2, "");

        while (file1.hasNext() && file2.hasNext()) {
            if ((tmp2.compareTo(tmp1) * (negativeOrPositive)) < 0) {
                if ((!hashMap.get(2).equals(""))
                        && (hashMap.get(2).compareTo(tmp2) * negativeOrPositive > 0)) {
                    throw new FileSystemException("2");
                } else {
                    hashMap.put(2, tmp2);
                }
                fileWriter.write((tmp2) + "\n");
                tmp2 = file2.nextLine();
                tmp2 = replaceChar(tmp2);
            } else {
                if ((!hashMap.get(1).equals(""))
                        && (hashMap.get(1).compareTo(tmp1) * negativeOrPositive > 0)) {
                    throw new FileSystemException("1");
                } else {
                    hashMap.put(1, tmp1);
                }
                fileWriter.write((tmp1) + "\n");
                tmp1 = file1.nextLine();
                tmp1 = replaceChar(tmp1);
            }
        }

//        в одном из массивов закончились элементы
        if (file2.hasNext()) {
            if ((!hashMap.get(1).equals(""))
                    && (hashMap.get(1).compareTo(tmp1) * negativeOrPositive > 0)) {
                throw new FileSystemException("2");
            }
            mergeOfOneInnerStrAndIsSorted(discend, file2, fileWriter, tmp1, tmp2);
        }
        if (file1.hasNext()) {
            if ((!hashMap.get(2).equals(""))
                    && (hashMap.get(2).compareTo(tmp2) * negativeOrPositive > 0)) {
                throw new FileSystemException("2");
            }
            mergeOfOneInnerStrAndIsSorted(discend, file1, fileWriter, tmp2, tmp1);
        }
    }

    private static boolean mergeOfOneInnerStrAndIsSorted(boolean discend, Scanner file, FileWriter fileWriter, String lastElement, String tmp2) throws IOException {
        boolean written = false;        //записан ли последний символ кратчайщего массива (костыли)
        int negativeOrPositive = discend ? -1 : 1;
        String previous = tmp2;

        while (file.hasNext()) {
            if ((tmp2.compareTo(lastElement) * (negativeOrPositive)) < 0 || written) {
                fileWriter.write((tmp2) + "\n");
                tmp2 = file.nextLine();
                tmp2 = replaceChar(tmp2);
                if (previous.compareTo(tmp2) * negativeOrPositive > 0 ){
                    return false;
                }
            } else if (!written) {
                fileWriter.write(lastElement + "\n");
                written = true;
            }
        }

        if (!written) {
            fileWriter.write("" + ((lastElement.compareTo(tmp2) * (negativeOrPositive)) < 0 ? lastElement : tmp2) + "\n");
        }
        fileWriter.write("" + ((tmp2.compareTo(lastElement) * (negativeOrPositive)) < 0 ? lastElement : tmp2));
        return true;
    }

    private static void mergeInnerInt(boolean discend, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        int negativeOrPositive = discend ? -1 : 1;
        int tmp1 = file1.nextInt() * (negativeOrPositive);
        int tmp2 = file2.nextInt() * (negativeOrPositive);

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, tmp1);
        hashMap.put(2, tmp2);

        while (file1.hasNext() && file2.hasNext()) {
            if (tmp1 > tmp2) {
                if ((hashMap.get(2) * negativeOrPositive) > (tmp2 * negativeOrPositive)) {
                    throw new FileSystemException("2");
                } else {
                    hashMap.put(2, tmp2);
                }
                fileWriter.write((tmp2 * (negativeOrPositive)) + "\n");
                tmp2 = file2.nextInt() * (negativeOrPositive);
            } else {
                if ((hashMap.get(1) * negativeOrPositive) > (tmp1) * negativeOrPositive) {
                    throw new FileSystemException("1");
                } else {
                    hashMap.put(1, tmp1);
                }
                fileWriter.write((tmp1 * (negativeOrPositive)) + "\n");
                tmp1 = file1.nextInt() * (negativeOrPositive);
            }
        }

        //в одном из массивов закончились элементы
        if (file2.hasNext()) {
            if ((hashMap.get(1) * negativeOrPositive) > (tmp1 * negativeOrPositive)){ //проверяем последний элемент, идет ли он по порядку?
                throw new FileSystemException("1");
            }
            if (!mergeOfOneInnerIntAndIsSorted(discend, file2, fileWriter, tmp1, tmp2)){
                throw new FileSystemException("2");
            }
        }
        if (file1.hasNext()) {
            if ((hashMap.get(2) * negativeOrPositive) > (tmp2 * negativeOrPositive)){ //проверяем последний элемент, идет ли он по порядку?
                throw new FileSystemException("2");
            }
            if (!mergeOfOneInnerIntAndIsSorted(discend, file1, fileWriter, tmp2, tmp1)){
                throw new FileSystemException("1");
            }
        }
    }

    private static boolean mergeOfOneInnerIntAndIsSorted(boolean discend, Scanner file, FileWriter fileWriter, int lastElement, int tmp2) throws IOException {
        boolean written = false;        //записан ли последний символ кратчайщего массива (костыли)
        int negativeOrPositive = discend ? -1 : 1;
        int previous = tmp2;

        while (file.hasNext()) {
            if (lastElement >= tmp2 || written) {
                fileWriter.write((tmp2 * (negativeOrPositive)) + "\n");
                tmp2 = file.nextInt() * (negativeOrPositive);
                if ((previous * negativeOrPositive) > (tmp2 * negativeOrPositive)){
                    return false;
                }
            } else if (!written) {
                fileWriter.write(lastElement * (negativeOrPositive) + "\n");
                written = true;
            }
        }

        if (!written) {
            fileWriter.write("" + (Math.min(lastElement, tmp2)) * (negativeOrPositive) + "\n");
        }
        fileWriter.write("" + (Math.max(lastElement, tmp2)) * (negativeOrPositive));
        return true;
    }

    private static String sortStr(boolean discend, Scanner file) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < 16384; i++) { // 16kb
                list.add(file.nextLine());
            }
            Collections.sort(list);
            if (discend){
                Collections.reverse(list);
            }
        } catch (NoSuchElementException e) {
            Collections.sort(list);
            if (discend){
                Collections.reverse(list);
            }
        }
        String pathToTmp = String.valueOf(Files.createTempFile("merge", ".txt"));
        try (FileWriter fileWriter = new FileWriter(pathToTmp)) {
            for (String tmp : list) {
                fileWriter.write(tmp + "\n");
            }
        }
        return pathToTmp;
    }

    private static String sortInt(boolean discend, Scanner file) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            for (int i = 0; i < 16384; i++) { // 16kb
                list.add(Integer.parseInt(file.nextLine()));
            }
            Collections.sort(list);
            if (discend){
                Collections.reverse(list);
            }
        } catch (NoSuchElementException e) {
            Collections.sort(list);
            if (discend){
                Collections.reverse(list);
            }
        }
        String pathToTmp = String.valueOf(Files.createTempFile("merge", ".txt"));
        try (FileWriter fileWriter = new FileWriter(pathToTmp)) {
            for (Integer tmp : list) {
                fileWriter.write(tmp + "\n");
            }
        }
        return pathToTmp;
    }
}