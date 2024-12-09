package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayNine  extends Day {

    public DayNine(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 9);
        expectations=new long[]{6283404590840L,6304576012713L};
        example = Arrays.asList(
                "2333133121414131402");
        /*
        Part 1: Your input is one line of Blocks that alternate between the size of a file and a gap. Fill
        the gaps starting on the left with files starting from the right, Fragmentation is allowed. Build a
        checksum with the Position of each used Slot * former position of using file. Skip the gaps.
        (23312 = 00...111.22 ->0022111... = 0*0 + 1* + 3*2..)
        Part 2: Fragmentation is not allowed! Once again.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        if(bPart2)
            return buildChecksum2(splitLine(input.getFirst())); //2858
        return buildChecksum(splitLine(input.getFirst())); //1928
    }

    private long buildChecksum2(int[][] layout){
        int lastBlock;
        int []deletedSpace=new int[layout[1].length];
        int counter =0;
        long result =0;
        for (int i = 0; i < layout[1].length; i++) {
            counter+=deletedSpace[i];

            while (layout[0][i]>0){
                result+= (long) i * counter;
                counter++;
                layout[0][i]--;
            }

            while(findMatchingBlock(layout, layout[1][i])!=-1){
                lastBlock=findMatchingBlock(layout, layout[1][i]);
                deletedSpace[lastBlock]=layout[0][lastBlock];// -1;
                while (layout[0][lastBlock]>0){
                    layout[1][i]--;
                    layout[0][lastBlock]--;
                    result+= (long) counter *lastBlock;
                    counter++;
                }
            }
            counter+=layout[1][i];
        }
        return result;
    }

    private int findMatchingBlock(int[][] layout, int size){
        for (int i =  layout[0].length-1; i>-1; i--) {
            if (layout[0][i]>0&&layout[0][i]<=size)
                return i;
        }
        return -1;
    }

    private long buildChecksum(int[][] layout){
        int lastBlock=layout[1].length-1;
        int counter =0;
        long result =0;
        for (int i = 0; i < layout[1].length; i++) {
            while (layout[0][i]>0){
                result+= (long) i * counter;
                counter++;
                layout[0][i]--;
            }

            if (i>=lastBlock)
                return result;

            while(layout[1][i]>0){
                layout[1][i]--;
                layout[0][lastBlock]--;
                result+= (long) counter *lastBlock;
                counter++;
                if (layout[0][lastBlock]==0)
                    lastBlock--;
                if (i>=lastBlock)
                    return result;
            }
        }
        return result;
    }

    private int[][] splitLine(String line){
        int [][]result=new int[2][line.length()/2+1];
        char[] lineAsArray=line.toCharArray();
        for (int i = 0; i < lineAsArray.length; i=i+2) {
            result[0][i/2]=Integer.parseInt(lineAsArray[i] +"");
            if(lineAsArray.length>(i+2))
                result[1][i/2]=Integer.parseInt(lineAsArray[i+1] +"");
        }
        return result;
    }
}
