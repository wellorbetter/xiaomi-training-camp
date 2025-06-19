#### Day2 作业：

#### 1、打印四种跳转模式的生命周期

Standard跳到自己，会重新创建一个自己（都是pause之后开始另一个，没有destroy是因为没有finish）

![image-20240602165556034](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602165556034.6m3qoickqz.webp)

SingleTask，从MainActivity跳到SingleTask然后再跳回SingleTask，SingleTask通过Restart，并没有create，同时再次跳到SingleTask的时候clear top，将MainActivity销毁了

![image-20240602165937551](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602165937551.sysf7p4i6.webp)

![image-20240602170030705](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602170030705.2yy70zgs9c.webp)

SingleTop跳到自己，直接复用了，没有重建

![image-20240602170309789](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602170309789.9rj8ng6zo5.webp)

MainActivity跳SIngleTop然后跳MainActivity然后再跳SingleTop，发现和Standard一样，重新创建了

![image-20240602171952705](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602171952705.6bgwvcxclz.webp)

![image-20240602184105998](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602184105998.54xlmr8g0s.webp)

SingleInstance ，MainActivity跳SingleInstance再跳MainActivity再跳SingleInstance，复用了，没有重创

![image-20240602184222809](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602184222809.2ruz5jumt7.webp)

SingleInstance t114，和t113不一样，说明是新建的任务栈

![image-20240602184504368](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602184504368.6f0it2qfb1.webp)



#### Service的生命周期

![image-20240602190106418](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602190106418.969l15cjcv.webp)

没有使用Service的，会被自动回收

绑定之后和绑定的对象共享生命周期，需要解绑之后没有使用才会被回收

![image-20240602190259865](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602190259865.45hi9l5oua.webp)

#### 广播

静态隐式广播是在Android8.0以上是不被允许的，会爆warning，可以添加intent.setFlags(0x1000000); 或者改为显式广播

![image-20240602191532232](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602191532232.2h85cefeo4.webp)



动态广播，检测飞行模式改变

![image-20240602191927540](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602191927540.7egm68t6h2.webp)

#### AIDL

AIDL跨进程通信

![image-20240602192226059](https://github.com/wellorbetter/picx-images-hosting/raw/master/image-20240602192226059.6t6yjxyq6f.webp)

#### 作业二总结：

##### 1、四种启动模式

- Standard     启动会重新创建
- SingleTask   如果栈内有，就会清空它上面的所有activity让它浮到栈顶，如果没有，就会新建一个栈把它放进去
- SingleTop    如果在栈顶就复用，否则和Standard一样
- SingleInstance  给activity单独开一个栈使用，可以一直复用



#### 2、startService bindService

- onCreate->onStartCommond->running->onDestroy
- onCreate->onBind->running->onUnbind->onDestroy

#### 3、广播

静态注册：manifest文件里面指定intent-filter TIPS：如果是隐式发送，需要setFlag(0x1000000)

动态注册：创建IntentFilter然后Register

#### 4、AIDL跨进程通信

创建AIDL文件，如果无法创建，在build.gradle下面添加buildFeatures{aidl = true}，写接口

创建Service，Binder继承AIDL文件.Stub，然后实现接口（服务端）

绑定Service，拿到接口，进行通信（客户端）