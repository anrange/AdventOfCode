package co.rangel.day6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MathProblem {


    public static List<Long> parseMathFile(String filePath) {
        List<Long> results = new ArrayList<>();
        Long total = 0l;
        try {
            List<List<Long>> matrix = new ArrayList<>();
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i=0; i<lines.size()-1; i++) {
                String line = lines.get(i);
                String[] parts = line.trim().split("\\s+");
                for(int j=0; j< parts.length; j++) {
                   if (parts[j].trim().isEmpty()) continue;
                   if( j >= matrix.size()) {
                       List<Long> array = new ArrayList<>();
                       array.add(Long.parseLong(parts[j]));
                       matrix.add(array);
                   }
                   else{
                       matrix.get(j).add(Long.parseLong(parts[j].trim()));
                   }
                }
            }
            String[] ops = lines.get(lines.size()-1).trim().split("\\s+");

            //do the operation

            System.out.println("Sizes: ops/nums" + ops.length + "/" + matrix.size() );
//            for(String op : ops) {
//                System.out.print(op + " ");
//            }
            int i = 0;
            for(int j=0; j<ops.length; j++) {
                String op = ops[j];
                if (!op.trim().isEmpty()) {
                    List<Long> column = matrix.get(i);
                    Long result=0L;

                    switch (op.trim()) {
                        case "+":
//                            exercise 1
                            for (Long n : column)
                                result += n;
                            break;
                       case "*":
//                          exercise 1
                            result = 1L;
                            for (Long n : column)
                                result *= n;

                            break;
                    }
                    results.add(result);
                    total += result;
                    i++;
                }
            }
            System.out.println("Total: " + total);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Got error: " + e.getLocalizedMessage());
        }

        return results;
    }

    public static List<Long> parseMathFileRTL(String filePath) {
        Long total = 0l;
        int totalRows = 0;
        int totalColumns = 0;
        List<List<Long>> numberList = new ArrayList<>();
        List<Long> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            totalRows = lines.size()-1;
            totalColumns = lines.get(0).length();
            //operation is the last line
            String[] ops = lines.get(lines.size()-1).trim().split("\\s+");
            System.out.println("Total numbers:" + ops.length);
            String opsUnfiltered = lines.get(lines.size()-1);
            for (int j=0; j < totalColumns; j++){
                String num = "";
                if(j < opsUnfiltered.length() && (opsUnfiltered.charAt(j) == '*' || opsUnfiltered.charAt(j) == '+' )){
//                    if(j>=results.size()) {
                        numberList.add(new ArrayList<>());
//                    }
                }
                for (int row = 0; row < totalRows; row++) {
                    String line = lines.get(row);
                    if (line.charAt(j) != ' ') {
                       num += line.charAt(j);
                    }
                }
                if (!num.isEmpty()) {
                    numberList.get(numberList.size()-1).add(Long.parseLong(num.trim()));
                }
            }
            System.out.println("Total numbers:" + numberList.size());

            int i = 0;
            for(int j=0; j<ops.length; j++) {
                String op = ops[j];
                if (!op.trim().isEmpty()) {
                    List<Long> column = numberList.get(i);
                    Long result=0L;

                    switch (op.trim()) {
                        case "+":
//                            exercise 2
                            for (Long n : column)
                                result += n;
                            break;
                        case "*":
//                          exercise 2
                            result = 1L;
                            for (Long n : column)
                                result *= n;

                            break;
                    }
                    total += result;
                    results.add(result);
                    i++;
                }
            }

            System.out.println("total = " + total);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Got error: " + e.getLocalizedMessage());
        }
        return results;
    }
    public static void main(String[] args) {
//        List<Long> r = parseMathFile(args[0]);

        List<Long> r2 = parseMathFileRTL(args[0]);


    }
}
