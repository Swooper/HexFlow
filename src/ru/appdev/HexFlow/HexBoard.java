package ru.appdev.HexFlow;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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

        // The following block of code is from Stackoverflow, specifically
        // http://stackoverflow.com/questions/19155559/how-to-get-android-device-screen-size and
        // http://stackoverflow.com/questions/8833825/error-getting-window-size-on-android-the-method-getwindowmanager-is-undefined
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        m_cellWidth = ((screenWidth - getPaddingLeft() - getPaddingRight()) / (2*BOARD_SIZE-0.66f)) * 4/3;
        // This may look weird but there's reasons.
        // The *4/3 has to do with the way the hex grid is arranged - each column overlaps the previous one by 1/4.
        // -0.66f instead of -1 is to avoid clipping off the right corners and edges of the last column.
        m_cellHeight = Math.sqrt(3)* (m_cellWidth /2); // This is the ratio between width and height in a hexagon.
        x_cellShift = 3* (float) m_cellWidth /4; // The x-axis offset between hex columns
        y_cellShift = (float) m_cellHeight /2; // The y-axis offset between hex columns

        m_paintGrid.setColor(Color.parseColor("#666666")); // Hardcoded colour until I can figure out how to use /res/values files properly.
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

    // Draws a whole grid of hexes, as determined by BOARD_SIZE.
    // Calls the drawHex method to draw each individual hex.
    private void drawHexGrid(Canvas canvas) {
        int columns = BOARD_SIZE*2-1;
        int rows = BOARD_SIZE*2-1;

        for(int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                float yij;
                if (2 * BOARD_SIZE > j + Math.abs(BOARD_SIZE - i)) { //Filtering out hexes we don't want to draw
                    yij = j * (float) m_cellHeight + Math.abs(BOARD_SIZE - i) * y_cellShift;
                    drawHex((i-1) * x_cellShift, yij, canvas);
                }
            }
        }

    }

    // Draws a single hex.
    // x and y input coordinates are the top left corner of
    // the square bounding the hex.
    //  Corners of the hex:
    //   B __ C
    // A /   \ D
    //   \___/
    //  F    E
    // TODO get rid of scale variable
    private void drawHex(float x, float y, Canvas canvas) {

        float h = (float) m_cellHeight;
        float w = (float) m_cellWidth;
        //A -> B
        canvas.drawLine( x, y+h/2, x+w/4, y, m_paintGrid);
        //B -> C
        canvas.drawLine( x + w/4, y, x+3*w/4, y, m_paintGrid);
        //C -> D
        canvas.drawLine( x+3*w/4, y, x+w, y+h/2, m_paintGrid);
        //D -> E
        canvas.drawLine( x+w, y+h/2, x+3*w/4, y+h, m_paintGrid);
        //E -> F
        canvas.drawLine( x+3*w/4, y+h, x+w/4, y+h, m_paintGrid);
        //F -> A
        canvas.drawLine( x+w/4, y+h, x, y+h/2, m_paintGrid);
    }

    // Simplified - the border between columns is a straight line in this game.
    private int xToCol( int x ) {
        int col;
        col = (int) ((x - m_cellWidth/8) / (3 / 4 * m_cellWidth));
        return col;
    }
    private int yToRow( int y , int x ) {
        int col = xToCol(x);
        int row;
        row = (int)( y-((m_cellHeight/2)*Math.abs(BOARD_SIZE - col)/ m_cellHeight));
        return row;
    }

    // TODO fix these two methods when I add the code that uses them.
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
    /**
    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int sw = Math.max(1, (int) m_paintGrid.getStrokeWidth());
        m_cellWidth = (xNew - getPaddingLeft() - getPaddingRight() - sw) / ((2 * BOARD_SIZE) - 1);
        m_cellHeight = Math.sqrt(3)* (m_cellWidth /2); // Proportions between width and height in a hexagon.
        x_cellShift = 3* (float) m_cellWidth /4; // The x-axis offset between hex columns
        y_cellShift = (float) m_cellHeight /2; // The y-axis offset between hex columns

        //m_cellHeight = (yNew - getPaddingTop() - getPaddingBottom() - sw) / (2*BOARD_SIZE-1);
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension( size + getPaddingLeft() + getPaddingRight(),
            size + getPaddingTop() + getPaddingBottom() );
    }*/
}
