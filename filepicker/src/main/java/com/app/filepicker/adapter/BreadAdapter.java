package com.app.filepicker.adapter;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.filepicker.R;
import com.app.filepicker.model.BreadModel;
import com.evil.recycler.adapter.RecyclerViewAdapter;
import com.evil.recycler.holder.RecyclerViewHolder;
import com.evil.recycler.inface.OnAdapterItemClickListener;

import java.util.List;

/**
 * BreadAdapter
 */

public class BreadAdapter extends RecyclerViewAdapter<BreadModel, BreadAdapter.BaseViewHolder> {

    public BreadAdapter(@Nullable List<BreadModel> data) {
        mDatas = data;
    }

    OnAdapterItemClickListener<BreadModel> getOnItemClickListener() {
        return mOnItemClickListener;
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
        return R.layout.bread_item;
    }

    static class BaseViewHolder extends RecyclerViewHolder<BreadModel> {
        private Button btnBread;
        private ImageButton imbBread;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final RecyclerView.Adapter adapter, BreadModel breadModel,
                final int position)
        {
            btnBread.setText(breadModel.getCurName());
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BreadAdapter breadAdapter = (BreadAdapter) adapter;
                    OnAdapterItemClickListener<BreadModel> onItemClickListener =
                            breadAdapter.getOnItemClickListener();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, breadAdapter.getDatas(), position);
                    }
                }
            };
            imbBread.setOnClickListener(listener);
            btnBread.setOnClickListener(listener);
        }

        @Override
        public void initView(View rootView) {
            btnBread = findViewById(R.id.btn_bread);
            imbBread = findViewById(R.id.imb_bread);
        }
    }
}
