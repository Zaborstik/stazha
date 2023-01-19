package src;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class авыаыф {
    public static void main(String[] args) {
        mergeNums("/Users/zaborstik/Desktop/testsstr/in1.txt", "/Users/zaborstik/Desktop/testsstr/in2.txt", false, false);
    }

    public static void mergeNums(String pathFile1, String pathFile2, boolean discend, boolean isNum){
        //isDiscendng по возрастанию - false, по убыванию true

        Path pathOut = Path.of(pathFile1).getParent().resolve("out.txt");
        if (!Files.exists(pathOut)){
            try {
                Files.createFile(pathOut);
            } catch (Exception e){
                System.out.println("Не удалось создать выходящий файл");
            }
        }

        try (Scanner file1 = new Scanner(new FileInputStream(pathFile1));
             Scanner file2 = new Scanner(new FileInputStream(pathFile2));
             FileWriter fileWriter = new FileWriter(pathOut.toFile())) {

            if (isNum){
                mergeInnerInt(discend, file1, file2, fileWriter);
            } else {
                mergeInnerString(discend, file1, file2, fileWriter);
            }


        } catch (FileNotFoundException e){
            System.out.println("Файл не найден");
        } catch (FileSystemException e){
            System.out.println("Файл поврежден, отусвет сортировка");

            int numOfFile = Integer.parseInt(e.getMessage());
            try (Scanner file = new Scanner(new FileInputStream(numOfFile == 1 ? pathFile1 : pathFile2));
                 FileWriter fileWriter = new FileWriter(Files.createTempFile(String.valueOf(Path.of(pathFile1).getParent()), null).toFile())) {
                sort(discend, file, fileWriter);
            } catch (IOException ex) {}
        }catch (IOException e) {
            System.out.println("Файл не читается");
        }catch (Exception e){
            System.out.println("Неизвестная ошибка");
        }
    }

    private static void mergeInnerString(boolean discend, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, String> hashMap = new HashMap<> ();

        String tmp1 = file1.nextLine();
        String tmp2 = file2.nextLine();

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, "");
        hashMap.put(2, "");

        while (file1.hasNext() && file2.hasNext()){
            if ((tmp2.compareTo(tmp1) * (discend ? -1 : 1)) < 0){
                if ((!tmp2.contains("Ё"))
                        &&(!hashMap.get(2).equals(""))
                        && ((hashMap.get(2).hashCode() * (discend ? -1 : 1)) > (tmp2.hashCode() * (discend ? -1 : 1)))){
                    throw new FileSystemException("2");
                } else {
                    if (!tmp2.contains("ё")) {
                        hashMap.put(2, tmp2);
                    }
                }
                fileWriter.write((tmp2) + "\n");
                tmp2 = file2.nextLine();
            } else {
                if ((!tmp1.contains("Ё"))
                        && (!hashMap.get(1).equals(""))
                        && ((hashMap.get(1).hashCode()* (discend ? -1 : 1)) > (tmp1.hashCode()) * (discend ? -1 : 1))){
                    throw new FileSystemException("1");
                } else {
                    if (!tmp1.contains("ё")){
                        hashMap.put(1, tmp1);
                    }
                }
                fileWriter.write((tmp1 + "\n"));
                tmp1 = file1.nextLine();
            }
        }

//        в одном из массивов закончились элементы
        if (file2.hasNext()) {
            mergeOfOneInnerStr(discend, file2, fileWriter, tmp1, tmp2);
        }
        if (file1.hasNext()) {
            mergeOfOneInnerStr(discend, file1, fileWriter, tmp2, tmp1);
        }
    }

    private static void mergeOfOneInnerStr(boolean discend, Scanner file, FileWriter fileWriter, String lastElement, String tmp2) throws IOException {
        boolean written = false;        //записан ли последний символ кратчайщего массива (костыли)

        while (file.hasNext()){
            if ((tmp2.compareTo(lastElement) * (discend ? -1 : 1)) < 0 || written) {
                fileWriter.write((tmp2) + "\n");
                tmp2 = file.nextLine();
            } else if (!written) {
                fileWriter.write(lastElement + "\n");
                written = true;
            }
        }
        if (!written) {
            fileWriter.write("" + ((lastElement.compareTo(tmp2) * (discend ? -1 : 1)) < 0 ? lastElement : tmp2) + "\n");
        }
        fileWriter.write("" + ((tmp2.compareTo(lastElement) * (discend ? -1 : 1)) < 0 ? lastElement : tmp2));
    }

    private static void mergeInnerInt(boolean discend, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        int tmp1 = file1.nextInt() * (discend ? -1 : 1);
        int tmp2 = file2.nextInt() * (discend ? -1 : 1);

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, tmp1);
        hashMap.put(2, tmp2);

        while (file1.hasNext() && file2.hasNext()){
            if (tmp1 > tmp2){
                if (hashMap.get(2) > tmp2){
                    throw new FileSystemException("2");
                } else {
                    hashMap.put(2, tmp2);
                }
                fileWriter.write((tmp2 * (discend ? -1 : 1)) + "\n");
                tmp2 = file2.nextInt() * (discend ? -1 : 1);
            } else {
                if (hashMap.get(1) > tmp1){
                    throw new FileSystemException("1");
                } else {
                    hashMap.put(1, tmp1);
                }
                fileWriter.write((tmp1 * (discend ? -1 : 1)) + "\n");
                tmp1 = file1.nextInt() * (discend ? -1 : 1);
            }
        }

        //в одном из массивов закончились элементы
        if (file2.hasNext()) {
            mergeOfOneInnerInt(discend, file2, fileWriter, tmp1, tmp2);
        }
        if (file1.hasNext()) {
            mergeOfOneInnerInt(discend, file1, fileWriter, tmp2, tmp1);
        }
    }

    private static void mergeOfOneInnerInt(boolean discend, Scanner file, FileWriter fileWriter, int lastElement, int tmp2) throws IOException {
        boolean written = false;        //записан ли последний символ кратчайщего массива (костыли)


        while (file.hasNext()){
            if (lastElement >= tmp2 || written) {
                fileWriter.write((tmp2 * (discend ? -1 : 1)) + "\n");
                tmp2 = file.nextInt() * (discend ? -1 : 1);
            } else if (!written) {
                fileWriter.write(lastElement * (discend ? -1 : 1) + "\n");
                written = true;
            }
        } // проверить как с str
        if (!written){
            fileWriter.write("" + (Math.min(lastElement,tmp2)) * (discend ? -1 : 1) + "\n");
        }
        fileWriter.write("" + (Math.max(lastElement,tmp2)) * (discend ? -1 : 1));
    }

    private static void sort(boolean discend, Scanner file, FileWriter fileWriter){
        String[] strings = new String[32768]; //64кб

    }
}

