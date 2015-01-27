package sdd.interiorexplorers;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import sdd.interiorexplorers.handlers.MapHandler;
import sdd.interiorexplorers.map.Building;
import sdd.interiorexplorers.map.MapParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class IndoorNavigator extends Activity {

	// private and final members

	private MapView mapView;
	private static final int searchReturn = 0;
	private static final int findUtilityReturn = 1;
	private static final int findRouteReturn = 2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		/*
		 * Add new menu options here. Increment the second argument to add by 1,
		 * so the next will be menu.add(0,1,0,nextoption);
		 */
		CharSequence search = new String("Search");
		menu.add(0, 0, 0, search);
		CharSequence findUtility = new String("Find Utility");
		menu.add(0, 1, 0, findUtility);
		CharSequence findRoute = new String("Find Route");
		menu.add(0, 2, 0, findRoute);
		CharSequence help = new String("Help");
		menu.add(0, 3, 0, help);
		return true;
	}

	/**
	 * Menu item selection logic
	 * 
	 * @param item
	 *            - selected menu item
	 * @return - whether or not activity load was successful
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 0:
			// new view
			intent = new Intent(IndoorNavigator.this, Search.class);
			startActivityForResult(intent, searchReturn);
			return true;
		case 1:
			intent = new Intent(IndoorNavigator.this, FindUtility.class);
			startActivityForResult(intent, findUtilityReturn);
			return true;
		case 2:
			intent = new Intent(IndoorNavigator.this, FindRoute.class);
			startActivityForResult(intent, findRouteReturn);
			return true;
		case 3:
			// Toast.makeText(IndoorNavigator.this, Integer.toString(value),
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(
					this,
					"Select Search to find a map.\nSelect "
							+ "Find Utility to zoom to a point.\nSelect Find Route "
							+ "to find your way.", Toast.LENGTH_LONG * 10)
							.show();
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (searchReturn): {

			if (resultCode == Activity.RESULT_OK) {
				// Use this return value to pick the map to display

				int value = data.getExtras().getInt("Building");
				// Toast.makeText(IndoorNavigator.this, Integer.toString(value),
				// Toast.LENGTH_SHORT).show();
				mapView.setFloor(MapHandler.getInstance().getListMaps()
						.get(value).getFloor().get(0));

			}
			break;
		}
		case (findUtilityReturn): {
			try {
				if (resultCode == Activity.RESULT_OK) {
					// Use this return value to pick the utility to focus on
					int value = data.getExtras().getInt("Utility");
					mapView.setFloor(MapHandler.getInstance().getCurrentMap()
							.getFloor().get(0));
					mapView.showById(value);
				} else if (resultCode == Activity.RESULT_CANCELED) {
					Toast.makeText(IndoorNavigator.this, "Location not found!",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(IndoorNavigator.this,
						"Please choose a map first.", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		case (findRouteReturn): {
			try {
				if (resultCode == Activity.RESULT_OK) {
					// draw line
					int val1 = data.getExtras().getInt("Point1");
					int val2 = data.getExtras().getInt("Point2");
					mapView.printPath(val1, val2);
				} else if (resultCode == Activity.RESULT_CANCELED) {
					Toast.makeText(IndoorNavigator.this,
							"Please choose a map first", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {
				Toast.makeText(IndoorNavigator.this,
						"Please choose a map first", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ///////////////////////////////////////////////////////////////
		// Add Map Example Code:
		loadMap(R.raw.examplemap);
		// ///////////////////////////////////////////////////////////////

		mapView = (MapView) findViewById(R.id.MAP);
		// mapView.setFloor(MapHandler.getInstance().getCurrentMap().getFloor().get(0));

		// ///////////////////////////////////////////////////////////////
		// Path Printing Example Code:
		// mapView.printPath(4, 6);
		// ///////////////////////////////////////////////////////////////

	}

	/**
	 * Loads a map
	 * 
	 * @param resourceNum
	 *            - integer value corresponding to a resource
	 */
	private void loadMap(int resourceNum) {
		// create parser
		SAXParser parser;
		XMLReader xmlreader;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();
			xmlreader = parser.getXMLReader();
		} catch (Exception e) {
			return;
		}
		MapParser mapHandler = new MapParser();
		InputStream in;
		in = getApplicationContext().getResources()
				.openRawResource(resourceNum);
		InputSource source = new InputSource(in);
		// parse map
		try {
			xmlreader.setContentHandler(mapHandler);
			xmlreader.parse(source);
		} catch (IOException e) {
			return;
		} catch (SAXException e) {
			return;
		}
		// set the current map
		Building building = mapHandler.getTempBld();
		MapHandler.getInstance().setCurrentMap(building);
	}
}