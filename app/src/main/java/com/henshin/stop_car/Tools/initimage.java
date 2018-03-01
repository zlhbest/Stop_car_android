package com.henshin.stop_car.Tools;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;
import java.io.IOException;

/**
 * Created by wangkeke on 2016/6/1.
 * 备注：
 */
public class initimage
{
    // 初始化图片处理
    @SuppressWarnings("deprecation")
    public static void initImageLoader(Context context)
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if(!f.exists())
        {
            try
            {
                f.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
//        File f = this.getExternalCacheDir();

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(new WeakMemoryCache()).discCache(new UnlimitedDiskCache(f))
                        // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .imageDownloader(new BaseImageDownloader(context, 5 * 10000, 30 * 1000))
                        .defaultDisplayImageOptions(defaultOptions)
                        .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
