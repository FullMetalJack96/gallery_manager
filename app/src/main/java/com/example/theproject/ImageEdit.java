package com.example.theproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageEdit {

	public static final float[] neg_tab = {
		    -1, 0, 0, 1, 0,
		        0, -1, 0, 1, 0,
		        0, 0, -1, 1, 0,
		        0, 0, 0, 1, 0

		        };
	
	public static final float[] red_tab = {
		    2, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 1, 0
		                };
	
	public static final float[] normal_tab = {
		    1.0f, 0, 0, 0, 0,
		          0, 1, 0, 0, 0,
		          0, 0, 1, 0, 0,
		          0, 0, 0, 1, 0
		                };

	public static final float[] blue = {
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 2, 0, 0,
			0, 0, 0, 1, 0
	};
	public static final float[] green = {
			0, 0, 0, 0, 0,
			0, 2, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 1, 0
	};



	
	
	public static Bitmap changeColor(Bitmap image, int color){

		Log.d("TAGUTAGU", image.toString());

		ColorMatrix cMatrix;

		Bitmap b = Bitmap.createBitmap(image.getWidth(), image.getHeight(),image.getConfig());

		switch(color){
			case 0:
				cMatrix = new ColorMatrix(red_tab);
				break;

			case 1:
				cMatrix = new ColorMatrix(neg_tab);
				break;
			case 2:
				cMatrix = new ColorMatrix();
				cMatrix.setSaturation(0);
				ColorMatrix cm_sepia = new ColorMatrix();
				cm_sepia.setScale(1, 1, 0.8f, 1);
				cMatrix.postConcat(cm_sepia);
				break;
			case 3:
				cMatrix = new ColorMatrix(blue);
				break;
			case 4:
				cMatrix = new ColorMatrix(green);
				break;
			case 5:
			default:
				cMatrix = new ColorMatrix(normal_tab);
				break;

		}
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		Canvas canvas = new Canvas(b);
		canvas.drawBitmap(b, 0, 0, paint);
		return b;
	}

	public static Bitmap applyColorMatrix(Bitmap src, ColorMatrix matrix) {
		Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		Canvas canvas = new Canvas(dest);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColorFilter(new ColorMatrixColorFilter(matrix));
		canvas.drawBitmap(src, 0, 0, paint);
		return dest;
	}

	public static Bitmap setContrastBrightness(Bitmap src, float bt, float ct, float sat) {

		final float[] contrastBrightness = {
				ct, 0, 0, 0, bt,
				0, ct, 0, 0, bt,
				0, 0, ct, 0, bt,
				0, 0, 0, 1, 0
		};

		ColorMatrix cbMatrix = new ColorMatrix(normal_tab);
		cbMatrix.reset();
		cbMatrix.setSaturation(sat);
		cbMatrix.postConcat(new ColorMatrix(contrastBrightness));
		Bitmap ret = applyColorMatrix(src, cbMatrix);
		return ret;
	}
}

