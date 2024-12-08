package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayEight extends Day {

    public DayEight(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 8);
        expectations=new long[]{220,813};
        example = Arrays.asList(
                "............",
                "........0...",
                ".....0......",
                ".......0....",
                "....0.......",
                "......A.....",
                "............",
                "............",
                "........A...",
                ".........A..",
                "............",
                "............");
        /*
        Part 1: You have a map with antennas (0-9, a-z, A-Z). A Pair of antennas with the same frequenz (=character)
        The line between antenna pairs with the same frequency is extended by the distance between the antennas in
        both directions and ends in antidots. How many antidots are within the map.
        Part 2: How many antidots do you count if the line can be extended several times and one of the antennas
        is already an antidot.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return eval(bPart2);
    }

    private long eval(boolean bPart2){
        Set <String> occurrences= new HashSet<>();
        char[][] map=List2CharArray(input);
        for (int i=0; i< map.length;i++){
            for (int j=0; j< map[0].length; j++){
                if (map[i][j]!='.' ){
                    occurrences.addAll(countAntiDots2(map, i, j, bPart2));
                }
            }
        }
        return occurrences.size();
    }


    private List<String> countAntiDots2(char[][]map, int i, int j, boolean bPart2){
        List<String> result=new ArrayList<>();
        int deltaI;
        int deltaJ;
        for (Integer[] position : searchMap(map, i, j, map[i][j])) {
            deltaI=position[0]-i;
            deltaJ=position[1]-j;
            boolean bValid=true;
            int resonanceFactor=bPart2?1:2;
            while (bValid) {
                bValid = isInMap(map, i + resonanceFactor * deltaI, j + resonanceFactor * deltaJ);
                if(bValid){
                    result.add(i + resonanceFactor  * deltaI + "-" + (j + resonanceFactor * deltaJ));
                    if (bPart2)
                        result.add(i + "-" + j);
                }
                resonanceFactor=bPart2?resonanceFactor:1;
                if ( isInMap(map, i - resonanceFactor * deltaI, j - resonanceFactor * deltaJ)){
                    result.add(i - deltaI * resonanceFactor + "-" + (j - deltaJ* resonanceFactor));
                    bValid=true;
                }
                resonanceFactor++;
                bValid= bValid && bPart2;
            }
        }
        return result;
    }
    private List<Integer[]> searchMap(char[][]map, int i, int j, char target){
        List<Integer[]>result=new LinkedList<>();
        for (int locI=i; locI< map.length;locI++){
            for (int locJ=0; locJ< map[0].length; locJ++){
                locJ=((locI==i && locJ==j)?j+1:locJ);
                if (map[locI][locJ]==target)
                    result.add(new Integer[]{locI, locJ});
            }
        }
        return result;
    }

    private boolean isInMap(char[][]map, int i, int j){
        return i>-1 && j>-1 && i< map.length && j< map[0].length;
    }
}