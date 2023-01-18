package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mergeNums("/Users/zaborstik/Desktop/testsstr/in1.txt", "/Users/zaborstik/Desktop/testsstr/in2.txt", true, false);
    }

    public static void mergeNums(String pathFile1, String pathFile2, boolean isDiscendng, boolean isNum){
        //isDiscendng по возрастанию - false, по убыванию true

        //путь для сохранения out.txt
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

            //объединяем массивы, в случае, если массив не отсортирован, выполняем cортировку
            if (isNum){
                mergeInnerInt(isDiscendng, file1, file2, fileWriter);
            } else {
                mergeInnerString(isDiscendng, file1, file2, fileWriter);
            }


        } catch (FileSystemException e){
            System.out.println("Файл поврежден, вероятнее всего отусвет сортировка");
        }catch (IOException e) {
            System.out.println("Файл не читается");
        }catch (Exception e){
            System.out.println("Неизвестная ошибка");
        }
    }

    private static void mergeInnerString(boolean isDiscendng, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, String> hashMap = new HashMap<> ();

        //переменные буфера
        String tmp1 = file1.nextLine();
        String tmp2 = file2.nextLine();

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, "");
        hashMap.put(2, "");

        while (file1.hasNext() && file2.hasNext()){
            if ((tmp1.compareTo(tmp2) * (isDiscendng ? -1 : 1)) < 0){
                if ((!hashMap.get(2).equals("")) && (hashMap.get(2).hashCode() < tmp2.hashCode())){
                    System.out.println(hashMap.get(2) + " " + tmp2 + " " + hashMap.get(2).hashCode() + " " + tmp2.hashCode());
                    throw new FileSystemException("Отсуствует сортировка");
                } else {
                    hashMap.put(2, tmp2);
                }

                fileWriter.write((tmp2) + "\n");
                tmp2 = file2.nextLine();
            } else {
                if (hashMap.get(1).hashCode() < tmp1.hashCode()){
                    //обработка неотсортированного массива
                    throw new FileSystemException("Отсуствует сортировка");
                } else {
                    hashMap.put(1, tmp1);
                }
                fileWriter.write((tmp1 + "\n"));
                tmp1 = file1.nextLine();
            }
        }

//        в одном из массивов закончились элементы
        if (file2.hasNext()) {
            mergeOfOneInnerStr(isDiscendng, file2, fileWriter, tmp1, tmp2);
        }
        if (file1.hasNext()) {
            mergeOfOneInnerStr(isDiscendng, file1, fileWriter, tmp2, tmp1);
        }
    }

    private static void mergeOfOneInnerStr(boolean isDiscendng, Scanner file, FileWriter fileWriter, String lastElement, String tmp2) throws IOException {
        boolean written = false;        //записан ли последний символ кратчайщего массива (костыли)

        while (file.hasNext()){
            if ((lastElement.compareTo(tmp2) * (isDiscendng ? -1 : 1)) < 0 || written) {
                fileWriter.write((tmp2) + "\n");
                tmp2 = file.nextLine();
            } else if (!written) {
                fileWriter.write(lastElement + "\n");
                written = true;
            }
        }
        fileWriter.write("" + ((tmp2.compareTo(lastElement) * (isDiscendng ? -1 : 1)) < 0 ? lastElement : tmp2) + "\n");
        fileWriter.write("" + ((lastElement.compareTo(tmp2) * (isDiscendng ? -1 : 1)) < 0 ? lastElement : tmp2));
    }

    private static void mergeInnerInt(boolean isDiscendng, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        //переменные буфера
        int num1 = file1.nextInt() * (isDiscendng ? -1 : 1);
        int num2 = file2.nextInt() * (isDiscendng ? -1 : 1);

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, num1);
        hashMap.put(2, num2);

        while (file1.hasNext() && file2.hasNext()){
            if (num1 > num2){
                if (hashMap.get(2) > num2){
                    System.out.println("warn");
                    //обработка неотсортированного массива
                    return;
                } else {
                    hashMap.put(2, num2);
                }

                fileWriter.write((num2 * (isDiscendng ? -1 : 1)) + "\n");
                num2 = file2.nextInt() * (isDiscendng ? -1 : 1);
            } else {
                if (hashMap.get(1) > num1){
                    System.out.println("warn");
                    //обработка неотсортированного массива
                    return;
                } else {
                    hashMap.put(1, num1);
                }

                fileWriter.write((num1 * (isDiscendng ? -1 : 1)) + "\n");
                num1 = file1.nextInt() * (isDiscendng ? -1 : 1);
            }
        }

        //в одном из массивов закончились элементы
        if (file2.hasNext()) {
            mergeOfOneInnerInt(isDiscendng, file2, fileWriter, num1, num2);
        }
        if (file1.hasNext()) {
            mergeOfOneInnerInt(isDiscendng, file1, fileWriter, num2, num1);
        }
    }

    private static void mergeOfOneInnerInt(boolean isDiscendng, Scanner file, FileWriter fileWriter, int lastElement, int num2) throws IOException {
        //записан ли последний символ кратчайщего массива (костыли)
        boolean written = false;

        while (file.hasNext()){
            if (lastElement >= num2 || written) {
                fileWriter.write((num2 * (isDiscendng ? -1 : 1)) + "\n");
                num2 = file.nextInt() * (isDiscendng ? -1 : 1);
            } else if (!written) {
                fileWriter.write(lastElement * (isDiscendng ? -1 : 1) + "\n");
                written = true;
            }
        }
        fileWriter.write("" + (Math.min(lastElement,num2)) * (isDiscendng ? -1 : 1) + "\n");
        fileWriter.write("" + (Math.max(lastElement,num2)) * (isDiscendng ? -1 : 1));
    }
}