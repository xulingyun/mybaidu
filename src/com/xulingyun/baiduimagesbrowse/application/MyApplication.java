package com.xulingyun.baiduimagesbrowse.application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
//		File cacheDir = StorageUtils.getOwnCacheDirectory(
//				getApplicationContext(), "UniversalImageLoader/Cache");
		// ImageLoaderConfiguration configuration =
		// ImageLoaderConfiguration.createDefault(this);
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(4)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.memoryCache(new FIFOLimitedMemoryCache(10 * 1024 * 1024))
				.diskCacheFileCount(5)
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(configuration);
	}

}
