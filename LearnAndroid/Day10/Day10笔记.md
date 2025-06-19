# Day10笔记



## 内存泄漏



### JAVA垃圾回收机制

#### 结构

虚拟机栈         栈帧           方法1(局部变量表 操作栈 动态连接 方法出口)  方法2.....

本地方法栈    类似虚拟机栈  只不过是本地方法的栈帧

程序计数器     每个线程隔离，用来记录当期线程执行的位置

堆区                 申请的内存，用来存储引用型变量的实际值

元空间             常量池、类信息、方法信息，会将虚拟机栈中所有栈帧的局部变量表的值都放到常量池里面（存一个备份），JDK7之后，会存一个引用，指向这个局部变量

编译产物



一般在内存空闲或者内存占用过高时对那些没有任何引用的对象不定时地进行回收

主要是回收堆

#### 引用计数算法

对象创建时会初始化一个应用计数器，如果被引用了+1，引用失效了-1，直到为0，就意味着不再引用，就可以进行回收了

优点：判定简单，效率高

缺点：无法回收循环引用

#### 可达性分析法

从GCRoot开始，如果不可达某个对象，就标识为不可达对象，就可以进行回收

#### 引用类型

强引用，只要被引用就不能被回收，只有置空才会被回收

软引用，内存不足的时候才会试图去回收软引用指向的对象

弱引用，GC回收时扫到了会被回收

虚引用

### 内存泄漏介绍

已经动态分配的堆内存由于某种原因，程序未释放或无法释放，造成系统内存的浪费

特征：隐蔽性、积累性

#### 内存泄漏的危害

泄漏积累到内存都被消耗完就会导致卡顿

如果程序在运行的时候出现了内存泄漏导致进程使用的内存超过分配的上限，就会被系统kill掉

#### 内存泄漏的原因

短生命周期的对象被长生命周期的对象持有，导致其无法被回收，造成了内存泄漏

Android中哪些对象被长生命周期对象持有会导致内存泄漏?

1、Activity

2、Fragment

3、View

4、Service

#### Android Profiler工具

Profiler-MEMORY：多次重复进入同一个页面，关闭页面后多次触发GC观察内存是否回到初始水平（也不尽然，有些activity 可能需要做一些消耗的操作，释放可能需要时间）

**activity某个方法里面实现了回调，会导致它引用activity，这样如何解决？**

​	在 Android 开发中，使用回调方法时可能会导致对 `Activity` 的引用，从而引起内存泄漏。如果一个对象（例如一个回调）持有对 `Activity` 的强引用，而该 `Activity` 又需要被销毁，则可能会导致 `Activity` 无法被垃圾回收，从而引发内存泄漏。

​	使用 `WeakReference` 持有对 `Activity` 的引用，这样可以避免强引用导致的内存泄漏。

### 内存泄漏检测工具-LeakCanary

基于MAT

```
debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.10")
releaseImplementation ("com.squareup.leakcanary:leakcanary-android-no-op:2.10")
```

1、单例模式引用了Activity、Fragment的Context

​	使用getApplicationContext()

2、匿名内部类隐式持有外部类的引用

​	比如：callback内部类，然后它生命周期比较长，可能在一分钟之后调用，但是activity 已经销毁了，但是我的callback存了它的引用。

 	解决方法：把匿名内部类转换为实现该方法方式，避免匿名内部类隐式持有外部类的引用

3、内部类隐形持有外部类引用

将内部类改成静态内部类，如果需要使用activity或者fragment，配合WeakReference解决内存泄漏

4、Timer、EventBus、BroudCast等需要反注册或者cancel()

在onDestry()/onDestroyView()中反注册/cancel()

5、Handler内存泄漏

静态内部类+WeakReferenve，同时onDestroy中removeCallback

6、WebView导致内存泄漏

​	Activity onDestroy中移除WebView并销毁



#### Hprof文件

java堆上所有内存使用信息

### ANR 问题

#### 产生原因

UI未响应用户/耗时过长

#### ANR类型

按键/触摸事件在特定时间内无响应   5s  有的也许是8s

广播在规定时间内无法处理完成         前台广播10s  后台广播60s

Service在规定时间内无法处理完成操作   前台Service20s，后台服务200s

#### 案例

主线程执行耗时，大量计算、循环、递归和IO

主线程死锁，当前对象锁被其他线程持有，主线程长时间无法获取当前对象锁

使用ReentrantLock代替synchronized，同时tryLock增加最大等待时间，超过最大等待时间主动释放锁

主进程	跨进程通信binder导致ANR：主线程获取当前网络状况，判断当前是否有网  解决方案：异步获取，放在子线程获取当前网络状况

SharePreference使用commit()保存数据 解决方案：使用apply)()/MMKV替代

**两个子线程之间相互死锁，会导致ANR吗	**

​	两个子线程之间相互死锁不会直接导致 ANR。ANR 通常指的是主线程被阻塞，导致应用程序无法响应用户输入或其他事件，从而超过了系统的响应时间阈值

#### 问题定位

traces文件获取  adb bugreport

确定ANR时间     am_anr  

检查CPU信息  （会发在anr前后一段时间内的，不一定和anr有关）

检查内存信息     /proc/meminfo   dumpsys meminfo    PSS

检查调用栈

检查 Low Memory Killer 基于Linux的OOM机制  搜索lowmemorykiller 检查是否内存紧张触发了kill

检查IO时间

#### 检测工具 ANR-WatchDog

```
implementation ("com.github.anrwatchdog:anrwatchdog:1.4.0")
```

```java
import com.github.anrwatchdog.ANRWatchDog;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new ANRWatchDog().start();
    }
}

```



原理：

​	ANR-WatchDog 的工作原理是监控主线程的响应时间。它通过定时器在主线程上启动，并在规定的时间段内检查主线程是否仍然在运行。如果主线程在指定时间内没有响应，ANR-WatchDog 会记录 ANR 并抛出异常或者触发回调，以便你可以捕获并处理 ANR。

弊端

- 例如ANRWatchDog线程休眠指定的时间为5秒，线程阻塞8秒
- 当第1s发送的时候，此时主线程处于空闲状态，马上回包表示没问题
- 当第5s发送的时候，此时主线程处于阻塞状态，但是到第8s，恢复了空闲状态，马上回包表示没问题
- 中间的8s阻塞就被忽略掉了

![image-20240610162112618](C:\Users\wellorbetter\AppData\Roaming\Typora\typora-user-images\image-20240610162112618.png)