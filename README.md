[![Release](https://jitpack.io/v/ashLikun/CommonAdapter.svg)](https://jitpack.io/#ashLikun/CommonAdapter)

# **CommonAdapter**

万能适配器

## 使用方法

build.gradle文件中添加:

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

并且:

```gradle
dependencies {
     implementation 'com.github.ashLikun.CommonAdapter:adapter:{latest version}'//没有databind
     implementation 'com.github.ashLikun.CommonAdapter:databindadapter:{latest version}'//使用databind
     //是否使用阿里巴巴的VLayou adapter  复杂布局
     compile ('com.alibaba.android:vlayout:1.2.13@aar') {
             transitive = true
         }
     //使用viewbinding
     implementation  androidx.databinding:viewbinding:+
}
```

### 1.用法

    使用前，对于Android Studio的用户，可以选择添加:

            如果使用MultipleAdapter
            需要使用阿里巴巴的库com.alibaba.android:vlayout:版本

            //普通padater
           binding.recyclerView.adapter = CommonAdapter(
                       this, neibuData, ItemViewBinding::class.java,
                       onItemClick = {
                           Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                       },
                   ) {  t ->
                       binding<ItemViewBinding>().apply {
                           textView.text = t?.name
                       }
                   }
           //多种样式
            MultiItemCommonAdapter<MultiItemData>(this,
                       bindings = hashMapOf(
                           "type1".hashCode() to ItemViewBinding::class.java,
                           "type2".hashCode() to ItemView1Binding::class.java
                       ),
                       onItemClick = {
                           Toast.makeText(this, it?.type + "  " + it?.data?.name, Toast.LENGTH_LONG).show()
                       },
                       itemType = { data -> data.type.hashCode() }) {  t ->
                       when (itemViewType) {
                           "type2".hashCode() -> binding<ItemView1Binding>().run {
                               textView.text = t?.type + "  " + t?.data?.name
                           }
                           else -> binding<ItemViewBinding>().run {
                               textView.text = t?.type + "  " + t?.data?.name
                           }
                       }
                   }
            //分段
             binding.recyclerView.adapter = SectionAdapter(
                        this, neibuData, ItemViewBinding::class.java,
                        bndingHead = ItemHeaderBinding::class.java,
                        apply = {
                            onItemClick = {
                                Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                            }
                        },
                        convertHeader = {  t ->
                            binding<ItemHeaderBinding>().run {
                                tvHeader.text = t?.name
                            }
                        }
                    ) {  t ->
                        binding<ItemViewBinding>().run {
                            textView.text = t?.name
                        }
                    }
            //VLayout
             val help by lazy {
                    MultipleAdapterHelp(binding.recyclerView)
                }
            help.adapter.addAdapters(data.map {
                        when (it.type) {
                            "type1" ->
                                CommonAdapter(this, it.datas, ItemView1Binding::class.java,
                                    layoutStyle = LayoutStyle(single = true),
                                    onItemClick = {
                                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                                    }) {  t ->
                                    binding<ItemView1Binding>().run {
                                        textView.setTextColor(0xffff3300.toInt())
                                        textView.text = t?.name
                                    }
                                }
                            "type2" ->
                                CommonAdapter(this, it.datas, ItemView1Binding::class.java,
                                    layoutStyle = LayoutStyle(spanCount = 3),
                                    onItemClick = {
                                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                                    }) {  t ->
                                    binding<ItemView1Binding>().run {
                                        textView.setTextColor(0xff0fff00.toInt())
                                        textView.text = t?.name
                                    }
                                }
                            "type4" ->
                                SectionAdapter(this, it.datas, ItemViewBinding::class.java,
                                    layoutStyle = LayoutStyle(spanCount = 3),
                                    onItemClick = {
                                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                                    },
                                    bndingHead = ItemHeaderBinding::class.java,
                                    convertHeader = {  t ->
                                        binding<ItemHeaderBinding>().run {
                                            tvHeader.text = t?.name
                                        }
                                    }
                                ) {  t ->
                                    binding<ItemViewBinding>().run {
                                        textView.text = t?.name
                                    }
                                }
                            else ->
                                CommonAdapter(this, it.datas, ItemViewBinding::class.java,
                                    onItemClick = {
                                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                                    }) {  t ->
                                    binding<ItemViewBinding>().run {
                                        textView.text = t?.name
                                    }
                                }
                        }
                    })

### 混肴

####

    保证CommonAdapter的footerSize和headerSize字段不被混肴
    #某一变量不混淆
    -keepclasseswithmembers class com.ashlikun.adapter.recyclerview.vlayout.IStartPosition {
        private int footerSize;
        private int  headerSize;
    }
     #某一变量不混淆
    -keepclasseswithmembers class com.ashlikun.adapter.recyclerview.BaseAdapter {
        private int footerSize;
        private int  headerSize;
    }
    #类不混淆
    -keep public class * extends com.ashlikun.adapter.recyclerview.BaseAdapter {*;}

    #Adapter库不混淆
    -keep public class * extends com.ashlikun.adapter.** {*;}

