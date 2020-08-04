package com.app.filepicker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.filepicker.SelectOptions;
import com.app.filepicker.model.EssFile;
import com.app.filepicker.util.Const;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.ui.MatisseActivity;

import java.util.ArrayList;
import java.util.List;

public class FilePickerActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 0x123;
    private static final int REQUEST_CODE_CHOOSE_MEDIA = 0x124;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getStringExtra("data");
        Intent intent = new Intent();
        if (data.equals(SelectOptions.CHOOSE_TYPE_BROWSER)) {
            intent.setClass(this, SelectFileByBrowserActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE);
        } else if (data.equals(SelectOptions.CHOOSE_TYPE_SCAN)) {
            intent.setClass(this, SelectFileByScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE);
        } else if (data.equals(SelectOptions.CHOOSE_TYPE_MEDIA)) {
            intent.setClass(this, MatisseActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_MEDIA);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK) {
                if (SelectOptions.getInstance().isSelectFile) {
                    ArrayList<EssFile> essFileList = data.getParcelableArrayListExtra(
                            Const.EXTRA_RESULT_SELECTION);
                    if (SelectOptions.getInstance().pickerSelectListener != null) {
                        SelectOptions.getInstance().pickerSelectListener.onFilePickerResult(
                                essFileList);
                    }
                } else {
                    String path = data.getStringExtra(Const.EXTRA_RESULT_SELECTION_FOLDER);
                    if (SelectOptions.getInstance().onFilePickerSelectDirListener != null) {
                        SelectOptions.getInstance().onFilePickerSelectDirListener
                                .onFilePickerResult(path);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE_MEDIA) {
            if (resultCode == RESULT_OK) {
                if (MatisseSelector.onMediaSelectListener != null) {
                    List<Uri> result = Matisse.obtainResult(data);
                    MatisseSelector.onMediaSelectListener.onPickerResult(result);
                }
                MatisseSelector.onMediaSelectListener = null;
            }
        }
        SelectOptions.getCleanInstance();
        finish();
    }
}
