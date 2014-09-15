package ru.appdev.HexFlow;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by haukur on 15/09/2014.
 */
public class CellPath {
    private ArrayList<Coordinate> m_path = new ArrayList<Coordinate>();
    public void append( Coordinate co ) {
        int idx = m_path.indexOf( co );
        if ( idx >= 0 ) {
            for ( int i=m_path.size()-1; i > idx; --i ) {
                m_path.remove(i);
            }
        }
        else {
            m_path.add(co);
        }
    }
    public List<Coordinate> getCoordinates() {
        return m_path;
    }
    public void reset() {
        m_path.clear();
    }
    public boolean isEmpty() {
        return m_path.isEmpty();
    }
}
