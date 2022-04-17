package com.example.dict;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SdcardTool {

    private Activity activity;
    private Context context;

    SdcardTool(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    void requestPermission() {
        int hasReadStoragePermission = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED && hasReadStoragePermission == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void savaFileToSD(String filename, String filecontent) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            requestPermission();
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            File file = new File(filename);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            try (FileOutputStream output = new FileOutputStream(filename)) {
                output.write(filecontent.getBytes());
            }
        } else {
            Toast.makeText(context, "sdcard write fail", Toast.LENGTH_SHORT).show();
        }
    }

    public String readFromSD(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            requestPermission();
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            File file = new File(filename);
            if (file.exists()) {
                try (FileInputStream input = new FileInputStream(file)) {
                    byte[] temp = new byte[1024];
                    int len;
                    while ((len = input.read(temp)) > 0) {
                        sb.append(new String(temp, 0, len));
                    }
                }
            }
        } else {
            Toast.makeText(context, "sdcard read fail", Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }

}