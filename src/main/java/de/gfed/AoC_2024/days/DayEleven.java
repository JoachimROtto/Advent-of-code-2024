package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayEleven  extends Day {

    Map<Pair, Long> steps=new HashMap<>();
    record Pair(long stone, long remainingBlinks){}

    public DayEleven(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 11);
        expectations=new long[]{233050,276661131175807L};
        example = Arrays.asList(
                "125 17");
        /*
        Part 1: You have a list of numbers. Transform them according to the first match of  the following
        rules:0->1, even count of digits ->split in the middle, else->*2024. Do it 25 times. How many
        numbers do you have now?
        Part 2: Do it 75 times.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        int repetition = bPart2?75:25;
        long result=0;
        for (Long stone : string2LongArray(input.getFirst())) {
            result+= transformSingleStone(stone, repetition);
        }
        return result;
    }

    private List<Long> transformStone(Long value) {
        List<Long> result=new ArrayList<>(2);
        if(value==0){
            result.add(1L);
            return result;
        }
        if(value.toString().length()%2==0){
            String valueString= value.toString();
            result.add(Long.parseLong(valueString.substring(0, valueString.length()/2)));
            result.add(Long.parseLong(valueString.substring(valueString.length()/2)));
            return result;
        }
        result.add(value*2024L);
        return result;
    }

    private Long transformSingleStone(Long stone, int TTL){
        long result=0;
        if (TTL==0)
            return 1L;

        if (steps.containsKey(new Pair(stone, TTL)))
            return steps.get(new Pair(stone, TTL));

        for (Long nextStone : transformStone(stone)) {
            result+= transformSingleStone(nextStone, TTL-1);
        }

        steps.put(new Pair(stone, TTL),result);
        return result;
    }
}