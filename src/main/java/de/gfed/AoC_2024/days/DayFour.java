package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayFour extends Day {

    public DayFour(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 4);
        expectations=new long[]{2639,2005};
        example = Arrays.asList(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX");
        /*
        Part 1: Find XMAS in any direction.
        Part 2: Not XMAS but 2 MAS (or reverse) shaping an X.
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        char[][] charMap=List2CharArray(input);
        return findXMAS(charMap,bPart2);
    }

    private char[][] List2CharArray(List<String> input){
        char [][] result = new char[input.size()][input.getFirst().length()];
        for (int i = 0; i < input.size(); i++) {
            result[i]=input.get(i).toCharArray();
        }
        return result;
    }

    private int findXMAS(char [][] charMap, boolean bPart2){
        int result = 0;
        char target = bPart2?'M': 'X';
        for (int i=0; i<charMap.length; i++){
            for (int j=0; j<charMap[0].length; j++){
                if (charMap[i][j]==target){
                    result+= countMatches(i,j,charMap, bPart2);
                }
            }
        }
        return result/(bPart2?2:1);
    }

    private int countMatches(int i, int j, char[][] charMap, boolean bPart2){
        int[][] directions= {{0,1},{1,1},{1,0},{0,-1},{1,-1},{-1,1},{-1,0},{-1,-1}};
        int result = 0;
        for (int k=0; k<directions.length; k++){
            if (!bPart2) {
                result += isMatchPart1(i, j, charMap, directions[k]) ? 1 : 0;
            } else {
                result += isMatchPart2(i, j, charMap, directions[k]) ? 1 : 0;
            }
        }

        return result;
    }

    private boolean isMatchPart1(int i, int j, char[][] charMap, int [] direction){

        if ((i + 3* direction[0]) <0 || (j + 3* direction[1]) <0 ||
                ((i + 3 * direction[0] )>(charMap.length-1)) || ((j + 3 * direction[1] )>(charMap[0].length-1)) )
            return false;

        return charMap[i + (1*direction[0])][j +(1*direction[1])]=='M' &&
                charMap[i + (2*direction[0])][j +(2*direction[1])]=='A' &&
                charMap[i + (3*direction[0])][j +(3*direction[1])]=='S';
    }

    private boolean isMatchPart2(int i, int j, char[][] charMap,  int [] direction){

        if ((i + 2* direction[0]) <0 || (j + 2* direction[1]) <0 ||
                ((i + 2 * direction[0] )>(charMap.length-1)) || ((j + 2 * direction[1] )>(charMap[0].length-1) )
                || direction[0]==0 || direction[1]==0)
            return false;

        if ( charMap[i + (1*direction[0])][j +(1*direction[1])]=='A' &&
                charMap[i + (2*direction[0])][j +(2*direction[1])]=='S')
        {
            return (charMap[i + (2*direction[0])][j]=='M' &&  charMap[i][j +(2*direction[1])]=='S') ||
                     (charMap[i][j +(2*direction[1])]=='M' &&  charMap[i + (2*direction[0])][j]=='S') ;
        }
        return false;
    }
}
