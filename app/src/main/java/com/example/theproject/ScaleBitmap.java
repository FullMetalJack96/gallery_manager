package com.example.theproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.WindowManager;

public class ScaleBitmap {

	protected Rect displaySize = new Rect();
	
	public static Bitmap bitmapConvert(byte[] data){

		Bitmap iBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap smallBmp = Bitmap.createScaledBitmap(iBitmap, 100, 100,false);
		
		//TODO nie na sztywno!!!!!!!!!
		
		return smallBmp;
	}
}
