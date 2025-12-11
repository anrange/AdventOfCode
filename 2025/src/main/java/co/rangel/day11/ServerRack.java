package co.rangel.day11;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerRack {

    public static class Node{
        List<Node> connections = new ArrayList<>();
        String name;
        public Node(String name) {
            this.name = name;
        }
        public void addConnection(Node node) {
            connections.add(node);
        }

    }

    public static int getPaths(String filePath){
        int paths = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            HashMap<String, Node> nodes = new HashMap<>();


            for (String line : lines) {
                String[] parts = line.split(" ");
                String sourceName = (parts[0].substring(0, parts[0].length() - 1));
                Node source = null;
                if(!nodes.containsKey(sourceName)){
                    source = new Node(sourceName);
                    nodes.put(sourceName, source);
                }
                else {
                    source = nodes.get(sourceName);
                }

                for (int i = 1; i < parts.length; i++) {
                    Node n = null;
                    if(!nodes.containsKey(parts[i])){
                        n = new Node(parts[i]);
                        nodes.put(n.name, n);
                    }
                    else{
                        n = nodes.get(parts[i]);
                    }
                    source.addConnection(n);
                }
            }
            //I have the whole graph in memory

            //I need to find the number of paths from start to end
            Node start = nodes.get("you");
            Node end = nodes.get("out");


            for(Node node : start.connections){
                paths += getPaths(node, end);
            }



        }
        catch (Exception e){
            System.out.println("Error reading file: " + e.getMessage());
        }
        return paths;
    }

    static int getPaths(Node node, Node end){

        if(node.name.equals(end.name)){
            return 1;
        }
        int paths = 0;
        for(Node n : node.connections){
            paths += getPaths(n, end);
        }
        return paths;

    }


    public static void main(String[] args) throws Exception {

        int sum = getPaths(args[0]);
        System.out.println(sum);
    }

}
