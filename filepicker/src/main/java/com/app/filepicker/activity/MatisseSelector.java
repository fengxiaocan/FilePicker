package com.app.filepicker.activity;

import android.content.Context;
import android.content.Intent;

import com.app.filepicker.OnMediaSelectListener;
import com.app.filepicker.SelectOptions;

public class MatisseSelector {
    static OnMediaSelectListener onMediaSelectListener;

    public MatisseSelector(OnMediaSelectListener onMediaSelectListener) {
        this.onMediaSelectListener = onMediaSelectListener;
    }

    public void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtra("data", SelectOptions.CHOOSE_TYPE_MEDIA);
        context.startActivity(intent);
    }
}
