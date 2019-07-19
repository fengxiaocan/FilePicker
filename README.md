# FilePicker
一个选择文件或者文件夹的库,集成知乎的Matisse图片选择库
1.文件浏览器:
              FilePicker.chooseForBrowser().setMaxCount(2).setFileTypes("png", "doc", "apk",
                        "mp3", "gif", "txt", "mp4", "zip").selectFiles(
                        new OnFilePickerSelectListener() {
                            @Override
                            public void onFilePickerResult(List<EssFile> essFiles) {
                                for (EssFile essFile : essFiles) {
                                    LogUtils.debug("noah", essFile.getAbsolutePath());
                                }
                            }
                        }).start(this);
