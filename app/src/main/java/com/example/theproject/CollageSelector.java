package com.example.theproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class CollageSelector extends Activity {
	
		public LinearLayout kolaz1, kolaz2, kolaz3,kolaz4,kolaz5,kolaz6
				;
		protected Rect displaySize = new Rect();
		private ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collage_selector);



		kolaz1 = (LinearLayout) findViewById(R.id.kolaz1);
		WindowManager wm = (WindowManager) CollageSelector.this.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getRectSize(displaySize);
		kolaz1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(4);
				components.add(new ComponentLayout(0, 0, 0.5f, 0.5f));
				components.add(new ComponentLayout(0.5f, 0, 0.5f, 0.5f));
				components.add(new ComponentLayout(0, 0.5f, 0.5f, 0.5f));
				components.add(new ComponentLayout(0.5f, 0.5f, 0.5f, 0.5f));

				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);


			}
		});
		kolaz2 = (LinearLayout) findViewById(R.id.kolaz2);
		kolaz2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(2);
				components.add(new ComponentLayout(0, 0, 0.5f, 1f));
				components.add(new ComponentLayout(0.5f, 0, 0.5f, 1f));

				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);

			}
		});
		kolaz3 = (LinearLayout) findViewById(R.id.kolaz3);
		kolaz3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(3);
				components.add(new ComponentLayout(0, 0, 1f, 0.7f));
				components.add(new ComponentLayout(0, 0.7f, 0.5f, 0.3f));
				components.add(new ComponentLayout(0.5f, 0.7f, 0.5f, 0.3f));

				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);

			}
		});
		kolaz4 = (LinearLayout) findViewById(R.id.kolaz4);
		kolaz4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(6);
				components.add(new ComponentLayout(0, 0, 0.7f, 0.333f));
				components.add(new ComponentLayout(0.666f,0 , 0.3333f, 0.333f));

				components.add(new ComponentLayout(0,0.333f , 0.3333f, 0.333f));
				components.add(new ComponentLayout(0.333f,0.333f , 0.7f, 0.333f));

				components.add(new ComponentLayout(0,0.666f , 0.7f, 0.333f));
				components.add(new ComponentLayout(0.666f,0.666f , 0.3333f, 0.333f));


				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);

			}
		});
		kolaz5 = (LinearLayout) findViewById(R.id.kolaz5);
		kolaz5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(3);
				components.add(new ComponentLayout(0, 0, 0.5f, 0.3f));
				components.add(new ComponentLayout(0.5f, 0, 0.5f, 0.3f));
				components.add(new ComponentLayout(0, 0.3f, 1f, 0.7f));

				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);

			}
		});
		kolaz6 = (LinearLayout) findViewById(R.id.kolaz6);
		kolaz6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<ComponentLayout> components = new ArrayList<>(3);
				components.add(new ComponentLayout(0, 0, 1f, 0.5f));
				components.add(new ComponentLayout(0, 0.333f, 1f, 0.5f));
				components.add(new ComponentLayout(0, 0.666f, 1f, 0.5f));

				Intent collageStarter = new Intent(CollageSelector.this, CollageActivity.class);
				collageStarter.putExtra("components", components);
				startActivity(collageStarter);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collage_selector, menu);
		return true;
	}

}
