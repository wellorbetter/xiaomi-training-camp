\### **Day4**



\#### **Android UI**



id 标识符



\### **像素 (Pixel)**

像素是显示设备上最小的图像构成单元。每个像素可以显示一个颜色，许多像素组成一幅完整的图像。



\### **分辨率 (Resolution)**

分辨率是指屏幕横向和纵向上像素的总数。例如，1920x1080表示屏幕横向有1920个像素，纵向有1080个像素。分辨率决定了屏幕的清晰度。



\### **屏幕尺寸 (Screen Size)**

屏幕尺寸通常指显示设备对角线的物理长度，通常以英寸为单位测量。例如，手机屏幕的尺寸可能是6英寸。



\#### **计算方法：**

屏幕尺寸可以使用勾股定理计算：



\[ \text{对角线尺寸} = \sqrt{(\text{宽度}^2 + \text{高度}^2)} \]



例如，如果屏幕的宽度是5英寸，高度是3英寸，那么屏幕尺寸就是：



\[ \sqrt{5^2 + 3^2} = \sqrt{25 + 9} = \sqrt{34} \approx 5.83 \text{英寸} \]



\### **DPI (Dots Per Inch)**

DPI表示每英寸包含的像素点数量，是衡量屏幕像素密度的指标。DPI越高，屏幕显示的图像越细腻。



\#### **计算方法：**

DPI可以通过以下公式计算：



\[ \text{DPI} = \frac{\sqrt{(\text{横向像素数}^2 + \text{纵向像素数}^2)}}{\text{对角线尺寸}} \]



例如，对于1920x1080分辨率、5英寸对角线的屏幕：



\[ \text{DPI} = \frac{\sqrt{1920^2 + 1080^2}}{5} = \frac{\sqrt{3686400 + 1166400}}{5} = \frac{\sqrt{4852800}}{5} \approx \frac{2202.91}{5} \approx 440.58 \text{DPI} \]



\### **DP (Density-independent Pixel)**

DP（密度无关像素）是安卓开发中的一个单位，用来解决不同分辨率设备上的界面适配问题。1 dp 在大部分设备上约等于 1/160 英寸。与物理像素不同，DP会根据设备的DPI进行相应调整。



\#### **计算方法：**

DP的计算涉及设备的屏幕密度（DPI）。公式如下：



\[ \text{DP} = \frac{\text{像素}}{(\text{DPI} / 160)} \]



例如，如果某个元素在设备上有320像素宽，设备的DPI是320：



\[ \text{DP} = \frac{320}{(320 / 160)} = \frac{320}{2} = 160 \text{DP} \]



\### **SP (Scale-independent Pixel)**

SP（比例无关像素）类似于DP，但它主要用于字体的尺寸设定。SP不仅考虑屏幕的DPI，还考虑用户的字体缩放偏好，确保字体在不同设备上的可读性一致。



\#### **计算方法：**

SP的计算与DP类似，但它还考虑用户的字体缩放偏好。假设用户的字体缩放系数是1.2，那么SP的计算公式如下：



\[ \text{SP} = \text{DP} \times \text{缩放系数} \]



例如，如果某个文本元素的DP值是16，字体缩放系数是1.2：



\[ \text{SP} = 16 \times 1.2 = 19.2 \text{SP} \]



\### **Margin和Padding的区别**

\- ***\*****Margin*****\***：用于控制视图与其他视图或父容器之间的外部间距。

\- ***\*****Padding*****\***：用于控制视图内容与视图边框之间的内部间距。



\#### **图示**

\```

+-----------------------------+

|    Margin (外边距)    |

|  +-----------------------+  |

|  |  Padding (内边距)   |  |

|  |  +-------------------+|  |

|  |  |          ||  |

|  |  |   Content    ||  |

|  |  |          ||  |

|  |  +-------------------+|  |

|  +-----------------------+  |

+-----------------------------+

\```



\### **封装资源**



在Android开发中，为了更好地管理和复用样式，可以将样式定义在`styles.xml`文件中。这里是一个示例，展示了如何封装TextView的样式：



\#### **定义样式**



在`res/values/styles.xml`文件中定义样式：

style只能指定一个，TextAppearance可以指定多个





\#### **text跑马灯**

需要获取焦点 requestFocus() ellipsize:marquee



\#### **SpannableString富文本**



\```java

TextView textView = **findViewById**(R.id.textView);

String text = "This is a spannable string";



*// 创建SpannableString*

SpannableString spannableString = new **SpannableString**(text);



*// 设置部分文本的颜色*

ForegroundColorSpan colorSpan = new **ForegroundColorSpan**(Color.RED);

spannableString.**setSpan**(colorSpan, 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



*// 设置部分文本的样式为粗体*

StyleSpan boldSpan = new **StyleSpan**(Typeface.BOLD);

spannableString.**setSpan**(boldSpan, 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



*// 应用SpannableString到TextView*

textView.**setText**(spannableString);

\```



\#### **EditText**

软键盘遮挡问题

设置 windowSoftInputMode 属性

`adjustResize`：当软键盘出现时，Activity的主窗口会被调整大小，以确保输入框不会被遮挡。

`adjustPan`：当软键盘出现时，Activity的主窗口不会调整大小，而是通过滑动使输入框可见



\#### **ImageView**

缩放



`centerInside` 会将图像按比例缩小或放大，使得整个图像能够显示在ImageView中，并且图像的宽度和高度都不会超过ImageView的宽度和高度。如果图像的宽度和高度都小于ImageView的宽度和高度，则不会进行缩放

`Center Crop`会将图像放大或缩小到充满整个ImageView，并且保持图像的宽高比例。ImageView会完全填充，但可能会裁剪图像的部分内容。

`Fit Center`会将图像按比例放大或缩小，以便整个图像能够显示在ImageView中，并且保持图像的宽高比例。

`Matrix`允许你通过矩阵来自定义缩放和平移。通常需要通过代码来设置Matrix对象，进行更高级的图像变换。

注：：centerInside 确保图像的宽度和高度都不会超过 ImageView 的宽度和高度，而 fitCenter 则是让图像的一个边（宽或高）充满 ImageView



\#### **Button**

四种button



\#### **Bitmap(位图)**

画架   



\#### **ProgressBar进度条**



最大进度值

最大进度值表示进度条的最大值，通常用来定义进度条的范围。可以通过 android:max 属性设置。

第一进度值

第一进度值表示当前的主要进度，可以通过 android:progress 属性设置。

第二进度值

第二进度值表示次要的进度值，通常用于显示缓冲进度。可以通过 android:secondaryProgress 属性设置。



\#### **SeekBar**

允许用户通过滑动来选择一个数值



setOnSeekBarChangeListener onProgressChanged可以拿到进度并进行操作





\#### **屏幕适配方案**



\#### **列表**



RecycleView



RecyclerView.Adapter<RVAdapter.RVViewHolder>

ViewHolder：保存变量防止重复创建（保存每个item的控件）



创建每个item的layout，传入adapter数据，使其适配item的layout

onCreateViewHolder里面用LayoutInflater来给加载每个item加载layout（即加载对应的控件，同时保存在viewholder里面）



onBindViewHolder，即保存到viewholder之后，即可拿到对应的控件进行操作



notifyItemInserted/notifyItemRemoved

在某个位置添加或者删除item，并通知进行重排