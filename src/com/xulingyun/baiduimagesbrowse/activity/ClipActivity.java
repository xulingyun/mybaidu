package com.xulingyun.baiduimagesbrowse.activity;

import com.xulingyun.baiduimagesbrowse.R;
import com.xulingyun.baiduimagesbrowse.view.ClipImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ClipActivity extends Activity {
	
	ClipImageView clipImageView;
	ImageView iv;
	Bitmap mBitmap;
	Intent intent;
	byte[] data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);
		getWindow().setBackgroundDrawable(null);
		clipImageView = (ClipImageView) findViewById(R.id.clipimage);
//		clipImageView.setDrawable(getResources().getDrawable(R.drawable.a1), 300,300);
//		mBitmap= clipImageView.getCropImage();
		intent = getIntent();
		data = intent.getByteArrayExtra("image");
		Bitmap bitmap =BitmapFactory.decodeByteArray(data, 0, data.length);
		clipImageView.setBackground(new BitmapDrawable(bitmap));
	}
	
}
