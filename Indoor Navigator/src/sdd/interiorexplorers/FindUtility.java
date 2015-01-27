package sdd.interiorexplorers;

import java.util.ArrayList;
import java.util.List;

import sdd.interiorexplorers.handlers.MapHandler;
import sdd.interiorexplorers.map.Building;
import sdd.interiorexplorers.map.Floor.Node;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class FindUtility extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get the current building from the singleton
        Building current = MapHandler.getInstance().getCurrentMap();
        
        //Make sure that a building has already been selected
        if (current == null)
        {
			Toast.makeText(FindUtility.this, "Please choose a map first", Toast.LENGTH_SHORT).show();

        	Intent resultIntent = new Intent();
			setResult(Activity.RESULT_CANCELED, resultIntent);
			finish();
        }
        
        setContentView(R.layout.findutility);
        
        /*Get all of the utilities on the current floor and put them in the Spinner*/
        final Spinner utilSpinner = (Spinner) findViewById(R.findutility.nodes);
        List<Node> tempList = new ArrayList<Node>(current.getFloor().get(0).getNode());
        tempList.addAll(current.getFloor().get(1).getNode());
        final List<Node> utilList = tempList;
        String [] x = new String[utilList.size()];
        int removed = 0;
        for (int i = 0; i < utilList.size(); i++)
        {
          x[i - removed]=utilList.get(i).getDescription();
        }
        String [] x_pruned = new String[utilList.size() - removed];
        for (int i = 0; i < x_pruned.length; ++i) {
          x_pruned[i] = x[i];
        }
        x = x_pruned;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FindUtility.this, android.R.layout.simple_spinner_item, x);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        utilSpinner.setAdapter(adapter);
        
        /*Set up the Buttons*/
        final Button enter = (Button) findViewById(R.findutility.enter);
        enter.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				/* Activated when the enter button is clicked.
				 * Get the ID of the selected node and pass
				 * it back to IndoorNavigator*/
				int i = utilSpinner.getSelectedItemPosition();
				int id = utilList.get(i).getId();
				Intent resultIntent = new Intent();
				resultIntent.putExtra("Utility", id);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				
			}
		});
        final Button exit = (Button) findViewById(R.findutility.exit);
        exit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				/*Activated when the exit button is clicked.
				 * Returns to IndoorNavigator */
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, resultIntent);
				finish();
			}
		});
        
    }
	

}
