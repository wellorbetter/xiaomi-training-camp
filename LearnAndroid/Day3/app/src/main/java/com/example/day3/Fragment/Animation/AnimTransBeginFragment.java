package com.example.day3.Fragment.Animation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.day3.R;

// 共享元素
// 过渡动画起始fragment
public class AnimTransBeginFragment extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 拿到img
        ImageView iv = (ImageView) getView().findViewById(R.id.iv);
        // 创建点击事件，点击就开始动画切换
        // 必须写在这里面，因为我需要拿到fragment的共享元素的transitionName
        // 然后指定addSharedElement
        // 然后压栈、replace、commit
        getActivity().findViewById(R.id.bt_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TAG = "666";
                // 这里很奇怪，用getChildrenFragmentManager拿不到Fragments
                Log.d(TAG, getParentFragmentManager().getFragments().toString());
                Fragment fragment = getParentFragmentManager().findFragmentByTag(AnimTransEndFragment.class.getName());
                if (fragment == null) {
                    getParentFragmentManager()
                            .beginTransaction()
                            .addSharedElement(iv, ViewCompat.getTransitionName(iv))
                            .addToBackStack(AnimTransEndFragment.class.getName())
                            .replace(R.id.fl, AnimTransEndFragment.class, null, AnimTransEndFragment.class.getName())
                            .commit();
                }
            }
        });

    }

    public AnimTransBeginFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anim_trans_begin, container, false);
    }

}