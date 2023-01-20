package src;

import java.util.ArrayList;
import java.util.Collections;

public class test {
    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>(){{
            add("А");
            add("б");
            add("в");
            add("г");
            add("д");
            add("Ё");
            add("ж");
            add("I");
            add("ô");
            add("â");
            add("j");
            add("ÿ");
            add("w");
            add("é");
            add("b");
            add("ñ");
        }};
        Collections.sort(strings);
        for (String s : strings) {
            System.out.print(s + " ");
        }
    }
}
