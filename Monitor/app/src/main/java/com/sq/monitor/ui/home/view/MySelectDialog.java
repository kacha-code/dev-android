package com.sq.monitor.ui.home.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sq.monitor.R;
import com.sq.monitor.ui.home.HomeFragment;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;


public class MySelectDialog extends Dialog {
    private Context context;
    private View root;
    TextView tvTitle;
    TextView tvCancel;
    TextView tvOk;
    LoopView loopView;
    private int itemCur = 0;
    private int whichTag = -1;

    public MySelectDialog(Context context, int tag) {
        super(context, R.style.style_dialog);
        this.whichTag = tag;
        this.context = context;
        this.root = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_dialog);

        findView();
        initListener();
    }

    private void findView() {
        tvTitle = findViewById(R.id.tv_title);
        tvCancel = findViewById(R.id.cancel);
        tvOk = findViewById(R.id.ok);
        loopView = findViewById(R.id.loopview_dialog);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        tvTitle.setText(title);
    }

    public void upateSelectedValue() {
        int selected = HomeFragment.itemSelected[this.whichTag];
        String value = HomeFragment.itemsListList.get(this.whichTag).get(selected);
        TextView tv = HomeFragment.tvValueList.get(this.whichTag);
        tv.setText(value);

    }


    public void setData(List<String> dataList) {
        //设置是否循环播放
        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("hltag", "Item " + index);
//                itemCur = index;
            }
        });
        //设置原始数据
        loopView.setItems(dataList);
        //设置初始位置
        loopView.setCurrentPosition(HomeFragment.itemSelected[whichTag]);
        //设置字体大小
        loopView.setTextSize(20);
//        loopView.setBackgroundColor(Color.GRAY);
        loopView.showContextMenu();
    }

    public void initListener() {
        Dialog dialog = this;
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Toast.makeText(getContext().getApplicationContext(), "取消！", Toast.LENGTH_SHORT).show();

            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // 更新HOmeFragment里的值
                HomeFragment.itemSelected[whichTag] = loopView.getSelectedItem();
                // 更新显示的值
                upateSelectedValue();

                Toast.makeText(getContext().getApplicationContext(), "确定！", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
