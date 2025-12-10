package co.rangel.day9;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Theater {

    static class Seat {
        long row;
        long col;

        public Seat(int row, int col) {
            this.row = (long)row;
            this.col = (long)col;
        }

        public long getArea(Seat s){
            return (Math.abs(row - s.row)+1L) * (Math.abs(col - s.col)+1L);
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + row +
                    ",y=" + col +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Seat seat = (Seat) o;
            return row == seat.row && col == seat.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    static class SeatArea {
        Seat tile1, tile2;
        long area;

        public SeatArea(Seat seat1, Seat seat2) {
            this.tile1 = seat1;
            this.tile2 = seat2;
            this.area = seat1.getArea(seat2);
        }

        @Override
        public String toString() {
            return "SeatArea{" +
                    "t1=" + tile1 +
                    ", t2=" + tile2 +
                    ", area=" + area +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            SeatArea seatArea = (SeatArea) o;
            return tile1.equals(seatArea.tile1) && tile2.equals(seatArea.tile2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tile1, tile2, area);
        }
    }


    public static List<List<String>> obtainGrid(String filePath) {
        // Read the file and obtain the grid
        List<List<Integer>> grid = new ArrayList<>();
        //descending sort
        PriorityQueue<SeatArea> areas = new PriorityQueue<>((a, b) -> Long.compare(b.area, a.area));
        List<Seat> seats = new ArrayList<>();
        int colsSize = 0;
        int rowsSize = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] cols = line.split(",");
                int col = Integer.parseInt(cols[0]);
                int row = Integer.parseInt(cols[1]);
                seats.add(new Seat(row, col));
                if (col > colsSize) {
                    colsSize = col;
                }
                if (row > rowsSize) {
                    rowsSize = row;
                }
                if (grid.size() < row) {
                    for (int i = grid.size(); i <= row + 1; i++) {
                        grid.add(new ArrayList<>());
                    }
                }
                List<Integer> rows = grid.get(row);
                if (rows.size() < col) {
                    for (int i = rows.size(); i <= col + 1; i++) {
                        rows.add(0);
                    }
                }
                rows.set(col, 1);
            }

//            System.out.println("rows " + grid.size());
//            int [][] gridArray = new int[grid.size()][colsSize+3];
//            for(int t=0; t < grid.size(); t++){
//                List<Integer> rows = grid.get(t);
////                if(rows.size() <= colsSize){
//                    for(int i = rows.size(); i <= colsSize+2; i++){
//                        rows.add(0);
//                    }
//                    for(int j = 0; j <= colsSize+2; j++){
//                        gridArray[t][j] = rows.get(j);
//                    }
////                }
//            }

            Seat p1 = seats.get(0);
            Seat p2 = seats.get(1);
            SeatArea maxArea = new SeatArea(p1, p2);
//            while(!seats.isEmpty()){
//                Seat s1 = seats.removeFirst();
//                for(Seat s2 : seats){
//                    if(s1.getArea(s2) > maxArea.area){
//                        maxArea = new SeatArea(s1, s2);
//                    }
//                    areas.add(new SeatArea(s1, s2));
//                }
//            }

            for(int i=0; i< seats.size(); i++){
                Seat s1 = seats.get(i);
                for(int j=i+1; j< seats.size(); j++){
                    Seat s2 = seats.get(j);
                    if(s1.getArea(s2) > maxArea.area){
                        maxArea = new SeatArea(s1, s2);
                    }
                    areas.add(new SeatArea(s1, s2));
                }
            }


            System.out.println("Max area:" + maxArea);
            System.out.println("======");
            for (int c=0; c < 5; c++){
                System.out.println(c + "). " + areas.poll());
            }


            System.out.println("Found grid of x:y -> " + rowsSize + ":" + colsSize);
//            printSeats(grid);
//            printSeats(gridArray);

        } catch (Exception e) {
            System.out.println("Error reading file");
        }
        return null;
    }



    public static void printSeats(List<List<Integer>> grid) {
        for (List<Integer> row : grid) {
            for (Integer seat : row) {
                if (seat == 0)
                    System.out.print(". ");
                else
                    System.out.print("# ");
            }
            System.out.println();
        }
    }

    public static void printSeats(int [][] grid) {
        for (int t =0; t < grid.length; t++) {
            for (int j=0; j < grid[t].length; j++) {
                int seat = grid[t][j];
                if (seat == 0)
                    System.out.print(". ");
                else
                    System.out.print("# ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Create a new instance of the Theater class
        // Call the printSeats method to print the seating arrangement
        Theater.obtainGrid(args[0]);
    }
}


