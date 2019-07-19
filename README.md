# FilePicker
一个选择文件或者文件夹的库,集成知乎的Matisse图片选择库

依赖:Tag-->[![](https://jitpack.io/v/fengxiaocan/FilePicker.svg)](https://jitpack.io/#fengxiaocan/FilePicker)

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.fengxiaocan:FilePicker:Tag'
	}


###1.文件浏览器:

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
                                
##2.选择文件夹

        FilePicker.chooseForBrowser().selectDirectory(new OnFilePickerSelectDirListener() {
                      @Override
                      public void onFilePickerResult(String directory) {
                          
                      }
                  }).start(this);
                        
                        
##3.选择筛选文件

        FilePicker.chooseForMimeType().setTheme(R.style.FilePicker_Dracula).setMaxCount(10)
                   .setFileTypes("png", "doc", "apk", "mp3", "gif", "txt", "mp4", "zip")
                   .selectFiles(new OnFilePickerSelectListener() {
                       @Override
                       public void onFilePickerResult(List<EssFile> essFiles) {
                           for (EssFile essFile : essFiles) {
                               LogUtils.debug("noah", essFile.getAbsolutePath());
                           }
                       }
                   }).start(this);
                   
##4.知乎图片选择库(已集成当前回调方式,可以在dialog或其他非application context 中打开选择图片或视频)
#    集成需要添加知乎图片选择库的依赖,默认集成Glide4.9版本
    `implementation 'com.zhihu.android:matisse:latest.release'`
    
#    可以使用下面的用法跟知乎的一样,或者 [Matisse](https://github.com/zhihu/Matisse)

        FilePicker.chooseMedia(FilePicker.ofImage()).theme(FilePicker.zhihuTheme())
                  .addFilter(new GifFilter()).showSingleMediaType(true)
                  .setOnSelectResultListener(list -> {
                  }).start(context);

