package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayTwentyFive extends Day {

    public DayTwentyFive(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 25);
        expectations=new long[]{3196,0};
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
        Part 1: You have a list of blocks consisting of . and #. A full (#) row at the top marks a lock
        and a full row at the bottom marks a key. The key and lock match if there are no more than 5 (+2)
        # in each column. How many pairs are there?
        Part 2: Is for free
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result=0;
        List<List<Integer[]>> keysAndLocks=getKeyAndLock(input);
        List<Integer[]> keys=keysAndLocks.get(0);
        List<Integer[]> locks=keysAndLocks.get(1);
        for (Integer[] key : keys) {
            for (Integer[] lock : locks) {
                if( matches(key, lock))
                    result++;
            }
        }
        if(bPart2)
            return 0;
        return result;
    }

    private boolean matches(Integer[] key, Integer[] lock) {
        for (int i = 0; i < key.length; i++) {
            if((key[i]+lock[i])>5)
                return false;
        }
        return true;
    }

    private List<List<Integer[]>> getKeyAndLock(List<String> input) {
        List<Integer[]> keys=new ArrayList<>();
        List<Integer[]> locks=new ArrayList<>();
        Integer[] length;
        for (int i = 0; i < input.size(); i+=8) {
            length=new Integer[]{0,0,0,0,0};
            for (int j = 0; j < 5; j++) {
                length[j]=length[j]+(input.get(i+1).charAt(j)=='#'?1:0);
                length[j]=length[j]+(input.get(i+2).charAt(j)=='#'?1:0);
                length[j]=length[j]+(input.get(i+3).charAt(j)=='#'?1:0);
                length[j]=length[j]+(input.get(i+4).charAt(j)=='#'?1:0);
                length[j]=length[j]+(input.get(i+5).charAt(j)=='#'?1:0);
            }
            if (input.get(i).contains(".")){
                keys.add(length);
            } else {
                locks.add(length);
            }
        }
        return List.of(keys, locks);
    }
}
