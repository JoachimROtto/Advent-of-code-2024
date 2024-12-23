package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map.Entry;

public class DayTwentyTwo extends Day {

    public DayTwentyTwo(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 22);
        expectations=new long[]{19458130434L,2130};
        example = Arrays.asList(
                "1",
                "10",
                "100",
                "2024");

        example = Arrays.asList(
                "1",
                "2",
                "3",
                "2024");
        example = Arrays.asList("123");
        /*
        Part 1: Calculate a price based on an initial secret and a longer formula in 2000 iterations for
        each buyer. Sum up.
        Part 2: The prices are just the last digit of each iteration and the buyer decide based of four
        consecutive changes of these last digits. You can tell them the sequence they should look for but
        only one for all. Which sequence leads to the best effort (=sum of prizes; first match only)?
         */

    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result=0;
        List<Map <String, Integer>> sequenceList=new ArrayList<>(input.size());
        Map <String, Integer> sequences;
        for (String line : input) {
            long secret =Long.parseLong(line);
            List<Integer> change = new ArrayList<>(2000);
            sequences = new HashMap<>();
            int lastPrice=(int)secret%10;
            for (int i = 0; i < 2000; i++) {
                secret=calcStep(secret);
                change.add((int) (secret%10) - lastPrice);
                lastPrice= (int) (secret%10);
                if (bPart2 && i>3){
                    saveSequence(sequences, change,(int) secret%10);
                }
            }
            sequenceList.add(sequences);
            result+=secret;
        }
        if(bPart2){
            Map<String, Integer> finalList = new HashMap<>();
            for (Map<String, Integer> sequenceItem : sequenceList)
                finalList = Stream.of(finalList, sequenceItem).flatMap(m -> m.entrySet().stream())
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, this::add));
            return finalList.values().stream().mapToLong(e -> e).max().getAsLong();
        }
        return result;
    }

    private int add(int a, int b){
        return a+b;
    }

    private void saveSequence(Map<String, Integer> sequences, List<Integer> change, int price) {
        StringBuilder sequence=new StringBuilder();
        for (int i = 4; i >0 ; i--) {
            sequence.append(change.get(change.size() - i)).append(",");
        }
        if (!sequences.containsKey(sequence.toString()))
            sequences.put(sequence.toString(), price);
    }

    private long calcStep(long secret){
        long result = ((secret*64) ^ secret) %16777216;
        result=(Math.floorDiv(result , 32) ^ result) %16777216;
        result=((result * 2048) ^ result) %16777216;
        return result;
    }
}