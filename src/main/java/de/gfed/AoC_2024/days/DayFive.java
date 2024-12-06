package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;
import java.util.stream.Collectors;


public class DayFive extends Day {

    public DayFive(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 5);
        expectations=new long[]{5651,4743};
        example = Arrays.asList(
                "47|53",
                "97|13",
                "97|61",
                "97|47",
                "75|29",
                "61|13",
                "75|53",
                "29|13",
                "97|29",
                "53|29",
                "61|53",
                "97|53",
                "61|29",
                "47|13",
                "75|47",
                "97|75",
                "47|61",
                "75|61",
                "47|29",
                "75|13",
                "53|13",
                "",
                "75,47,61,53,29",
                "97,61,53,29,13",
                "75,29,13",
                "75,97,47,61,53",
                "61,13,29",
                "97,13,75,29,47");
        /*
        Part 1: Extract correct multiplication (mul(number,number)) and add up.
        Part 2: Upcoming don't() disables, upcoming do() enables multiplication.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;
        Map<Integer, List<Integer>> pageOrdering=extractPageOrdering(input);
        List<List<Integer>> updates = extractUpdates(input);
        List<Integer> sortedUpdate;

        for (List<Integer> update : updates) {
            sortedUpdate = update.stream().sorted(new PageOrder(pageOrdering)).toList();

            if (bPart2 ^ sortedUpdate.equals(update))
                result += sortedUpdate.get(sortedUpdate.size() / 2);
        }
        return result;
    }

    static class PageOrder implements Comparator<Integer>{
        Map<Integer, List<Integer>> pageOrdering;
        public PageOrder(Map<Integer, List<Integer>> pageOrdering){
            this.pageOrdering=pageOrdering;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            if (pageOrdering.containsKey(o1) && pageOrdering.get(o1).contains(o2))
                return -1;
            return 0;
        }
    }

    private Map<Integer, List<Integer>> extractPageOrdering(List<String> input){
        Map<Integer, List<Integer>> result= new HashMap<>();
        Integer key;
        Integer value;
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isEmpty() )
                return result;
            key = Integer.parseInt(input.get(i).split("\\|")[0]);
            value = Integer.parseInt(input.get(i).split("\\|")[1]);
            if (result.containsKey(key)) {
                result.get(key).add(value);
            } else {
                result.put(key, new LinkedList<>(List.of(value)));
            }
        }
        return result;
    }

    private List<List<Integer>> extractUpdates(List<String> input){
        List<List<Integer>> result = new LinkedList<>();
        for (int i = input.indexOf("")+1; i < input.size(); i++) {
            result.add(Arrays.stream(input.get(i).split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        }
        return result;
    }
}
