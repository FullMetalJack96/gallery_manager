package com.example.theproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PhotoPreviewActivity extends Activity {

    public String path;
    protected LinearLayout layoutPhoto;
    protected ImageView delete;
    protected ImageView back;

    protected Point displaySize = new Point();
    protected int selectedId = 0;
    public File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    public ArrayList<String> fileNames = new ArrayList<>();

    protected int elemWidthS, elemWidthL, elemHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_photo_preview);

        layoutPhoto = (LinearLayout) findViewById(R.id.photoPreviewLinear);

        final Rect displaySize = getDisplaySize(PhotoPreviewActivity.this);
        elemHeight = (int) (displaySize.height() * 0.25f);

        LinearLayout.LayoutParams prmsSmall = new LinearLayout.LayoutParams(-1, -1, 2);
        LinearLayout.LayoutParams prmsLarge = new LinearLayout.LayoutParams(-1, -1, 1);

        if (getIntent().getStringExtra("path") == null) return;
        path = getIntent().getStringExtra("path");
        //Toast.makeText(PhotoPreviewActivity.this, path, Toast.LENGTH_SHORT).show();


        File f = new File(path);
        File file[] = f.listFiles();

        EvenState rowState = EvenState.ODD;
        for (File files : f.listFiles()) {
            fileNames.add(files.getAbsolutePath());
        }

        for (int rows = 0; rows < 1 + Math.floor(f.list().length / 2); rows++) {

            if((2*rows) >= fileNames.size()) return;

            final LinearLayout layout = new LinearLayout(PhotoPreviewActivity.this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(-1, elemHeight));
            final ImageView iv1 = new ImageView(PhotoPreviewActivity.this);

            switch (rowState) {
                case ODD:
                    iv1.setLayoutParams(prmsSmall);
                    break;
                case EVEN:
                    iv1.setLayoutParams(prmsLarge);
                    break;
            }

            Bitmap b1 = BitmapFactory.decodeFile(fileNames.get(2*rows));

            final int index = rows;
            iv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv1.setImageBitmap(ImagingHelper.getScaledBitmap(b1, b1.getWidth() / 3, b1.getHeight() / 3));
            iv1.setId(10000 + rows);
            iv1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   // Toast.makeText(PhotoPreviewActivity.this, "iv1: " + v.getId(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
                    builder.setTitle("Czy chcesz usunąć zdjęcie?")
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    File fileToRemove = new File(fileNames.get(2 * index));
                                    layout.removeView(iv1);
                                    fileNames.remove(2 * index);
                                    fileToRemove.delete();

                                }
                            })
                            .setNegativeButton("Anuluj",null)

                            .show();
                    return false;
                }
            });
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PhotoPreviewActivity.this, EditPhotoActivity.class);
                    intent.putExtra("photo", fileNames.get(index));
                    intent.putExtra("path", path);
                    //startActivity(intent);
                    startActivityForResult(intent, 12345);
                }
            });
            layout.addView(iv1);

            if(2*rows + 1 < fileNames.size()) {
                final ImageView iv2 = new ImageView(PhotoPreviewActivity.this);
                switch (rowState) {
                    case ODD:
                        iv2.setLayoutParams(prmsLarge);
                        break;
                    case EVEN:
                        iv2.setLayoutParams(prmsSmall);
                        break;
                }
                Bitmap b2 = BitmapFactory.decodeFile(fileNames.get((2 * rows) + 1));
                iv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv2.setImageBitmap(ImagingHelper.getScaledBitmap(b2, b2.getWidth() / 3, b2.getHeight() / 3));
                iv2.setId(20000 + rows);
                iv2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                      //  Toast.makeText(PhotoPreviewActivity.this, "iv2: " + v.getId(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
                        builder.setTitle("Czy chcesz usunąć zdjęcie?")
                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File fileToRemove = new File(fileNames.get(((2*index)+1)));
                                    layout.removeView(iv2);
                                    fileNames.remove(((2 * index) + 1));
                                        fileToRemove.delete();
                                    }
                                })
                                .setNegativeButton("Anuluj",null)

                                .show();
                        return false;
                    }
                });
                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PhotoPreviewActivity.this, EditPhotoActivity.class);
                        intent.putExtra("photo", fileNames.get((2*index)+1));
                        intent.putExtra("path", path);
                        //startActivity(intent);
                        startActivityForResult(intent, 12345);
                    }
                });
                layout.addView(iv2);
            }
            rowState = (rowState == EvenState.ODD ? EvenState.EVEN : EvenState.ODD);
            layoutPhoto.addView(layout);
        }
    back = (ImageView) findViewById(R.id.backButton1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewActivity.this.finish();
            }
        });
        delete = (ImageView) findViewById(R.id.deleteButton1);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
                builder.setTitle("Pytanie")
                        .setMessage("Czy chcesz usunąć galerię?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String home = path;
                                File file = new File(home);
                                File[] files = file.listFiles();
                                for (File inFile : files) {
                                    inFile.delete();
                                }
                                Toast.makeText(PhotoPreviewActivity.this, "Usunięto: " + path, Toast.LENGTH_LONG).show();
                                file.delete();
                                Intent intent = new Intent(PhotoPreviewActivity.this, GalleryActivity.class);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_preview, menu);
        return true;
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_RUNNING_LOW) {
            System.gc();
        }
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

    public static Rect getDisplaySize(Context context) {
        Rect returned = new Rect();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRectSize(returned);

        return returned;
    }

    public enum EvenState {
        ODD,
        EVEN
    }
}
