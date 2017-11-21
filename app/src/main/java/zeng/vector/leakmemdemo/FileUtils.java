package zeng.vector.leakmemdemo;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileUtils {
    private static final String TAG = "vz-FileUtils";

    public static boolean writeToFile(File file, String content, boolean append){
        if(TextUtils.isEmpty(content) || file == null || file.isDirectory()){
            Log.e(TAG, "content " + content + ", file " + file);
            return false;
        }
        FileWriter fileWriter = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
