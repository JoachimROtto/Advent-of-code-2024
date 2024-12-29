package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayFifteen extends Day {

    public DayFifteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 15);
        expectations=new long[]{1509863,1548815};
        example = Arrays.asList(
                "#######",
                "#...#.#",
                "#.....#",
                "#..OO@#",
                "#..O..#",
                "#.....#",
                "#######",
                "",
                "<vv<<^^<<^^");

        /*
        Part 1: The input is a labyrinth with walls (#), boxes (O) and a robot (@) as well as movement instructions.
        The robot moves according to the instructions and pushes the boxes as long as they are not against walls.
        At the end, each box has a value of 100 * y-axis + x-axis. What is the sum?
        Part 2: The entries widen on the x-axis. Barrels are now twice as wide and can now lie n:n on top of each
        other. How does the result change?
        */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        char[][]map = extractMap(input,bPart2);
        List<String> movements = extractMovements(input);
        for (int i = 0; i < movements.size(); i++) {
            map=moveRobotRec(map, movements.get(i), searchRobot(map), bPart2);
        }
        return sumCoords(map);
    }

    private char[][] moveRobotRec(char[][] map, String movements, int[] position, boolean bPart2) {
        if (movements.isEmpty())
            return map;
        switch (movements.charAt(0)) {
            case '^':
                map= prepareMoveCol(map, position, true,bPart2);
                if (map[position[1]][position[0]]=='.')
                    position[1]--;
                break;
            case 'v':
                map= prepareMoveCol(map, position, false,bPart2);
                if (map[position[1]][position[0]]=='.')
                    position[1]++;
                break;
            case '>':
                map= prepareMoveRow(map, position, false,bPart2);
                if (map[position[1]][position[0]]=='.')
                    position[0]++;
                break;
            case '<':
                map= prepareMoveRow(map, position, true,bPart2);
                if (map[position[1]][position[0]]=='.')
                    position[0]--;
                break;
            default:
                throw new IllegalStateException("Unexpected move: " + movements.charAt(0));
        }
        return moveRobotRec(map,movements.substring(1),position,bPart2);
    }

    private char[][] prepareMoveRow(char[][] map, int[] position, boolean reverse, boolean bPart2) {
        int newX= position[0] + (reverse?-1:1);
        int newY= position[1];
        if (map[newY][newX]=='.'){
            map[position[1]][position[0]]='.';
            map[newY][newX]='@';
            return map;
        }
        for (int i=position[0]; i>0 && i<map[0].length; i=i + (reverse?-1:1)) {
            if ( map[newY][i] == '#' )
                return map;
            if ( map[newY][i] == '.' ) {
                map[newY][i] = 'O';
                map[position[1]][position[0]] = '.';
                map[newY][newX] = '@';
                if (bPart2){
                    for (int j = i; j != (position[0] + (reverse?-1:1)) ; j+=reverse?2:-2) {
                        map[newY][j] = reverse?'[':']';
                        map[newY][j+(reverse?1:-1)] = reverse?']':'[';
                    }}
                return map;
            }
        }
        return map;
    }

    private char[][] prepareMoveCol(char[][] map, int[] position, boolean reverse, boolean bPart2) {
        int newX = position[0];
        int newY = position[1] + (reverse ? -1 : 1);
        if ( map[newY][newX] == '.' ) {
            map[position[1]][position[0]] = '.';
            map[newY][newX] = '@';
            return map;
        }
        if(bPart2){
            return tryMoveBloc(map, position, reverse);
        }
        for (int i = position[1]; i > 0 && i < map.length; i = i + (reverse ? -1 : 1)) {
            if ( map[i][newX] == '#' )
                return map;
            if ( map[i][newX] == '.' ) {
                map[i][newX] = 'O';
                map[position[1]][position[0]] = '.';
                map[newY][newX] = '@';
                return map;
            }
        }
            return map;
        }

    private char[][] tryMoveBloc(char[][] map, int[] position, boolean reverse) {
        char[][]newMap=copyMap(map);
        if (moveBlockRec(newMap, map[position[1]][position[0]], position, reverse)){
            newMap[position[1]][position[0]]='.';
            newMap[position[1]+(reverse?-1:1)][position[0]]='@';
            return replaceMap(newMap);
        }
        return map;
    }

    private boolean moveBlockRec(char[][] map, char sign, int[] position, boolean reverse) {
        int newY=position[1]+(reverse?-1:1);
        if (map[newY][position[0]]=='#')
            return false;
        if (map[newY][position[0]]=='.' || map[newY][position[0]]=='Y' || map[newY][position[0]]=='X'){
            map[newY][position[0]]=(sign=='['?'X':'Y');
            return true;
        }
        char mySign=map[newY][position[0]];
        map[newY][position[0]]=sign;
        int l=position[0] + (mySign=='['?0:-1);
        boolean result =moveBlockRec(map, '[', new int[]{l,newY}, reverse) &&
                moveBlockRec(map, ']', new int[]{l+1, newY}, reverse);
        if (sign!='@'){
            map[newY][l]='.';
            map[newY][l+1]='.';
            map[newY][position[0]]=sign;
        } else {
            map[newY][l]='[';
            map[newY][l+1]=']';
            if (map[newY][position[0]-1]=='[')
                map[newY][position[0]-1]='.';
            if (map[newY][position[0]+1]==']')
                map[newY][position[0]+1]='.';
        }
        return result;
    }

    private int[] searchRobot(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if(map[i][j]=='@')
                    return new int[]{j,i};
            }
        }
        return new int[]{0,0};
    }

    private long sumCoords(char[][] map) {
        long result=0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j]=='O' || map[i][j]=='[')
                    result+=100L*i+j;
            }
        }
        return result;
    }

    private List<String> extractMovements(List<String> input) {
        List<String> result =new ArrayList<>();
        int row=input.indexOf("");
        for (int i = row; i < input.size(); i++) {
            result.add(input.get(i));
        }
        return result;
    }

    private char[][] extractMap(List<String> input, boolean bPart2) {
        char[][]result=new char[input.indexOf("")][input.getFirst().length() * (bPart2?2:1)] ;
        String line;
        for (int i = 0; i < input.indexOf(""); i++) {
            line=input.get(i);
            if (bPart2){
                line=line.replaceAll("#","##");
                line=line.replaceAll("O","[]");
                line=line.replaceAll("\\.","..");
                line=line.replaceAll("@","@.");
            }
            result[i]=line.toCharArray();
        }
        return result;
    }

    private char[][] replaceMap(char[][] map) {
        char[][]result=new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                result[i][j]=map[i][j];
                if(map[i][j]=='X')
                    result[i][j]='[';
                if(map[i][j]=='Y')
                    result[i][j]=']';
            }
        }
        return result;
    }

    private char[][] copyMap(char[][] map) {
        char[][]result=new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, result[i], 0, map[0].length);
        }
        return result;
    }
}