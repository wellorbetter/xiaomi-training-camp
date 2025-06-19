### Day3笔记

#### Fragment

简要介绍
为什么要用Fragment 重用、UI、资源
Fragment的优点

#### 静态的Fragment

fragment指定name

#### attach / detach

只会将view删除，但是还存在，不会影响状态
attach / detach 它们和onAttach、onDetach没关系，并不会进入这两种状态
attach / detach 它们做的事情是创建view、删除view，而hide并不会影响生命周期

#### add

add只会占据内容，下面的fragment不受影响，同样也在resume，一般和hide结合
每个种类的Fragment只能添加一次，同类Fragment不能重复添加

#### replace

会删除下面的fragment，但是如果替换的是静态绑定的，是不会删除静态绑定的，也不会覆盖它
可以任意替换，但是似乎container里面有多个fragment存在是不能replace的 **原来是我的写法有问题，不要保存一个transaction使用，replace会删除所有的**

#### remove

remove似乎也是能删除具有多个fragment的
删除下面的

#### Fragment回退栈

如果在fragment中使用设备上的返回，会导致当前页面的所有fragment全部退出
如果想要实现activity栈那种退出效果，需要通过addToBackStack手动压栈，出栈
这个时候它只会执行到onDestroyView()，fragment本身没有销毁，再用的时候只需要重新创建view

#### CASE:

异常恢复会导致fragment重新创建，所以每次创建fragment的时候，需要先判断是否创建过，以避免重新创建

#### Fragment通信

**1、接口**
观察者模式，观察者不必亲自观察，派一个哨兵观察即可
固定模式：被观察者创建interface，并保存。观察者实现interface
即在被观察者里面定义并保存哨兵，观察者派出具体的哨兵

**2、public方法**
直接暴露setParams方法，简单易写，但是需要手动设置参数

**3、Fragment Result API**
startActivityForResult

#### ViewPager+FragmentStatePagerAdapter

xml文件创建ViewPager，Activity里面绑定之后，需要给ViewPager设置Adapter

即ViewPager每一页都是Fragment，Adapter即是适配器，即将传入的参数适配为需要的(需要可以拿到页面长度，根据位置拿到页面)，一般FragmentStatePagerAdapter里面
会传入List<Fragment>，即按照页面顺序对应的Fragment

#### Fragment切换动画

影响当前这个事务下的fragment
fragmenttransaction.setCustomAnimations 四个参数
setTransition


#### 共享元素动画

退出和进入是一个动画
定义共享元素
在Fragment中设置过渡

两个fragment都要设置xml的transactionName（也就是SharedElement），然后点击button的onclick需要在开始的Fragment的里面设置，因为需要拿到transactionName(也可以指定)和SharedElement，然后在第二个fragment里面设置setSharedElementEnterTransition和setSharedElementReturnTransition