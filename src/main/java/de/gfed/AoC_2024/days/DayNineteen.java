package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayNineteen extends Day {

    public DayNineteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 19);
        expectations=new long[]{296,619970556776002L};
        example = Arrays.asList(
                "r, wr, b, g, bwu, rb, gb, br",
                "",
                "brwrr",
                "bggr",
                "gbbr",
                "rrbgbr",
                "ubwu",
                "bwurrg",
                "brgr",
                "bbrgwb");
        /*
        Part 1: There are design patterns and design. How many designs can consist of the given patterns?
        Part 2: And how many different ways?
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return eval(input, bPart2);
    }

    private long eval(List<String> input, boolean bPart2){
        long result=0;
        List<String> patterns = Arrays.stream(input.getFirst().split(", ")).toList();
        List<String> designs = input.stream().skip(2).toList();
        for (String design : designs) {
            if (!bPart2){
                result+= designableVariants(design, patterns)>0?1:0;
            } else {
                result+= designableVariants(design, patterns);
            }

        }
        return result;
    }

    private long designableVariants(String design, List<String> patterns) {
        Map<Long, Long> progress=new HashMap<>();
        progress.put(0L,1L);
        for (long i = 0; i < design.length(); i++) {
            for (String pattern : patterns) {
                if (design.startsWith(pattern, (int) i) && progress.containsKey(i)){
                    progress.put(i+pattern.length(),
                            progress.get(i) + progress.getOrDefault(i + pattern.length(), 0L));
                }
            }
        }
        return progress.getOrDefault((long)design.length(),0L);
    }
}