package com.app.test;


import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.filepicker.FilePicker;
import com.app.filepicker.OnFilePickerSelectListener;
import com.app.filepicker.filter.GifFilter;
import com.app.filepicker.model.EssFile;
import com.app.filepicker.util.LogUtils;

import java.security.MessageDigest;
import java.security.Security;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtFile;
    private Button mBtFind;
    private Button mBtImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x123);
        }
        final String content = "我希望我所作的事情对别人有所帮助！";

        Set<String> messageDigest = Security.getAlgorithms("Cipher");

        for (String key : messageDigest) {
            Log.e("noah",key);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log(EncryptMold.MD5 ,content);
//                log(EncryptMold.SHA1,content);
//                log(EncryptMold.SHA224,content);
//                log(EncryptMold.SHA256,content);
//                log(EncryptMold.SHA384,content);
//                log(EncryptMold.SHA512,content);
//            }
//        }).start();
    }

    public static void log(EncryptMold mold,String content){
        Log.e("noah", mold.algorithm + "加密:" + mold.encryptToHex(content));
    }

    private void initView() {
        mBtFile = (Button) findViewById(R.id.bt_file);
        mBtFind = (Button) findViewById(R.id.bt_find);
        mBtImage = (Button) findViewById(R.id.bt_image);

        mBtFile.setOnClickListener(this);
        mBtFind.setOnClickListener(this);
        mBtImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_file:
                FilePicker.chooseForBrowser().setMaxCount(2).setFileTypes("png", "doc", "apk",
                        "mp3", "gif", "txt", "mp4", "zip").selectFiles(
                        new OnFilePickerSelectListener() {
                            @Override
                            public void onFilePickerResult(List<EssFile> essFiles) {
                                for (EssFile essFile : essFiles) {
                                    LogUtils.debug("noah", essFile.getAbsolutePath());
                                }
                            }
                        }).start(this);
                break;
            case R.id.bt_find:
                FilePicker.chooseForMimeType().setTheme(R.style.FilePicker_Dracula).setMaxCount(10)
                        .setFileTypes("png", "doc", "apk", "mp3", "gif", "txt", "mp4", "zip")
                        .selectFiles(new OnFilePickerSelectListener() {
                            @Override
                            public void onFilePickerResult(List<EssFile> essFiles) {
                                for (EssFile essFile : essFiles) {
                                    LogUtils.debug("noah", essFile.getAbsolutePath());
                                }
                            }
                        }).start(this);
                break;
            case R.id.bt_image:
                FilePicker.chooseMedia(FilePicker.ofImage()).theme(FilePicker.zhihuTheme())
                        .addFilter(new GifFilter()).showSingleMediaType(true)
                        .setOnSelectResultListener(list -> {
                            for (Uri uri : list) {
                                LogUtils.error("noah", uri.getPath());
                            }
                        }).start(this);
                break;
        }
    }
}
