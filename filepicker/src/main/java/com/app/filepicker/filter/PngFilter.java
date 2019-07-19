package com.app.filepicker.filter;

import android.content.Context;

import com.app.filepicker.util.ImageType;
import com.app.filepicker.util.UriUtils;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.util.Set;

public class PngFilter extends Filter {
    private IncapableCause incapableCause;

    public PngFilter() { }

    public PngFilter(IncapableCause incapableCause) {
        this.incapableCause = incapableCause;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return null;
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!ImageType.isPng(UriUtils.getPath(context, item.getContentUri()))) {
            if (incapableCause == null) {
                return new IncapableCause(IncapableCause.TOAST, "只能选择PNG格式的图片");
            } else {
                return incapableCause;
            }
        }
        return null;
    }

}
