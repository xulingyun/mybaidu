package com.xulingyun.baiduimagesbrowse.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xulingyun.baiduimagesbrowse.R;
import com.xulingyun.baiduimagesbrowse.view.MySurfaceView;

public class CameraActivity extends Activity{
	ImageView iv;
	MySurfaceView mv;
	ImageView switchCamera;
	boolean isClicked = false;
	int index=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mv = (MySurfaceView) findViewById(R.id.sv);
		iv = (ImageView) findViewById(R.id.paizhao);
		switchCamera = (ImageView) findViewById(R.id.camera);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mv.autoOrPreview(isClicked);
				isClicked = !isClicked;
			}
		});
		
		switchCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index==0)
					index = 1;
				else
					index=0;
				mv.switchCamera(index);
			}
		});
	}
}
