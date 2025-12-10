package co.rangel.day10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {

    public static int bit_mask(int[] a ){
        int mask = 0;
        for(int i = 0; i < a.length; i++){
            mask += (int)Math.pow(2,a[i]);
        }
        return mask;
    }


    public static void processFile(String filePath) {
        try {
            int total = 0;
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                // Extract pattern in brackets
                String iLight = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
                int light_mask = 0;
                for(int i=0; i < iLight.length(); i++){
                    if (iLight.charAt(i) == '#')
                        light_mask += (int)Math.pow(2, i);
                }
                // Extract parentheses groups
                Pattern parenPattern = Pattern.compile("\\(([^)]+)\\)");
                Matcher parenMatcher = parenPattern.matcher(line);
                List<Integer> buttonList = new ArrayList<>();
                while (parenMatcher.find()) {
                    String [] p = parenMatcher.group(1).split(",");
                    Integer button = 0;
                    for(int i=0; i < p.length; i++){
                        button += (int)Math.pow(2, Integer.parseInt(p[i]));
                    }
                    buttonList.add(button);
                }

                Set<Integer> current = new HashSet<>();
                current.add(light_mask);

                Integer end = 0;
                int iteration = 0;
                while (current.contains(end) == false) {
                    Set<Integer> nx = new HashSet<>();
                    for (Integer c : current) {
                        for (Integer button : buttonList) {
                            nx.add(c ^ button);
                        }
                    }
                    current = nx;
                    iteration++;
                }
                total += iteration;
            }
            System.out.println("Total: " + total);


        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        String filePath = args[0];
        processFile(filePath);
    }
}
