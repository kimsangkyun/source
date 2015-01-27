package sdd.interiorexplorers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EditPOI extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("Hello, you are in EditPOI.");
		setContentView(tv);
	}
	
	public void ss(){}
}
