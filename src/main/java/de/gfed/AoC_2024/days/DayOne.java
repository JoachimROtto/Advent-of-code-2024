package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayOne extends Day {

    public DayOne(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 1);
        expectations=new long[]{3714264,18805872};
        example = Arrays.asList(
                "3   4",
                "4   3",
                "2   5",
                "1   3",
                "3   9",
                "3   3");
        /*
        Part 1: Each line consists of two columns. Get the columns, sort asc, add the distances
        (difference of the columns per row).
        Part 2: Add the similarity score (item in the left column * number of occurrence in the right column)
        not the distance.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;

        List<Long> leftInput= new java.util.ArrayList<>(input.stream().mapToLong(
                item -> Long.parseLong(item.split(" {3}")[0]))
                .boxed().toList());

        List<Long> rightInput= new java.util.ArrayList<>(input.stream().mapToLong(
                        item -> Long.parseLong(item.split(" {3}")[1]))
                .boxed().toList());

        if (bPart2) {
            Map<Long, Long> occurrences = countOccurrences(rightInput);
            for (int i = 0; i < leftInput.size(); i++) {
                result+=leftInput.get(i) *
                        (!occurrences.containsKey(leftInput.get(i)) ? 0:occurrences.get(leftInput.get(i))
                        );
            }
            return result;
        }

        leftInput.sort(Long::compareTo);
        rightInput.sort(Long::compareTo);
        for (int i = 0; i < leftInput.size(); i++) {
            result += Math.abs(rightInput.get(i) - leftInput.get(i));
        }
        return result;
    }

    private Map<Long,Long> countOccurrences(List<Long> input){
        Map<Long,Long> result = new HashMap<>();
        input.forEach(item ->{
            if (!result.containsKey(item)){
                result.put(item, input.stream().filter(i -> Objects.equals(i, item)).count());
            }
        });

        return result;

    }

}