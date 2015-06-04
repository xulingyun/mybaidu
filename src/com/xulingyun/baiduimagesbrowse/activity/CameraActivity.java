package com.xulingyun.baiduimagesbrowse.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xulingyun.baiduimagesbrowse.R;
import com.xulingyun.baiduimagesbrowse.view.MySurfaceView;

public class CameraActivity extends Activity implements SensorEventListener,OnClickListener{
	/** 返回 */
	ImageView back;
	/** 标题 */
	TextView title;
	/** 灯光 */
	Button light;
	/** 切换摄像头 */
	Button switchCamera;
	/** 画面 */
	MySurfaceView mv;
	/** 画面上面的覆盖层 */
	ImageView img_cover;
	/**模式*/
	ImageView mode;
	/** 拍照 */
	ImageView camera;
	/**从相册选*/
	TextView fromAlbums;
	boolean isClicked = false;
	/** 初始化摄像头，0为后置摄像头 */
	int index = 0;
	/** 感应器管理 */
	SensorManager mSensorManager;
	/** 感应器 */
	Sensor mSensor;
	/**拍照进度条*/
	ProgressBar progressBar;
	boolean isLight = false;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		initView();
		setOnclick();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerSensor();
	}

	private void registerSensor() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		light = (Button) findViewById(R.id.light);
		switchCamera = (Button) findViewById(R.id.switchCamera);
		mv = (MySurfaceView) findViewById(R.id.sv);
		img_cover = (ImageView) findViewById(R.id.img_cover);
		mode = (ImageView) findViewById(R.id.mode);
		camera = (ImageView) findViewById(R.id.camera);
		fromAlbums = (TextView) findViewById(R.id.fromAlbums);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		camera.setEnabled(false);
	}
	/**
	 * 初始化事件
	 */
	private void setOnclick(){
		back.setOnClickListener(this);
		title.setOnClickListener(this);
		light.setOnClickListener(this);
		switchCamera.setOnClickListener(this);
		mode.setOnClickListener(this);
		camera.setOnClickListener(this);
		fromAlbums.setOnClickListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor == null) {
			return;
		}
		int x = (int) event.values[0];
		int y = (int) event.values[1];
//		int z = (int) event.values[2];
		if (x >= 4 || x <= -4) {
			if (y >= -4 && y <= 4) {
				if (img_cover.getVisibility() == View.VISIBLE)
					img_cover.setVisibility(View.INVISIBLE);
					camera.setEnabled(true);
			} else {
				if (img_cover.getVisibility() == View.INVISIBLE){
					img_cover.setVisibility(View.VISIBLE);
					camera.setEnabled(false);
				}
			}
		} else {
			if (img_cover.getVisibility() == View.INVISIBLE){
				img_cover.setVisibility(View.VISIBLE);
				camera.setEnabled(false);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		mSensorManager = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			break;
		case R.id.light:
			isLight = !isLight;
			break;
		case R.id.switchCamera:
			if (index == 0) {
				index = 1;
				light.setVisibility(View.INVISIBLE);
			} else {
				index = 0;
				light.setVisibility(View.VISIBLE);
			}
			mv.switchCamera(index);
			break;
		case R.id.camera:
			mv.autoOrPreview(isClicked,isLight,progressBar);
			isClicked = !isClicked;
			break;
		case R.id.fromAlbums:
			Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
//			intent.setType("image/*");
			startActivityForResult(intent, 100); 
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("--------------------requestCode:"+requestCode);
		if(requestCode==100){
			System.out.println("--------------------resultCode:"+resultCode);
			if(resultCode==Activity.RESULT_OK){
				System.out.println("--------------end------:");
				   Uri selectedImage = data.getData();
				   String[] filePathColumns={MediaStore.Images.Media.DATA};
				   Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
				   c.moveToFirst();
				   int columnIndex = c.getColumnIndex(filePathColumns[0]);
				   String picturePath= c.getString(columnIndex);
				   c.close();
				   Intent intent = new Intent(CameraActivity.this,ClipActivity.class);
				   intent.putExtra("ImagePath", picturePath);
				   startActivity(intent);
			}
		}
		
	}

}
