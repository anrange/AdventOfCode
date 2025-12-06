package co.rangel.day5;

import com.sun.source.tree.Tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Ingredients {
    public static class Range {
        public long start, end;
        
        public Range(String start, String end) {
            this.start = Long.parseLong(start);
            this.end = Long.parseLong(end);
        }
        public Range(Long start, Long end) {
            this.start = start;
            this.end = end;
        }
    }
    
    public static class ParseResult {
        public List<Range> ranges;
        public List<Long> numbers;
        
        public ParseResult(List<Range> ranges, List<Long> numbers) {
            this.ranges = ranges;
            this.numbers = numbers;
        }
    }

    public static class CheckIngredientsResult {
        public List<Long> spoiledIngredients;
        public List<Long> goodIngredients;

        public CheckIngredientsResult() {
            this.spoiledIngredients = new ArrayList<>();
            this.goodIngredients = new ArrayList<>();
        }

        public void addSpoiledIngredient(Long ingredient) {
            spoiledIngredients.add(ingredient);
        }

        public void addGoodIngredient(Long ingredient) {
            goodIngredients.add(ingredient);
        }

    }
    
    public ParseResult parseFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
//        TreeSet<Range> rangeSet = new TreeSet<>(Comparator.comparingLong(r -> r.start));
        List<Long> numbers = new ArrayList<>();
        List<Range> ranges = new ArrayList<>();
        
        int i = 0;
        // Parse ranges until empty line
        while (i < lines.size() && !lines.get(i).trim().isEmpty()) {
            String[] parts = lines.get(i).split("-");
//            ranges.add(new Range(parts[0],parts[1]));
            insertMergeIngredient(new Range(parts[0], parts[1]), ranges);
            i++;
        }
        
        // Skip empty line
        i++;
        
        // Parse numbers
        while (i < lines.size()) {
            numbers.add(Long.parseLong(lines.get(i).trim()));
            i++;
        }
        
        return new ParseResult(ranges, numbers);
    }

    public static CheckIngredientsResult checkIngredients(List<Range> ranges, List<Long> ingredients) {
        CheckIngredientsResult result = new CheckIngredientsResult();
        if (ranges == null || ranges.isEmpty()) {
            return result; // nothing to check -> spoiled
        }
        for (long ingredient : ingredients) {
            boolean spoiled = checkSpoiled(ingredient, ranges);

            if (spoiled) {
                result.addSpoiledIngredient(ingredient);
            } else {
                result.addGoodIngredient(ingredient);
            }
        }

        return result;
    }


    public static boolean checkSpoiled(long ingredient, List<Range> ranges) {

        for( Range range : ranges) {
            if (ingredient >= range.start && ingredient <= range.end) {
                return false; // ingredient is in range -> not spoiled
            }
        }

        return true;

    }

    public static Long checkIngredientsSecondPhase(List<Range> ranges) {
        long total = 0L;
        for (Range range : ranges) {
            total += (range.end - range.start) + 1;
        }
        return total;
    }


    /**
     * Insert 'range' into 'ranges' (sorted by start, non-overlapping) and merge overlaps.
     * If you want to merge adjacent intervals (e.g., [1,5] + [6,10]), set 'adjacent' to 1; else 0.
     */
    public static void insertMergeIngredient(Range range, List<Range> ranges) {
        if (ranges.isEmpty()) {
            ranges.add(new Range(range.start, range.end));
            return;
        }

        // 1) Find insertion index by start (linear
        int i = 0;
        while (i < ranges.size() && ranges.get(i).start <= range.start) i++;

        // Start with the new interval
        long mergedStart = range.start;
        long mergedEnd   = range.end;

        // 2) Merge left neighbor if overlapping/adjacent
        int left = i - 1;
        if (left >= 0 && ranges.get(left).end  >= mergedStart) {
            mergedStart = Math.min(mergedStart, ranges.get(left).start);
            mergedEnd   = Math.max(mergedEnd,   ranges.get(left).end);
            ranges.remove(left);
            i = left; // new insertion point shifts left
        }

        // 3) Merge all right neighbors that overlap/are adjacent
        while (i < ranges.size() && ranges.get(i).start <= mergedEnd) {
            Range r = ranges.get(i);
            mergedStart = Math.min(mergedStart, r.start);
            mergedEnd   = Math.max(mergedEnd,   r.end);
            ranges.remove(i); // do not advance 'i' because list shrinks
        }

        // 4) Insert the merged interval
        ranges.add(i, new Range(mergedStart, mergedEnd));
    }



    public static void main(String[] args) throws IOException {
        Ingredients ingredients = new Ingredients();
        ParseResult result = ingredients.parseFile(args[0]);
        
        System.out.println("Ranges: " + result.ranges.size());
        for (Range range : result.ranges) {
            System.out.println(range.start + "-" + range.end);
        }
        System.out.println("Numbers: " + result.numbers.size());

       Long t = checkIngredientsSecondPhase(result.ranges);
        System.out.println("Got #" + t);

//        CheckIngredientsResult r = checkIngredients(result.ranges, result.numbers);
//        System.out.println("Good: " + r.goodIngredients.size());
////        for (long i : r.goodIngredients) {
////            System.out.println(i);
////        }
//        System.out.println("Spoiled: " + r.spoiledIngredients.size());
//
//
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
//            for (Long num : r.spoiledIngredients) {
//                writer.write(num.toString());
//                writer.newLine(); // write each number on a new line
//            }
////            System.out.println("Numbers written to " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
