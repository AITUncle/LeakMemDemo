package zeng.vector.leakmemdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 严苛模式：
 * 现场策略：是针对当前现场的策略。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TG = "vz-MainActivity";
    public static RefWatcher sRefWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_stick_vm:
                StrictModeUtil.detectVM();
                Toast.makeText(this, "StrictMode 启动", Toast.LENGTH_SHORT).show();
                return;
            case R.id.btn_install_leakcanary:
                if(sRefWatcher == null) {
                    sRefWatcher = LeakCanary.install(MyApp.sApp);
                    Toast.makeText(this, "LeakCanary 启动", Toast.LENGTH_SHORT).show();
                }
                return;
            case R.id.btn_test_weak_reference:
                Util.testWeakReference(v);
                return;
            case R.id.btn_start_leak_act:
                startActivity(new Intent(this, LeakAct.class));
                return;
        }

    }
}
