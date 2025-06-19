### Day1：

课程：

##### Android 历史



##### Android 五层架构，印象不深刻



1. Linux内核层
2. 系统应用层
3. C++\C native层 Android Run Environment 层
4. Android Framwork 框架层 Activity等、ServiceManager等
5. 硬件抽象层



#### git的使用

1. git add .
2. git commit -m ""
3. git push -u master



##### Application：配置全局信息，生命周期最长

extends Application，具有一些生命周期函数(包括OnCreate，可以写一些个人化初始信息)，需要在manifest文件application name属性指定(会自动生成提示，选择需要的Application)



![image-20240601164714229](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240601164714229.4jnxyul080.webp)



build之后生成数字签名，然后指定打包类型debug/release，生成即可



adb：安卓调试桥 adb intstall .apk 安装apk文件 还有其他一些命令，之前用adb找过Activity的栈



#### 作业：运行hello world



![image-20240601164419855](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240601164419855.7egm4n06zj.webp)

#### adb安装release.apk



![image-20240601163406910](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240601163406910.8hgbfiw0v1.webp)

