package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;
import java.util.Arrays;
import java.util.List;

public class DayThirteen extends Day {

    public DayThirteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 13);
        expectations=new long[]{33209,83102355665474L};
        example = Arrays.asList(
                "Button A: X+94, Y+34",
                "Button B: X+22, Y+67" ,
                "Prize: X=8400, Y=5400",
                "",
                "Button A: X+26, Y+66",
                "Button B: X+67, Y+21",
                "Prize: X=12748, Y=12176",
                "",
                "Button A: X+17, Y+86",
                "Button B: X+84, Y+37",
                "Prize: X=7870, Y=6450",
                "",
                "Button A: X+69, Y+23",
                "Button B: X+27, Y+71",
                "Prize: X=18641, Y=10279",
                "");
        /*
        Part 1: Your input is a triplet of movement of two buttons and a target. Button A costs 3 and
        button B costs 1. What is the cheapest way to the target? Add up the sum.
        Part 2: A part of the target was lost! Add 10000000000000 each.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return eval(input, bPart2);
    }

    private long eval(List<String> input, boolean bPart2){
        long result=0;
        for (int i = 0; i < input.size(); i+=4) {
            long[] buttonA=parseButton(input.get(i));
            long[] buttonB=parseButton(input.get(i+1));
            long[] target=parseTarget(input.get(i+2), bPart2?10000000000000L:0L);
            result+=calc(buttonA, buttonB, target);
        }
        return result;
    }

    private long calc(long[] buttonA, long[] buttonB, long[] target) {
        long numerator = target[0] * buttonA[1] - target[1] * buttonA[0];
        long b = numerator / ( buttonB[0] * buttonA[1] - buttonB[1] * buttonA[0]);
        long remX = target[0] - b * buttonB[0];
        long l = buttonA[0] == 0 ? target[1] : remX;
        long r = buttonA[0] == 0 ? buttonA[1] : buttonA[0];
        long a = l / r;
        return (a * buttonA[1] + b * buttonB[1] == target[1] && l % r == 0) ? 3 * a + b : 0;
    }

    long[] parseTarget(String line, long offset) {
        return new long[]{
                Long.parseLong(line.substring(line.indexOf("X")+2, line.indexOf(",")))+offset,
                Long.parseLong(line.split("Y")[1].substring(1))+offset};
    }

    private long[] parseButton(String line) {
        return new long[]{
                Long.parseLong(line.substring(line.indexOf("X")+1, line.indexOf(","))),
                Long.parseLong(line.split("Y")[1])};
    }
}


