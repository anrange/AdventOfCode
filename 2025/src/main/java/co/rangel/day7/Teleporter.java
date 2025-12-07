package co.rangel.day7;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Teleporter {

    public static  int teleport(String filePath) {
        int count = 0;
        try {
            int columns = 0;
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            columns = lines.get(0).length();
            int[] tachyon = new int[columns]; //initialized on 0
            for (String line : lines) {
                for (int i=0; i < columns; i++) {
                    if (line.charAt(i) == 'S') {
                        tachyon[i] = 1; //start
                    } else if (line.charAt(i) == '^') {
                        //try to split
                        if (!(tachyon[i - 1] == 1 && tachyon[i] == 0 && tachyon[i + 1] == 1)) {
                            tachyon[i] = 0;
                            tachyon[i - 1] = 1;
                            tachyon[i + 1] = 1;
                            //split happened
                            count++;
                        } else {
                            System.out.println("Not happened");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total count:" + count);
        return count;

    }

    public static  long teleportSplit(String filePath) {
        long count = 0L;
        try {
            int columns = 0;
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            columns = lines.get(0).length();
            long[] tachyon = new long[columns]; //initialized on 0
            for (int row =0; row < lines.size(); row++) {
                String line = lines.get(row);
                for (int i = 0; i < columns; i++) {
                    if (line.charAt(i) == 'S') {
                        tachyon[i] = 1L; //start
                    } else if (line.charAt(i) == '^') {
                        //try to split
                        tachyon[i - 1] += tachyon[i];
                        tachyon[i + 1] += tachyon[i];
                        tachyon[i] = 0L;
                        //split happened
                    }
//                    else {
//                        System.out.println("Not happened");
//                    }
                }
                System.out.print("Row: " + row + " -> [");
                for (int i = 0; i < columns; i++) {
                    System.out.printf("%03d ", tachyon[i]);
                }
                System.out.println("]");
            }
            for (int i = 0; i < columns; i++) {
                count += tachyon[i];
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total count:" + count);

        return count;

    }


    public static void main(String[] args) {
        String filePath = args[0];
//        int count = Teleporter.teleport(filePath);
//        System.out.println("Count:" + count);

        long count = Teleporter.teleportSplit(filePath);
        System.out.println("Count:" + count);

    }

}
