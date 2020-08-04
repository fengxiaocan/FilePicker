package com.app.filepicker.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.filepicker.BaseFileFragment;
import com.app.filepicker.R;
import com.app.filepicker.SelectOptions;
import com.app.filepicker.adapter.FileListAdapter;
import com.app.filepicker.loader.EssMimeTypeCollection;
import com.app.filepicker.model.EssFile;
import com.app.filepicker.model.FileScanActEvent;
import com.app.filepicker.model.FileScanFragEvent;
import com.app.filepicker.model.FileScanSortChangedEvent;
import com.evil.recycler.holder.RecyclerViewHolder;
import com.evil.recycler.inface.OnAdapterItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * FileTypeListFragment
 */
public class FileTypeListFragment extends BaseFileFragment
        implements OnAdapterItemClickListener<EssFile>,
        EssMimeTypeCollection.EssMimeTypeCallbacks {

    private static final String ARG_FileType = "ARG_FileType";
    private static final String ARG_IsSingle = "mIsSingle";
    private static final String ARG_MaxCount = "mMaxCount";
    private static final String ARG_SortType = "mSortType";
    private static final String ARG_Loader_Id = "ARG_Loader_Id";

    private String mFileType;
    private boolean mIsSingle;
    private int mMaxCount;
    private int mSortType;
    private int mLoaderId;

    private boolean mSortTypeHasChanged = false;

    private List<EssFile> mSelectedFileList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FileListAdapter mAdapter;
    private EssMimeTypeCollection mMimeTypeCollection = new EssMimeTypeCollection();

    public static FileTypeListFragment newInstance(String param1, boolean IsSingle, int mMaxCount, int mSortType, int loaderId) {
        FileTypeListFragment fragment = new FileTypeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FileType, param1);
        args.putBoolean(ARG_IsSingle, IsSingle);
        args.putInt(ARG_MaxCount, mMaxCount);
        args.putInt(ARG_SortType, mSortType);
        args.putInt(ARG_Loader_Id, loaderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFileType = getArguments().getString(ARG_FileType);
            mIsSingle = getArguments().getBoolean(ARG_IsSingle);
            mMaxCount = getArguments().getInt(ARG_MaxCount);
            mSortType = getArguments().getInt(ARG_SortType);
            mLoaderId = getArguments().getInt(ARG_Loader_Id);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_file_type_list;
    }

    @Override
    protected void lazyLoad() {
        mMimeTypeCollection.load(mFileType, mSortType,mLoaderId);
    }

    @Override
    protected void initUI(View view) {
        mMimeTypeCollection.onCreate(getActivity(), this);
        EventBus.getDefault().register(this);
        mRecyclerView = view.findViewById(R.id.rcv_file_list_scan);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FileListAdapter(new ArrayList<EssFile>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(LayoutInflater.from(getContext())
                                            .inflate(R.layout.loading_layout, mRecyclerView,
                                                    false));
        mAdapter.setOnItemClickListener(this);
        super.initUI(view);
    }

    /**
     * 接收到Activity刷新最大数量后
     *
     * @param event event
     */
    @Subscribe
    public void onFreshCount(FileScanActEvent event) {
        mMaxCount = event.getCanSelectMaxCount();
    }

    /**
     * 接收到Activity改变排序方式后
     */
    @Subscribe
    public void onFreshSortType(FileScanSortChangedEvent event) {
        mSortType = event.getSortType();
        if(mLoaderId == event.getCurrentItem()+EssMimeTypeCollection.LOADER_ID){
            mMimeTypeCollection.load(mFileType, mSortType,mLoaderId);
        }else {
            mSortTypeHasChanged = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            if(!isFirstLoad && mSortTypeHasChanged){
                mSortTypeHasChanged = false;
                mAdapter.clearAndNotify();
                mMimeTypeCollection.load(mFileType, mSortType,mLoaderId);
            }
        }
    }

    /**
     * 查找文件位置
     */
    private int findFileIndex(EssFile item) {
        for (int i = 0; i < mSelectedFileList.size(); i++) {
            if (mSelectedFileList.get(i).getAbsolutePath().equals(item.getAbsolutePath())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mMimeTypeCollection.onDestroy();
    }

    @Override
    public void onFileLoad(List<EssFile> essFileList) {
        mAdapter.setEmptyView(LayoutInflater.from(getContext())
                                            .inflate(R.layout.empty_file_list, mRecyclerView, false));
        mAdapter.setDatasAndNotify(essFileList);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onFileReset() {

    }

    @Override
    public void onItemClick(View view, RecyclerViewHolder<EssFile> holder, int position) {
        List<EssFile> list = holder.getAdapterDatas();
        EssFile item = list.get(position);
        //选中某文件后，判断是否单选
        if (mIsSingle) {
            mSelectedFileList.add(item);
            EventBus.getDefault().post(new FileScanFragEvent(item, true));
            return;
        }
        if (list.get(position).isChecked()) {
            int index = findFileIndex(item);
            if (index != -1) {
                mSelectedFileList.remove(index);
                EventBus.getDefault().post(new FileScanFragEvent(item, false));
                list.get(position).setChecked(!list.get(position).isChecked());
                mAdapter.notifyItemChanged(position, "");
            }
        } else {
            if (mMaxCount <= 0) {
                //超出最大可选择数量后
                Snackbar.make(mRecyclerView,
                        "您最多只能选择" + SelectOptions.getInstance().maxCount + "个。",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }
            mSelectedFileList.add(item);
            EventBus.getDefault().post(new FileScanFragEvent(item, true));
            list.get(position).setChecked(!list.get(position).isChecked());
            mAdapter.notifyItemChanged(position, "");
        }
    }

}
