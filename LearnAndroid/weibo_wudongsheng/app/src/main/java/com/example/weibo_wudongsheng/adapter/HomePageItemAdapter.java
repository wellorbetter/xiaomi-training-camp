package com.example.weibo_wudongsheng.adapter;

import static android.media.MediaPlayer.SEEK_CLOSEST;
import static com.example.weibo_wudongsheng.bean.WeiboInfo.VIEW_TYPE_IMAGES;
import static com.example.weibo_wudongsheng.bean.WeiboInfo.VIEW_TYPE_SINGLE_IMAGE;
import static com.example.weibo_wudongsheng.bean.WeiboInfo.VIEW_TYPE_TEXT;
import static com.example.weibo_wudongsheng.bean.WeiboInfo.VIEW_TYPE_VIDEO;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.weibo_wudongsheng.MyApp;
import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.activity.DetailItemActivity;
import com.example.weibo_wudongsheng.activity.LoginActivity;
import com.example.weibo_wudongsheng.bean.WeiboInfo;
import com.example.weibo_wudongsheng.customize.CircleImageButton;
import com.example.weibo_wudongsheng.utils.PixelTransformHelper;
import com.example.weibo_wudongsheng.utils.WeiboRequestHelper;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView适配器，用于显示不同类型的微博内容（文本、图片、视频）。
 */
public class HomePageItemAdapter extends BaseMultiItemQuickAdapter<WeiboInfo, BaseViewHolder> implements LoadMoreModule {


    // HashMap 用于存储 MediaPlayer 实例
    // 非常精髓的一个点,因为如果那个MediaPlayer没有引用,它就会自动进入销毁
    // 而且我要每个item都创建一个MediaPlayer,所以就必须有东西来保存这些实例
    // 且可以在它们需要销毁的时候移除引用
    private HashMap<Integer, MediaPlayer> mediaPlayerHashMap;
    private HashMap<Integer, Integer> progressHashMap;
    private Context context; // Glide 和 MediaPlayer 所需的上下文引用
    private PixelTransformHelper pixelTransformHelper;
    private WeiboRequestHelper weiboRequestHelper;
    /**
     * 适配器的构造函数。
     *
     * @param data    要显示的 WeiboInfo 对象列表
     * @param context 应用程序的上下文
     */
    public HomePageItemAdapter(@Nullable List<WeiboInfo> data, Context context) {
        super(data);
        this.context = context;
        addItemType(VIEW_TYPE_VIDEO, R.layout.recommend_video_items);
        addItemType(VIEW_TYPE_IMAGES, R.layout.recommend_images_items);
        addItemType(VIEW_TYPE_TEXT, R.layout.recommend_text_items);
        addItemType(VIEW_TYPE_SINGLE_IMAGE, R.layout.recommend_single_image_items);
        mediaPlayerHashMap = new HashMap<>();
        progressHashMap = new HashMap<>();
        weiboRequestHelper = new WeiboRequestHelper(context.getApplicationContext());
        pixelTransformHelper = PixelTransformHelper.newInstance(context.getApplicationContext());
    }
    public OnDataEmptyListener mOnDataEmptyListener;
    public interface OnDataEmptyListener {
        /**
         * onDataEmpty 数据为空时调用，切换
         */
        void onDataEmpty();
    }
    @Override
    protected void convert(@NonNull BaseViewHolder holder, WeiboInfo item) {
        CircleImageButton avatar = holder.findView(R.id.cib_avatar);
        TextView nickName = holder.findView(R.id.tv_nickname);
        TextView releaseTime = holder.findView(R.id.tv_release_time);
        TextView content = holder.findView(R.id.tv_content);
        Button bt_like = holder.findView(R.id.bt_like);
        Button bt_comment = holder.findView(R.id.bt_comment);
        Button bt_share = holder.findView(R.id.bt_share);
        TextView tv_like = holder.findView(R.id.tv_like);
        TextView tv_comment = holder.findView(R.id.tv_comment);
        TextView tv_share = holder.findView(R.id.tv_share);
        Button bt_clear = holder.findView(R.id.bt_clear);

        bt_clear.setTag(item.getId());
        if (bt_clear.getTag() == item.getId()) {

            if (item.getLikeFlag()) {
                bt_like.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

            bt_clear.setEnabled(true);
            bt_clear.setOnClickListener(v -> {
                int recordIndex = getItemPosition(item);
                getData().remove(item);
                // 更新本地的数据
                MMKV.defaultMMKV().encode("weibo_data", new Gson().toJson(getData()));
                // 点击一次之后取消点击功能，防止短时间点击多次引起错误
                bt_clear.setEnabled(false);
                if (getData().size() == 0) {
                    // 数据清空了，需要切换到EmptyData页面
                    EventBus.getDefault().post("empty_data");
                }
                notifyItemRemoved(recordIndex);
                // 通知范围变化
                notifyItemRangeChanged(recordIndex, getItemCount());
            });
            bt_comment.setOnClickListener(v -> {
                // 点击评论
                Toast.makeText(getContext().getApplicationContext(), "点击第" + (getItemPosition(item) + 1) + "条数据评论按钮", Toast.LENGTH_SHORT).show();
            });
            // 点击喜欢，如果没登录先跳转到登录界面
            final boolean[] isClick = {item.getLikeFlag()};
            bt_like.setOnClickListener(v -> {
                if (!MyApp.isLogin) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    getContext().startActivity(intent);
                } else {
                    // 已经登录了，可以点赞
                    if (!isClick[0]) {
                        weiboRequestHelper.requestLike(item.getId());
                        bt_like.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        // 这里valueof很关键，一个小坑，用数字不会提示报错
                        tv_like.setText(String.valueOf(item.getLikeCount() + 1));
                        animateLike(bt_like);
                    }
                    else {
                        weiboRequestHelper.requestUnLikeWeibo(item.getId());
                        bt_like.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        tv_like.setText(String.valueOf(item.getLikeCount()));
                        animateUnLike(bt_like);
                    }
                    isClick[0] = !isClick[0];
                }
            });
        }




        avatar.setTag(item.getAvatar());

        loadAvatarImageWithTag(item, avatar);
        nickName.setText(item.getUsername());
        releaseTime.setText(item.getCreateTime());
        content.setText(item.getTitle());
        tv_like.setText(item.getLikeCount() == 0 ? "" : String.valueOf(item.getLikeCount()));
        tv_comment.setText("");
        tv_share.setText("");
        item.setItemType();
        int type = holder.getItemViewType();
        switch (type) {
            case VIEW_TYPE_IMAGES:
                setupImagesView(holder, item);
                break;
            case VIEW_TYPE_VIDEO:
                setupVideoView(holder, item);
                break;
            case VIEW_TYPE_TEXT:
                setupTextView(holder);
                break;
            default:
                setupSingleImage(holder, item);
                break;
        }
    }

