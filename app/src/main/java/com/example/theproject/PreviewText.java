package com.example.theproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class PreviewText extends View{
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	protected Typeface font;
	protected int strokeColor;
	protected int fillColor;
	protected String text;
	
	public PreviewText(Context context,Typeface font,int strokeColor, int fillColor, String text) {
		super(context);
		this.font = font;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.text = text;
		
		paint.reset();            // czyszczenie
		paint.setAntiAlias(true);    // wygadzanie
		paint.setTextSize(100);        // wielkc fonta
		paint.setTypeface(font); // 
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//ustanienie
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(fillColor);
		canvas.drawText(text, 100, 100, paint);
		//teraz kraam napis
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setColor(strokeColor);
		canvas.drawText(text, 100, 100, paint);
	}

}
