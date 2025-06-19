package com.example.day3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.day3.Fragment.Animation.AnimCustomFragment1;
import com.example.day3.Fragment.Animation.AnimCustomFragment2;
import com.example.day3.Fragment.Animation.AnimTransBeginFragment;
import com.example.day3.Fragment.Animation.AnimTransEndFragment;
import com.example.day3.R;


// 切换动画 不共享
public class FragmentCustomAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_custom_animation);

        // 加载一个Fragment，然后替换它，替换的时候添加动画
        initView();
        initEvent();

    }
    // 这个就不用写在fragment里面了，又不需要拿到shareElement
    private void initEvent() {
        bt_anim.setOnClickListener(this);
    }

    private void initView() {
        bt_anim = findViewById(R.id.bt_anim);
        // 复用FragmentTransAnimationActivity的代码
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(AnimCustomFragment1.class.getName());
        // 防止重复加
        if (fragment == null) {
            fragment = new AnimCustomFragment1();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl, fragment, AnimCustomFragment1.class.getName())
                    .commit();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_anim) {
            // replace , 小改AnimTransBeginFragment的代码
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(AnimCustomFragment2.class.getName());
            if (fragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.slide_out,
                                R.anim.slide_in,
                                R.anim.slide_out
                        )
                        .addToBackStack(AnimCustomFragment2.class.getName())
                        .replace(R.id.fl, AnimCustomFragment2.class, null, AnimCustomFragment2.class.getName())
                        .commit();
            }
        }
    }
}