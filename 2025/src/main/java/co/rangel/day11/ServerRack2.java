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
            //I have the whole graph in memory

            //I need to find the number of paths from start to end
            RackNode start = nodes.get("svr");
            RackNode end = nodes.get("out");
            paths = countPathsMustPassThroughDAG(start, end);

//            for(RackNode rackNode : start.connections){
//                paths += getPaths(rackNode, end, false, false);
//            }


        }
        catch (Exception e){
            System.out.println("Error reading file: " + e.getMessage());
        }
        return paths;
    }

    static int getPaths(RackNode rackNode, RackNode end, boolean dac, boolean fft){


        if(rackNode.name.equals(end.name)){
            if(dac && fft){
                return 1;
            }
            return 0;
        }
        int paths = 0;
        for(RackNode n : rackNode.connections){
            paths += getPaths(n, end, dac || n.name.equals("dac"), fft || n.name.equals("fft"));
        }
        return paths;

    }



    private static class Key {
        final RackNode node;
        final boolean seenA;
        final boolean seenB;
        Key(RackNode node, boolean seenA, boolean seenB) {
            this.node = node; this.seenA = seenA; this.seenB = seenB;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key k = (Key) o;
            return node == k.node && seenA == k.seenA && seenB == k.seenB;
        }
        @Override public int hashCode() {
            int h = System.identityHashCode(node);
            h = 31*h + (seenA ? 1 : 0);
            h = 31*h + (seenB ? 1 : 0);
            return h;
        }
    }

    public static long countPathsMustPassThroughDAG(RackNode start,
                                                   RackNode end) {
        Map<Key, Long> memo = new HashMap<>();
        return dfsDAG(start, end, memo, false, false, "dac","fft");
    }

    private static long dfsDAG(RackNode current,
                              RackNode end,
                              Map<Key, Long> memo,
                              boolean seenA,
                              boolean seenB,
                              String mustA,
                              String mustB) {

        boolean nowSeenA = seenA || (current.name != null && current.name.equals(mustA));
        boolean nowSeenB = seenB || (current.name != null && current.name.equals(mustB));

        if (current == end) {
            return (nowSeenA && nowSeenB) ? 1L : 0L;
        }

        Key key = new Key(current, nowSeenA, nowSeenB);
        Long cached = memo.get(key);
        if (cached != null) return cached;

        long total = 0L;
        for (RackNode next : current.connections) {
            total += dfsDAG(next, end, memo, nowSeenA, nowSeenB, mustA, mustB);
        }
        memo.put(key, total);
        return total;
    }



    public static void main(String[] args) throws Exception {

        long sum = getPaths(args[0]);
        System.out.println(sum);
    }

}
