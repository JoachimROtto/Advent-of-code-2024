package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DayFourteen extends Day {

    public DayFourteen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 14);
        expectations=new long[]{208437768,7492};
        example = Arrays.asList(
                "p=0,4 v=3,-3",
                "p=6,3 v=-1,-3",
                "p=10,3 v=-1,2",
                "p=2,0 v=2,-1",
                "p=0,0 v=1,3",
                "p=3,0 v=-2,-2",
                "p=7,6 v=-1,-3",
                "p=3,0 v=-1,-2",
                "p=9,3 v=2,3",
                "p=7,3 v=-1,2",
                "p=2,4 v=2,-3",
                "p=9,5 v=-3,-3");
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
        if (bPart2)
            return eval2(input);
        return eval1(input);
    }

    private long eval1(List<String> input) {
        AtomicLong result= new AtomicLong(1);
        final int[]boundaries=debugMode?new int[]{11,7}:new int[]{101,103};
        int[] robotQuadrant=new int[5];
        int cycles =100;
        for (String line : input) {
            Robot robot=parseRobot(line);
            robot=robot.moveRobot( boundaries, cycles);
            robotQuadrant[robot.getQuadrant( boundaries)]++;
        }
        Arrays.stream(robotQuadrant).limit(4).filter(i->i!=0).mapToLong(a -> result.updateAndGet(v -> v * a)).sum();
        return result.get();
    }
    private long eval2(List<String> input) {
        List<Robot> robots=new ArrayList<>(input.size());
        for (String line : input) {
            robots.add(parseRobot(line));
        }
        for (int i = 0; i < 10000; i++) {
            List<Integer[]> position=new ArrayList<>(input.size());
            for (int j = 0; j < robots.size(); j++) {
                Robot robotLoc=robots.get(j);
                robotLoc=robotLoc.moveRobot( new int[]{101,103}, 1);
                robots.set(j,robotLoc);
                position.add(new Integer[]{robotLoc.X(), robotLoc.Y()});
            }
            if (guessedTrees(position))
                return i+1;
        }
        return 0;
    }

    private boolean guessedTrees(List<Integer[]> position) {
        char[][] map=new char[101][103];
        boolean isCandidate=false;
        for (int i = 0; i < map.length; i++) {
            Arrays.fill(map[i], ' ');
        }
        for (Integer[] integers : position) {
            map[integers[0]][integers[1]]='X';
        }
        for (int i = 0; i < map.length; i++) {
            if ( Arrays.toString(map[i]).contains("X, ".repeat(10)))
                isCandidate=true;
        }
        if (!isCandidate)
            return false;
        for (int i = 0; i < map.length; i++) {
            System.out.println(Arrays.toString(map[i]));
        }
        return true;
    }

    private Robot parseRobot(String line) {
        String[] elements=line.split(" ");
        return new Robot(
                Integer.parseInt(elements[0].substring(elements[0].indexOf("=")+1).split(",")[0]),
                Integer.parseInt(elements[0].substring(elements[0].indexOf("=")+1).split(",")[1]),
                Integer.parseInt(elements[1].substring(elements[1].indexOf("=")+1).split(",")[0]),
                Integer.parseInt(elements[1].substring(elements[1].indexOf("=")+1).split(",")[1]));
    }

    record Robot(int X,int Y, int vX, int vY){
        public Robot moveRobot(int[] boundaries,int cycles) {
            return new Robot(
                    move(X,vX,cycles,boundaries[0]),
                    move(Y,vY,cycles,boundaries[1]),
                    vX,vY);
        }
        private int move(int start, int v, int cycles, int boundary){
            return (boundary + start +
                    ((v*cycles)%boundary)
            )%boundary;
        }

        public int getQuadrant(int[] boundaries) {
            int result=4;
            if(X<boundaries[0]/2 && Y!=(boundaries[1]/2))
                result=0;
            if(X>boundaries[0]/2 && Y!=(boundaries[1]/2))
                result=1;
            if(Y>boundaries[1]/2&& X!=(boundaries[0]/2))
                result+=2;
            return result;
        }
    }
}