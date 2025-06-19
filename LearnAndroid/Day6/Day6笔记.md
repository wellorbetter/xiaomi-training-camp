### Day6 笔记



#### 组件库

组件：AAR(可以看源码)/JAR(不能看源码)



new module 选择Library，在library里面可以写一些类，然后在app里面可以引用所写的类(直接写，然后在类名上alt+enter自动implementation(project(mapOf("path" to ":mylibrary")))，也可以手动加)



在library的build.gradle 加入下面这个 (爆红sync一下就好)


```
id("maven-publish")   （这个放在plugins下面）

publishing {   （这个放在dependencies同级
  publications {
    register<MavenPublication>("release") {
      groupId = "com.xiaomi.lib"
      artifactId = "image"
      version = "1.0.0"
      afterEvaluate {
        artifact(tasks.getByName("bundleReleaseAar"))
      }
    }
  }
  repositories {
    maven {
      name = "myRepo"
      url = uri("${project.buildDir}/repo")
    }
  }
}
gradle moduleName:publishToMavenLocal



```

mavenLocal()放在全局的setting.gradle

#### 图片库

Glide 在build.gradle添加依赖

```
implementation("com.github.bumptech.glide:glide:4.16.0")

```

高斯模糊（插件）

```
implementation("jp.wasabeef:glide-transformations:4.3.0")
```

#### EventBus 事件传递组件

```
implementation("org.greenrobot:eventbus:3.3.1")
```

post和postSticky的区别

post

找到当前线程的发布事件队列，将当前事件对象加入到队列中
如果当前线程为开始发布，则开启while循环，将队列中的所有事件发布，若已经在发布中了则不用理会
事件发布先根据当前事件类型找到其所有订阅信息（包含订阅者和订阅方法）集合，然后对集合中所有订阅信息进行事件发布
每个订阅信息的事件发布根据不同的threadMode切换到不同线程，通过反射调用订阅信息里的调用者的订阅方法

postSticky

将当前粘性事件保存下来，等后面注册该粘性事件的订阅者注册时发送给他（而不是一直发送）
调用post发送事件



效果就是sticky可以让某个在事件发送之后的activity拿到这个信息



 #### 滑动刷新框架

##### 瀑布流



##### 适配器组件

BaseRecyclerViewAdapter

```java
implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.14")
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
```

只需要

```java
public class HelperAdapter extends BaseQuickAdapter<Item, BaseViewHolder> 
```

继承之后往里面传入数据和布局	

```java
public HelperAdapter(int layoutResId, @Nullable List<Item> data) {
	super(layoutResId, data);
}
```

在convert里面用数据对传入布局进行一些适配

```java
@Override
protected void convert(@NonNull BaseViewHolder baseViewHolder, Item item) {
    baseViewHolder.setText(R.id.tv, item.getText());
    baseViewHolder.setImageDrawable(R.id.iv, item.getDrawable());
}
```

#### 下拉刷新

拦截事件：触发下拉滑动，纵向滑动距离>0，根据实际的y轴增量进行业务处理并进行异步加载（UI线程更新UI）

##### SwipeRefreshLayout

也是一个布局，需要放在RecyclerView外面，然后设置id，给activity拿到，拿到之后就可以进行下拉监听

setOnRefreshListener之后实现onRefresh，在onRefresh里面使用handler异步加载数据并提示更新，然后设置刷新为false

```java
 @Override
 public void onRefresh() {
     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             items.add(0, new Item(getDrawable(R.drawable.img), "新数据"));
             adapter.notifyDataSetChanged();
             swipe_layout.setRefreshing(false);
         }
     },200);
 }
```



#### 上拉加载

BaseQuickAdapter实现LoadMoreModule

然后adapter初始化loadMore的一些设置（setAdapter之前）

然后setOnLoadMoreListener，可以设置是否加载到底

对应的需要设置adapter.getLoadMoreModule().loadMoreComplete();表示当前这次下拉是否结束

```java
  public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int start = items.size();
                for (int i = 0; i < 10; i++) {
                    items.add(new Item(getDrawable(R.drawable.img), "新数据"));
                }
                adapter.notifyItemRangeInserted(start, 10);
                adapter.getLoadMoreModule().loadMoreComplete();
                // Check if there are more items to load, if not, call loadMoreEnd()
                if (items.size() >= 50) { // Example condition for ending load more
                    adapter.getLoadMoreModule().loadMoreEnd();
                }
            }
        }, 200);
    }
```

#### PullToRefresh

支持ListView，不支持RecyclerView

AsyncTask

**leakcanary** 内存泄漏

