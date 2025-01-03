package de.gfed.AoC_2024.days;


import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.Arrays;

public class DayTwelve extends Day {

    public DayTwelve(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 12);
        expectations=new long[]{1449902,908042};
        example = Arrays.asList(
                "RRRRIICCFF",
                "RRRRIICCCF",
                "VVRRRCCFFF",
                "VVRCCCJFFF",
                "VVVVCJJCFE",
                "VVIVCCJJEE",
                "VVIIICJJEE",
                "MIIIIIJJEE",
                "MIIISIJEEE",
                "MMMISSJEEE");

        /*
     Part 1: The input is a map on which plant species have been marked with letters. Connected plantings
     with the same plants should be fenced in. The price for the fence is the number of fenced plants *
     number of border areas with foreign plants (AAAA -> 4 * 10). How much do all the fences cost together?
     Part 2: There is a discount: border areas that do not change direction count as 1 (AAAA -> 4*4).
     How much do the fences cost now?
      */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;
        char[][] map = list2CharArray(input);
        long[] price;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j]!=' ' && map[i][j]!='0' && map[i][j]!='1') {
                    price= bPart2? calcFence2(map,i, j, map[i][j]):calcFence(map, i, j, map[i][j]);
                    result += (price[0] * (price[1]));
                    deleteMarks(map);
                }
            }
        }
        return result;
    }


    private long[] calcFence2(char[][] map, int i, int j, char target) {
        long plantCount=calcFence(map,i,j,target)[0];
        if (plantCount==1) {
            return new long[]{4, plantCount};
        }
        target='1';
        int minY=i;
        int maxY=i;
        int minX=j;
        int maxX=j;
        for (int k = 0; k < map.length; k++) {
            for (int m = 0; m < map[k].length; m++) {
                if (map[k][m]==target){
                    maxY=k;
                    minX=Math.min(minX, m);
                    maxX=Math.max(maxX, m);
                }
            }
       }
        if (minX==maxX|| minY==maxY) {
            return new long[]{4, plantCount};
        }

        long fenceCount;
        long targetCount=0;
        for (int k = minY; k < maxY+1; k++) {
            for (int m = minX; m < maxX+1; m++) {
                fenceCount= hasCorners(map, k, m, target, minX,minY, maxX, maxY);
                targetCount += fenceCount;
            }}
        return new long[]{targetCount, plantCount};
    }

    private long hasCorners(char[][] map, int k, int m, char target, int minX, int minY, int maxX, int maxY) {

        int cornerCount=0;
        if (k==minY){
            if (m==minX)
                return map[k][m]==target?1:0;
            if (m==maxX)
                cornerCount+=map[k][m]==target?1:0;
            cornerCount+=(map[k][m-1]!=map[k][m]&&
                    (map[k][m-1] ==target || map[k][m]==target))?1:0;
            return cornerCount;
        }
        if(k==maxY){
            if (m==minX){
                cornerCount += map[k][m] == target ? 1 : 0;
                cornerCount+= (map[k - 1][m] != map[k][m] &&
                        (map[k - 1][m] ==target || map[k][m]==target))?1:0;
                return cornerCount;
            }
            if (m==maxX) {
                cornerCount += map[k][m] == target ? 1 : 0;
            }
            cornerCount+= (map[k][m-1]!=map[k][m]&&
                    (map[k ][m-1] ==target || map[k][m]==target))?1:0;
        }
        if (m==minX) {
            cornerCount +=(map[k - 1][m] != map[k][m] &&
                    (map[k - 1][m] ==target || map[k][m]==target))?1:0;
            return cornerCount;
        }
        if (m==maxX) {
            cornerCount += (map[k - 1][m] != map[k][m] &&
                    (map[k - 1][m] ==target || map[k][m]==target))?1:0;
        }
        cornerCount+=isCorner(map,k,m,target)?1:0;
        if (((map[k - 1][m-1] == target && map[k][m]== target) ^
                (map[k - 1][m] == target && map[k][m-1]==target))
                && !isCorner(map,k,m,target))
                cornerCount+=2;
        return cornerCount;
    }

    private boolean isCorner(char[][] map, int k, int m, char target) {
        int targetCount=0;
        targetCount+=map[k-1][m-1]!=target?0:1;
        targetCount+=map[k][m-1]!=target?0:1;
        targetCount+=map[k][m]!=target?0:1;
        targetCount+=map[k-1][m]!=target?0:1;
        return (targetCount==3 || targetCount==1);
    }

    private long[] calcFence(char[][] map,int i, int j, char target) {
        long[] result = new long[]{1,0};
        long[] nextResult;

        map[i][j]='0';
        nextResult=lookAround(map,i+1,j,target);
        result[0]+=nextResult[0];
        result[1]+=nextResult[1];

        nextResult=lookAround(map,i,j+1,target);
        result[0]+=nextResult[0];
        result[1]+=nextResult[1];

        nextResult=lookAround(map,i-1,j,target);
        result[0]+=nextResult[0];
        result[1]+=nextResult[1];

        nextResult=lookAround(map, i,j-1,target);
        result[0]+=nextResult[0];
        result[1]+=nextResult[1];

        map[i][j]='1';
        return result;
    }

    private long[] lookAround(char[][] map,int i, int j, char target){
        long[] result = new long[]{0,0};
        long[] nextResult;
        if (isSameChar(map, i,j,target) && map[i][j]!='0'&& map[i][j]!='1') {
            nextResult=calcFence(map,i,j,target);
            result[0]+=nextResult[0];
            result[1]+=nextResult[1];
        } else {
            result[1]++;
        }
        return result;
    }

    private void deleteMarks(char[][] map){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j]=='0' || map[i][j]=='1')
                    map[i][j]=' ';
            }
        }
    }

    private boolean isSameChar(char[][] map, int i, int j, char target) {
        if (i<0 || j<0|| i> map.length-1 || j> map[0].length-1 )
            return false;
        return map[i][j] == target || map[i][j] == '0' || map[i][j] == '1';
    }
}
