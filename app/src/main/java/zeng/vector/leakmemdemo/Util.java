package zeng.vector.leakmemdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by vectorzeng on 2017/11/19.
 */

public class Util {
    private static final String TG = "vz-Util";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 检查应用程序是否允许写入存储设备
     * 如果应用程序不允许那么会提示用户授予权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static boolean writeToFileNoClose(File file, String content, boolean append){
        if(TextUtils.isEmpty(content) || file == null || file.isDirectory()){
            Log.e(TG, "content " + content + ", file " + file);
            return false;
        }
        FileWriter fileWriter;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            fileWriter.write("over \n");
            fileWriter.flush();
            Log.i(TG, content + " >> " + file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
        return false;
    }

    public static void leakThread(){
        new Thread("LeakThread"){
            @Override
            public void run() {
                super.run();
                Log.i(TG, "leakThread.begin.loop");
                Looper.prepare();
                Looper.loop();
                Log.i(TG, "leakThread.end");
            }
        }.start();
    }
    static class TestObj {
        int id = 0;

        public TestObj(int id) {
            this.id = id;
        }
    }

    public static void testWeakReference(View view){
        Object o = new TestObj(100);
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        SoftReference<Object> softReference = new SoftReference<>(o, referenceQueue);

        Log.i(TG, "testWeakReference1.1 obj: " + softReference.get());
        Log.i(TG, "testWeakReference1.2 referenceQueue: " + referenceQueue.toString());
        o = null;
        System.gc();
        Log.i(TG, "testWeakReference2.1 obj: " + softReference.get());
        Log.i(TG, "testWeakReference2.2 referenceQueue: " + referenceQueue.toString());
    }

    



}
