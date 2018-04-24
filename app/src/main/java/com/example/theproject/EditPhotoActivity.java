package com.example.theproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.graphics.AvoidXfermode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class EditPhotoActivity extends Activity implements OnSeekBarChangeListener, View.OnTouchListener{

		public ImageView toLettersActivity;
		public ImageView flip;
		public ImageView imageVIEW;
		public LinearLayout imageLayout;
		public Bitmap image;
		public ImageView mirror;
		public LinearLayout seekBars;
		public ImageView effects;
		public ImageView contrast;
		public ImageView cropImage;
		public SeekBar seekContrast;
		public SeekBar seekBrightness;
		public SeekBar seekSaturation;
		public ImageView accept;
		public byte[] fotoData;
		public String parentPath;
		protected Bitmap sourceBitmap, effectedBitmap, sendableBitmap;
		private ProgressDialog pDialog;
		protected ColorMatrix sEffect;
		public int mirrorStatus = 0;
		protected int _xDelta;
		private int _yDelta;
		protected final int CROPPED_IMAGE = 121212;
		protected ArrayList<Typeface> tfList;

		public static File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		public byte[] convertPhoto(){
			try{
			String tempFileName = "tymczasowy.jpg";// dodaj bica date do nazwy pliku

			Bitmap tmpBitmap = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
			
			File folder = new File(dir.getPath()+"/PictureManager");
			
			if(!folder.exists()){
				folder.mkdirs();
			}
			File file = new File(folder.getAbsolutePath()+"/"+tempFileName);
			FileOutputStream fs = new FileOutputStream(file);
			fs.write(stream.toByteArray());
			fotoData = stream.toByteArray();
			fs.close();
			tmpBitmap.recycle();
			}catch(Exception ex){
				Toast.makeText(EditPhotoActivity.this,ex.getMessage() + "", Toast.LENGTH_LONG).show();
			}
			return fotoData;
		}
		//NETWORK
	@SuppressWarnings("deprecation")
		private class UploadFoto extends AsyncTask<String, Void, String>{

			@Override
			protected String doInBackground(String... params) {
				String result = null;
				try{
				// TODO Auto-generated method stub
				HttpPost httpPost = new HttpPost("http://4ib2.spec.pl.hostingasp.pl/Paciorek_Jacek/PictureManager/Send.aspx");

				httpPost.setEntity(new ByteArrayEntity(convertPhoto())); // fotoData - nasze zdcie przekonwertowane na byte[]

				HttpClient httpClient = new DefaultHttpClient(); // klient http

				HttpResponse httpResponse = null; // obiekt odpowiedzi z serwera

				httpResponse = httpClient.execute(httpPost); // wykonanie wysnia

				result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8); // odebranie odpowiedzi z

				//serwera, kt potem wyswietlimy w onPostExecute

				//Uwaga - zawsze sprawdzaj czy result !=null, czyli czy serwer odpowiedzia

				}catch(Exception ex){
					result  = ex.getMessage().toString();
				}
				return result;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.show();
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if(result != null){
					Toast.makeText(EditPhotoActivity.this,"Odebrano: "+result, Toast.LENGTH_LONG).show();
				}
				super.onPostExecute(result);
				pDialog.dismiss();
			}
		}


		//NETWORK
		public void sharePicture(){
			try{
			Intent share = new Intent(Intent.ACTION_SEND);
			//typ ry
			share.setType("image/jpeg");
			//twor tymczasowy plik
			String tempFileName = "tymczasowy.jpg";// dodaj bate do nazwy pliku
			Bitmap tmpBitmap = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

			File folder = new File(dir.getPath()+"/PictureManager");

			if(!folder.exists()){
				folder.mkdirs();
			}
			File file = new File(folder.getAbsolutePath()+"/"+tempFileName);
			FileOutputStream fs = new FileOutputStream(file);
			fs.write(stream.toByteArray());
			fs.close();
			tmpBitmap.recycle();

			share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));

			startActivity(Intent.createChooser(share, "Podziel sie plikiem!"));
			}catch(Exception ex){
				Toast.makeText(EditPhotoActivity.this,ex.getMessage()+"", Toast.LENGTH_LONG).show();
			}

		}

	public void getTypefaces() {
		try {
			tfList.clear();
			String[] fontList = getAssets().list("fonts");
			for(String font : fontList) {
				Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/" + font);
				tfList.add(tf);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);



		
		setContentView(R.layout.activity_edit_photo);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String photoPath = extras.getString("photo");
			parentPath = extras.getString("path");
			Bitmap photo = BitmapFactory.decodeFile(photoPath);

			imageVIEW = (ImageView) findViewById(R.id.myImage);
			imageVIEW.setImageBitmap(photo);
			this.sourceBitmap = photo;
			this.effectedBitmap = photo;
			this.sEffect = new ColorMatrix(ImageEdit.normal_tab);
			this.tfList = new ArrayList<>();
			getTypefaces();
		}



		image = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();

		toLettersActivity = (ImageView) findViewById(R.id.toLettersActivity);
		toLettersActivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchIntent = new Intent(EditPhotoActivity.this, LettersActivity.class);
				EditPhotoActivity.this.startActivityForResult(launchIntent, 10000);
			}
		});

		flip = (ImageView) findViewById(R.id.flip);

		flip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				flipPicture();
			}
		});
