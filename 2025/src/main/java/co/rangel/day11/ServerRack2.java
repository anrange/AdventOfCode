package co.rangel.day11;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ServerRack2 {

    public static class RackNode {
        List<RackNode> connections = new ArrayList<>();
        String name;
        public RackNode(String name) {
            this.name = name;
        }
        public void addConnection(RackNode rackNode) {
            connections.add(rackNode);
        }
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            RackNode rackNode = (RackNode) o;
            return name.equals(rackNode.name);
        }
        @Override
        public int hashCode() {
            return Objects.hash(connections, name);
        }
    }

    public static long getPaths(String filePath){
        long paths = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            HashMap<String, RackNode> nodes = new HashMap<>();
            for (String line : lines) {
                String[] parts = line.split(" ");
                String sourceName = (parts[0].substring(0, parts[0].length() - 1));
                RackNode source = null;
                if(!nodes.containsKey(sourceName)){
                    source = new RackNode(sourceName);
                    nodes.put(sourceName, source);
                }
                else {
                    source = nodes.get(sourceName);
                }

                for (int i = 1; i < parts.length; i++) {
                    RackNode n = null;
                    if(!nodes.containsKey(parts[i])){
                        n = new RackNode(parts[i]);
                        nodes.put(n.name, n);
                    }
                    else{
                        n = nodes.get(parts[i]);
                    }
                    source.addConnection(n);
                }
            }
            //I need to find the number of paths from start to end
            RackNode start = nodes.get("svr");
            RackNode end = nodes.get("out");

            HashMap<String, Long> visited = new HashMap<>();
            for(RackNode rackNode : start.connections){
                paths += getPaths(rackNode, end, visited,  false, false);
            }
        }
        catch (Exception e){
            System.out.println("Error reading file: " + e.getMessage());
        }
        return paths;
    }

    static long getPaths(RackNode rackNode, RackNode end, Map<String, Long> visited, boolean dac, boolean fft){
        if(rackNode.name.equals(end.name)){
            if(dac && fft){
                return 1L;
            }
            return 0L;
        }
        String _key = rackNode.name + "-" + dac + "-" + fft;
        if(visited.containsKey(_key)){
            return visited.get(_key).longValue();
        }
        long paths = 0L;
        for(RackNode n : rackNode.connections){
            paths += getPaths(n, end, visited ,dac || n.name.equals("dac"), fft || n.name.equals("fft"));
        }
        visited.put(_key, Long.valueOf(paths));

        return paths;

    }

    public static void main(String[] args) throws Exception {

        long sum = getPaths(args[0]);
        System.out.println(sum);
    }

}
