package de.gfed.AoC_2024.days;


import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;
import java.util.*;


public class DayEighteen extends Day {

    public DayEighteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 18);
        expectations=new long[]{234,0}; //58,19
        example = Arrays.asList(
                "5,4",
                "4,2",
                "4,5",
                "3,0",
                "2,1",
                "6,3",
                "2,4",
                "1,5",
                "0,6",
                "3,3",
                "2,6",
                "5,1",
                "1,2",
                "5,5",
                "2,5",
                "6,5",
                "1,4",
                "0,4",
                "6,4",
                "1,1",
                "6,1",
                "1,0",
                "0,5",
                "1,6",
                "2,0");
        /*
        Part 1:In a 70*70 field, 1024 blockages are recorded based on a list. What is the shortest path
        from 0/0 to 70/70?
        Part 2: There are many more blockages. After which blockage is the field no longer passable?
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        int limit =71;
        char [][]map=buildMap(input, limit, 1024);
        if(bPart2)
            for (int i = 1024; i < input.size(); i++) {
                map=buildMap(input, limit, i);
                if (searchWalk(map)==0) {
                    System.out.println(input.get(i - 1));
                    return 0;
                }
            }
        return searchWalk(map);
    }

    private char[][] buildMap(List<String> input, int dimension, int limit) {
        char [][]result = new char[dimension][dimension];
        for (int i = 0; i < limit; i++) {
            int x=Integer.parseInt(input.get(i).split(",")[0]);
            int y=Integer.parseInt(input.get(i).split(",")[1]);
            result[y][x]='#';
        }
        return result;
    }

    private long searchWalk(char [][]map){
        long result = 0;
        Queue<Position> seen= new ArrayDeque<>();
        map[0][0]='X';
        seen.add(new Position(0,0,0));
        while(!seen.isEmpty()){
            Position currenPosition=seen.remove();
            if (currenPosition.x==70 && currenPosition.y==70)
                return currenPosition.steps;
            for (int i = 0; i < 4; i++) {
                seen.addAll(validSteps(map, currenPosition));
            }
        }
        return result;
    }

    private List<Position> validSteps(char[][] map, Position currenPosition) {
        List<Position> result = new ArrayList<>(4);
        if (isValidStep(map, currenPosition.x+1, currenPosition.y))
            result.add(new Position(currenPosition.x+1, currenPosition.y, currenPosition.steps+1));
        if (isValidStep(map, currenPosition.x-1, currenPosition.y))
            result.add(new Position(currenPosition.x-1, currenPosition.y, currenPosition.steps+1));
        if (isValidStep(map, currenPosition.x, currenPosition.y+1))
            result.add(new Position(currenPosition.x, currenPosition.y+1, currenPosition.steps+1));
        if (isValidStep(map, currenPosition.x, currenPosition.y-1))
            result.add(new Position(currenPosition.x, currenPosition.y-1, currenPosition.steps+1));
        return result;
    }

    private boolean isValidStep(char[][] map, int x, int y) {
        if (x<0 || y<0 || x> map.length-1 || y>map.length-1)
            return false;
        if (map[y][x]=='#' || map[y][x]=='X')
            return false;
        map[y][x]='X';
        return true;
    }
    record Position (int x, int y, int steps){}
}