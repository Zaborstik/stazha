package src;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(new FileInputStream("/Users/zaborstik/Desktop/testsstr/in2.txt"))){
            for (int i = 0; i < 200; i++) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e){

        }
    }

    public static void merge() throws FileSystemException {
        throw new FileSystemException("1");
    }
}

