package ru.appdev.HexFlow;

/**
 * Created by haukur on 25/09/2014.
 */
public class Level {

    private String mPuzzle;
    private int mSize;
    private String mFlows; // TODO turn this into a list

    Level(String puzzle, int size, String flows) {
        mPuzzle = puzzle;
        mSize = size;
        mFlows = flows;
    }

}
