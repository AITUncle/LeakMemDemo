package zeng.vector.leakmemdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * Created by vectorzeng on 2017/11/19.
 */

public class LeakAct extends FragmentActivity implements View.OnClickListener{
    private static final String TG = "vz-LeakAct";
    private static LeakAct self;
    private LeakFragment leakFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_leak);
        LeakSingleon.getInstance().init(this);
        self = this;
        Log.i(TG, "onCreate " + self);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TG, "onDestroy delay");
            }
        }, 60000);
    }

    private LeakFragment createLeakFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("leak_fragment");
        if(fragment == null){
            fragment = new LeakFragment();
        }
        return (LeakFragment) fragment;
    }

    public void onClick(View v) {
        int id = v.getId();
        Log.i(TG, "onClick " + id);
        switch (id){
            case R.id.btn_closeable_leak:
                File f = new File(Environment.getExternalStorageDirectory(), "vz.txt");
                boolean ioRet = Util.writeToFileNoClose(f, "abc---\nadb", false);
                Log.i(TG, "onClick 1 " + ioRet);
                return;

            case R.id.btn_thread_leak:
                Util.leakThread();
                return;

            case R.id.btn_show_leak_fragment: {
                if(leakFragment == null) {
                    leakFragment = createLeakFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_fg, leakFragment, "leak_fragment");
                    ft.commit();
                }else{
                    Log.e(TG, "show leak fragment error :leakFragment!=null");
                }
                return;
            }
            case R.id.btn_remove_leak_fragment: {
                if(leakFragment != null) {
                    Log.i(TG, "onClick btn_remove_leak_fragment");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(leakFragment);
                    ft.commit();
                    leakFragment = null;
                }else{
                    Log.e(TG, "remove leak fragment error :leakFragment==null");
                }
                return;
            }
            case R.id.btn_regiest_broadcast:
                Log.i(TG, "onClick 2");
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("vector");
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                    }
                }, intentFilter);
                return;
        }
    }
}
