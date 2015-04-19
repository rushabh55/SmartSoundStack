package com.rit.songstack;

import java.util.Random;

/**
 * Created by Rushabh on 4/19/2015.
 */

class Rand extends Random {
    int nextInt(int min, int max) {
        return min + next(max);
    }
}
public abstract class HeartBeat {
    private static Rand r = new Rand();
    static int min = 55;
    static int max = 130;
    static int delta = 5;
    public static int getHeartBeat(){
        int initialRandom = r.nextInt(min, max);
        return initialRandom;
    }

    public static int getHeartBeatLerped(int current){
        int currMin = current - delta;
        int currMax = current + delta;
        return r.nextInt(currMin, currMax);
    }
}
