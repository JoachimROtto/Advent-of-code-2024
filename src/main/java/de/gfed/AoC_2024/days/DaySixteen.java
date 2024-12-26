package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;
import java.util.List;

public class DaySixteen extends Day {

    public DaySixteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 16);
        expectations=new long[]{102460,527};
        example = Arrays.asList(
                "###############",
                "#.......#....E#",
                "#.#.###.#.###.#",
                "#.....#.#...#.#",
                "#.###.#####.#.#",
                "#.#.#.......#.#",
                "#.#.#####.###.#",
                "#...........#.#",
                "###.#.#####.#.#",
                "#...#.....#.#.#",
                "#.#.#.###.#.#.#",
                "#.....#...#.#.#",
                "#.###.#.#.#.#.#",
                "#S..#.....#...#",
                "###############");
        /*
        Part 1: A labyrinth is walked through from bottom right to top left. One step costs 1,
        turning (counter)clockwise costs 1000. What is the cheapest way?
        Part 2: The cheapest way is ambiguous. How many parts are touched by any of the cheapest way?
        */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        char [][] map=list2CharArray(input);
        List<Point> listOfCorners =  findCorners(map);
        return takeAWalk(map, listOfCorners, bPart2);
    }

    private long takeAWalk(char[][] map, List<Point> listOfCorners, boolean bPart2) {
        List <Point> seen=new ArrayList<>();
        List <Point> done=new ArrayList<>();
        seen.add(new Point(1, map.length-2, 0,1));
        while (!seen.isEmpty()){
            Point current = seen.removeFirst();
            done.add(current);
            List <Point> neighbours = findNeighbours(current, map, listOfCorners, current.distance);
            for (Point neighbour : neighbours) {
                if (!done.contains(neighbour) && !seen.contains(neighbour))
                    seen.add(neighbour);
            }
            seen.sort(Comparator.comparingInt(a -> a.distance));
        }
        Point end = listOfCorners.get(listOfCorners.indexOf(new Point(map[0].length-2,1,0,-1)));
        if(bPart2)
            return findPassages(end, map)+1;
        return end.distance;
    }

    private long findPassages(Point end, char[][]map){
        long result=0;
        if(end.distance==0 || map[end.y][end.x]=='O')
            return result;
        map[end.y][end.x]='O';
        if (end.pred.size()>1)
            result=result - end.pred.size() ;
        for (Point point : end.pred) {
            result+=(end.distance%1000) - (point.distance%1000);
            result+= findPassages(point, map);
        }
        return result;
    }

    private List<Point> findNeighbours(Point current, char[][] map, List<Point> listOfCorners, int offset) {
        return walk(current.x, current.y, current.direction, 0, map, listOfCorners, offset, current);
    }

    private List<Point> walk(int x, int y, int direction, int distance, char[][] map, List<Point> listOfCorners, int offset, Point pred) {
        List<Point> result=new ArrayList<>();
        if (listOfCorners.contains(new Point(x,y,Integer.MAX_VALUE/2,-1)) && distance>0){
            Point end = listOfCorners.get(listOfCorners.indexOf(new Point(x,y,Integer.MAX_VALUE/2,-1)));
            int distanceLoc = distance+ offset;
            int correction =(end.pred!=null &&(pred.direction!=end.pred.getFirst().direction)?1000:0);
            if ((distanceLoc - correction)<= end.distance) {
                end.setPredecessor(pred, distanceLoc%1000!= end.distance%1000);
            }
            if (distanceLoc<= end.distance){
                end.setDistance(distanceLoc);
                end.setDirection(direction);
            }
            return List.of(end);
        }
        if (map[y][x]=='#')
            return result;
        if(direction!=0)
            result.addAll(walk(x, y+1, 2, distance+(direction==2?1:1001), map, listOfCorners, offset, pred));
        if(direction!=1)
            result.addAll(walk(x-1, y, 3, distance+(direction==3?1:1001), map, listOfCorners, offset, pred));
        if(direction!=2)
            result.addAll(walk(x, y-1, 0, distance+(direction==0?1:1001), map, listOfCorners, offset,pred));
        if(direction!=3)
            result.addAll(walk(x+1, y, 1, distance+(direction==1?1:1001), map, listOfCorners, offset,pred));
        return result;
    }

    private List<Point> findCorners(char[][] map) {
        List<Point> result=new ArrayList<>();
        for (int i = 1; i < map.length-1; i++) {
            for (int j = 1; j < map[0].length-1; j++) {
                if (map[i][j]=='#')
                    continue;
                int wallCount=0;
                wallCount+=(map[i-1][j]=='#'?1:0);
                wallCount+=(map[i+1][j]=='#'?1:0);
                wallCount+=(map[i][j+1]=='#'?1:0);
                wallCount+=(map[i][j-1]=='#'?1:0);
                if (wallCount<2)
                    result.add(new Point(j,i,Integer.MAX_VALUE/2,-1));
            }
        }
        result.add(new Point(1,map.length-2,0,-1));
        result.add(new Point(map[0].length-2,1,Integer.MAX_VALUE/2,-1));
        return result;
    }

    class Point{
        int x;
        int y;
        int distance;
        int direction;
        List<Point> pred;

        Point (int x, int y, int distance, int direction){
            this.x=x;
            this.y=y;
            this.distance=distance;
            this.direction=direction;
        }
        public void setDistance(int distance){
            this.distance=distance;
        }

        public void setPredecessor(Point item, boolean overwrite){
            if (pred==null || overwrite)
                pred=new ArrayList<>();
            if ( !pred.contains(item) )
                pred.add(item);
        }

        public void setDirection(int direction){
            this.direction=direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (other == null)
                return false;
            if (!(other instanceof Point)) {
                return false;
            }
            return ((Point)other).y == y && ((Point)other).x == x;
        }
    }
}