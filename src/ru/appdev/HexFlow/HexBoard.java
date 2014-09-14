package ru.appdev.HexFlow;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;

/**
 * Created by haukur on 12/09/2014.
 */
public class HexBoard extends View {

    private final int BOARD_SIZE = 3;
    private double m_cellWidth;
    private double m_cellHeight;
    private float x_cellShift;
    private float y_cellShift;

    private Paint m_paintGrid = new Paint();
    private Paint m_paintPath = new Paint();
    private Path m_path = new Path();

    public HexBoard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        m_cellWidth = 20;
        m_cellHeight = Math.sqrt(3)* (m_cellWidth /2); // This is the proportion between width and height in a hexagon.
        x_cellShift = 3* (float) m_cellWidth /4; // The x-axis offset between hex columns
        y_cellShift = (float) m_cellHeight /2; // The y-axis offset between hex columns

        m_paintGrid.setColor(Color.parseColor("#666666"));
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        drawHexGrid(canvas);
        m_path.reset();
        /**if ( !m_cellPath.isEmpty() ) {
            List<Coordinate> colist = m_cellPath.getCoordinates();
            Coordinate co = colist.get( 0 );
            m_path.moveTo( colToX(co.getCol()) + m_cellWidth / 2,
                    rowToY(co.getRow()) + m_cellHeight / 2 );
            for ( int i=1; i<colist.size(); ++i ) {
                co = colist.get(i);
                m_path.lineTo( colToX(co.getCol()) + m_cellWidth / 2,
                        rowToY(co.getRow()) + m_cellHeight / 2 );
            }
        }
        canvas.drawPath( m_path, m_paintPath);*/
    }

    private void drawHexGrid(Canvas canvas) {
        int columns = BOARD_SIZE*2-1;
        int rows = BOARD_SIZE*2-1;

        for(int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                float yij;
                if (2 * BOARD_SIZE > j + Math.abs(BOARD_SIZE - i)) { //Filtering out hexes we don't want to draw
                    yij = j * (float) m_cellHeight + Math.abs(BOARD_SIZE - i) * y_cellShift;
                    drawHex(i * x_cellShift, yij, 10.0f, canvas);
                }
            }
        }

    }
    // x and y input coordinates are the top left corner of
    // the square bounding the hex.
    //  Corners of the hex:
    //   B __ C
    // A /   \ D
    //   \___/
    //  F    E
    private void drawHex(float x, float y, float scale, Canvas canvas) {

        float h = (float) m_cellHeight;
        float w = (float) m_cellWidth;
        //A -> B
        canvas.drawLine( scale*x, scale*(y+h/2), scale*(x+w/4), scale*y, m_paintGrid);
        //B -> C
        canvas.drawLine( scale*(x + w/4), scale*y, scale*(x+3*w/4), scale*y, m_paintGrid);
        //C -> D
        canvas.drawLine( scale*(x+3*w/4), scale*y, scale*(x+w), scale*(y+h/2), m_paintGrid);
        //D -> E
        canvas.drawLine( scale*(x+w), scale*(y+h/2), scale*(x+3*w/4), scale*(y+h), m_paintGrid);
        //E -> F
        canvas.drawLine( scale*(x+3*w/4), scale*(y+h), scale*(x+w/4), scale*(y+h), m_paintGrid);
        //F -> A
        canvas.drawLine( scale*(x+w/4), scale*(y+h), scale*x, scale*(y+h/2), m_paintGrid);
    }
    private int xToCol( int x ) {
        return (int) (x  / m_cellWidth);
    }
    private int yToRow( int y ) {
        return (int) (y  / m_cellHeight);
    }
    private int colToX( int col ) {
        return (int) (col * m_cellWidth);
    }
    private int rowToY( int row ) {
        return (int) (row * m_cellHeight);
    }

    public void setColor( int color ) {
        m_paintPath.setColor( color );
        invalidate();
    }
}
