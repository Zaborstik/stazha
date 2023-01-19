package src;

import java.io.FileInputStream;
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
        try {
            merge();
        } catch (FileSystemException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void merge() throws FileSystemException {
        throw new FileSystemException("1");
    }
}

