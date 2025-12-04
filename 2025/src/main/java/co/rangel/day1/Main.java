package co.rangel.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// To run this code pass the path to the file:
// example: 2025/day1/exercise1.txt
// Result will be value for section 2. Zeroes will be the value for first section
public class Main {
    private static final int ADD = 1;
    private static final int SUB = -1;

    public static List<String> readWordsFromFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        String filePath = args[0];

        try {
            List<String> rotations  = readWordsFromFile(filePath);
            int zeros = 0;
            int result = 0;
            int count = 50;
            int operation  = ADD;
            for (String rotation : rotations) {
                System.out.println("count:" + count +", GOT:" + rotation);
                if( rotation.charAt(0) == 'L'){
                    operation  = SUB;
                }
                else if( rotation.charAt(0) == 'R'){
                    operation  = ADD;
                }
                else{
                    System.out.println("Got a wrong character " + rotation.charAt(0));
                }
                int _number  = Integer.parseInt(rotation.substring(1)) ;
                int loops = _number / 100;
                int number = _number % 100;
                int prev = count;
                count += (number * operation);
                if(number > 0) {
                    if (count < 0) {
                        count = 100 - Math.abs(count);
                        if(prev != 0) result += 1;


                    } else if (count > 100) {
                        count = count % 100;
                        result += 1;

                    } else if (count == 0 || count == 100) {
                        zeros += 1;
                        result += 1;
                        count = 0;
                    }
                }
                result += loops;
                System.out.println("Count" + count + ",r:" + result);
            }
            System.out.println("total=" + result + ", zeroes=" + zeros);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}