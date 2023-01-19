package src;

public class еуче2 {
    public static void main(String[] args) {
//        String[] strings = {"А", "Б", "Д", "Г", "А"};
//        String[] strings1 = {"Е", "Ё", "А", "К", "Л"};
//        for (String s :
//                merge(strings1, strings)) {
//            System.out.println(s);
//        }

        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            System.out.println(String.valueOf(i).hashCode());;
        }
    }
//    public static String[] merge( String[] leftPart, String[] rightPart ) {
//        int cursorLeft = 0, cursorRight = 0, counter = 0;
//        String[] merged = new String[leftPart.length + rightPart.length];
//        while ( cursorLeft < leftPart.length && cursorRight < rightPart.length ) {
//            if (leftPart[cursorLeft].compareTo(rightPart[cursorRight] ) < 0 ) {
//                merged[counter] = leftPart[cursorLeft];
//                cursorLeft+=1;
//            } else {
//                merged[counter] = rightPart[cursorRight];
//                cursorRight+=1;
//            }
//            counter++;
//        }
//        if ( cursorLeft < leftPart.length ) {
//            System.arraycopy( leftPart, cursorLeft, merged, counter, merged.length - counter );
//        }
//        if ( cursorRight < rightPart.length ) {
//            System.arraycopy( rightPart, cursorRight, merged, counter, merged.length - counter );
//        }
//        return merged;
//    }
}
