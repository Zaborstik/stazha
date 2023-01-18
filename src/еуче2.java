package src;

public class еуче2 {
    public static void main(String[] args) {
        String str = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        char[] chars = str.toCharArray();
        for (char c : chars) {
            System.out.println(String.valueOf(c).hashCode());
        }
    }
}
