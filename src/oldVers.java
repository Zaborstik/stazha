import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class oldVers {
    public static void main(String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        if (!Files.exists(Path.of("/Users/zaborstik/Desktop/out.txt"))){
            try {
                Files.createFile(Path.of("/Users/zaborstik/Desktop/out.txt"));
            } catch (Exception e){
                System.out.println("Не удалось создать выходящий файл");
            }
        }

        try (Scanner file1 = new Scanner(new FileInputStream("/Users/zaborstik/Desktop/in1.txt"));
             Scanner file2 = new Scanner(new FileInputStream("/Users/zaborstik/Desktop/in2.txt"));
             FileWriter fileWriter = new FileWriter("/Users/zaborstik/Desktop/out.txt")) {
            //записан ли последний символ кратчайщего массива (костыли)
            boolean written = false;
            //по возрастанию - false, по убыванию true
            boolean isDiscendng = true;
            //переменные буфера
            int num1 = file1.nextInt() * (isDiscendng ? -1 : 1);
            int num2 = file2.nextInt() * (isDiscendng ? -1 : 1);

            //кладем в мапу, чтобы проверять, отсортирован ли данный массив
            hashMap.put(1, num1);
            hashMap.put(2, num2);

            //работаем
            while (file1.hasNext() && file2.hasNext()){
                if (num1 > num2){
                    if (hashMap.get(2) > num2); //обработка неотсортированного массива
                    fileWriter.write((num2 * (isDiscendng ? -1 : 1)) + "\n");
                    num2 = file2.nextInt() * (isDiscendng ? -1 : 1);
                } else {
                    if (hashMap.get(1) > num1); //обработка неотсортированного массива
                    fileWriter.write((num1 * (isDiscendng ? -1 : 1)) + "\n");
                    num1 = file1.nextInt() * (isDiscendng ? -1 : 1);
                }
            }

            if (file2.hasNext()) {
                while (file2.hasNext()) {
                    if (num1 >= num2 || written) {
                        fileWriter.write((num2 * (isDiscendng ? -1 : 1)) + "\n");
                        num2 = file2.nextInt() * (isDiscendng ? -1 : 1);
                    } else if (!written) {
                        fileWriter.write(num1 * (isDiscendng ? -1 : 1));
                        written = true;
                    }
                }
                fileWriter.write("" + (num2) * (isDiscendng ? -1 : 1));
            }

            if (file1.hasNext()) {
                while (file1.hasNext()) {
                    if (num1 <= num2 || written) {
                        fileWriter.write((num1 * (isDiscendng ? -1 : 1)) + "\n");
                        num1 = file1.nextInt() * (isDiscendng ? -1 : 1);
                    } else if (!written) {
                        fileWriter.write(num2 * (isDiscendng ? -1 : 1));
                        written = true;
                    }
                }
                fileWriter.write("" + (num1 * (isDiscendng ? -1 : 1)));
            }

        } catch (IOException e) {
            System.out.println("Файл не читается");
        }
    }
}