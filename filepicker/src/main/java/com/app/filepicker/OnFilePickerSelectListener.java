package com.app.filepicker;

import com.app.filepicker.model.EssFile;

import java.util.List;

public interface OnFilePickerSelectListener {
    void onFilePickerResult(List<EssFile> essFiles);
}
