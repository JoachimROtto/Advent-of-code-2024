package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayThree extends Day {

    public DayThree(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 3);
        expectations=new long[]{188192787,113965544};
        example = Arrays.asList(
                "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))");
        /*
        Part 1: Extract correct multiplication (mul(number,number)) and add up.
        Part 2: Upcoming don't() disables, upcoming do() enables multiplication.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;

        String inputInOneLine = bPart2
                ? filterInput(mergeLines(input))
                : mergeLines(input);

        String[] multis = inputInOneLine.split("mul");
        for (int i = 1; i < multis.length; i++) {
            result+=tryMultiply(multis[i]);
        }
        return result;
    }

    private long tryMultiply(String input){
        if (!input.startsWith("("))
            return 0;
        try {
            long a = Long.parseLong(input.substring(1).split(",")[0]);
            long b = Long.parseLong(input.substring(1, input.indexOf(")")).split(",")[1]);
            return a*b;
        }
        catch (Exception e){
            return 0;
        }
    }

    protected String mergeLines(List<String> input){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.size(); i++)
            result.append(input.get(i));
        return result.toString();
    }

    private String filterInput(String input){
        String[] particles = input.split("don't()");
        StringBuilder result = new StringBuilder(particles[0]);
        for (int j = 1; j < particles.length; j++) {
            if (particles[j].contains("do()"))
                result.append(particles[j].substring(particles[j].indexOf("do()")));
            }
        return result.toString();
    }

}
