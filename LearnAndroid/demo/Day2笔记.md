### Day2

#### 课程 四大组件



#### 简介



##### Activity



活动、页面、交互



###### Activity创建

直接new Activity(自动)或者new class extends Acitivity（或者继承Activity），然后在res/layout下面手动创建layout，并在OnCreate里面绑定SetContentView（手动绑定需要在manifest里面注册）



具有生命周期(类似React Hook)



四种启动模式

1、在manifest中指定launchmodel

2、intent.addFlags  **FLAG_ACTIVITY_NEW_TASK**  相当于singleTask、	**FLAG_ACTIVITY_CLEAR_TOP**相当于singleTop

隐式跳转 action category匹配

Uri  匹配规则  mimeType匹配数据格式 使用setData、setType设置（会将另一个设置为空，可以用setDataAndType）

权限

manifest设置user-permission，然后动态申请

```java
if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
    != PackageManager.PERMISSION_GRANTED){
ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},2); // 最后一个参数在查看申请结果有用
// 没有权限就申请
}else {
    
}
```



查看申请结果

```
onRequestPermissionsResult

if (requestCode == 1) {
    if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "申请失败", Toast.LENGTH_SHORT).show();
    } else {
        // 执行具体逻辑
    }
}
```

##### Service 



前台服务/后台服务



启动：start/startForeground



主线程，不能开启耗时操作

IntentService 



绑定/不绑定

```
serviceConnection要是同一个，使用Binder通信
```

同样需要到manifest中声明



同样有生命周期

onbind等



属性process可以指定放到独立进程中运行



##### Broadcast Receiver



进程间通信



Message（一笔带过）



AIDL



底层Binder



Client/Server 思想



数据需要序列化，把对象转化成相同的可以传输的格式



Serializable(磁盘)、Parcelable(内存)



1、创建aidl  2、Service服务端实现接口并向客服端公开接口  3、客服端使用接口



build.gradle app里面设置

buildFeatures{aidl = true}



需要生成java文件之后才能使用(build->clean project -> rebuild project)



可以在Project视图下查看build/generated/aidl_source_output_dir下面查看是不是生成了java文件

然后就可以让Service里面的Binder继承aidl对应的接口



onServiceConnected里面使用IRemoteService.Stub.asInterface接收binder



静态注册：manifest中注册receiver，filter添加action，指定name，onReceive使用

静态注册的隐式发送在Android8.0以上被禁用了



动态注册，intentfilter+register



##### Content Provider

管理应用程序数据的组件 共享数据给其他应用程序



ContentResolver getContentResolver

查手机联系人：

manifest添加user-permission，动态申请权限

开子线程读联系人(耗时操作)

使用resolver.query 拿到cusor

do while cursor.moveToNext()遍历

cursor.close()

