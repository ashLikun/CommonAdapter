# **CommonAdapter**
万能适配器

### 1.用法
    使用前，对于Android Studio的用户，可以选择添加:
    compile 'com.github.ashLikun.CommonAdapter:adapter:1.1.1'//没有databind
    compile 'com.github.ashLikun.CommonAdapter:databindadapter:1.1.1'//使用databind

            //普通padater
             recyclerView.setAdapter(new CommonAdapter<String>(this, R.layout.item_view, listDatas) {
                 @Override
                 public void convert(ViewHolder holder, String o) {
                 }
             });
             //普通的多种布局
              recyclerView.setAdapter(new MultiItemCommonAdapter<String>(this, listDatas) {
                         @Override
                         public void convert(ViewHolder holder, String o) {

                         }

                         @Override
                         public int getLayoutId(int itemType) {
                             return itemType == 1 ? R.layout.item_view : R.layout.item_view2;
                         }

                         @Override
                         public int getItemViewType(int position, String o) {
                             if (position == 2 || position == 8 || position == 15 || position == 25 || position == 31) {
                                 return 1;
                             }
                             return 2;
                         }
                     });
              //分组Adapter
                recyclerView.setAdapter(new SectionAdapter<String>(this, R.layout.item_view, listDatas) {

                          @Override
                          public void convert(ViewHolder holder, String s) {
                              holder.setText(R.id.textView, s);
                          }

                          @Override
                          public int sectionHeaderLayoutId() {
                              return R.layout.item_view2;
                          }

                          @Override
                          public String getTitle(int position, String s) {
                              if (position % 4 == 0) {
                                  return "标题" + position / 4;
                              }
                              return null;
                          }

                          @Override
                          public void setTitle(ViewHolder holder, String title, String s) {
                              holder.setText(R.id.textView, title);
                          }
                      });

            //使用数据绑定
                   //普通的
                  recyclerView.setAdapter(new CommonBindAdapter<String, ItemViewBinding>(this, R.layout.item_view, listDatas) {
                      @Override
                      public void convert(DataBindHolder<ItemViewBinding> holder, String s) {
                          holder.dataBind.setItemData(s);
                      }
                  });
                  //简易的
                   recyclerView.setAdapter(new EasyBindAdapter(this, R.layout.item_view, listDatas, BR.itemData));
                   //多种列表
                    recyclerView.setAdapter(new MultiItemBindAdapter<String>(this, listDatas) {

                               @Override
                               public void convert(DataBindHolder<ViewDataBinding> holder, String s) {
                                   if (holder.getItemViewType() == 1) {
                                       holder.getDataBind(ItemView1Binding.class).setItemData(s);
                                   } else {
                                   }
                               }

                               @Override
                               public int getLayoutId(int itemType) {
                                   if (itemType == 1) {
                                       return R.layout.item_view1;
                                   } else {
                                       return R.layout.item_view2;
                                   }
                               }

                               @Override
                               public int getItemViewType(int position, String s) {
                                   if (position == 2 || position == 8 || position == 15 || position == 25 || position == 31) {
                                       return 1;
                                   }
                                   return 2;
                               }
                           });
                    //分组的Adapter
                      recyclerView.setAdapter(new SectionAdapter<String, ItemViewBinding>(this, R.layout.item_view, listDatas) {
                                @Override
                                public void convert(DataBindHolder<ItemViewBinding> holder, String s) {
                                    holder.dataBind.setItemData(s);
                                }

                                @Override
                                public int sectionHeaderLayoutId() {
                                    return R.layout.item_view2;
                                }

                                @Override
                                public String getTitle(int position, String s) {
                                    if (position % 4 == 0) {
                                        return "标题" + position / 4;
                                    }
                                    return null;
                                }

                                @Override
                                public void setTitle(DataBindHolder holder, String title, String s) {
                                    holder.setText(R.id.textView, title);
                                }
                            });

### 混肴
####
    保证CommonAdapter的footerSize和headerSize字段不被混肴
    #某一变量不混淆
    -keepclasseswithmembers class com.xxx.xxx {
        private com.ashlikun.adapter.recyclerview.BaseAdapter footerSize;
        private com.ashlikun.adapter.recyclerview.BaseAdapter headerSize;
    }

