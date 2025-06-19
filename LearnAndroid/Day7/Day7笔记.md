### Day7 笔记

#### Android 动画

##### 帧动画 anim

手动设置动画的每帧图片，然后播放

使用animation-list完成drawable动画，然后在代码中绑定img控件，并setimgresource，然后再设置动画资源并启动

##### `Animation TranslateAnimation`

```java
Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
animation.setDuration(1000);
ivAnimDemo.setAnimation(animation);
```



也可以动态addFrame加载

##### `AnimationDrawable`

```java
AnimationDrawable animationDrawable = new AnimationDrawable();
animationDrawable.setOneShot(false);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run1), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run2), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run3), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run4), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run5), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run6), 100);
animationDrawable.addFrame(ContextCompat.getDrawable(this, R.drawable.run7), 100);
ivAnimDemo.setImageDrawable(animationDrawable);
animationDrawable.start();
```



##### 补间动画 anim

作用view 自动补全过渡状态，只是绘制的地方发送了变化，实际上的位置并没有发生变化，点击事件并不会随着动画而移动

animation, 有旋转、透明度、放缩，可以分别设置，也可以用animationset一起配置

#####  `AnimationSet` `AlphaAnimation`

#####  `RotateAnimation` `ScaleAnimation` `TranslateAnimation`

```java
AnimationSet animationSet = new AnimationSet(true);
AlphaAnimation alphaAnimation = new AlphaAnimation(
    0.0f, 1.0f
);
alphaAnimation.setRepeatCount(3);
RotateAnimation rotateAnimation = new RotateAnimation(
    0, 360,
    Animation.RELATIVE_TO_SELF, 0.5f,
    Animation.RELATIVE_TO_SELF, 0.5f
);
rotateAnimation.setRepeatCount(3);
ScaleAnimation scaleAnimation = new ScaleAnimation(
    1f, 1.2f, 1f, 1.2f,
    Animation.RELATIVE_TO_SELF, 0.5f,
    Animation.RELATIVE_TO_SELF, 0.5f
);
scaleAnimation.setRepeatCount(3);
animationSet.addAnimation(alphaAnimation);
animationSet.addAnimation(rotateAnimation);
animationSet.addAnimation(scaleAnimation);
animationSet.setDuration(1000);
animationSet.setRepeatMode(Animation.RESTART);
```



##### 属性动画  animator

作用任何东西的属性

#####  `PropertyValuesHolder ObjectAnimator AnimatorSet`

```
PropertyValuesHolder+ObjectAnimator 同时作用多个属性

PropertyValuesHolder rotationXHolder = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0f, 360f);
PropertyValuesHolder translateXHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 100);
ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(tvAnim, rotationXHolder, translateXHolder);
animator.setDuration(1000);	
animator.start();
```

```java
AnimatorSet 可以同时作用于多个对象 如果放了多个，重复次数需要每个animator都需要分别设置setRepeatCount，设置AnimatorSet没用
ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(tvAnim, View.ROTATION_X, 0f, 360f);
ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(ivAnimDemo, View.TRANSLATION_X, 0f, 360f);
AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(rotationXAnimator, translateXAnimator);
// 可以指定播放顺序
animatorSet
    .play(rotationXAnimator)
    .after(translateXAnimator);
animatorSet.setDuration(1000);
animatorSet.start();
```

ValueAnimator 表示的是一个过程，它并不依赖与某个控件，但是它可以用来作用于某些控件的属性，它本身表示某个属性变化过程，它就可以在这个过程中来调用设置其他控件的属性值，使得这些控件实现了动态动画

#####  `ValueAnimator `

```java
 ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
 valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
     @Override
     public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
         float currentValue = (float) valueAnimator.getAnimatedValue();
         ivAnimDemo.setRotation(currentValue * 360);
         ivAnimDemo.setTranslationX(currentValue * 100);
         tvAnim.setRotation(currentValue * 360 * 2);
     }
 });
 valueAnimator.setDuration(1000);
 valueAnimator.start();
```

##### `ViewPropertyAnimator`

工具类快速修改某个属性 通过.animate()获取实例，支持链式调用

```java
tvAnim.animate()
        .rotationX(360)
        .translationX(100)
        .setDuration(1000)
        .setStartDelay(1000)
        .start();
```

##### 估值器  

对应：不需要这个int，但是传进来的int，怎么办

比如说RGBA，拆成R、G、B、A，然后根据变换的次数，分别乘以对应的fraction，然后拼接起来变成int，使得过渡平滑

```
ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tvAnim,
        "backgroundColor", Color.parseColor("#009688"), Color.RED);
objectAnimator.setEvaluator(new ArgbEvaluator());
objectAnimator.setDuration(1000);
objectAnimator.start();
```

##### 插值器

Interpolator  有很多效果 还可以自定义

```java
objectAnimator.setInterpolator()
```



#### 