package com.app.filepicker;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SDUtils {

    /**
     * 获取挂载的sd卡目录
     *
     * @return
     */
    public static List<File> getStorageList() {
        File directory = Environment.getExternalStorageDirectory();
        List<File> files = new ArrayList<>();
        files.add(directory);
        File storage = getStorage(directory);
        File[] listFiles = storage.listFiles();
        if (listFiles != null && listFiles.length > 1) {
            for (File file : listFiles) {
                if (!directory.getAbsolutePath().contains(file.getAbsolutePath())) {
                    File[] files1 = file.listFiles();
                    if (files1 != null) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    /**
     * 获取SD卡外层路径
     *
     * @param file
     * @return
     */
    private static File getStorage(File file) {
        File parentFile = file.getParentFile();
        File[] files = parentFile.listFiles();
        if (files == null || files.length <= 1) {
            return getStorage(parentFile);
        } else {
            return parentFile;
        }
    }
}
