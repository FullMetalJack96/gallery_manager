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

public class CameraGallerySelect extends Activity {

    public Button edit;
    public File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    public static int [] prgmImages={R.drawable.folderblue};
    public ArrayList<String> fileNames = new ArrayList<>();
    public GridView gv;
    public ArrayList prgmName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera_gallery_select);

        gv=(GridView) findViewById(R.id.gridView1);

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
        CameraGalleryAdapter gAdapter = new CameraGalleryAdapter(CameraGallerySelect.this, fileNames, prgmImages);
        gv.setAdapter(gAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CameraGallerySelect.this,CameraActivity.class);
                intent.putExtra("path", startingDir.getPath() + "/PhotoMgr/"+ fileNames.get(position)+"/");
                startActivity(intent);




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
