#### Day10 作业

##### 1、内存泄漏

源码阅读+Leakcanary分析

发现三种问题

###### 1、static变量保存activity

直接定位到leaks，发现引用了activity

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/509f547fb5bb1eeee3b4855fe8753ac.3nrgwjrhsu.webp)

使用弱引用即可修复

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/a00b5747de7b21d949b7261cd0bdde0.77demctyfj.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/d54cc7e0e1dc612c7357edff7dde440.1e8gd26u93.webp)

###### 2、匿名内部类引用了activity，并且是callback(执行的时间可能比activity生命周期长)

解决方案：使用自定义内部类+弱引用

这个是Handler内部的callback

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/2b6251e93d12957c1381ea024e1a31e.7zqa43aq3h.webp)

这个target，就是Message的handler，一眼定位到上面的handler（看源码也快）

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/4e80c2963b3f86b99faa69d0f14463f.1vyi1n8385.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/5356760dbac81d5b4d2eac37abbb136.58h7w0owls.webp)

下面的两个网络请求的callback也是，这个定位用leak分析也很好看，直接一个callback就看到了

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/badaf886d9a5e65e484ecc156343b66.ibyxlxmm1.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/a51a6768eb9d1469102d6f8bd6b905a.3rb2u9kold.webp)



![](https://github.com/wellorbetter/picx-images-hosting/raw/master/f0904f12aaa1121673f61640efd5576.8vmrjjkntj.webp)

另一个也是一样的

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/58571ccb52753f173ca7d6ad79b3918.9dct84mc3b.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/ba46a10f9474e9e1c30c86c78d281bc.2yy7cj4gv0.webp)

###### 3、引用了context，但是传入的activity

解决：传入、接收使用getApplicationContext

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/f53707148e46338932af5594cc50d51.5j41p648s1.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/86e1ae2e3c81b4017fe92a36a1a41e4.54xlyavwqd.webp)

修改为ApplicationContext

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/723dfdecfc63ceb3a48647abae4b293.4uas55gsgx.webp)

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/c7bcf6d229bce482edfa1ebb3402539.4xue2v9wi9.webp)

#####  ANR 改进ANR-Wathdog

ANR-Wathdog有个问题，就是如果刚进来的时候，主线程空闲，这个时候ANR-Wathdog就会睡眠五秒，然后如果同时主线程开始睡眠6s(小于10s都行)，那么ANR-Wathdog都不会捕获到这个ANR



所以就一秒一查，累计五次确认无响应就捕获ANR，成功解决ANR-Wathdog的这个问题



思路就很简单，确认的时候就把_count置为0，不然就++，累计五次就说明已经五次未响应，那就捕获了



成功截图

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/57de14ebbaeae0e5b656ffbfdf384d7.4914iumj4f.webp)

而使用ANR-Wathdog并不能捕获它

![](https://github.com/wellorbetter/picx-images-hosting/raw/master/6660e03440c25cab5c160ccd7ea4ccd.lvkvbqt26.webp)