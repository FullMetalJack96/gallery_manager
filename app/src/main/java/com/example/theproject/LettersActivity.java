package com.example.theproject;

import java.io.IOException;

import com.example.theproject.ColorPickerDialog.OnColorSelectedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LettersActivity extends Activity {

    public LinearLayout fontsLayout;
    public RelativeLayout previewLayout;
    public EditText textField;
    public ImageView acceptButton;
    public ImageView cancelButton;
    public Typeface font;
    public ImageView colorPicker1;
    public ImageView colorPicker2;
    public int initialColor;
    public int strokeColor = 0xafafafaf;
    public int fillColor = 0x00000000;
    public String[] lista;
    public int selectedTypeface = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_letters);
        fontsLayout = (LinearLayout) findViewById(R.id.fontsLayout);
        previewLayout = (RelativeLayout) findViewById(R.id.PreviewLayout);
        textField = (EditText) findViewById(R.id.fontText);

        cancelButton = (ImageView) findViewById(R.id.cancel);
        acceptButton = (ImageView) findViewById(R.id.accept);

        colorPicker1 = (ImageView) findViewById(R.id.colorPicker1);
        colorPicker2 = (ImageView) findViewById(R.id.image);

        AssetManager assetManager = getAssets();

        final PreviewText preview = new PreviewText(LettersActivity.this, font, 0xafafafaf, 0x00000000, textField.getText().toString());

        try {

            lista = assetManager.list("fonts");
            //Toast.makeText(LettersActivity.this, lista.length+"", Toast.LENGTH_LONG).show();

            if (lista != null) {
                for (int i = 0; i < lista.length; i++) {
                    final int index = i;
                    //Toast.makeText(LettersActivity.this, lista[i], Toast.LENGTH_LONG).show();
                    final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/" + lista[i]);
                    TextView text = new TextView(LettersActivity.this);
                    text.setText("Costam");
                    text.setTextSize(60);

                    text.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            font = tf;
                            selectedTypeface = index;
                        }
                    });

                    text.setBackgroundColor(0xaaaaaa);
                    text.setTypeface(tf);
                    fontsLayout.addView(text);
                    preview.font = tf; //SZTYWNO
                }
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LettersActivity.this, "Wyjście", Toast.LENGTH_SHORT).show();
                finishSuccess();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAGUTAGU", "Wyjście - porażka");
                setResult(-1);
                LettersActivity.this.finishActivity(10000);
            }
        });

        colorPicker1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ColorPickerDialog colorPicker = new ColorPickerDialog(LettersActivity.this, initialColor, new OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        fillColor = color;
                    }
                });
                colorPicker.show();
            }
        });
        colorPicker2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPicker2 = new ColorPickerDialog(LettersActivity.this, initialColor, new OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        strokeColor = color;
                    }
                });
                colorPicker2.show();

            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(LettersActivity.this, preview.font.toString(), Toast.LENGTH_SHORT).show();
                final PreviewText preview = new PreviewText(LettersActivity.this, font, strokeColor, fillColor, textField.getText().toString());
                previewLayout.removeAllViews();
                previewLayout.addView(preview);

            }

        };
        textField.addTextChangedListener(textWatcher);


    }
    public void finishFailure() {
        Log.d("TAGUTAGU", "Wyjście - porażka");
        setResult(-1);
        LettersActivity.this.finishActivity(10000);
    }

    public void finishSuccess() {
        Log.d("TAGUTAGU", "Wyjście z sukcesem");
        Intent intent = new Intent();
        intent.putExtra("fillColor", fillColor);
        intent.putExtra("strokeColor", strokeColor);
        intent.putExtra("text", textField.getText().toString());
        intent.putExtra("typeface",selectedTypeface);
        setResult(10000, intent);
        finish();
    }
    @Override
         public void onBackPressed() {
        finishFailure();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.letters, menu);
        return true;
    }

}
