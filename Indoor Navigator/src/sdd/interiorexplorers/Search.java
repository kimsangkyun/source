package sdd.interiorexplorers;


import java.util.List;

import sdd.interiorexplorers.handlers.MapHandler;
import sdd.interiorexplorers.map.Building;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Search extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);
        final Spinner buildings = (Spinner) findViewById(R.search.buildings);
        List<Building> maps = null;
        /*Get all of the maps from the MapHandler*/
        maps = MapHandler.getInstance().getListMaps();
        
        /*Put the String representation of all of the maps into an array*/
        String [] x = new String[maps.size()];
        for (int i = 0; i < maps.size(); i++)
        {
        	x[i]=maps.get(i).getDescription();
        }
        /*Set up the Spinner and Buttons*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_item, x);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildings.setAdapter(adapter);
        final Button enter = (Button) findViewById(R.search.enter);
        enter.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				/*Activated when enter is clicked. Returns the chosen 
				 * map from the Spinner to IndoorNavigator*/
				int i = buildings.getSelectedItemPosition();
				Intent resultIntent = new Intent();
				resultIntent.putExtra("Building", i);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				
			}
		});
        final Button exit = (Button) findViewById(R.search.exit);
        exit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Activated when exit is clicked. Returns to IndoorNavigator.
				finish();
			}
		});
        
	}

}
