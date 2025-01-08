package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DaySix extends Day {
    enum Direction {
        NORTH, WEST, SOUTH, EAST
    }

    public DaySix(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 6);
        expectations = new long[]{5409, 2022};
        example = Arrays.asList(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#...");
        /*
        Part 1: Predict the guard's(^) path. Rules: Start north. If you see a #, turn 90°. Stop at an edge.
        How many . will be passed?
        Part 2: Add exactly one Obstacle to catch the guard in a loop. How many possibilities do you have?
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return bPart2 ?
                evalPart2() :
                lengthOfFoundPath(list2CharArray(input), findGuard(input,'^'), Direction.NORTH, 2);
    }

    private long lengthOfFoundPath(char[][] input, int[] position, Direction direction, long currentLength) {
        int[] nextPosition = nextPos(position, direction);
        if ( nextPosition[0] < 0 || nextPosition[1] < 0 ||
                nextPosition[0] == input.length || nextPosition[1] == input[0].length ){
            input[position[0]][position[1]] = dir2Char(direction);
            return currentLength;
        }
        if ( input[nextPosition[0]][nextPosition[1]] == '#' ) {
            return lengthOfFoundPath(input, position, nextDirection(direction), currentLength);
        }
        if ( input[position[0]][position[1]] == dir2Char(direction) ) {
            return -1;
        }
        if ( input[position[0]][position[1]] == '.' ) {
            input[position[0]][position[1]] = dir2Char(direction);
            currentLength++;
        }
        return lengthOfFoundPath(input, nextPosition, direction, currentLength);
    }

    private long evalPart2() {
        char[][] walk = list2CharArray(input);
        int[] guard = findGuard(input, '^');
        lengthOfFoundPath(walk, guard, Direction.NORTH, 2);
        char[][] map;
        long result = 0;
        for (int i =0; i < walk.length; i++) {
            for (int j = 0; j < walk[0].length; j++) {
                if ( !("^.#").contains(walk[i][j]+"") ) {
                    map = list2CharArray(input);
                    map[i][j] = '#';
                    if ( lengthOfFoundPath(map, guard, Direction.NORTH, 2) ==-1 ) {
                        result++;
                    }
                    map[i][j] = '.';
                }
            }
        }
        return result;
    }


    private Direction nextDirection(Direction direction) {
        return Direction.values()[(Integer.parseInt(dir2Char(direction) + "")) % 4];
    }

    private int[] nextPos(int[] position, Direction direction) {
        return switch (direction) {
            case NORTH -> new int[]{position[0] - 1, position[1]};
            case WEST -> new int[]{position[0], position[1] + 1};
            case SOUTH -> new int[]{position[0] + 1, position[1]};
            case EAST -> new int[]{position[0], position[1] - 1};
        };
    }

    private char dir2Char(Direction direction) {
        return switch (direction) {
            case NORTH -> '1';
            case WEST -> '2';
            case SOUTH -> '3';
            case EAST -> '4';
        };
    }
}