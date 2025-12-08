package co.rangel.day8;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;

public class JunctionBox {

    private static class Point{
        int x,y,z;
//        List<Point> points = null;
        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
//            points = new ArrayList<>();
        }

        public long distance(Point other) {
            return (long) (Math.pow(x - other.x, 2) +
                                Math.pow(y - other.y, 2) +
                                Math.pow(z - other.z, 2));

        }

        @Override
        public String toString() {
            return "{" + x + "," + y + "," + z + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class Distance{
        Point a, b;
        long distance;

        public Distance(Point a, Point b, long distance) {
            this.a = a;
            this.b = b;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "Distance{" +
                    "a=" + a +
                    ", b=" + b +
                    ", distance=" + distance +
                    '}';
        }
    }


    private static class Circuit implements Comparable<Circuit> {
        List<Point> points = new ArrayList<>();

        public int addJunction(Point point) {
            if(points.contains(point)){
                return points.indexOf(point);
            }
            points.add(point);
            return points.size() - 1;
        }

        public boolean hasJunction(Point point) {
            return points.contains(point);
        }

        @Override
        public String toString() {
            StringBuffer pointStr = new StringBuffer("{");
            for (Point point : points) {
                pointStr.append(point + ", ");
            }
            pointStr.append("}");
            return pointStr.toString();
        }

        @Override
        public int compareTo(Circuit other) {
            return Integer.compare(this.points.size(), other.points.size());
        }

    }

    public static int junctionBox(String filePath) {
        int count = 0;
        List<Point> points = new ArrayList<>();
        PriorityQueue<Distance> distances = new PriorityQueue<>(Comparator.comparingLong(o -> o.distance));

        try {
            List<String> boxes = Files.readAllLines(Paths.get(filePath));
            for (String line : boxes) {
                String[] point = line.split(",");
                points.add(new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2])));
            }

            for (int i = 1; i < points.size(); i++) {
                for (int j = 0; j < i; j++) {
                    distances.add(new Distance(points.get(i), points.get(j), points.get(i).distance(points.get(j))));
                }
            }



            List<Circuit> circuits = new ArrayList<>();
            int counter = 0;
            int merged = 0;
            while (!distances.isEmpty() && counter <1000) {
                Distance distance = distances.poll();
                counter++;
                Circuit a = getCircuit(circuits, distance.a);
                Circuit b = getCircuit(circuits, distance.b);

                if (a == null && b == null) {
                    Circuit circuit = new Circuit();
                    circuit.addJunction(distance.a);
                    circuit.addJunction(distance.b);
                    circuits.add(circuit);

                } else if (a == null) {
                    b.addJunction(distance.a);
                    merged++;

                } else if (b == null) {
                    a.addJunction(distance.b);
                    merged++;

                } else if (a != b) {
                    //two circuits merging !!
                    mergeTwoCircuits(circuits, a, b);
                    merged+=2;
                    System.out.println("Merged two circuits");
                } else {
                    //a == b so ignore
                    System.out.println("Inside existing circuit");

                }
            }

            System.out.println("Merged:" + merged);

            for(Circuit circuit : circuits){
                System.out.println(circuit.points);
            }


            circuits.sort(Collections.reverseOrder());


            return circuits.get(0).points.size() * circuits.get(1).points.size() * circuits.get(2).points.size();







        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }



    }

    public static long junctionBox2(String filePath) {
        int count = 0;
        HashSet<Point> pointsSet = new HashSet<>();
        List<Point> points = new ArrayList<>();
        PriorityQueue<Distance> distances = new PriorityQueue<>(Comparator.comparingLong(o -> o.distance));

        try {
            List<String> boxes = Files.readAllLines(Paths.get(filePath));
            for (String line : boxes) {
                String[] point = line.split(",");
                Point t = new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
                points.add(t);
                pointsSet.add(t);
            }

            for (int i = 1; i < points.size(); i++) {
                for (int j = 0; j < i; j++) {
                    distances.add(new Distance(points.get(i), points.get(j), points.get(i).distance(points.get(j))));
                }
            }



            List<Circuit> circuits = new ArrayList<>();
            int counter = 0;
            int merged = 0;

            while (!distances.isEmpty() && pointsSet.size() > 1) {
                Distance distance = distances.poll();
                counter++;
                Circuit a = getCircuit(circuits, distance.a);
                Circuit b = getCircuit(circuits, distance.b);
                pointsSet.remove(distance.a);
                pointsSet.remove(distance.b);
                if (a == null && b == null) {
                    Circuit circuit = new Circuit();
                    circuit.addJunction(distance.a);
                    circuit.addJunction(distance.b);
                    circuits.add(circuit);

                } else if (a == null) {
                    b.addJunction(distance.a);
                    merged++;

                } else if (b == null) {
                    a.addJunction(distance.b);
                    merged++;

                } else if (a != b) {
                    //two circuits merging !!
                    mergeTwoCircuits(circuits, a, b);
                    merged+=2;
                    System.out.println("Merged two circuits");
                } else {
                    //a == b so ignore
                    System.out.println("Inside existing circuit");

                }
            }

            System.out.println("Merged:" + merged);

            do {
                Distance distance = distances.poll();

                if (pointsSet.contains(distance.a) || pointsSet.contains(distance.b)) {
                    return (long) distance.a.x * (long) distance.b.x;
                }
            } while(true);











        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }



    }


    private static void mergeTwoCircuits(List<Circuit> circuits, Circuit a, Circuit b) {

        for(Point point : b.points){
            a.addJunction(point);
        }
        circuits.remove(b);

    }

    private static Circuit getCircuit(List<Circuit> circuits, Point b) {

//        if(circuits.isEmpty()){
//            Circuit circuit = new Circuit();
//            circuit.addJunction(b);
//            circuits.add(circuit);
//            return circuit;
//        }
        for(Circuit circuit : circuits){
            if(circuit.hasJunction(b)){
                return circuit;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        String filePath = args[0];
//        int count = JunctionBox.junctionBox(filePath);
//        System.out.println("Count:" + count);

        long count2 = JunctionBox.junctionBox2(filePath);
        System.out.println("Multiply 2 = " + count2);
    }
}
