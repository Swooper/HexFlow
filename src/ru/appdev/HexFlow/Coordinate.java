package ru.appdev.HexFlow;

/**
 * Created by haukur on 15/09/2014.
 */
public class Coordinate {
    private int m_col;
    private int m_row;
    Coordinate( int col, int row ) {
        m_col = col;
        m_row = row;
    }
    public int getCol() {
        return m_col;
    }
    public int getRow() {
        return m_row;
    }
    @Override
    public boolean equals( Object other ) {
        if ( !(other instanceof Coordinate) ) {
            return false;
        }
        Coordinate otherCo = (Coordinate) other;
        return otherCo.getCol() == this.getCol()&& otherCo.getRow() == this.getRow();
    }
}