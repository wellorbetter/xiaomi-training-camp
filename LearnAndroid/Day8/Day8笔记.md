## Day8笔记

#### View的绘制

1、measure  测量 (计算宽高)

2、layout       计算位置

3、draw          绘制

viewgroup递归遍历调用子view的绘制（逐级向下，树状结构）

#### 坐标系

左上角为原点，右边->X轴正向，下边->y轴正向

只需要知道view的左上角和右下角即可知道view的位置以及宽高

### 自定义VIew

#### Draw函数

优先考虑重写onDraw

有七个绘制步骤

#### onDraw函数

第三步绘制内容时调用

onDraw的设计模式



**模板方法模式（Template Method Pattern）**：

- `onDraw` 是 `View` 类的一个方法，`View` 类定义了绘制的基本结构，而具体的绘制细节由子类通过重写 `onDraw` 方法来实现。
- 模板方法模式定义了一个操作中的算法骨架，而将一些步骤延迟到子类中。它使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

#### 绘制

- 绘制onDraw会提供Canvas画布，如果想绘制东西还需要一个画笔`Paint mPaint;`
- 可以绘制几何图形、文本，设置绘制的颜色和样式
- 可以创建、操作图层
- 可以使用路径绘制复杂图形
- 可以绘制位图
- 可以对画布进行平移、旋转、偏移等



### 自定义ViewGroup

#### measure 函数(final，不可重写)

计算view的宽高，onMeasure完成真正的计算，子类应该重写onMeasure

#### onMeasure函数

计算view的宽高，应由子类重写，必须调用setMeasuredDimension(width, height)存储视图(约定)

##### MeasureSpec

用一个int位保存了两个值

高2位 测量模式SpecMode 

低30位 尺寸SpecSizse

封装好了，可以封箱和拆箱

###### SpecMode

`UNSPECIFIED`     `父View对子View不做限制`

`EXACTLY `             `父View计算好了子View的大小，最终大小就是SpecSize的值`

`AT_MOST`             `父View指定了可用的SpecSize，子View不能超过它`



在布局时需要填写控件的布局重参数layout_width, layout_height

MATCH_PARENT/WARP_CONTENT/精确值



子View的布局参数(childView.getLayoutParams)+父View的测量要求=子View的MeasureSpec

#### layout函数

给所有子View分配位置，派生类不应该覆盖这个方法，有子View的派生类应该覆盖onLayout

#### onLayout函数

为布局的子View分配位置

有子View的派生类应该重写它，并为每个子View调用layout()方法

通常是在测量过程中存储子View的宽高实现的（分配位置）

### 响应手势操作

**MotionEvent: ACTION （事件）+ 事件位置**



手势由多个MotionEvent组合而成

down + move +  up/cancel

##### DestrueDetector类

重写onTouchEvent方法，将事件给DestrueDetector处理

**事件分发机制**

##### ScaleGestureDetector类

缩放手势工具类，类似DestrueDetector

##### ViewDragHelper

拖拽