package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class DayTwentyOne extends Day {

    private Map<Move, String> numPadMoves= new HashMap<>();
    private Map<Move, String> dirPadMoves= new HashMap<>();
    private final Map<Pair<Integer, String>, Long> moveCache = new HashMap<>();
    public DayTwentyOne(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 21);
        expectations = new long[]{205160, 252473394928452L};
        example =  Arrays.asList(
                "839A",
                "169A",
                "579A",
                "670A",
                "638A");

        /*
        Part 1: You have a Numpad (789, 456, 123, _0A) to enter a number (A=Enter) via a robot.
        The robot is controlled with a directional Keypad (_^A, <v>). This is again controlled
        with a directional Keypad. There are therefore many variants for controlling the first
        pad. The complexity of a variant is the number sequence to be entered * the length of
        the variant. How complex is the shortest variant?
        Part 2: What is the result if 25 directional Keypads are used?
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        numPadMoves = findNumPadMoves(buildNumPad());
        dirPadMoves = findDirPadMoves(buildKeyPad());
        return getTotalComplexity(input, bPart2?25:2);
    }

    private long getTotalComplexity(List<String> lines, int dirPadCount) {
        long result = 0;
        for (String line : lines) {
            result+=Long.parseLong(line.substring(0, 3), 10) * countSteps(getNumpadMoves(line), dirPadCount);
        }
        return result;
    }

    private String getNumpadMoves(String line) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            output.append(numPadMoves.get(new Move((i == 0 ? 'A' : line.charAt(i - 1)), line.charAt(i))));
        }
        return output.toString();
    }

    private long countSteps(String line, int dirPadCount) {
        if (dirPadCount == 0) return line.length();
        if ("A".equals(line)) return 1;
        Pair<Integer, String> key = Pair.of(dirPadCount, line);
        if (moveCache.containsKey(key)) return moveCache.get(key);
        long sum = 0;
        String[] moves = line.split("A");
        for (String move : moves) {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i <= move.length(); i++) {
                output.append(dirPadMoves.get(
                       new Move(
                                (i == 0 ? 'A' : move.charAt(i - 1)),
                                (i == move.length() ? 'A' : move.charAt(i)))
                ));
            }
            sum += countSteps(output.toString(), dirPadCount - 1);
        }
        moveCache.put(key, sum);
        return sum;
    }

    private Map<Move, String> findDirPadMoves(Map<Character, Position> dirPadCells) {
        Map<Move, String> result = new HashMap<>();
        for (char from : dirPadCells.keySet()) {
            for (char to : dirPadCells.keySet()) {
                result.put(new Move(from, to), generateDirPadMoves(dirPadCells.get(from), dirPadCells.get(to)));
            }
        }
        return result;
    }

    private Map<Move, String> findNumPadMoves(Map<Character, Position> numPadCells) {
        Map<Move, String> result = new HashMap<>();
        for (char from : numPadCells.keySet()) {
            for (char to : numPadCells.keySet()) {
                result.put(new Move(from, to), generateNumPadMoves(numPadCells.get(from), numPadCells.get(to)));
            }
        }
        return result;
    }

    private String generateNumPadMoves(Position startPos, Position endPos) {
        boolean vertFirst = startPos.x() <= endPos.x();
        if (startPos.y() == 3 && endPos.x() == 0) vertFirst = true;
        if (startPos.x() == 0 && endPos.y() == 3) vertFirst = false;
        return generateMoves(startPos, endPos, vertFirst);
    }

    private String generateDirPadMoves(Position startPos, Position endPos) {
        boolean vertFirst = startPos.x() <= endPos.x();
        if (startPos.y() == 0 && endPos.x() == 0) vertFirst = true;
        if (startPos.x() == 0 && endPos.y() == 0) vertFirst = false;
        return generateMoves(startPos, endPos, vertFirst);
    }

    private String generateMoves(Position start, Position end, boolean vertFirst) {
        String dir=start.x()>end.x()?"<":">";
        int count=Math.abs(end.x() - start.x());
        String result=dir.repeat(count);

        dir=start.y()>end.y()?"^":"v";
        count=Math.abs(end.y() - start.y());
        return (vertFirst?dir.repeat(count) + result: result+dir.repeat(count))+"A";
    }

    private Map<Character, Position> buildNumPad() {
        return Map.ofEntries(
                Map.entry('7', new Position(0,0)),
                Map.entry('8', new Position(1,0)),
                Map.entry('9', new Position(2,0)),
                Map.entry('4', new Position(0,1)),
                Map.entry('5', new Position(1,1)),
                Map.entry('6', new Position(2,1)),
                Map.entry('1', new Position(0,2)),
                Map.entry('2', new Position(1,2)),
                Map.entry('3', new Position(2,2)),
                Map.entry('0', new Position(1,3)),
                Map.entry('A', new Position(2,3))
        );
    }

    private Map<Character, Position> buildKeyPad() {
        return Map.of(
                '^', new Position(1,0),
                'A', new Position(2,0),
                '<', new Position(0,1),
                'v', new Position(1,1),
                '>', new Position(2,1)
        );
    }
}
record Position(Integer x, Integer y){}
record Move (char from, char to){}