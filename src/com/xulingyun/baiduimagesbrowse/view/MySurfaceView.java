package com.xulingyun.baiduimagesbrowse.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.xulingyun.baiduimagesbrowse.activity.ClipActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, AutoFocusCallback {

	SurfaceHolder holder;
	Camera myCamera;
	boolean isLight;
	Context context;
	ProgressBar progressBar;
	
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		holder = getHolder();// 获得surfaceHolder引用
		holder.addCallback(this);
		// holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置类型
		isLight = false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (myCamera == null) {
			myCamera = Camera.open(0);// 开启相机,不能放在构造函数中，不然不会显示画面.
			myCamera.setDisplayOrientation(90);
			try {
				myCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters params = myCamera.getParameters();
		params.setPictureFormat(ImageFormat.JPEG);
		System.out.println("Quality------------------"+params.getJpegQuality());
		params.setPreviewSize(1280, 720);
		myCamera.setParameters(params);
		myCamera.startPreview();
		myCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myCamera.stopPreview();// 停止预览
		myCamera.release();// 释放相机资源
		myCamera = null;
	}

	private ShutterCallback shutter = new ShutterCallback() {

		@Override
		public void onShutter() {
			Log.d("ddd", "shutter");

		}
	};
	private PictureCallback raw = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				System.out.println("保存图片raw:data,"+data);
				Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
				Matrix matrix = new Matrix();
				matrix.setRotate(180);

				Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
						oldBitmap.getWidth(), oldBitmap.getHeight(), matrix,
						true);

				File file = new File(Environment.getExternalStorageDirectory()
						.getPath()+File.separator + System.currentTimeMillis() + "x.jpg");
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				System.out.println("保存完成");
				progressBar.setVisibility(View.INVISIBLE);
				Intent intent = new Intent(context, ClipActivity.class);
				intent.putExtra("image", data);
				context.startActivity(intent);
				System.out.println("启动完成");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				System.out.println("保存图片");
				Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				Matrix matrix = new Matrix();
				matrix.setRotate(180);

				Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
						oldBitmap.getWidth(), oldBitmap.getHeight(), matrix,
						true);

				File file = new File(Environment.getExternalStorageDirectory()
						.getPath()+File.separator + System.currentTimeMillis() + ".jpg");
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				System.out.println("保存完成");
				progressBar.setVisibility(View.INVISIBLE);
				Intent intent = new Intent(context, ClipActivity.class);
				intent.putExtra("image", data);
				context.startActivity(intent);
				System.out.println("启动完成");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void tackPicture() {
		myCamera.takePicture(shutter, raw, jpeg);
	}

	public void voerTack() {
		myCamera.startPreview();
	}

	public void switchCamera(int index) {
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;// 取消原来摄像头
		myCamera = Camera.open(index);// 打开当前选中的摄像头
		myCamera.setDisplayOrientation(90);
		try {
			myCamera.setPreviewDisplay(holder);
			myCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		if (success) {
			progressBar.setVisibility(View.VISIBLE);
			// 设置参数,并拍照
			Camera.Parameters params = myCamera.getParameters();
			params.setPictureFormat(ImageFormat.JPEG);
			params.setPictureSize(3264, 2448);
			params.setJpegQuality(60);
			params.setPreviewSize(1280, 720);
			if (isLight) {
				params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			} else {
				params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
			myCamera.setParameters(params);
			myCamera.takePicture(null, null, jpeg);
		}
	}

	public void autoOrPreview(boolean b, boolean isLight,ProgressBar progressBar) {
		this.isLight = isLight;
		this.progressBar = progressBar;
		if (!b) {
			myCamera.autoFocus(this);// 自动对焦
		} else {
			myCamera.startPreview();// 开启预览
		}
	}
}
