package co.rangel.day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


class Result{
    int [][] matrix;
    int counter;

    public  Result(int [][] matrix, int counter){
        this.matrix = matrix;
        this.counter = counter;
    }
}

public class MatrixReader {



    private enum CellType {
        CORNER, EDGE, MIDDLE
    }

    public static int[][] readMatrix(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int[][] matrix = new int[lines.size()][lines.get(0).length()];
        
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                matrix[i][j] = lines.get(i).charAt(j) == '@' ? 1 : 0;
            }
        }
        
        return matrix;
    }

    public static  Result rollsBlocked(int[][] matrix){

        int columnSize = matrix[0].length;
        int rowSize = matrix.length;
        int counter = 0;
        int[][] result = new int[rowSize][columnSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                //check the cellType {0,1,2} {corner, edge, middler}
                if (matrix[i][j] == 1) { //only check if there's a paper roll
                    CellType cellType = getCellType(i, j, rowSize, columnSize);
                    result[i][j] = 1;
                    switch (cellType) {

                        case CellType.CORNER:
                            //all corners don't have 4 adjacent cells
                            result[i][j] = 2;
                            counter += 1;
                            break;

                        case EDGE:
                            int adjacent = getNumAdjacentEdge(matrix, i, j, rowSize, columnSize);
                            if (adjacent < 4) {
                                result[i][j] = 2;
                                counter += 1;
                            }
                            break;

                        case MIDDLE:
                            int adjacentMiddle = getNumAdjacentMiddle(matrix, i, j, rowSize, columnSize);
                            if (adjacentMiddle < 4) {
                                result[i][j] = 2;
                                counter += 1;
                            }
                            break;
                        default:
                            System.out.println("Wrong cell type!!");
                    }
                }

            }
        }
//        System.out.println("Total: " + counter);
        return new Result(result, counter);




    }

    private static int getNumAdjacentEdge(int[][] matrix, int i, int j, int rowSize, int columnSize) {
        //edge has to check 5 places
        if( i == 0){
            //upper row
            return  matrix[i][j-1] +
                    matrix[i][j+1] +
                    matrix[i+1][j-1] +
                    matrix[i+1][j] +
                    matrix[i+1][j+1];
        }
        else if( i == rowSize-1){
            //lower row
            return  matrix[i][j-1] +
                    matrix[i][j+1] +
                    matrix[i-1][j-1] +
                    matrix[i-1][j] +
                    matrix[i-1][j+1];
        }
        else if( j == 0){
            //left column
            return  matrix[i-1][j] +
                    matrix[i-1][j+1] +
                    matrix[i][j+1] +
                    matrix[i+1][j] +
                    matrix[i+1][j+1];
        }
        else if( j == columnSize-1){
            //right column
            return  matrix[i-1][j] +
                    matrix[i-1][j-1] +
                    matrix[i][j-1] +
                    matrix[i+1][j] +
                    matrix[i+1][j-1];
        }
        else{
            System.out.println("Wrong cell type!!");
            return 0;
        }
    }

    private static int getNumAdjacentMiddle(int[][] matrix, int i, int j, int rowSize, int columnSize) {

        // Add all the adjacent cells from the middle cell
        return  matrix[i-1][j-1] +
                matrix[i-1][j] +
                matrix[i-1][j+1] +
                matrix[i][j-1] +
                matrix[i][j+1] +
                matrix[i+1][j-1] +
                matrix[i+1][j] +
                matrix[i+1][j+1];



    }

    private static CellType getCellType(int x, int y, int rows, int cols){

        if(x == 0 && y == 0){
            return CellType.CORNER;
        }else if(x == 0 && y == cols-1){
            return CellType.CORNER;
        }else if(x == rows-1 && y == 0){
            return CellType.CORNER;
        }else if(x == rows-1 && y == cols-1){
            return CellType.CORNER;
        }else if(x == 0 || x == rows-1){
            return CellType.EDGE;
        }else if(y == 0 || y == cols-1){
            return CellType.EDGE;
        }else{
            return CellType.MIDDLE;
        }
    }

    private static void printMatrix(int[][] matrix) {



        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                switch (matrix[i][j]) {
                    case 0 -> System.out.print(". ");
                    case 1 -> System.out.print("@ ");
                    default -> System.out.print("X "); //should be 2
                }
            }
            System.out.println();
        }
    }

    private static int [][] cleanMatrix(int [][] matrix, int[][] result){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(result[i][j] == 2){
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    public static void main(String args[])  throws IOException {

        System.out.println("Loading matrix from: " + args[0]);
        int[][] matrix = readMatrix(args[0]);

//        printMatrix(matrix);
        Result r;
        int total = 0;
        do {
            r = rollsBlocked(matrix);
            cleanMatrix(matrix, r.matrix);
            total += r.counter;
            System.out.println("Found:" + r.counter);
        } while(r.counter > 0);

        System.out.println("Total: " + total);




//        printMatrix(result);




    }
}