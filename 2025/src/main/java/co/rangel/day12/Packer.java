package co.rangel.day12;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;

public class Packer {
    static class Shape{
        char[][] image;
        int height;
        int width;
        long area;

        Shape(){
            super();
        }
        public Shape(List<String> lines){
            //###
            //##.
            //##.

            width = lines.get(0).length();
            height = lines.size();
            image = new char[height][width];
            area = 0L;
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    image[i][j] = lines.get(i).charAt(j);
                    if(image[i][j] == '#'){
                        area++;
                    }
                }
            }

        }

        public List<Shape> rotate(){
            Shape rotatedClock = new Shape();
            rotatedClock.width = height;
            rotatedClock.height = width;
            rotatedClock.image = new char[width][height];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    rotatedClock.image[j][height - i - 1] = image[i][j];
                }
            }
            Shape rotatedCounter = new Shape();
            rotatedCounter.width = height;
            rotatedCounter.height = width;
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    rotatedCounter.image[width - j - 1][i] = image[i][j];
                }
            }

            Shape rotated180 = new Shape();
            rotated180.width = width;
            rotated180.height = height;

            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    rotated180.image[height - i - 1][width - j - 1] = image[i][j];
                }
            }
            List<Shape> rotations = List.of(rotatedClock, rotatedCounter, rotated180);
            return rotations;
        }

        @Override
        public String toString() {
            return "Shape{" +
                    "image=\n" + getArrayStr() +
                    ", height=" + height +
                    ", width=" + width +
                    '}';
        }

        String getArrayStr(){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    sb.append(image[i][j]);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }


    static class Regions{
        class Region{
            Shape shape;
            int count;

            Region(Shape s, int count){
                this.shape = s;
                this.count = count;
            }
        }
        long totalArea = 0L;
        List<Region> shapes;
        long width, height;

        Regions(int height, int width){
            this.height =(long) height;
            this.width = (long) width;
            this.shapes = new ArrayList<>();

        }
        void addShape(Shape s, int count){
            shapes.add(new Region(s, count));
            totalArea += s.area * count;
        }

        boolean isPossible(){

            return (height * width) >= totalArea;
        }


//        public String toString(){
//            StringBuilder sb = new StringBuilder();
//            sb.append(height).append("x").append(width).append(": ");
//            for(Shape s: shapes.keySet()){
//                sb.append(s).append(" ");
//            }
//            return sb.toString();
//        }
    }


    static long organize(String file){
        long total = 0l;
        HashMap<Integer, Shape> shapes = new HashMap<>();
        try{
            List<String> lines = Files.readAllLines(Path.of(file));
            int i = 0;
            do{
//                read the element
                String shape_key = lines.get(i).trim().substring(0,lines.get(i).trim().length() -1);
                i+=1;
                List<String> _shape = new ArrayList<>();
            while(!lines.get(i).trim().isEmpty()){
                _shape.add(lines.get(i).trim());
                i++;
            }
            i+=1;
            Shape s = new Shape(_shape);
            shapes.put(Integer.parseInt(shape_key), s);

            }while(i < lines.size() && lines.get(i).trim().split(" ").length < 2);

//        load the matrices
//            4x4: 0 0 0 0 2 0
//            12x5: 1 0 1 0 2 2
//            12x5: 1 0 1 0 3 2

            List<Regions> allRegions = new ArrayList<>();
            total = 0;
            while(i < lines.size()){
                String[] line_parts = lines.get(i).trim().split(" ");
                String[] wxl = line_parts[0].substring(0,line_parts[0].length()-1).split("x");
                int height = Integer.parseInt(wxl[0]);
                int width = Integer.parseInt(wxl[1]);
                Regions region = new Regions(height, width);
                for(int j = 1; j < line_parts.length; j++){
                    int t =  Integer.parseInt(line_parts[j]);
                    if ( t > 0){
                        Shape s = shapes.get(Integer.valueOf(j-1));
                        region.addShape(s, t);
                    }
                }
                allRegions.add(region);
                i+=1;
                if(region.isPossible()){
                    total++;
                }
            }
//            return total;
//            for(Shape s: shapes.values()){
//                System.out.println(s);
//            }
//            total = 0L;
//            for(Regions r: allRegions) {
//
//                for(Regions.Region w : r.shapes){
//                    System.out.println(w.shape + "-" + w.count);
//                }
//                total += fitShapes(r);
//                System.out.println();
//            }
//


        }
        catch(Exception e){
            System.out.println(e.fillInStackTrace().toString());
            return -1L;
        }
        return total;
    }

//    private static long fitShapes(Regions r) {
//
//        long area = 0L;
//        for(Regions.Region w: r.shapes){
//            for(int i=0; i < w.count; i++){
//                area
//
//                if (matrix2 != null){
//                    matrix = matrix2;
//                }
//                else{
//                    System.out.println("No fit for " + w.shape);
//                }
//            })
//            int[][] matrix2 = placeShape(w.shape, matrix);
//            for(int i = 0; i < w.count; i++){
//                List<Shape> rotations = w.shape.rotate();
//                for(Shape s: rotations){
//                    System.out.println(s);
//                }
//            }
//        }
//
//        for (Regions.Region w: r.shapes){
//            for(int i=0; i < w.count; i++){
//                int[][] matrix2 = placeShape(w.shape, matrix);
//
//                if (matrix2 != null){
//                    matrix = matrix2;
//                }
//                else{
//                    System.out.println("No fit for " + w.shape);
//                }
//            })
//            int[][] matrix2 = placeShape(w.shape, matrix);
//            for(int i = 0; i < w.count; i++){
//                List<Shape> rotations = w.shape.rotate();
//                for(Shape s: rotations){
//                    System.out.println(s);
//                }
//            }
//        })
//
//
//    }

    public static void main(String[] args) {
        System.out.println(organize(args[0]));
    }
}
