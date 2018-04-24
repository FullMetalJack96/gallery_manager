package com.example.theproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends Activity {

	public Button edit;
	public File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

	public static int [] prgmImages={R.drawable.folderblue};
	public ArrayList<String> fileNames = new ArrayList<>();
	public GridView gv;
	public ArrayList prgmName;


	protected FloatingActionButton newFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gallery);
		gv=(GridView) findViewById(R.id.gridView1);
		File dir = new File(startingDir.getPath()+"/PhotoMgr/");
		if(!dir.exists()){
			dir.mkdirs();
		}
		newFolder = (FloatingActionButton) findViewById(R.id.newFolder);
		newFolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
				final EditText txt = new EditText(GalleryActivity.this);
				builder.setTitle("Podaj nazwę galerii")
						.setView(txt)
						.setPositiveButton("Stwórz", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String directory = txt.getText().toString();
								File getDir = new File(startingDir.getPath() + "/PhotoMgr/" + directory + "/");
								if (!getDir.exists()) {
									getDir.mkdirs();
									Toast.makeText(GalleryActivity.this, "Stworzono galerię: " + directory, Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(GalleryActivity.this, "Galeria już istnieje :)", Toast.LENGTH_SHORT).show();
								}
								getFileList();

							}
						})
						.setNegativeButton("Anuluj", null)
						.show();
			}

		});

		getFileList();

	}

	public void getFileList(){
		fileNames.clear();
		String home = startingDir.getPath() + "/PhotoMgr/";
		File f = new File(home);
		File[] files = f.listFiles();
		for (File inFile : files) {
			if (inFile.isDirectory()) {
				fileNames.add(inFile.getName());
			}
		}
		GridAdapter gAdapter = new GridAdapter(GalleryActivity.this, fileNames, prgmImages);
		gv.setAdapter(gAdapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent start = new Intent(GalleryActivity.this, PhotoPreviewActivity.class);
				start.putExtra("path", startingDir.getPath() + "/PhotoMgr/"+fileNames.get(position));
				startActivity(start);

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

}
