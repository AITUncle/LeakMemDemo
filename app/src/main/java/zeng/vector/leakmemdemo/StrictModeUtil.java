package zeng.vector.leakmemdemo;

import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import java.io.File;

/**
 * Created by vectorzeng on 2017/11/20.
 *
 * StrictMode is not a security mechanism and is not guaranteed to find all disk or network accesses.
 * Notably, disk or network access from JNI calls won't necessarily trigger it. --- JNI的代码无法检测。
 * StrictMode.VmPolicy:
 *  StrictMode policy applied to all threads in the virtual machine's process.
 *
 *  1) detectActivityLeaks --- 虽然检测到了泄漏，但是没有引用链信息
 *  2) detectLeakedClosableObjects
 *  3) detectLeakedSqlLiteObjects
 *
 *  注意：检测的时机是触发GC
 *
 * activity泄漏后的日志输出：
 *      E/StrictMode: class zeng.vector.leakmemdemo.LeakAct; instances=2; limit=1
     android.os.StrictMode$InstanceCountViolation: class zeng.vector.leakmemdemo.LeakAct; instances=2; limit=1
     at android.os.StrictMode.setClassInstanceLimit(StrictMode.java:1)
 *
 */

public class StrictModeUtil {
    private static final String TG = "vz-StrictModeUtil";

    /**
     *
     */
    public static void detectVM(){
        Log.i(TG, "StrictMode setVmPolicy");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        builder.detectActivityLeaks()           //检测Activity的泄漏 -- 无效
                .detectLeakedClosableObjects()  // very good
                .detectLeakedSqlLiteObjects()   // very good
                .penaltyLog().penaltyDeath();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            Log.i(TG, "StrictMode setVmPolicy 1" );
            //检测Activity的泄漏 -- 无效
            builder.detectLeakedRegistrationObjects();//BroadcastReceiver && ServiceConnection
        }
        StrictMode.setVmPolicy(builder.build());
    }


    public static void detectThread(){
        new Thread() {
            @Override
            public void run() {
                Log.i(TG, "StrictMode setThreadPolicy.");
                StrictMode.setThreadPolicy(
                        new StrictMode.ThreadPolicy.Builder()
                                .detectCustomSlowCalls()//发现UI线程调用的哪些方法执行得比较慢
                                .detectDiskReads()
                                .detectDiskWrites()
                                .detectNetwork()
                                //.detectResourceMismatches()
                                .penaltyLog()//处罚策略：输出日志
                                .penaltyDeath()//处罚策略：崩溃
                                .penaltyDialog()//处罚策略：弹窗
                                .build());

                File file = new File(Environment.getExternalStorageDirectory(), "vz.txt");
                Log.i(TG, "StrictMode setThreadPolicy 1 write file " + file);
                FileUtils.writeToFile(file, "vector zeng\n", true);
            }
        }.start();
    }
}