    // 点赞动画
    private void animateLike(View view) {
        // 缩放动画，从1.0倍放大到1.2倍，再恢复到1.0倍
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.2f, 1.0f);

        // 旋转动画，沿Y轴旋转360度
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 360f);

        // 组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    // 取消点赞动画
    private void animateUnLike(View view) {
        // 缩放动画，从1.0倍缩小到0.8倍，再恢复到1.0倍
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.8f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.8f, 1.0f);

        // 组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    /**
     * 防止异步加载出现乱序问题，加载头像资源
     *
     * @param item    数据
     * @param avatar  要加载的图片
     */
    private void loadAvatarImageWithTag(WeiboInfo item, CircleImageButton avatar) {
        Glide.with(getContext()).load(Uri.parse(item.getAvatar())).into(
                new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (avatar.getTag() == item.getAvatar()) {
                            avatar.setImageDrawable(resource);
                        } else {
                            Log.d("loadimage", "乱序了");
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                }
        );
    }

    /**
     * 设置视频视图。
     *
     * @param holder   视图的 holder
     * @param item     数据
     */
    private void setupVideoView(BaseViewHolder holder, WeiboInfo item) {
        ImageView iv_video = holder.findView(R.id.iv_video);
        Button btn_video_play = holder.findView(R.id.btn_video_play);
        SurfaceView video = holder.findView(R.id.sf_video);
        ProgressBar progressBar = holder.findView(R.id.sb_video);


        iv_video.setTag(item.getPoster());
        loadPosterImageWithTag(item, iv_video);
        setupMediaPlayer(item, btn_video_play, iv_video, video, progressBar);
    }

    /**
     * 防止异步加载图片乱序  加载视频封面
     *
     * @param item      数据
     * @param iv_video  要加载的imageview
     */
    private void loadPosterImageWithTag(WeiboInfo item, ImageView iv_video) {
        Glide.with(getContext()).load(item.getPoster()).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (iv_video.getTag() == item.getPoster()) {
                    iv_video.setImageDrawable(resource);
                } else{
                    Log.d("loadImage", "异步加载乱序");
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    /**
     * 设置 MediaPlayer 和 SurfaceView，加载完成后设置对应的点击事件。
     *
     * @param item              数据
     * @param btn_video_play    播放/暂停按钮
     * @param video             播放视频的 SurfaceView
     * @param progressBar       视频加载进度条
     */
    private void setupMediaPlayer(WeiboInfo item, Button btn_video_play, ImageView iv_video, SurfaceView video, ProgressBar progressBar) {
        // 开始的时候显示图片,点了之后才显示视频
        iv_video.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);
        SurfaceHolder surfaceHolder = video.getHolder();
        video.setTag(item.getVideoUrl());
        btn_video_play.setEnabled(true);
        btn_video_play.setVisibility(View.VISIBLE);
        // 最开始的时候，还没有准备好，因为这个时候imageview是覆盖在了这个video上面，所以这里需要
        // 先让imageview给video让道，让video可见，然后准备好了之后自动播放，就可以达到点击这个按钮
        // 播放的效果，虽然会经历稍微的小加载黑屏
        // 似乎这个progressbar也不会清空
        btn_video_play.setOnClickListener(v -> {
            iv_video.setVisibility(View.INVISIBLE);
            video.setVisibility(View.VISIBLE);
            btn_video_play.setVisibility(View.INVISIBLE);
            btn_video_play.setEnabled(false);
            progressBar.setProgress(0);
        });

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                initializeMediaPlayerWithTag(item, video, holder, btn_video_play, progressBar);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Log.d("HomePageItemAdapter", "Surface changed");
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                releaseMediaPlayer(item, video, btn_video_play);
            }
        });
    }

    /**
     * 释放 MediaPlayer 资源。
     *
     * @param item    数据
     */
    private void releaseMediaPlayer(WeiboInfo item, SurfaceView video, Button btn_video_play) {
        MediaPlayer mediaPlayer = mediaPlayerHashMap.getOrDefault(item.hashCode(), null);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                progressHashMap.put(item.hashCode(), mediaPlayer.getCurrentPosition());
                mediaPlayer.reset();
            }
            // 销毁的时候同时需要将progressbar的更新给停止
            stopUpdatingProgressBar();
            mediaPlayer.release();
            // 同时清理点击事件，以免执行了isPlaying导致生命周期异常
            video.setOnClickListener(null);
            btn_video_play.setOnClickListener(null);
            //mediaPlayerHashMap.remove(getItemPosition(item));
            mediaPlayerHashMap.clear();
        }
    }

    /**
     * 初始化 MediaPlayer。
     *
     * @param item              数据
     * @param holder            SurfaceHolder
     * @param btn_video_play    播放/暂停按钮
     * @param progressBar       进度条
     */
    private void initializeMediaPlayerWithTag(WeiboInfo item, SurfaceView video, SurfaceHolder holder, Button btn_video_play, ProgressBar progressBar) {
        if (video.getTag() == item.getVideoUrl()) {
            MediaPlayer mediaPlayer = mediaPlayerHashMap.get(item.hashCode());
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayerHashMap.put(item.hashCode(), mediaPlayer);
            } else {
                mediaPlayer.reset();
            }
            mediaPlayerHashMap.put(getItemPosition(item), mediaPlayer);
            mediaPlayer.setDisplay(holder);
            try {
                mediaPlayer.setDataSource(getContext(), Uri.parse(item.getVideoUrl()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();
                Log.d("HomePageItemAdapter", "MediaPlayer preparing asynchronously");
            } catch (IOException e) {
                Log.e("HomePageItemAdapter", "Error setting data source", e);
            }
            setupMediaPlayerListeners(item, video, mediaPlayer, btn_video_play, progressBar);
        }
    }

    /**
     * 设置 MediaPlayer 的监听器。
     *
     * @param video             播放视频的 SurfaceView
     * @param mediaPlayer       MediaPlayer 实例
     * @param btn_video_play    播放/暂停按钮
     * @param progressBar       视频加载进度条
     */
    private void setupMediaPlayerListeners(WeiboInfo item, SurfaceView video, MediaPlayer mediaPlayer, Button btn_video_play, ProgressBar progressBar) {


        // 设置 MediaPlayer 准备好后的监听器
        mediaPlayer.setOnPreparedListener(mp -> {
            progressBar.setProgress(0);
            Log.d("HomePageItemAdapter", "MediaPlayer prepared");
            btn_video_play.setVisibility(View.INVISIBLE); // 隐藏播放按钮
            mp.start(); // 开始播放视频
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(progressHashMap.getOrDefault(item.hashCode(), 0), SEEK_CLOSEST);
            }
            // 设置点击 SurfaceView 暂停视频的功能
            video.setOnClickListener(v -> {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btn_video_play.setEnabled(true);
                    btn_video_play.setVisibility(View.VISIBLE);
                }
            });
            // 设置播放/暂停按钮的点击事件
            btn_video_play.setOnClickListener(v -> {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // 开始播放视频
                    btn_video_play.setVisibility(View.INVISIBLE); // 隐藏播放按钮
                    btn_video_play.setEnabled(false); // 禁用播放按钮
                    startUpdatingProgressBar(mediaPlayer, progressBar); // 启动进度条更新
                }
            });
            startUpdatingProgressBar(mediaPlayer, progressBar); // 启动进度条更新
        });

        // 设置 MediaPlayer 出现错误时的监听器
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e("HomePageItemAdapter", "MediaPlayer error: " + what + ", " + extra);
            releaseMediaPlayer(item, video, btn_video_play);
            stopUpdatingProgressBar(); // 停止进度条更新
            return true;
        });

        // 设置 MediaPlayer 播放完成时的监听器
        mediaPlayer.setOnCompletionListener(mp -> stopUpdatingProgressBar()); // 播放完成时停止进度条更新
    }

    /**
     * 声明为全局是为了方便销毁时取消执行进度条更新,以防触发MediaPlayer的生命周期bug
     *
     * handler 用于处理进度条更新的 Handler
     * updateProgress 用于更新进度的Runnable
     */
    private final Handler handler = new Handler();
    private Runnable updateProgress;

    /**
     * 启动进度条更新。
     *
     * @param mediaPlayer   MediaPlayer 实例
     * @param progressBar   视频加载进度条
     */
    private void startUpdatingProgressBar(MediaPlayer mediaPlayer, ProgressBar progressBar) {
        progressBar.setMax(mediaPlayer.getDuration());
        updateProgress = () -> {
            progressBar.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(updateProgress, 50);
            }
        };
        handler.postDelayed(updateProgress, 50);
    }


    /**
     * 停止进度条更新。
     */
    private void stopUpdatingProgressBar() {
        if (updateProgress != null) {
            // 移除进度条更新任务
            handler.removeCallbacks(updateProgress);
            // 清空 Runnable
            updateProgress = null;
        }
    }

    /**
     * 设置文本视图。
     *
     * @param holder    保存视图的 holder
     */
    private void setupTextView(BaseViewHolder holder) {
    }


    /**
     * 设置图片视图。
     *
     * @param holder    保存视图的 holder
     * @param item      数据
     */
    private void setupImagesView(@NonNull BaseViewHolder holder, WeiboInfo item) {
        RecyclerView images = holder.findView(R.id.rv_images);
        images.setAdapter(new ImageAdapter(context, item.getImages(), item.getUsername()));
        images.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    private void setupSingleImage(BaseViewHolder holder, WeiboInfo item) {
        ImageView iv_image = holder.findView(R.id.iv_image);
        iv_image.setOnClickListener(v -> {
            // 点击图片跳转详情页面
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) item.getImages());
            Bundle bundle = new Bundle();
            bundle.putInt("position", 0);
            bundle.putString("nickname", item.getUsername());
            intent.putExtras(bundle);
            // 还要传nickname
            context.startActivity(intent);
        });
        loadImage(item, iv_image);
    }
    /**
     * 使用 Glide 加载图片，根据图片的宽高设置横屏或竖屏模式。
     *
     * @param item      数据
     * @param iv_video  要加载图片的 ImageView
     */
    private void loadImage(WeiboInfo item, ImageView iv_video) {
        Glide.with(getContext())
                .load(Uri.parse(item.getImages().get(0)))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();

                        // 根据图片的宽高设置图片的显示模式
                        if (width > height) {
                            // 横屏模式
                            setSingleImage(336, 189, iv_video);
                        } else {
                            // 竖屏模式
                            setSingleImage(189, 336, iv_video);
                        }

                        iv_video.setImageDrawable(resource);
                        iv_video.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 给ImageView指定宽高
     *
     * @param widthInDp   传入的宽 单位:dp
     * @param heightInDp  传入的高 单位:dp
     * @param iv_video    要设置的ImageView
     */
    private void setSingleImage(int widthInDp, int heightInDp, ImageView iv_video) {
        int widthInPixels = pixelTransformHelper.dp2Pixel(widthInDp);
        int heightInPixels = pixelTransformHelper.dp2Pixel(heightInDp);
        iv_video.getLayoutParams().width = widthInPixels;
        iv_video.getLayoutParams().height = heightInPixels;
    }

}
