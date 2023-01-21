package src;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class sort {
    public static ArrayDeque<String> files = new ArrayDeque<>();
    public static boolean orderOfNums = false; //нарушена ли сортировка
    public static void main(String[] args) {
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

        try {
            Files.delete(Path.of("out.txt"));
            Files.createFile(Path.of("out.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!Files.exists(Path.of(files.getFirst()))){
            System.out.println("Файл " + files.pop() + " не существует");
        }

        String pathToTmp = null;
        try {
            pathToTmp = String.valueOf(Files.createTempFile("out", ".txt"));
        } catch (IOException e) {

        }

        while (!files.isEmpty()){
            if (!Files.exists(Path.of(files.getFirst()))){
                System.out.println("Файл " + files.pop() + " не существует");
            } else {
                try {
                    mergeSort(files.pop(), discend, isNum, false, pathToTmp);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void mergeSort(String pathFile, boolean discend, boolean isNum, boolean recursive, String pathToTmp) throws IOException {
        //discend по алфавиту - false (по возрастанию), против true (по убыванию)

        if (isNum) {
            if (orderOfNums) {
                try (Scanner scanner = new Scanner(new FileInputStream(pathToTmp));
                     FileWriter tempFileWriter = new FileWriter("out.txt")) {
                    while (scanner.hasNext()) {
                        tempFileWriter.write(scanner.nextLine() + "\n");
                    }
                }
                orderOfNums = false;
            }
        }

        if(!recursive) {
            try (Scanner scanner = new Scanner(new FileInputStream("out.txt"));
                 FileWriter tempFileWriter = new FileWriter(pathToTmp)) {
                 while (scanner.hasNext()) {
                    tempFileWriter.write(scanner.nextLine() + "\n");
                 }
            }
        }

        try (Scanner file1 = new Scanner(new FileInputStream(pathFile));
             Scanner file2 = new Scanner(new FileInputStream(pathToTmp));
             FileWriter fileWriter = new FileWriter("out.txt")) {
            if (isNum) {
                mergeInnerInt(discend, file1, file2, fileWriter);
            } else {
                mergeInnerString(discend, file1, file2, fileWriter);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (FileSystemException e) {
            System.out.println("Файл поврежден, нет сортировки");

            orderOfNums = true;
            int numOfFile = Integer.parseInt(e.getMessage());
            try (Scanner scanner = new Scanner(new FileInputStream(numOfFile == 1 ? pathFile : pathToTmp))) {
                while (scanner.hasNext()) {
                    if (isNum){
                        files.add(sortInt(discend, scanner));
                    } else {
                        files.add(sortStr(discend, scanner));
                    }
                }
            } catch (IOException exception) {
                System.out.println("Не удалось создать временный файл, разрешите доступ к папке, или переместите в другое место, затем запустите повторно");
            }
        } catch (NumberFormatException e){
            System.out.println("В файлах присутствуют пробелы, уберите пробелы, запустите повторно");
        } catch (IOException e) {
            System.out.println("Файл не читается");
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка, файл поврежден, продолжение работы программы невозможно");
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

        String tmp1 = null;
        String tmp2 = null;
        if (!file1.hasNext()){
            if (!file2.hasNext()){
                System.out.println("Файлы не содержат элементов");
            } else {
                tmp1 = replaceChar(file2.nextLine());
                tmp2 = replaceChar(file2.nextLine());
            }
        } else if (!file2.hasNext()){
            tmp2 = replaceChar(file1.nextLine());
            tmp1 = replaceChar(file1.nextLine());
        } else {
            tmp1 = replaceChar(file1.nextLine());
            tmp2 = replaceChar(file2.nextLine());
        }

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
                throw new FileSystemException("1");
            }
            if (!mergeOfOneInnerStrAndIsSorted(discend, file2, fileWriter, tmp1, tmp2)){
                throw new FileSystemException("2");
            }
        }
        if (file1.hasNext()) {
            if ((!hashMap.get(2).equals(""))
                    && (hashMap.get(2).compareTo(tmp2) * negativeOrPositive > 0)) {
                throw new FileSystemException("2");
            }
            if (!mergeOfOneInnerStrAndIsSorted(discend, file1, fileWriter, tmp2, tmp1)){
                throw new FileSystemException("1");
            }
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
                } else {
                    previous = tmp2;
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

        int tmp1 = 0;
        int tmp2 = 0;
        if (!file1.hasNext()){
            if (!file2.hasNext()){
                System.out.println("Файлы не содержат элементов");
            } else {
                tmp1 = file2.nextInt() * (negativeOrPositive);
                tmp2 = file2.nextInt() * (negativeOrPositive);
            }
        } else if (!file2.hasNext()){
            tmp2 = file1.nextInt() * (negativeOrPositive);
            tmp1 = file1.nextInt() * (negativeOrPositive);
        } else {
            tmp1 = file1.nextInt() * (negativeOrPositive);
            tmp2 = file2.nextInt() * (negativeOrPositive);
        }
        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, tmp1);
        hashMap.put(2, tmp2);

        while (file1.hasNext() && file2.hasNext()) {
            if (tmp1 > tmp2) {
                if (hashMap.get(2) > tmp2 ) {
                    throw new FileSystemException("2");
                } else {
                    hashMap.put(2, tmp2);
                }
                fileWriter.write((tmp2 * (negativeOrPositive)) + "\n");
                tmp2 = file2.nextInt() * (negativeOrPositive);
            } else {
                if (hashMap.get(1) > tmp1) {
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
            if (hashMap.get(1) > tmp1){ //проверяем последний элемент, идет ли он по порядку?
                throw new FileSystemException("1");
            }
            if (!mergeOfOneInnerIntAndIsSorted(discend, file2, fileWriter, tmp1, tmp2)){
                throw new FileSystemException("2");
            }
        }
        if (file1.hasNext()) {
            if (hashMap.get(2) > tmp2){ //проверяем последний элемент, идет ли он по порядку?
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
                if (previous > tmp2){
                    return false;
                }
            } else if (!written) {
                fileWriter.write(lastElement * (negativeOrPositive) + "\n");
                written = true;
            }
        }

        if (!written) {
            fileWriter.write("" + (Math.min(lastElement , tmp2)) * (negativeOrPositive) + "\n");
        }
        fileWriter.write("" + (Math.max(lastElement, tmp2)) * (negativeOrPositive));
        return true;
    }

    private static String sortStr(boolean discend, Scanner file) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < 16384; i++) { // 16kb
                list.add(replaceChar(file.nextLine()));
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

    private static int nextInt(Scanner file){
        try {
            return Integer.parseInt(file.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Была встречена и пропущена строка");
        }
        return nextInt(file);
    }

    private static String sortInt(boolean discend, Scanner file) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            for (int i = 0; i < 16384; i++) { // 16kb
                list.add(nextInt(file));
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