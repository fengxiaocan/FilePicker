package com.app.filepicker;

import android.net.Uri;

import java.util.List;

public interface OnMediaSelectListener {
    void onPickerResult(List<Uri> list);
}
