package ru.appdev.HexFlow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
/**
 * Created by haukur on 12/09/2014.
 */
public class PlayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        SharedPreferences settings = getSharedPreferences( "ColorPref", MODE_PRIVATE );
        int colour = settings.getInt( "pathColor", Color.LTGRAY );
        HexBoard board = (HexBoard) findViewById( R.id.board );
        board.setColor( colour );
    }


}
