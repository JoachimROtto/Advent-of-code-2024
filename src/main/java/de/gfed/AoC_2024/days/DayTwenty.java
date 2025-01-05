package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayTwenty extends Day {
    public DayTwenty(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 20);
        expectations=new long[]{1263,957831};
        example = Arrays.asList(
                "###############",
                "#...#...#.....#",
                "#.#.#.#.#.###.#",
                "#S#...#.#.#...#",
                "#######.#.#.###",
                "#######.#.#...#",
                "#######.#.###.#",
                "###..E#...#...#",
                "###.#######.###",
                "#...###...#...#",
                "#.#####.#.###.#",
                "#.#...#.#.#...#",
                "#.#.#.#.#.#.###",
                "#...#...#...###",
                "###############");

        /*
        Part 1: The input is a labyrinth with exactly one path from S to E. You can cheat
        and break through the wall (#) once for two steps. These steps count. With how many
        variations with a distinct start and end of the cheating can you save at least 100 steps?
        Part 2: Now you can ignore the wall for 20 steps.
        */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return eval(input, bPart2); //1928
    }

    private long eval(List<String> input, boolean bPart2){
        char[][] map=list2CharArray(input);
        int[][] distances=new int[map.length][map[0].length];
        for (int[] distance : distances) {
            Arrays.fill(distance, Integer.MAX_VALUE);
        }
        Map<Integer, Integer[]>  trail = takeAWalk(map, findMarker(map, 'S'), distances,-1, new HashMap<>());
        return cheat(map, trail, 99,bPart2?20:2 );
    }

    private int cheat(char[][] map, Map<Integer, Integer[]> trail, int offset, int cheatLength) {
        int result=0;
        for (int i=0; i<Math.pow(map.length, 2); i++){
            if (!trail.containsKey(i))
                i= (int) Math.pow(map.length, 2);
            for (int j = i+offset; j<Math.pow(map.length, 2); j++){
                if (trail.containsKey(j) && isCheat(trail.get(i), trail.get(j), offset,  cheatLength, j-i))
                    result++;
            }
        }
        return result;
    }

    private boolean isCheat(Integer[] from,Integer[] to, int offset, int cheatLength, int difDistances) {
        int distance=(Math.abs(from[0]- to[0])+ Math.abs(from[1]-to[1]));
        if (distance>cheatLength)
            return false;
        int spared = sparedSteps2(from, to,difDistances);
        return spared > offset;
    }

    private int sparedSteps2(Integer[] from, Integer[] to, int difDistances) {
        return (difDistances-Math.abs(from[0]- to[0])- Math.abs(from[1]-to[1]));
    }

    private int[] findMarker(char[][] map, char marker){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j]==marker)
                    return new int[]{i,j};
            }
        }
        return new int[]{0,0};
    }

    private Map<Integer, Integer[]>  takeAWalk(char[][] map, int[] start, int[][] distances, int steps, Map<Integer, Integer[]>  trail) {
        if (start[0]<0 || start[1]<0 || start[0]> map.length-1 || start[1]> map[0].length-1 )
            return trail;
        if (map[start[0]][start[1]]=='#')
            return trail;
        if (distances[start[0]][start[1]]<steps+1)
            return trail;
        distances[start[0]][start[1]]=steps+1;
        trail.put(steps+1, new Integer[]{start[0], start[1]});
        trail.putAll(takeAWalk(map, new int[]{start[0]+1,start[1]}, distances, steps+1,trail));
        trail.putAll(takeAWalk(map, new int[]{start[0]-1,start[1]}, distances, steps+1,trail));
        trail.putAll(takeAWalk(map, new int[]{start[0],start[1]+1}, distances, steps+1,trail));
        trail.putAll(takeAWalk(map, new int[]{start[0],start[1]-1}, distances, steps+1,trail));
        return trail;
    }
}