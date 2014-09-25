package ru.appdev.HexFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;

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

    private void readPack( InputStream is, List<Pack> packs) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( is );
            NodeList nList = doc.getElementsByTagName( "pack" );
            for ( int c=0; c<nList.getLength(); ++c ) {
                Node nNode = nList.item(c);
                if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element eNode = (Element) nNode;
                    String name = eNode.getElementsByTagName( "name" ).item(0).getFirstChild().getNodeValue();
                    String description = eNode.getElementsByTagName( "description" ).item(0).getFirstChild().getNodeValue();
                    String file = eNode.getElementsByTagName( "file" ).item(0).getFirstChild().getNodeValue();
                    packs.add( new Pack( name, description, file ) );
                }
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
