package zeng.vector.leakmemdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by vectorzeng on 2017/11/21.
 */

public class LeakFragment extends Fragment {
	private static final String TG = "vz-LeakFragment";
	private static ArrayList<LeakFragment> sList = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i(TG, "onCreateView");
		sList.add(this);
		return inflater.inflate(R.layout.fragment_leak, container, false);
	}

	@Override
	public void onDestroy() {
		Log.i(TG, "onDestroy");
		super.onDestroy();
		if(MainActivity.sRefWatcher != null){
			System.gc();
			Log.i(TG, "onDestroy2 watch fragment " + this);
			MainActivity.sRefWatcher.watch(this);
		}
	}
}
