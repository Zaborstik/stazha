package src;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        mergeNums("/Users/zaborstik/Desktop/testsstr/in2.txt", "/Users/zaborstik/Desktop/testsstr/in1.txt", false, false);
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

            } else {
                mergeInnerString(isDiscendng, file1, file2, fileWriter);
            }


        } catch (IOException e) {
            System.out.println("Файл не читается");
        }
    }

    private static void mergeInnerString(boolean isDiscendng, Scanner file1, Scanner file2, FileWriter fileWriter) throws IOException {
        HashMap<Integer, String> hashMap = new HashMap<> ();

        //переменные буфера
        String tmp1 = file1.nextLine();
        String tmp2 = file2.nextLine();

        //кладем в мапу, чтобы проверять, отсортирован ли данный массив
        hashMap.put(1, tmp1);
        hashMap.put(2, tmp2);

        while (file1.hasNext() && file2.hasNext()) {
            if ((tmp2.compareTo(tmp1) * (isDiscendng ? -1 : 1)) < 0) {
                if (Objects.equals(hashMap.get(2), tmp2)){
                    System.out.println("warn");
                    //обработка неотсортированного массива
                    return;
                } else {
                    hashMap.put(2, tmp2);
                }

                fileWriter.write((tmp2) + "\n");
                tmp2 = file2.nextLine();
            } else {
//                if (hashMap.get(1) > num1){
//                    System.out.println("warn");
//                    //обработка неотсортированного массива
//                    return;
//                } else {
//                    hashMap.put(1, num1);
//                }
                fileWriter.write((tmp1 + "\n"));
                tmp1 = file1.nextLine();
            }
        }
    }
}

