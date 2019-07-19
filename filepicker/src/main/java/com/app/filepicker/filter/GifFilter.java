package com.app.filepicker.filter;

import android.content.Context;

import com.app.filepicker.util.ImageType;
import com.app.filepicker.util.UriUtils;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.util.Set;

public class GifFilter extends Filter {
    private IncapableCause incapableCause;

    public GifFilter() { }

    public GifFilter(IncapableCause incapableCause) {
        this.incapableCause = incapableCause;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return null;
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!ImageType.isGif(UriUtils.getPath(context, item.getContentUri()))) {
            if (incapableCause == null) {
                return new IncapableCause(IncapableCause.TOAST, "只能选择Gif图");
            } else {
                return incapableCause;
            }
        }
        return null;
    }

}
