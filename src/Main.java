package src;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mergeNums("/Users/zaborstik/Desktop/in1.txt", "/Users/zaborstik/Desktop/in2.txt", false);
    }

    public static void mergeNums(String pathFile1, String pathFile2, boolean isDiscendng){ //по возрастанию - false, по убыванию true

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

            //объединяем массивы, в случае, если массив не отсортирован, выполняем ортировку
            mergeInner(isDiscendng, file1, file2, fileWriter);

        } catch (IOException e) {
            System.out.println("Файл не читается");
        }
    }

    private static void mergeInner(boolean isDiscendng, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
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
            mergeOfOneInner(isDiscendng, file2, fileWriter, num1, num2);
        }

        if (file1.hasNext()) {
            mergeOfOneInner(isDiscendng, file1, fileWriter, num2, num1);
        }
    }

    private static void mergeOfOneInner(boolean isDiscendng, Scanner file, FileWriter fileWriter, int lastElement, int num2) throws IOException {
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
        fileWriter.write("" + (num2) * (isDiscendng ? -1 : 1));
    }
}