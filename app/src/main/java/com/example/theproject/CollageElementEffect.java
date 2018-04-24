package com.example.theproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CollageElementEffect extends Activity {

    protected ImageView collageComponentEffect, copy1, copy2, copy3, copy4,accept,cancel;
    protected Bitmap sourceBitmap, effectedBitmap;
    protected LinearLayout effectList;

    protected ColorMatrix cmRed, cmBlue, cmGreen, cmNeg;
    protected Point displaySize = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collage_element_effect);

        copy1 = new ImageView(CollageElementEffect.this);
        copy2 = new ImageView(CollageElementEffect.this);
        copy3 = new ImageView(CollageElementEffect.this);
        copy4 = new ImageView(CollageElementEffect.this);

        collageComponentEffect = (ImageView) findViewById(R.id.collageComponentEffect);

        Bundle extras = getIntent().getExtras();
        byte[] arr = extras.getByteArray("pic");
        Bitmap bitmap = ImagingHelper.makeScaledBitmapFromRaw(arr);
        sourceBitmap = bitmap;
        effectedBitmap = bitmap;
        collageComponentEffect.setImageBitmap(bitmap);
        collageComponentEffect.setScaleType(ImageView.ScaleType.CENTER_CROP);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(displaySize);

        effectList = (LinearLayout) findViewById(R.id.effectList);

        cmRed = new ColorMatrix(ImageEdit.red_tab);
        cmBlue = new ColorMatrix(ImageEdit.blue);
        cmGreen = new ColorMatrix(ImageEdit.green);
        cmNeg = new ColorMatrix(ImageEdit.neg_tab);

        copy1.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmRed));
        copy2.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmBlue));
        copy3.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmGreen));
        copy4.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmNeg));


        copy1.setLayoutParams(new LinearLayout.LayoutParams(displaySize.x / 4, -1, 1));
        copy1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        copy2.setLayoutParams(new LinearLayout.LayoutParams(displaySize.x / 4, -1, 1));
        copy2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        copy3.setLayoutParams(new LinearLayout.LayoutParams(displaySize.x / 4, -1, 1));
        copy3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        copy4.setLayoutParams(new LinearLayout.LayoutParams(displaySize.x / 4, -1, 1));
        copy4.setScaleType(ImageView.ScaleType.CENTER_CROP);


        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collageComponentEffect.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmRed));
            }
        });
        copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collageComponentEffect.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmBlue));
            }
        });
        copy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collageComponentEffect.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmGreen));
            }
        });
        copy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collageComponentEffect.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, cmNeg));
            }
        });

        effectList.addView(copy1);
        effectList.addView(copy2);
        effectList.addView(copy3);
        effectList.addView(copy4);
        accept = (ImageView) findViewById(R.id.acceptBtn);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                Bitmap source = ((BitmapDrawable) collageComponentEffect.getDrawable()).getBitmap();

                byte[] returned = ImagingHelper.getRawFromBitmap(source);

                returnIntent.putExtra("data", returned);
                setResult(12345, returnIntent);
             //  Toast.makeText(CollageElementEffect.this, "Wyjście", Toast.LENGTH_SHORT).show();
                CollageElementEffect.this.finish();
            }
        });

        cancel = (ImageView) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] data = new byte[0];
                Intent returnIntent = new Intent();
                returnIntent.putExtra("data", data);

                setResult(66677, returnIntent);
                CollageElementEffect.this.finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collage_element_effect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
