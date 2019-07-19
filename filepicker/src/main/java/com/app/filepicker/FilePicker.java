package com.app.filepicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.StyleRes;

import com.app.filepicker.activity.FilePickerActivity;
import com.zhihu.matisse.MimeType;

import java.util.Set;

/**
 * FilePicker
 */

public final class FilePicker {

    private final SelectOptions selectOptions;
    private String chooseType;

    private FilePicker(String chooseType) {
        selectOptions = SelectOptions.getCleanInstance();
        this.chooseType = chooseType;
    }

    public static FilePicker chooseForBrowser() {
        return new FilePicker(SelectOptions.CHOOSE_TYPE_BROWSER);
    }

    public static FilePicker chooseForMimeType() {
        return new FilePicker(SelectOptions.CHOOSE_TYPE_SCAN);
    }

    public static MatissePicker chooseMedia(Set<MimeType> mimeTypes) {
        return new MatissePicker(mimeTypes, true);
    }

    public static MatissePicker chooseMedia(Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        return new MatissePicker(mimeTypes, mediaTypeExclusive);
    }

    public static Set<MimeType> ofAll() {
        return MimeType.ofAll();
    }

    public static Set<MimeType> of(MimeType type, MimeType... rest) {
        return MimeType.of(type, rest);
    }

    public static Set<MimeType> ofImage() {
        return MimeType.ofImage();
    }

    public static Set<MimeType> ofGif() {
        return MimeType.of(MimeType.GIF);
    }

    public static Set<MimeType> ofPng() {
        return MimeType.of(MimeType.PNG);
    }

    public static Set<MimeType> ofMp4() {
        return MimeType.of(MimeType.MP4);
    }

    public static Set<MimeType> ofVideo() {
        return MimeType.ofVideo();
    }

    public static @StyleRes
    int zhihuTheme() {
        return R.style.Matisse_Zhihu;
    }

    public static int draculaheme() {
        return R.style.Matisse_Dracula;
    }

    public FilePicker setMaxCount(int maxCount) {
        selectOptions.maxCount = maxCount;
        if (maxCount <= 1) {
            selectOptions.maxCount = 1;
            selectOptions.isSingle = true;
        } else {
            selectOptions.isSingle = false;
        }
        return this;
    }

    /**
     * 选择文件夹
     *
     * @return
     */
    public FilePicker selectDirectory(OnFilePickerSelectDirListener onFilePickerSelectDirListener) {
        selectOptions.isSelectFile = false;
        selectOptions.onFilePickerSelectDirListener = onFilePickerSelectDirListener;
        return this;
    }

    /**
     * 选择文件
     *
     * @return
     */
    public FilePicker selectFiles(OnFilePickerSelectListener onFilePickerSelectListener) {
        selectOptions.isSelectFile = true;
        selectOptions.pickerSelectListener = onFilePickerSelectListener;
        return this;
    }
    //
    //    public FilePicker setCompressImage(boolean compressImage) {
    //        selectOptions.compressImage = compressImage;
    //        return this;
    //    }

    public FilePicker setTargetPath(String path) {
        selectOptions.targetPath = path;
        return this;
    }

    public FilePicker setTheme(@StyleRes int theme) {
        selectOptions.themeId = theme;
        return this;
    }

    public FilePicker setFileTypes(String... fileTypes) {
        selectOptions.mFileTypes = fileTypes;
        return this;
    }

    public FilePicker setSortType(String sortType) {
        selectOptions.mSortType = sortType;
        return this;
    }

    public FilePicker isSingle() {
        selectOptions.isSingle = true;
        selectOptions.maxCount = 1;
        return this;
    }

    public FilePicker onlyShowImages() {
        selectOptions.onlyShowImages = true;
        return this;
    }

    public FilePicker onlyShowVideos() {
        selectOptions.onlyShowVideos = true;
        return this;
    }

    public FilePicker placeHolder(Drawable placeHolder) {
        selectOptions.placeHolder = placeHolder;
        return this;
    }

    public FilePicker enabledCapture(boolean enabledCapture) {
        selectOptions.enabledCapture = enabledCapture;
        return this;
    }

    public void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtra("data", chooseType);
        context.startActivity(intent);
    }

}
