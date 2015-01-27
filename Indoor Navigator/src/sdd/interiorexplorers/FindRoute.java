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

public class FindRoute extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.findroute);

		/* Get the current map and make sure it has already been selected */
		Building current = MapHandler.getInstance().getCurrentMap();
		if (current == null) {
			Toast.makeText(FindRoute.this, "Please choose a map first",
					Toast.LENGTH_SHORT).show();

			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_CANCELED, resultIntent);
			finish();
		}

		/*
		 * Set up the spinners for both nodes. The same list of nodes can be
		 * used for both spinners
		 */
		final Spinner node1Spinner = (Spinner) findViewById(R.findroute.nodes1);
		List<Node> tempList = new ArrayList<Node>(current.getFloor().get(0)
				.getNode());
		tempList.addAll(current.getFloor().get(1).getNode());
		final List<Node> utilList = tempList;
		String[] x = new String[utilList.size()];
		int removed = 0;
		for (int i = 0; i < utilList.size(); i++) {
			x[i - removed] = utilList.get(i).getDescription();
		}
		String[] x_pruned = new String[utilList.size() - removed];
		for (int i = 0; i < x_pruned.length; ++i) {
			x_pruned[i] = x[i];
		}
		x = x_pruned;

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(FindRoute.this,
				android.R.layout.simple_spinner_item, x);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		node1Spinner.setAdapter(adapter);
		final Spinner node2Spinner = (Spinner) findViewById(R.findroute.nodes2);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
				FindRoute.this, android.R.layout.simple_spinner_item, x);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		node2Spinner.setAdapter(adapter2);

		/* Set up the buttons */
		final Button enter = (Button) findViewById(R.findroute.enter);
		enter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				/*
				 * Activated when the enter button is clicked. Get the ID's of
				 * the nodes that were selected in the spinners and pass them
				 * back to IndoorNavigator
				 */
				int i1 = node1Spinner.getSelectedItemPosition();
				int i2 = node2Spinner.getSelectedItemPosition();
				int id1 = utilList.get(i1).getId();
				int id2 = utilList.get(i2).getId();

				Intent resultIntent = new Intent();
				resultIntent.putExtra("Point1", id1);
				resultIntent.putExtra("Point2", id2);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();

			}
		});
		
		final Button exit = (Button) findViewById(R.findroute.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*
				 * Activated when the exit button is clicked. Returns to
				 * IndoorNavigator
				 */
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_CANCELED, resultIntent);
				finish();
			}
		});
	}

}
