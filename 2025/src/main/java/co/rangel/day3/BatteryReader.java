package co.rangel.day3;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BatteryReader {

    public static List<String> readWordsFromFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    public static int maxJoltage(String battery){

        int max = Character.getNumericValue(battery.charAt(0));
        int pos = 0;
        for(int i=1; i< battery.length() - 1; i++){
            int value = Character.getNumericValue(battery.charAt(i));
            if (value > max) {
                max = value;
                pos = i;
            }
        }
        int max2 = Character.getNumericValue(battery.charAt(pos+1));
        int pos2 = pos + 1;
        for (int i = pos + 2; i < battery.length(); i++) {
            int value = Character.getNumericValue(battery.charAt(i));
            if (value > max2) {
                max2 = value;
                pos2 = i;
            }
        }

        System.out.println("Max:pos" + max + ":" + pos + ", M2:pos" + max2 + ":" + pos2);
        int jolt = (max * 10) + max2;
        System.out.println("joltage: " + jolt);
        return jolt;

    }

    public static BigInteger max12Joltage(String battery){
        int[] maxArray = new int[12];
        int pos = 0;
        String rt = "";
        for(int i=0; i< 12; i++) {
            int [] result = maxValue(battery, pos, 12 - i);
            maxArray[i] = result[0];
            pos = result[1]+1;
            rt += Integer.toString(result[0]);
        }

        BigInteger joltage = new BigInteger(rt);
        System.out.println("joltage: " + joltage.toString());
        return joltage;

    }

    private static int[] maxValue(String battery, int start, int end){

        int max = Character.getNumericValue(battery.charAt(start));
        int pos = start;
        for (int i = start + 1; i <= battery.length() - end; i++) {
            int value = Character.getNumericValue(battery.charAt(i));
            if (value > max) {
                max = value;
                pos = i;
            }
        }
        return new int[]{max, pos};
    }

    public static void main(String[] args) throws IOException {

        String filePath = args[0];

        List<String> batteries = readWordsFromFile(filePath);

        BigInteger joltage = new BigInteger("0");
        for(String battery : batteries){
            System.out.print("Bat:" + battery + "->");
//            joltage += maxJoltage(battery);
            joltage = joltage.add(max12Joltage(battery));

        }
        System.out.println("result:" + joltage);
    }
}