package ru.appdev.HexFlow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;

/**
 * Created by haukur on 25.9.2014.
 */
public class Pack {

    private String mName;
    private String mDescription;
    private String mFile;

    Pack(String name, String description, String file)  {
        mName = name;
        mDescription = description;
        mFile = file;
    }

    String getName() { return mName; }
    String getDescription() { return mDescription; }
    String getFile() { return mFile; }

    private void readLevels(InputStream is, List<Level> levels ) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( is );
            NodeList nList = doc.getElementsByTagName( "challenge" );
            for ( int c=0; c<nList.getLength(); ++c ) {
                Node nNode = nList.item(c);
                if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                    Element eNode = (Element) nNode;
                    String puzzle = eNode.getElementsByTagName( "puzzle" ).item(0).getFirstChild().getNodeValue();
                    String size = eNode.getElementsByTagName( "size" ).item(0).getFirstChild().getNodeValue();
                    String flows = eNode.getElementsByTagName( "flows" ).item(0).getFirstChild().getNodeValue(); // TODO Turn this into a list
                    levels.add( new Level( puzzle, Integer.parseInt(size), flows ) );
                }
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
