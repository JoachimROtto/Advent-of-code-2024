package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayTen  extends Day {

    public DayTen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 10);
        expectations=new long[]{733,1514};
        example = Arrays.asList(
                "89010123",
                "78121874",
                "87430965",
                "96549874",
                "45678903",
                "32019012",
                "01329801",
                "10456732");
        /*
        Part 1: You have a map of numbers. There are trails in it from 0-9. Each 0 may reach different 9 in
        different ways. Sum up the distinct 9 per each 0.
        Part 2: Did I say distinct? Sum up each way reaching a 9 per each 0.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;
        int[][]map=list2IntArray(input);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j]==0)
                    result += bPart2?
                            countFollowingPathsRec2(map, i, j, map[i][j]) :
                            countFollowingPathsRec(map, i, j, map[i][j], new HashSet<>()).size();
            }
        }
        return result;
    }

    private Set<String> countFollowingPathsRec(int[][] map, int i, int j, int target, Set<String> destinations) {
        if (i<0 || j<0 || i> map.length-1 || j> map.length-1)
            return destinations;
        if (map[i][j]!=target)
            return destinations;
        if(target==9){
           destinations.add(i + " - " + j);
           return destinations;
        }
     destinations.addAll(countFollowingPathsRec(map, i-1,j, target+1, destinations));
        destinations.addAll(countFollowingPathsRec(map, i,j+1, target+1,destinations));
        destinations.addAll(countFollowingPathsRec(map, i+1,j, target+1,destinations));
        destinations.addAll(countFollowingPathsRec(map, i,j-1, target+1, destinations));

        return destinations;
    }

    private long countFollowingPathsRec2(int[][] map, int i, int j, int target) {
        if (i<0 || j<0 || i> map.length-1 || j> map.length-1)
            return 0;
        if (map[i][j]!=target)
            return 0;
        if(target==9){
            return 1;
        }
        return countFollowingPathsRec2(map, i-1,j, target+1)+
        countFollowingPathsRec2(map, i,j+1, target+1)+
        countFollowingPathsRec2(map, i+1,j, target+1)+
        countFollowingPathsRec2(map, i,j-1, target+1);
    }
}
