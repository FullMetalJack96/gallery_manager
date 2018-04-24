package com.example.theproject;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	Camera _camera;
	SurfaceHolder _surfaceHolder;
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		// TODO Auto-generated constructor stub
		this._camera = camera;
		this._surfaceHolder = this.getHolder();
		this._surfaceHolder.addCallback(this);
		this._surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			_camera.setPreviewDisplay(_surfaceHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_camera.setDisplayOrientation(90);
		_camera.startPreview();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			_camera.setPreviewDisplay(_surfaceHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_camera.startPreview();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (_camera != null) {
			_camera.stopPreview();
			_camera.release();
			_camera = null;
			}

	}

	

}
