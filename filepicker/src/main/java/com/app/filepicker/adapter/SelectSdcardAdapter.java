package com.app.filepicker.adapter;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.filepicker.R;
import com.evil.recycler.adapter.RecyclerViewAdapter;
import com.evil.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * SelectSdcardAdapter
 */

public class SelectSdcardAdapter extends
        RecyclerViewAdapter<String, SelectSdcardAdapter.BaseViewHolder> {
    public SelectSdcardAdapter(@Nullable List<String> data) {
        setDatas(data);
    }

    @Override
    public boolean attachParent() {
        return false;
    }

    @Override
    public BaseViewHolder createViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }

    @Override
    public int onCreateLayoutRes(int viewType) {
        return R.layout.item_select_sdcard;
    }

    static class BaseViewHolder extends RecyclerViewHolder<String> {
        private TextView tvItemSelectSdcard;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(RecyclerView.Adapter adapter, String s, int position) {
            tvItemSelectSdcard.setText(s);
        }

        @Override
        public void initView(View rootView) {
            tvItemSelectSdcard = findViewById(R.id.tv_item_select_sdcard);
        }
    }
}
