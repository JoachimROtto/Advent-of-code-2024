package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;
import java.util.*;

public class DayTwentyThree extends Day {

    public DayTwentyThree(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 23);
        expectations=new long[]{1411,0};
        example = Arrays.asList(
                "kh-tc",
                "qp-kh",
                "de-cg",
                "ka-co",
                "yn-aq",
                "qp-ub",
                "cg-tb",
                "vc-aq",
                "tb-ka",
                "wh-tc",
                "yn-cg",
                "kh-ub",
                "ta-co",
                "de-co",
                "tc-td",
                "tb-wq",
                "wh-td",
                "ta-ka",
                "td-qp",
                "aq-cg",
                "wq-ub",
                "ub-vc",
                "de-ta",
                "wq-aq",
                "wq-vc",
                "wh-yn",
                "ka-de",
                "kh-ta",
                "co-tc",
                "wh-qp",
                "tb-vc",
                "td-yn");
        /*
        Part 1:You have a list of bidirectional connections as input. A group consists of three
        elements that are connected pairwise and at least one of them begins with t. How many of these
        groups are there?
        Part2: A group now consists of any number of elements that are connected pairwise and at least
        one of which begins with t. Which group is the largest? The element names (sorted alphabetically
        and separated by commas without spaces) are a password.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;
        List<Node> nodes=findNodes(input);
        Set<String> computers= findComputersWithT(input);
        int maxGroup=0;
        Set<String> lanPartyCandidate=new HashSet<>();
        List<Node> connectedNeighbours;
        for (String computer : computers) {
            Set<String> neighbours = findNeighbour(computer, nodes);
            connectedNeighbours= findConnectedNeighbours(neighbours, nodes);
            if (maxGroup<connectedNeighbours.size()){
                maxGroup=connectedNeighbours.size();
                lanPartyCandidate=neighbours;
                lanPartyCandidate.add(computer);
            }
            result += connectedNeighbours.size();
        }
        if (bPart2){
            List <String> part2=fullConnected(lanPartyCandidate, nodes).stream().sorted().toList();
            System.out.println(part2.toString().replaceAll(" ",""));
            return 0;
        }
        return result/2;
    }

    private List<Node> findConnectedNeighbours(Set<String> neighbours, List<Node> nodes) {
        Node node;
        List<Node> thirdConnection= new ArrayList<>();
        for (String neighbour1 : neighbours) {
            if (neighbour1.startsWith("t"))
                continue;
            for (String neighbour2 : neighbours) {
                if (!neighbour1.equals(neighbour2)) {
                    node = new Node(neighbour1, neighbour2);
                    if (nodes.contains(node))
                        thirdConnection.add(node);
                }
            }
        }
        return thirdConnection;
    }

    private List<String> fullConnected(Set<String> neighbours, List<Node> nodes){
        List<String> result = new ArrayList<>();
        boolean isFullConnected=true;
        for (String neighbour : neighbours) {
            List<String> thisNet = new ArrayList<>();
            thisNet.add(neighbour);
            for (String nextNeighbour : neighbours) {
                if (!thisNet.contains(nextNeighbour)){
                    for (int i = 0; i < thisNet.size(); i++) {
                        if (!nodes.contains(new Node(nextNeighbour, thisNet.get(i))))
                            isFullConnected=false;
                    }
                    if(isFullConnected)
                        thisNet.add(nextNeighbour);
                    isFullConnected=true;
                }
            }
            thisNet=thisNet.stream().distinct().toList();
            if (thisNet.size()>result.size())
                result=thisNet;
        }
        return result;
    }

    private Set<String> findNeighbour(String computer, List<Node> nodes) {
        Set<String> result= new HashSet<>();
        List<Node> filteredNodes =nodes.stream().filter(item -> item.computer1.equals(computer)).toList();
        for (Node node : filteredNodes) {
            result.add(node.computer2);
        }
        return result;
    }

    private Set<String> findComputersWithT(List<String> input) {
        Set<String> result=new HashSet<>();
        for (String item : input) {
            if (item.split("-")[0].startsWith("t"))
                result.add(item.split("-")[0]);
            if (item.split("-")[1].startsWith("t"))
                result.add(item.split("-")[1]);
        }
        return result;
    }

    private List<Node> findNodes(List<String> input) {
        List<Node> result = new ArrayList<>(2*input.size());
        for (String item : input) {
            result.add(new Node(item.split("-")[0], item.split("-")[1]));
            result.add(new Node(item.split("-")[1], item.split("-")[0]));
        }
        return result;
    }

    record Node(String computer1, String computer2){}
}