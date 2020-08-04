package com.app.filepicker.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.app.filepicker.R;
import com.app.filepicker.model.EssFile;
import com.app.filepicker.util.FileSizeUtil;
import com.app.filepicker.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evil.recycler.adapter.RecyclerViewAdapter;
import com.evil.recycler.holder.BaseRecyclerHolder;
import com.evil.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * FileListAdapter
 */

public class FileListAdapter
        extends RecyclerViewAdapter<EssFile, FileListAdapter.BaseViewHolder> {


    private onLoadFileCountListener loadFileCountListener;

    public FileListAdapter(@Nullable List<EssFile> data) {
        setDatas(data);
    }

    @Override
    public boolean attachParent() {
        return true;
    }

    @Override
    public BaseViewHolder createViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }

    @Override
    public int onCreateLayoutRes(int viewType) {
        return R.layout.item_file_list;
    }

    public onLoadFileCountListener getLoadFileCountListener() {
        return loadFileCountListener;
    }

    public void setLoadFileCountListener(onLoadFileCountListener loadFileCountListener) {
        this.loadFileCountListener = loadFileCountListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).setLoadFileCountListener(loadFileCountListener);
        }
        super.onBindViewHolder(holder, position);
    }


    public interface onLoadFileCountListener {
        void onLoadFileCount(int posistion);
    }

    static class BaseViewHolder extends RecyclerViewHolder<EssFile> {
        private ImageView ivItemFileSelectLeft;
        private TextView tvItemFileList;
        private TextView tvItemFileListDesc;
        private AppCompatCheckBox checkboxItemFileList;
        private ImageView ivItemFileSelectRight;
        private onLoadFileCountListener loadFileCountListener;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }


        public void setLoadFileCountListener(onLoadFileCountListener loadFileCountListener) {
            this.loadFileCountListener = loadFileCountListener;
        }

        @Override
        public void onBindData(EssFile item) {
            if (item.isDirectory()) {
                ivItemFileSelectRight.setVisibility(View.VISIBLE);
                if (item.getChildFolderCount().equals("加载中")) {
                    //查找数量
                    if (loadFileCountListener != null) {
                        loadFileCountListener.onLoadFileCount(getDataPosition());
                    }
                }
                tvItemFileListDesc.setText(
                        String.format(getContext().getResources().getString(R.string.folder_desc),
                                item.getChildFileCount(), item.getChildFolderCount()));
            } else {
                ivItemFileSelectRight.setVisibility(View.GONE);
                tvItemFileListDesc.setText(
                        String.format(getContext().getResources().getString(R.string.file_desc),
                                FileUtils.getDateTime(item.getAbsolutePath()),
                                FileSizeUtil.getAutoFileOrFilesSize(item.getFile())));
            }
            tvItemFileList.setText(item.getName());
            if (item.isChecked()) {
                checkboxItemFileList.setVisibility(View.VISIBLE);
            } else {
                checkboxItemFileList.setVisibility(View.GONE);
            }
            String fileNameExtension = FileUtils.getExtension(item.getName()).toLowerCase();
            switch (fileNameExtension) {
                case "apk":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.apk);
                    break;
                case "avi":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.avi);
                    break;
                case "doc":
                case "docx":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.doc);
                    break;
                case "exe":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.exe);
                    break;
                case "flv":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.flv);
                    break;
                case "gif":
                    RequestOptions options = new RequestOptions().centerCrop().placeholder(
                            R.mipmap.gif);
                    Glide.with(getContext()).load(item.getAbsolutePath()).apply(options).into(
                            ivItemFileSelectLeft);
                    break;
                case "jpg":
                case "jpeg":
                case "png":
                    RequestOptions options2 = new RequestOptions().centerCrop().placeholder(
                            R.mipmap.png);
                    Glide.with(getContext()).load(item.getAbsolutePath()).apply(options2).into(
                            ivItemFileSelectLeft);
                    break;
                case "mp3":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.mp3);
                    break;
                case "mp4":
                case "f4v":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.movie);
                    break;
                case "pdf":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.pdf);
                    break;
                case "ppt":
                case "pptx":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.ppt);
                    break;
                case "wav":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.wav);
                    break;
                case "xls":
                case "xlsx":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.xls);
                    break;
                case "zip":
                    ivItemFileSelectLeft.setImageResource(R.mipmap.zip);
                    break;
                case "ext":
                default:
                    if (item.isDirectory()) {
                        ivItemFileSelectLeft.setImageResource(R.mipmap.folder);
                    } else {
                        ivItemFileSelectLeft.setImageResource(R.mipmap.documents);
                    }
                    break;
            }
        }

        @Override
        public void initView(View rootView) {
            ivItemFileSelectLeft = findViewById(R.id.iv_item_file_select_left);
            tvItemFileList = findViewById(R.id.tv_item_file_list);
            tvItemFileListDesc = findViewById(R.id.tv_item_file_list_desc);
            checkboxItemFileList = findViewById(R.id.checkbox_item_file_list);
            ivItemFileSelectRight = findViewById(R.id.iv_item_file_select_right);
        }
    }

}
