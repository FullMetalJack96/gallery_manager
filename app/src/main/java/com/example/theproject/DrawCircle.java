package com.example.theproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DrawCircle extends View{
 
	protected Rect displaySize = new Rect();
	protected float desiredCircleRadius = 0.0f;
	protected Context mContext;


	public DrawCircle(Context context) {
		super(context);
		this.mContext = context;

		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getRectSize(displaySize);

		if (displaySize.height() > displaySize.width())
			desiredCircleRadius = displaySize.width() * 0.4f;
		else desiredCircleRadius = displaySize.height() * 0.4f;
		// TODO Auto-generated constructor stub
	}

	
@Override
protected void onDraw(Canvas canvas) {
	// TODO Auto-generated method stub
	super.onDraw(canvas);

	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	paint.setAntiAlias(true);
	paint.setStyle(Paint.Style.STROKE);
	paint.setStrokeWidth(2.0f);
	paint.setColor(Color.argb(255, 255, 255, 255));
	canvas.drawCircle(displaySize.centerX(), displaySize.centerY(), desiredCircleRadius/2, paint);
}

	public Point getPointOnCircle(double angleRadians) {

		Point returned = new Point();
		int calculatedX = (int) (displaySize.centerX() + (desiredCircleRadius/2 * Math.cos(angleRadians)));
		int calculatedY = (int) (displaySize.centerY() + (desiredCircleRadius/2 * Math.sin(angleRadians)));

		returned.set(calculatedX, calculatedY);

		return returned;
	}
	
}