//		mirror = (ImageView) findViewById(R.id.flipHorizontal);
//		mirror.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mirrorStatus++;
//
//				if(mirrorStatus>3){
//					mirrorStatus = 0;
//				}
//				switch (mirrorStatus){
//					case 1:
//						mirror(-1.0f, 1.0f);
//						break;
//					case 2:
//						mirror(1.0f, -1.0f);
//						break;
//					case 3:
//						mirror(1.0f, -1.0f);
//						break;
//					case 4:
//						mirror(-1.0f, 1.0f);
//						break;
//				}
//				//mirror();
//			}
//		});
//		effects = (ImageView) findViewById(R.id.wand);
//		final String[] OptionTab = {"Czerwony","Negatyw","Winieta","Sepia", "Niebieski", "Zielony","Normalny"};
//
//		effects.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				AlertDialog.Builder alert = new AlertDialog.Builder(EditPhotoActivity.this);
//				alert.setTitle("Efekty");
//				alert.setItems(OptionTab, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						ColorMatrix effect;
//						 Toast.makeText(EditPhotoActivity.this, "Zmieniono efekt na " + OptionTab[which], Toast.LENGTH_SHORT).show();
//
//						switch(which){
//							case 0:
//								effect = new ColorMatrix(ImageEdit.red_tab);
//								break;
//
//							case 1:
//								effect = new ColorMatrix(ImageEdit.neg_tab);
//								break;
//							case 2:
//
//								imageVIEW.setImageBitmap(vignette(effectedBitmap));
//								return;
//
//							case 3:
//								effect = new ColorMatrix();
//								effect.setSaturation(0);
//								ColorMatrix cm_sepia = new ColorMatrix();
//								cm_sepia.setScale(1, 1, 0.8f, 1);
//								effect.postConcat(cm_sepia);
//								break;
//							case 4:
//								effect = new ColorMatrix(ImageEdit.blue);
//								break;
//							case 5:
//								effect = new ColorMatrix(ImageEdit.green);
//								break;
//							case 6:
//							default:
//								effect = new ColorMatrix(ImageEdit.normal_tab);
//								break;
//
//						}
//						setImageColorTransformation(effect);
//
//					}
//				});
//
//
//				alert.show();
//			}
//		});
//		seekBars = (LinearLayout) findViewById(R.id.seekBars);
////		contrast = (ImageView) findViewById(R.id.contrast);
//		contrast.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				switch(seekBars.getVisibility()){
//				case View.VISIBLE:
//					seekBars.setVisibility(View.GONE);
//					break;
//				case View.GONE:
//					seekBars.setVisibility(View.VISIBLE);
//					break;
//
//				}
//
//			}
//		});
//
//		seekBrightness = (SeekBar) findViewById(R.id.seekBrightness);
//		seekBrightness.setOnSeekBarChangeListener(this);
//		seekContrast = (SeekBar) findViewById(R.id.SeekContrast);
//		seekContrast.setOnSeekBarChangeListener(this);
//		seekSaturation = (SeekBar) findViewById(R.id.SeekSaturation);
//		seekSaturation.setOnSeekBarChangeListener(this);
		
		
		accept = (ImageView) findViewById(R.id.accept);
		accept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(EditPhotoActivity.this);
				final String[] saveOptions = {"Zapisz lokalnie","Zapisz na serwerze","Udostępnij"};
				alert.setTitle("Zapisz zdjecie");
				alert.setItems(saveOptions, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 
						 switch(which){
						 case 0:
							 Toast.makeText(EditPhotoActivity.this, "Zapisuje lokalnie", Toast.LENGTH_SHORT).show();

							 SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
							 String filename = dFormat.format(new Date());
							 sendableBitmap = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
							 File newPhoto = new File(parentPath + "/" + filename + ".jpg");
							 Networking network = new Networking();

							 boolean status = network.connect(EditPhotoActivity.this);
							 if(status){
								 try {
									 FileOutputStream fos = new FileOutputStream(newPhoto);
									 fos.write(ImagingHelper.getRawFromBitmap(sendableBitmap));
									 fos.close();
									 Toast.makeText(EditPhotoActivity.this, "Zapisano zdjęcie!", Toast.LENGTH_SHORT).show();
								 } catch (Exception e) {
									 Toast.makeText(EditPhotoActivity.this, "Nie można było zapisać zdjęcia", Toast.LENGTH_SHORT).show();
									 e.printStackTrace();
								 } finally {
									 sendableBitmap.recycle();
								 }

						 }else{
								 Toast.makeText(EditPhotoActivity.this, "Nie połączono z internetem", Toast.LENGTH_SHORT).show();
						 }

							 break;
						 case 1:
							 Toast.makeText(EditPhotoActivity.this, "Zapisuje na srv", Toast.LENGTH_SHORT).show();
							 new UploadFoto().execute();
							 break;
						 case 2:
							 Toast.makeText(EditPhotoActivity.this, "Udostepniam", Toast.LENGTH_SHORT).show();
							 sharePicture();
							 break;
						 
						 }
					}
				});
				
				
				alert.show();
			}
		});
		
	}
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if(level == TRIM_MEMORY_RUNNING_LOW) {
			System.gc();
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.getLayoutParams();
				_xDelta = X - lParams.leftMargin;
				_yDelta = Y - lParams.topMargin;
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
						.getLayoutParams();
				layoutParams.leftMargin = X - _xDelta;
				layoutParams.topMargin = Y - _yDelta;
				layoutParams.rightMargin = -250;
				layoutParams.bottomMargin = -250;
				view.setLayoutParams(layoutParams);
				break;
		}
		imageLayout.invalidate();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 10000:
				switch (resultCode) {
					case 10000:
						//Toast.makeText(EditPhotoActivity.this, "Otrzymano odpowiedź: " + (data == null ? "nie ma danych" : "są dane"), Toast.LENGTH_SHORT).show();
						PreviewText preview = new PreviewText(EditPhotoActivity.this,tfList.get(data.getIntExtra("typeface", 0)), data.getIntExtra("fillColor",0), data.getIntExtra("strokeColor",0),  data.getStringExtra("text"));
						imageLayout = (LinearLayout) findViewById(R.id.editableimagelayout);
						imageLayout.addView(preview);
						preview.setOnTouchListener(this);
						break;


					default:
						Toast.makeText(EditPhotoActivity.this, "NOOOOOOO!", Toast.LENGTH_SHORT).show();
				}
				break;
			case 121212:
				//Toast.makeText(EditPhotoActivity.this, "Otrzymano odpowiedź: " + (data == null ? "nie ma danych" : "są dane"), Toast.LENGTH_SHORT).show();
				try{
					imageVIEW = (ImageView) findViewById(R.id.myImage);
					Bitmap bitmap = ImagingHelper.getScaledBitmap(ImagingHelper.getRotatedBitmapFromRaw(data.getByteArrayExtra("cropped"), 90), imageVIEW.getWidth(), imageVIEW.getHeight());
					imageVIEW.setImageBitmap(bitmap);

				}catch (Exception ex){

				//	Toast.makeText(EditPhotoActivity.this, "Porażka :("+ ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

				}


				break;
			default:
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_photo, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
				switch(seekBar.getId()){
				case R.id.seekBrightness:
//					Bitmap bitmap0 = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
//					Bitmap b0 = ImageEdit.changeBrightness(bitmap0, progress);
//					imageVIEW.setImageBitmap(b0);
				case R.id.SeekContrast:
//					Bitmap bitmap = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
//					Bitmap b = ImageEdit.changeContrast(bitmap, progress);
//					imageVIEW.setImageBitmap(b);
				case R.id.SeekSaturation:
//					Bitmap bitmap2 = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
//					Bitmap b2 = ImageEdit.changeSaturation(bitmap2, progress);
//					imageVIEW.setImageBitmap(b2);
					float contrast = 1+(seekContrast.getProgress() / 0x3f);
					float saturation = seekSaturation.getProgress() / 25.5f;
					float brightness = (-256 + (seekBrightness.getProgress() * 2.0f));
					effectedBitmap = ImageEdit.setContrastBrightness(sourceBitmap, brightness, contrast, saturation);
					imageVIEW.setImageBitmap(ImageEdit.applyColorMatrix(effectedBitmap, sEffect));
					break;
				
				}
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	public void flipPicture() {
		Matrix transformMatrix = new Matrix();
		transformMatrix.postRotate(90);
		Bitmap source = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
		Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
		Bitmap sourceCopy = sourceBitmap;
		sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
		imageVIEW.setImageBitmap(translated);
	}
	public void mirror(float x, float y) {
		Matrix transformMatrix = new Matrix();
		transformMatrix.postScale(x, y);
		Bitmap source = ((BitmapDrawable) imageVIEW.getDrawable()).getBitmap();
		Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
		Bitmap sourceCopy = sourceBitmap;
		sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
		imageVIEW.setImageBitmap(translated);
	}

}
