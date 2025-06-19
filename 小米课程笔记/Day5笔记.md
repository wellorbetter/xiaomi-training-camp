## **Day5  布局与优化**



### **布局**



#### **动态添加布局**

1、拿到rootView里面的最顶层的布局

如果是Fragment

```java
onCreateView

View rootView = inflater.inflate(R.layout.fragment_class, container, false);

LinearLayout parentLayout = rootView.findViewById(R.id.parent_layout);
```

如果是activity

```java
onCreate

LinearLayout mainLayout = findViewById(R.id.main_layout);
```

创建布局并添加(控件类似，但是在new出控件的时候，需要给控件本身指定一些属性, 然后和布局一样指定布局参数(宽高，位置))

new 布局/控件，new LayoutParams设置布局参数

```java
// 创建RelativeLayout

RelativeLayout relativeLayout = new RelativeLayout(getContext());

// 如果是textView的话，这里还可以setText添加属性

RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(

​    RelativeLayout.LayoutParams.WRAP_CONTENT,

​    RelativeLayout.LayoutParams.WRAP_CONTENT);

// 可以添加其他的属性

relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

relativeLayout.setLayoutParams(relativeLayoutParams);



// 将RelativeLayout添加到LinearLayout

parentLayout.addView(relativeLayout);
```





#### **FrameLayout**

帧布局，占位的基础布局，后面的FrameLayout会覆盖前面的FrameLayout





#### **LinearLayout**

线性布局，水平竖直依次排列，有weight指定比例，指定比例的view/viewgroup按照比例划分空间



#### **RelativeLayout**

相对于某个控件(id)/父布局的上下左右对齐



#### **TableLayout**



#### **AbsoluteLayout**



#### **ConstraintLayout**



约束布局，四个方向都可以添加限制，是为了解决布局嵌套而推出的

只需要用一层ConstraintLayout即可达到非常不错的效果

不仅有相对于某个控件和父布局的上下左右对其，还有baseline(文字)对其



角度定位

允许你以度数来指定视图的旋转角度，以及相对于父布局或其他视图的角度对齐



```markdown
layout_constraintCircle：指定视图围绕某个圆的中心定位。

layout_constraintCircleAngle：指定视图相对于圆的角度位置。

layout_constraintCircleRadius：指定圆的半径。

layout_constraintCircleOffset：指定视图在圆周上的偏移量。

layout_constraintWidth_percent和 layout_constraintHeight_percent：这两个属性允许你以百分比的方式设置视图的大小。
```



左右/上下同时限制to parent，设置width/height 为0dp即可实现match_parent



可以设置宽高比  前宽后高

```
app:layout_constraintDimensionRatio="2:1"
```





设置宽高比时，通常需要将视图的宽度或高度设置为 0dp，这样 ConstraintLayout 才会根据约束条件计算视图的实际尺寸。



如果同时设置了 app:layout_constraintDimensionRatio 和具体的尺寸（如 android:layout_width 或 android:layout_height），ConstraintLayout 会优先考虑具体的尺寸。



#### **链**

首链尾链套parent，然后把这些控件/布局像双向链表一样用约束串起来



使用 app:layout_constraintHorizontal_chainStyle 属性来定义水平链的样式。

spread：将链中的视图均匀分布。

spread_inside：将链中的视图均匀分布，但是不包括链的两端。

packed：将链中的视图紧密包装在一起。

layout_constraintVertical_chainStyle 同理



#### **绘制**



view绘制过程： 测量、布局、绘制



布局加载过程：

1、LayoutInflater inflate 加载布局

2、AssetsManager IO加载XML资源

3、createFromTag 根据tag递归创建view

4、对于每个view进行绘制(三个阶段)



#### **布局优化**



`include`/`merge`/`ViewStub` 布局优化



`include`类似于组件这种东西，就是某个xml可以使用`include`引用别的地方写的布局，所以可以把一个大的布局拆成很多个小的，实现重用和解耦



`include`得同时设置宽高



id保持一个/引用和被引用只写一个id



保持一个 id：如果你在被引用的布局中设置了 id，那么在引用该布局的位置就不需要再设置 id。反之亦然。

引用和被引用只写一个 id：在 include 标签中，只需要在引用位置（引用该布局的布局）设置 id，而不需要在被引用的布局中设置 id。



`merge`标签可以优化布局层次结构，减少不必要的嵌套，提高性能。`merge` 标签允许你在一个布局文件中定义多个根视图元素，而不再生成一个额外的根视图容器。这样，在引用该布局文件时，可以将这些元素直接添加到引用它的父布局中。



`ViewStub` 懒加载

```java
viewStub = view.findViewById(R.id.viewStub);

if (inflate == null) {

  inflate = viewStub.inflate();

}
```



#### **GPU 过度绘制**

蓝、绿、淡红、深红





#### **Layout Inspecter 导出布局树**