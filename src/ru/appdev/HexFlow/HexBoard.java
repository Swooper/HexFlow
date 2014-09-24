package ru.appdev.HexFlow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by haukur on 12/09/2014.
 */
public class HexBoard extends View {

    private final int BOARD_SIZE = 5;
    private int m_cellWidth;
    private int m_cellHeight;
    private int m_xCellShift;
    private int m_yCellShift;

    private Paint m_paintGrid = new Paint();
    private Paint m_paintPath = new Paint();
    private Path m_path = new Path();
    private CellPath m_cellPath = new CellPath();

    public HexBoard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // The following block of code is from StackOverflow, specifically
        // http://stackoverflow.com/questions/19155559/how-to-get-android-device-screen-size and
        // http://stackoverflow.com/questions/8833825/error-getting-window-size-on-android-the-method-getwindowmanager-is-undefined
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        m_cellWidth = (int) ((screenWidth - getPaddingLeft() - getPaddingRight()) / (2*BOARD_SIZE-0.66f)) * 4/3;
        // This may look weird but there's reasons.
        // The *4/3 has to do with the way the hex grid is arranged - each column overlaps the previous one by 1/4.
        // -0.66f instead of -1 is to avoid clipping off the right corners and edges of the last column.
        m_cellHeight = (int) (Math.sqrt(3)* (m_cellWidth /2)); // This is the ratio between width and height in a hexagon.
        m_xCellShift = (int) (3* (float) m_cellWidth /4); // The x-axis offset between hex columns
        m_yCellShift = m_cellHeight /2; // The y-axis offset between hex columns

        m_paintGrid.setColor(Color.parseColor("#666666")); // Hardcoded colour until I can figure out how to use /res/values files properly.
        m_paintPath.setStyle( Paint.Style.STROKE );
        m_paintPath.setColor(Color.parseColor("#33B5E5")); // ditto
        m_paintPath.setStrokeWidth(32);
        m_paintPath.setStrokeCap( Paint.Cap.ROUND );
        m_paintPath.setStrokeJoin( Paint.Join.ROUND );
        m_paintPath.setAntiAlias( true );

        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        drawHexGrid(canvas);
        drawPath(canvas);
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
                    yij = j * (float) m_cellHeight + Math.abs(BOARD_SIZE - i) * m_yCellShift;
                    drawHex((i-1) * m_xCellShift, yij, canvas);
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

    private void drawPath( Canvas canvas ) {
        m_path.reset();
        if ( !m_cellPath.isEmpty() ) {
            List<Coordinate> colist = m_cellPath.getCoordinates();
            Coordinate co = colist.get( 0 );
            m_path.moveTo( (float) (colToX(co.getCol()) + m_cellWidth / 2),
                    (float) (rowToY(co.getRow(), co.getCol()) + m_cellHeight / 2 ));
            for ( int i=1; i<colist.size(); ++i ) {
                co = colist.get(i);
                m_path.lineTo( (float) (colToX(co.getCol()) + m_cellWidth / 2),
                        (float) (rowToY(co.getRow(), co.getCol()) + m_cellHeight / 2 ));
            }
        }
        canvas.drawPath( m_path, m_paintPath );
    }

    // Simplified - the border between columns is a straight line in this game rather
    // than the actual zig-zag border between the hexes.
    private int xToCol( int x ) {
        return (int) ((x - (float) m_cellWidth*0.125) / (0.75 * (float) m_cellWidth))+1;
    }

    private int yToRow( int y , int x ) {
        int adjusted_y = (int) ((float) m_cellHeight/2*Math.abs(BOARD_SIZE - xToCol(x))); // Y coordinate adjusted for the non-even upper edge of the board
        if(y < adjusted_y) {
            return 0;
        }
        else {
            return (int) ((y - adjusted_y) / (float) m_cellHeight);
        }
    }

    private int colToX( int col ) {
        return ((col - 1) * m_cellWidth * 3) / 4;
    }

    // This method has to account for the y-offset of each row.
    private int rowToY( int row, int col ) {
        return row * m_cellHeight + (Math.abs(BOARD_SIZE - col) * (m_cellHeight/2));
    }

    public void setColor( int color ) {
        m_paintPath.setColor( color );
        invalidate();
    }

    // (c1, r1) must always be the origin point, (c2, r2) the destination.
    private boolean areNeighbours( int c1, int r1, int c2, int r2 ) {
        return ((Math.abs(c1-c2) + Math.abs(r1-r2) == 1) ||
                (Math.abs(c1-c2) == 1 && Math.abs(r1-r2) == 1 ));
    }

    // TODO fix this
    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int c = xToCol( x );
        int r = yToRow( y, x );
        if(r==0 || c==0) {
            return false;
        }
        if ( c >= BOARD_SIZE*2 || r >= (BOARD_SIZE*2-Math.abs(BOARD_SIZE-c)) ) {
            return true;
        }
        if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
            m_path.reset();
            m_path.moveTo((float) (colToX(c) + m_cellWidth / 2), (float) (rowToY(r, c) + m_cellHeight / 2 ));
            m_cellPath.reset();
            m_cellPath.append( new Coordinate(c,r) );
        }
        else if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
            m_path.lineTo((float) (colToX(c) + m_cellWidth / 2), (float) (rowToY(r, c) + m_cellHeight / 2 ));
            if ( !m_cellPath.isEmpty() ) {
                List<Coordinate> coordinateList = m_cellPath.getCoordinates();
                Coordinate last = coordinateList.get(coordinateList.size()-1);
                if ( areNeighbours(last.getCol(),last.getRow(), c, r)) {
                    m_cellPath.append(new Coordinate(c, r));
                    invalidate();
                }
            }
        }
        return true;
    }

    /**
    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int sw = Math.max(1, (int) m_paintGrid.getStrokeWidth());
        m_cellWidth = (xNew - getPaddingLeft() - getPaddingRight() - sw) / ((2 * BOARD_SIZE) - 1);
        m_cellHeight = Math.sqrt(3)* (m_cellWidth /2); // Proportions between width and height in a hexagon.
        x_cellShift = 3* (float) m_cellWidth /4; // The x-axis offset between hex columns
        y_cellShift = (float) m_cellHeight /2; // The y-axis offset between hex columns

        m_cellHeight = (yNew - getPaddingTop() - getPaddingBottom() - sw) / (2*BOARD_SIZE-1);
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
