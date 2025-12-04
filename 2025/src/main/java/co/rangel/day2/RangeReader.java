package co.rangel.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RangeReader {
    public static class Range {
        public long start, end;
        
        public Range(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
    
    public static List<Range> readRanges(String filePath) throws IOException {
        List<Range> ranges = new ArrayList<>();
        String content = Files.readString(Paths.get(filePath));
        int max = 0;
        long maxRange = 0;
        for (String rangeStr : content.split(",")) {
            String[] parts = rangeStr.trim().split("-");
            ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
            if(parts[1].length() > max) {
                max = parts[1].length();
                maxRange = Long.parseLong(parts[1]);
            }
        }
        System.out.println("Max Length/# " + max +"/" + maxRange);
        return ranges;
    }

    public static long findInvalid(Range range) {
        long result = 0;
        for (long i = range.start; i <= range.end; i++) {

            if (_isInvalid(Long.toString(i))){
                result+=i;
                System.out.println("I:" + i);
            }
        }
        return result;
    }

    private static boolean _isInvalid(String number){
        if(number.length() == 1) return false;
        if(number.charAt(0) == '0') {
            System.out.println("Found leading zero:" + number);
            return false; //none of the numbers have leading 0
        }
        if(invalidHalf(number)) return true;
        if(invalidSolo(number)) return true;
        if (invalidPair(number)) return true;
        if (invalidTrio(number)) return true;
        return false;
    }

    private static boolean invalidSolo(String number){
        //checks if all the digits are the same
        for(int i = 0; i < number.length()-1; i++){
            if(number.charAt(i) != number.charAt(i+1)) return false;
        }
        System.out.println("SOLO");
        return true;
    }
    private static boolean invalidPair(String number){
        // return is True if number formed of repeated pairs
        if( number.length() % 2 != 0 || number.length() < 6) return false;
        for(int i = 0; i < number.length() - 3 ; i+=2){
            if(number.charAt(i) !=  number.charAt(i+2)) return false;
            if(number.charAt(i+1) !=  number.charAt(i+3)) return false;
        }
        System.out.println("PAIR");
        return true;
    }

    private static boolean invalidTrio(String number){
        // return True if thres a sequence of three repeteaded numbers
        if( number.length() % 2 == 0 || number.length() < 9) return false; //only apply to odd numbers

        for(int i = 0; i < number.length() - 5 ; i+=3){
            if(number.charAt(i) !=  number.charAt(i+3)) return false;
            if(number.charAt(i+1) !=  number.charAt(i+4)) return false;
            if(number.charAt(i+2) !=  number.charAt(i+5)) return false;
        }
        System.out.println("TRI");
        return true;
    }


    private static boolean invalidHalf(String number){
        // return is True if invalid else false
        if( number.length() % 2 != 0 ) return false;
        if(number.charAt(0) == '0') {
            System.out.println("Found leading zero:" + number);
            return false; //none of the numbers have leading 0
        }
        int half = number.length()/2;
        for(int i = 0; i < half; i++){
            if(number.charAt(i) !=  number.charAt(half + i)) return false;
        }
        System.out.println("HALF");
        return true;
    }


    
    public static void main(String[] args) throws IOException {
        List<Range> ranges = readRanges(args[0]);
        long total = 0;
        for (Range range : ranges) {
            total += findInvalid(range);
        }
        System.out.println("Total: " + total);

    }
}