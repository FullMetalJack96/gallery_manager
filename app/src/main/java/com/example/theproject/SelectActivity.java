package com.example.theproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SelectActivity extends Activity {

	public Button btncamera;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btncamera = (Button) findViewById(R.id.buttoncamera);
		btncamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SelectActivity.this, CameraActivity.class));
			}
		});
		
		
		
	//	btncamera = (Button) findViewById(R.id.buttoncamera);
	
	//	btncamera.setOnClickListener(new View.OnClickListener() {
			
	//		@Override
	//		public void onClick(View v) {
	//			// TODO Auto-generated method stub
	//			startActivity(new Intent(SelectActivity.this, CameraActivity.class));
	//		}
	//	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

}
