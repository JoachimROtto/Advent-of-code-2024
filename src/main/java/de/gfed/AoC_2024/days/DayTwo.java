package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DayTwo extends Day {

    public DayTwo(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 2);
        expectations=new long[]{383,436};
        example = Arrays.asList(
                "7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9");
        /*
        Part 1: Each line consists of levels. A line is safe if the levels increase or decrease the whole line
        and by a difference of only 1-3.
        Part 2: Tolerate a single transition (by removing! Only one changed/missing direction can be fixed).
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;

        for (String line : input) {
            result += isSafe(line, bPart2)?1:0;
        }
        return result;
    }
    private boolean isSafe(String input, boolean bPart2){
        AtomicBoolean result= new AtomicBoolean(true);
        String[] lineArray =input.split(" ");
        final boolean decrease=Integer.parseInt(lineArray[0])>Integer.parseInt(lineArray[lineArray.length-1]);

        Arrays.stream(lineArray).reduce((a, b) ->{
            result.set(result.get() && isSafePosition(Integer.parseInt(a),Integer.parseInt(b), decrease));
            return b;
        });
        if (!result.get() && bPart2){
            for (int i=0; i<lineArray.length; i++){
                if (isSafe(removeLevel(lineArray, i),false))
                    return true;
            }
        }

        return result.get();
    }

    private boolean isSafePosition(int a, int b, boolean decrease){
        if (a==b)
            return false;
        if (decrease)
            return (a-b<4 && a-b>0);
        return (b-a<4 && b-a>0);
    }

    private String removeLevel(String[] input, int index) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            if (i != index) {
                result.append(input[i]).append(" ");
            }
        }
        return result.toString().trim();
    }
}
