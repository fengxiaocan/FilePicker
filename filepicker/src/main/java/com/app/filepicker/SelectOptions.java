package com.app.filepicker;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import com.app.filepicker.util.FileUtils;

import java.io.File;

/**
 * SelectOptions
 */

public class SelectOptions {

    public static final String CHOOSE_TYPE_BROWSER = "choose_type_browser";
    public static final String CHOOSE_TYPE_SCAN = "choose_type_scan";
    public static final String CHOOSE_TYPE_MEDIA = "choose_type_media";

    public static final String defaultTargetPath =
            Environment.getExternalStorageDirectory() + "/essPictures";

    public String[] mFileTypes;
    public String mSortType;
    public boolean isSingle = false;
    public int maxCount = 10;
    public boolean onlyShowImages = false;
    public boolean onlyShowVideos = false;
    public boolean enabledCapture = false;
    public OnFilePickerSelectListener pickerSelectListener;
    public OnFilePickerSelectDirListener onFilePickerSelectDirListener;
    public boolean isSelectFile = true;//是否选择文件
    public Drawable placeHolder;
//    public boolean compressImage = false;
    public String targetPath = defaultTargetPath;
    public int themeId = R.style.FilePicker_Elec;

    public static SelectOptions getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectOptions getCleanInstance() {
        SelectOptions options = getInstance();
        options.reset();
        return options;
    }

    public String[] getFileTypes() {
        if (mFileTypes == null || mFileTypes.length == 0) {
            return new String[]{};
        }
        return mFileTypes;
    }

    public int getSortType() {
        if (TextUtils.isEmpty(mSortType)) {
            return FileUtils.BY_NAME_ASC;
        }
        return Integer.valueOf(mSortType);
    }

    public void setSortType(int sortType) {
        mSortType = String.valueOf(sortType);
    }

    public String getTargetPath() {
        if (!new File(targetPath).exists()) {
            File file = new File(defaultTargetPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return defaultTargetPath;
        }
        return targetPath;
    }

    private void reset() {
        mFileTypes = new String[]{};
        mSortType = String.valueOf(FileUtils.BY_NAME_ASC);
        isSingle = false;
        maxCount = 10;
        onlyShowImages = false;
        onlyShowVideos = false;
        enabledCapture = false;
//        compressImage = true;
        themeId = R.style.FilePicker_Elec;
        pickerSelectListener = null;
        onFilePickerSelectDirListener = null;
    }

    private static final class InstanceHolder {
        private static final SelectOptions INSTANCE = new SelectOptions();
    }

}
