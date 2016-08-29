# Pixiv-Android
第三方Android客户端，api从Pixiv android端中抓取

架构为mvp+dagger+rxjava+retrofit，mvp采用mosby，目录结构采用 CleanArchitecture
* data 层，定义数据与api
* domain 层，定义获取数据的操作
* presentation 层，View 和 Presenter

### 2016-08-29
完成首页数据获取操作，图片显示添加模糊效果（先下载小图模糊显示，然后再显示大图）

### 2016-08-28
创建下拉刷新的Fragment模板

### 2016-08-26
搭建基础框架
