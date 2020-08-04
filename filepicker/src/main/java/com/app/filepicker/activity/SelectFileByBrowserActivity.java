package com.app.filepicker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialoglib.RxAlertDialog;
import com.app.filepicker.R;
import com.app.filepicker.SelectOptions;
import com.app.filepicker.adapter.BreadAdapter;
import com.app.filepicker.adapter.FileListAdapter;
import com.app.filepicker.adapter.SelectSdcardAdapter;
import com.app.filepicker.model.BreadModel;
import com.app.filepicker.model.EssFile;
import com.app.filepicker.model.EssFileCountCallBack;
import com.app.filepicker.model.EssFileListCallBack;
import com.app.filepicker.task.EssFileCountTask;
import com.app.filepicker.task.EssFileListTask;
import com.app.filepicker.util.Const;
import com.app.filepicker.util.FileUtils;
import com.app.filepicker.util.LogUtils;
import com.evil.recycler.holder.RecyclerViewHolder;
import com.evil.recycler.inface.OnAdapterItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件浏览界面
 */
public class SelectFileByBrowserActivity extends AppCompatActivity
        implements OnAdapterItemClickListener<EssFile>,
        View.OnClickListener, EssFileListCallBack, EssFileCountCallBack,
        FileListAdapter.onLoadFileCountListener {

    /*todo 是否可预览文件，默认可预览*/
    private boolean mCanPreview = true;

    /*当前目录，默认是SD卡根目录*/
    private String mCurFolder =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /*所有可访问存储设备列表*/
    private List<String> mSdCardList;

    private FileListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView mBreadRecyclerView;
    private ImageView mImbSelectSdCard;
    private Toolbar mToolBar;
    private BreadAdapter mBreadAdapter;
    private PopupWindow mSelectSdCardWindow;

    /*是否刚才切换了SD卡路径*/
    private boolean mHasChangeSdCard = false;
    /*已选中的文件列表*/
    private ArrayList<EssFile> mSelectedFileList = new ArrayList<>();
    /*当前选中排序方式的位置*/
    private int mSelectSortTypeIndex = 0;
    private MenuItem mCountMenuItem;

    private EssFileListTask essFileListTask;
    private EssFileCountTask essFileCountTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        mSdCardList = FileUtils.getAllSdPaths(this);
        if (!mSdCardList.isEmpty()) {
            mCurFolder = mSdCardList.get(0) + File.separator;
        }
        initUi();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_menu, menu);
        mCountMenuItem = menu.findItem(R.id.browser_select_count);
        if (SelectOptions.getInstance().isSelectFile) {
            mCountMenuItem.setTitle(String.format(getString(R.string.selected_file_count),
                    String.valueOf(mSelectedFileList.size()),
                    String.valueOf(SelectOptions.getInstance().maxCount)));
        } else {
            mCountMenuItem.setTitle("选择当前文件夹");
        }
        return true;
    }

    private void initUi() {
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        if (SelectOptions.getInstance().isSelectFile) {
            getSupportActionBar().setTitle("文件选择");
        } else {
            getSupportActionBar().setTitle("文件夹选择");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = findViewById(R.id.rcv_file_list);
        mBreadRecyclerView = findViewById(R.id.breadcrumbs_view);
        mImbSelectSdCard = findViewById(R.id.imb_select_sdcard);
        mImbSelectSdCard.setOnClickListener(this);
        if (!mSdCardList.isEmpty() && mSdCardList.size() > 1) {
            mImbSelectSdCard.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileListAdapter(new ArrayList<EssFile>());
        mAdapter.setLoadFileCountListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(
                LayoutInflater.from(this).inflate(R.layout.empty_file_list, mRecyclerView, false));

        mAdapter.setOnItemClickListener(this);

        mBreadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBreadAdapter = new BreadAdapter(new ArrayList<BreadModel>());
        mBreadRecyclerView.setAdapter(mBreadAdapter);
        //        mBreadAdapter.setOnItemChildClickListener(this);
        mBreadAdapter.setOnItemClickListener(new OnAdapterItemClickListener<BreadModel>() {
            @Override
            public void onItemClick(View view, RecyclerViewHolder<BreadModel> holder, int position) {
                LogUtils.error("noah", "mBreadAdapter");
                //点击某个路径时
                String queryPath = FileUtils.getBreadModelListByPosition(mSdCardList, holder.getAdapterDatas(),
                        position);
                if (mCurFolder.equals(queryPath)) {
                    return;
                }
                executeListTask(mSelectedFileList, queryPath,
                        SelectOptions.getInstance().getFileTypes(),
                        SelectOptions.getInstance().getSortType());
            }
        });
    }

    private void initData() {
        executeListTask(mSelectedFileList, mCurFolder, SelectOptions.getInstance().getFileTypes(),
                SelectOptions.getInstance().getSortType());
    }

    private void executeListTask(List<EssFile> essFileList, String queryPath, String[] types,
                                 int sortType) {
        essFileListTask = new EssFileListTask(essFileList, queryPath, types, sortType, this);
        essFileListTask.execute();
    }

    /**
     * 查找到文件列表后
     *
     * @param queryPath 查询路径
     * @param fileList  文件列表
     */
    @Override
    public void onFindFileList(String queryPath, List<EssFile> fileList) {
        mCurFolder = queryPath;
        mAdapter.setDatasAndNotify(fileList);
        List<BreadModel> breadModelList = FileUtils.getBreadModeListFromPath(mSdCardList,
                mCurFolder);
        if (mHasChangeSdCard) {
            mBreadAdapter.setDatasAndNotify(breadModelList);
            mHasChangeSdCard = false;
        } else {
            if (breadModelList.size() > mBreadAdapter.getDatas().size()) {
                //新增
                List<BreadModel> newList = BreadModel.getNewBreadModel(mBreadAdapter.getDatas(),
                        breadModelList);
                mBreadAdapter.addDatasAndNotify(newList);
            } else {
                //减少
                int removePosition = BreadModel.getRemovedBreadModel(mBreadAdapter.getDatas(),
                        breadModelList);
                if (removePosition > 0) {
                    mBreadAdapter.setDatasAndNotify(
                            mBreadAdapter.getDatas().subList(0, removePosition));
                }
            }
        }

        mBreadRecyclerView.smoothScrollToPosition(mBreadAdapter.getItemCount() - 1);
        //先让其滚动到顶部，然后再scrollBy，滚动到之前保存的位置
        mRecyclerView.scrollToPosition(0);
        int scrollYPosition = mBreadAdapter.getDatas().get(mBreadAdapter.getDatas().size() - 1)
                .getPrePosition();
        //恢复之前的滚动位置
        mRecyclerView.scrollBy(0, scrollYPosition);
    }

    /**
     * 显示选择SdCard的PopupWindow
     * 点击其他区域隐藏，阴影
     */
    private void showPopupWindow() {
        if (mSelectSdCardWindow != null) {
            mSelectSdCardWindow.showAsDropDown(mImbSelectSdCard);
            return;
        }
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_select_sdcard, null);
        mSelectSdCardWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mSelectSdCardWindow.setFocusable(true);
        mSelectSdCardWindow.setOutsideTouchable(true);
        RecyclerView recyclerView = popView.findViewById(R.id.rcv_pop_select_sdcard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SelectSdcardAdapter adapter = new SelectSdcardAdapter(
                FileUtils.getAllSdCardList(mSdCardList));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnAdapterItemClickListener<String>() {
            @Override
            public void onItemClick(View view, RecyclerViewHolder<String> holder, int position) {
                LogUtils.error("noah", "mSelectSdCardWindow");
                mSelectSdCardWindow.dismiss();
                mHasChangeSdCard = true;
                executeListTask(mSelectedFileList,
                        FileUtils.getChangeSdCard(holder.getAdapterDatas().get(position), mSdCardList),
                        SelectOptions.getInstance().getFileTypes(),
                        SelectOptions.getInstance().getSortType());
            }
        });
        mSelectSdCardWindow.showAsDropDown(mImbSelectSdCard);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    @Override
    public void onFindChildFileAndFolderCount(int position, String childFileCount,
                                              String childFolderCount) {
        mAdapter.getDatas().get(position).setChildCounts(childFileCount, childFolderCount);
        mAdapter.notifyItemChanged(position, "childCountChanges");
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
    public void onBackPressed() {
        if (!FileUtils.canBackParent(mCurFolder, mSdCardList)) {
            super.onBackPressed();
            return;
        }
        executeListTask(mSelectedFileList, new File(mCurFolder).getParentFile().getAbsolutePath() +
                        File.separator,
                SelectOptions.getInstance().getFileTypes(),
                SelectOptions.getInstance().getSortType());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (essFileListTask != null) {
            essFileListTask.cancel(true);
        }
        if (essFileCountTask != null) {
            essFileCountTask.cancel(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imb_select_sdcard) {
            showPopupWindow();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.browser_select_count) {
            if (!SelectOptions.getInstance().isSelectFile) {
                Intent result = new Intent();
                result.putExtra(Const.EXTRA_RESULT_SELECTION_FOLDER, mCurFolder);
                setResult(RESULT_OK, result);
                finish();
                return true;
            }
            //选中
            if (mSelectedFileList.isEmpty()) {
                return true;
            }
            //不为空
            Intent result = new Intent();
            result.putParcelableArrayListExtra(Const.EXTRA_RESULT_SELECTION, mSelectedFileList);
            setResult(RESULT_OK, result);
            super.onBackPressed();
        } else if (i == R.id.browser_sort) {
            //排序

            new ADBuilder(this).setSingleChoiceItems(R.array.sort_list, 0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectSortTypeIndex = which;
                        }
                    }).setNegativeButton("降序", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (mSelectSortTypeIndex) {
                        case 0:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_NAME_DESC);
                            break;
                        case 1:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_TIME_ASC);
                            break;
                        case 2:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_SIZE_DESC);
                            break;
                        case 3:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_EXTENSION_DESC);
                            break;
                    }
                    //恢复排序
                    mBreadAdapter.getDatas().get(mBreadAdapter.getDatas().size() - 1)
                            .setPrePosition(
                                    0);
                    executeListTask(mSelectedFileList, mCurFolder,
                            SelectOptions.getInstance().getFileTypes(),
                            SelectOptions.getInstance().getSortType());
                }
            }).setPositiveButton("升序", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (mSelectSortTypeIndex) {
                        case 0:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_NAME_ASC);
                            break;
                        case 1:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_TIME_DESC);
                            break;
                        case 2:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_SIZE_ASC);
                            break;
                        case 3:
                            SelectOptions.getInstance().setSortType(FileUtils.BY_EXTENSION_ASC);
                            break;
                    }
                    //恢复排序
                    mBreadAdapter.getDatas().get(mBreadAdapter.getDatas().size() - 1)
                            .setPrePosition(
                                    0);
                    executeListTask(mSelectedFileList, mCurFolder,
                            SelectOptions.getInstance().getFileTypes(),
                            SelectOptions.getInstance().getSortType());
                }
            }).setTitle("请选择").show();

        }
        return true;
    }

    @Override
    public void onLoadFileCount(int position) {
        essFileCountTask = new EssFileCountTask(position,
                mAdapter.getDatas().get(position).getAbsolutePath(),
                SelectOptions.getInstance().getFileTypes(), this);
        essFileCountTask.execute();
    }

    @Override
    public void onItemClick(View view, RecyclerViewHolder<EssFile> holder, int position) {
        List<EssFile> list = holder.getAdapterDatas();
        EssFile item = list.get(position);
        if (item.isDirectory()) {
            //点击文件夹
            //保存当前的垂直滚动位置
            mBreadAdapter.getDatas().get(mBreadAdapter.getDatas().size() - 1).setPrePosition(
                    mRecyclerView.computeVerticalScrollOffset());
            executeListTask(mSelectedFileList, mCurFolder + item.getName() + File.separator,
                    SelectOptions.getInstance().getFileTypes(),
                    SelectOptions.getInstance().getSortType());
        } else {
            if (!SelectOptions.getInstance().isSelectFile) {
                //选择文件夹
                return;
            }
            //选中某文件后，判断是否单选
            if (SelectOptions.getInstance().isSingle) {
                Intent result = new Intent();
                result.putParcelableArrayListExtra(Const.EXTRA_RESULT_SELECTION, mSelectedFileList);
                setResult(RESULT_OK, result);
                finish();
                return;
            }
            if (list.get(position).isChecked()) {
                int index = findFileIndex(item);
                if (index != -1) {
                    mSelectedFileList.remove(index);
                }
            } else {
                if (mSelectedFileList.size() >= SelectOptions.getInstance().maxCount) {
                    //超出最大可选择数量后
                    Snackbar.make(mRecyclerView,
                            "您最多只能选择" + SelectOptions.getInstance().maxCount + "个。",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mSelectedFileList.add(item);
            }
            list.get(position).setChecked(!list.get(position).isChecked());
            mAdapter.notifyItemChanged(position, "");
            mCountMenuItem.setTitle(String.format(getString(R.string.selected_file_count),
                    String.valueOf(mSelectedFileList.size()),
                    String.valueOf(SelectOptions.getInstance().maxCount)));
        }
    }
}
