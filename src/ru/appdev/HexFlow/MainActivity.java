package ru.appdev.HexFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void buttonClick( View view ) {
        Button button = (Button) view;
        int id = button.getId();
        if ( id == R.id.button_new_game ) {
            startActivity( new Intent( this, PlayActivity.class ) );
        }
        else if ( id == R.id.button_level_list ) {
            startActivity( new Intent( this, LevelListActivity.class ) );
        }
        else if ( id == R.id.button_options ) {
            startActivity( new Intent( this, OptionsActivity.class ) );
        }
        //else if ( id == R.id.button_resume_game ) {
            //TODO: Figure out how to resume game
        //}
    }
}
