# **CommonAdapter**
万能适配器

### 1.用法
使用前，对于Android Studio的用户，可以选择添加:
     compile 'com.github.ashLikun.CommonAdapter:adapter:1.0.2'//没有databind
     compile 'com.github.ashLikun.CommonAdapter:adapterDatabind:1.0.2'//使用databind

### 混肴
####
    保证CommonAdapter的footerSize和headerSize字段不被混肴
    #某一变量不混淆
    -keepclasseswithmembers class com.xxx.xxx {
        private com.ashlikun.adapter.recyclerview.CommonAdapter footerSize;
        private com.ashlikun.adapter.recyclerview.CommonAdapter headerSize;
    }

