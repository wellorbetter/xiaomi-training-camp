

#### 作业6

实现网络请求动态上拉加载下拉刷新，EventBus传递消息

完成作业比较晚，因为写网络请求出现了亿点点bug（使用的知乎日报api）

![文件结构](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240607004437958.3go8vlb2gq.webp)

DetailActivity即点击图片、文字跳转到对应的页面，根据点击的数据格式会显示对应的格式

MainActivity即入口，HomePage即主页，PersonalPage即不用完成的个人主页。

ViewPagerAdapter即实现和MainActivity的ViewPager+BottomNavigationView底部导航的适配器

Items实现了MultiItemEntity，以便用于多布局结合使用（Image+Text），HomePageAdapter使用了BaseRecyclerViewAdapterHelper组件

网络请求使用OKhttp，异步请求封装在NewsService里面，DateUtil即每次上拉和下滑的时候会对天数进行处理，封装了一个工具类，ConvertUtil就是把知乎日报的数据拔下来转成作业需要的格式（写处理逻辑写太久了，没gpt估计还写不完）

使用Glide动态加载图片

页面大致如下![主页](https://github.com/wellorbetter/picx-images-hosting/raw/master/237ef4884d30f6f6d8cbb29ee54c251.969l764wwf.webp)

![跳转页面](https://github.com/wellorbetter/picx-images-hosting/raw/master/cac4525af94802ca311901dc2cc5fd3.99t74vxyi3.webp)