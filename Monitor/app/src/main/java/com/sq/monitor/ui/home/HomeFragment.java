package com.sq.monitor.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sq.monitor.MoiterReportActivity;
import com.sq.monitor.MoniterListActivity;
import com.sq.monitor.R;
import com.sq.monitor.ui.home.view.MyAlertDialog;
import com.sq.monitor.ui.home.view.MySelectDialog;
import com.sq.monitor.ui.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    public static boolean isRunning = true;

    public static final List<String> initialChipsItems = Arrays.asList(new String[]{"100", "1,000", "10,000", "100,000", "1,000,000",
            "10,000,000", "100,000,000"});
    public static final List<String> winRatioItems = Arrays.asList(new String[]{"100%", "50%", "25%", "20%", "10%", "5%", "2%", "1%"});
    public static final List<String> loseRatioItems = Arrays.asList(new String[]{"1:1", "1:4", "1:9", "1:19", "1:99", "1:0.95"});
    public static final List<String> betPolicyItems = Arrays.asList(new String[]{"固定下注", "比例下注", "随机下注", "自动倍投"});
    public static final List<String> chipsPerBetItems = Arrays.asList(new String[]{"100%", "50%", "25%", "20%", "10%", "5%", "2%", "1%"});

    public static final List<List<String>> itemsListList = new ArrayList<>();
    public static int itemSelected[] = new int[]{0, 0, 0, 0, 0};
    public static List<TextView> tvValueList = new ArrayList<>();

    public static final int TAG_INITIAL_CHIPS = 0;
    public static final int TAG_WIN_RATIO = 1;
    public static final int TAG_LOSE_RATIO = 2;
    public static final int TAG_BET_POLICY = 3;
    public static final int TAG_CHIPS_PER_BET = 4;

    final int betAmountUnit = 100;

    long betAmountManuel = 0;

    ImageView ivTitleLeft;
    ImageView ivTitleRight;
    ImageView ivStop;
    ImageView ivRun;
    private HomeViewModel homeViewModel;
    RadioGroup rgSpeed;
    LinearLayout llManuelMoney;
    ImageView ivMinusBet;
    ImageView ivAddBet;
    EditText etBetAmount;
    LinearLayout llInitialChips;

    LinearLayout llWinRatio;
    LinearLayout llLoseRatio;
    LinearLayout llSetPolicy;
    LinearLayout llChipsPerBet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        itemsListList.add(initialChipsItems);
        itemsListList.add(winRatioItems);
        itemsListList.add(loseRatioItems);
        itemsListList.add(betPolicyItems);
        itemsListList.add(chipsPerBetItems);

        findView(root);
        setListener();

        return root;
    }


    private void findView(View root) {
        ivTitleLeft = root.findViewById(R.id.top_left);
        ivTitleRight = root.findViewById(R.id.top_right);
        rgSpeed = root.findViewById(R.id.rg_speed);
        llManuelMoney = root.findViewById(R.id.ll_manuel_money);
        ivMinusBet = root.findViewById(R.id.iv_minus_bet);
        ivAddBet = root.findViewById(R.id.iv_add_bet);
        etBetAmount = root.findViewById(R.id.et_bet_amount);
        llInitialChips = root.findViewById(R.id.ll_initial_chips);
        llWinRatio = root.findViewById(R.id.ll_win_ratio);
        llLoseRatio = root.findViewById(R.id.ll_lose_ratio);
        llSetPolicy = root.findViewById(R.id.ll_set_policy);
        llChipsPerBet = root.findViewById(R.id.ll_chips_per_bet);

        tvValueList.add(root.findViewById(R.id.tv_initial_chips));
        tvValueList.add(root.findViewById(R.id.tv_win_ratio));
        tvValueList.add(root.findViewById(R.id.tv_lose_ratio));
        tvValueList.add(root.findViewById(R.id.tv_set_policy));
        tvValueList.add(root.findViewById(R.id.tv_chips_per_bet));

        ivRun = root.findViewById(R.id.iv_run_pause);
        ivStop = root.findViewById(R.id.iv_stop);
    }

    private void setListener() {

        // 标题栏左侧菜单
        ivTitleLeft.setOnClickListener(v -> {
            ToastUtil.toast(getContext(), "模拟序列");
            Intent intent = new Intent(getContext(), MoniterListActivity.class);
            getContext().startActivity(intent);
        });

        // 标题栏右侧菜单
        ivTitleRight.setOnClickListener(v -> {
//            ToastUtil.toast(getContext(), "运行数据为空");
            Intent intent = new Intent(getContext(), MoiterReportActivity.class);
            getContext().startActivity(intent);
        });

        // 初始筹码
        llInitialChips.setOnClickListener(v -> {

            showSingleChoiceDialog(R.string.initial_chips, TAG_INITIAL_CHIPS);
        });
        // 胜率
        llWinRatio.setOnClickListener(v -> {
            showSingleChoiceDialog(R.string.win_ratio, TAG_WIN_RATIO);
        });
        // 赔率
        llLoseRatio.setOnClickListener(v -> {
            showSingleChoiceDialog(R.string.lose_ratio, TAG_LOSE_RATIO);
        });
        // 下注策略
        llSetPolicy.setOnClickListener(v -> {
            showSingleChoiceDialog(R.string.bet_policy, TAG_BET_POLICY);
        });

        // 每注筹码
        llChipsPerBet.setOnClickListener(v -> {
            showSingleChoiceDialog(R.string.chips_per_bet, TAG_CHIPS_PER_BET);
        });

        // 底部模拟速度选择
        rgSpeed.setOnCheckedChangeListener((group, checkedId) -> {
            // 选中手动下注radiobutton
            if (checkedId == R.id.rb_speed_manuel) {
                llManuelMoney.setVisibility(View.VISIBLE);
            } else {
                llManuelMoney.setVisibility(View.GONE);
            }

            switch (checkedId) {
                case R.id.rb_speed_manuel:
                    break;
                case R.id.rb_speed_slow:
                    break;
                case R.id.rb_speed_mid:
                    break;
                case R.id.rb_speed_fast:
                    break;
            }

        });

        // 输入下注金额
        etBetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etBetAmount.getText().toString();
                if (text == null || "".equals(text)) {
                    betAmountManuel = 0;
                    return;
                }
                betAmountManuel = Integer.valueOf(text);

            }
        });

        // 下注金额减100
        ivMinusBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                betAmountManuel -= betAmountUnit;
                if (betAmountManuel <= 0) {
                    betAmountManuel = 0;
                    etBetAmount.setText(null);
                    etBetAmount.setHint("请输入下注额");
                } else {
                    etBetAmount.setText(betAmountManuel + "");
                }
            }
        });

        // 下注金额加100
        ivAddBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                betAmountManuel += betAmountUnit;
                etBetAmount.setText("" + betAmountManuel);
            }
        });

        // 停止模拟
        ivStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    ToastUtil.toast(getContext(), "请先启动模拟器！");
                    return;
                }

                showAlert("确定结束模拟器吗？", getString(R.string.btn_cancel), getString(R.string.btn_yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.toast(getContext(), "模拟器已结束！");
                    }
                });
            }
        });

        // 开始模拟
        ivRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (betAmountManuel == 0) {
                    ToastUtil.toast(getContext(), "请输入正确的下注额！");
                    return;
                }

            }
        });

    }

    private void showAlert( String msg, String negativeText, String positiveText, View.OnClickListener listener){
        MyAlertDialog myDialog = new MyAlertDialog(getContext()).builder();
        myDialog.setGone().setMsg(msg).setNegativeButton(negativeText, null).setPositiveButton(positiveText, listener).show();
    }

    private void showAlertWithTitle(String title, String msg, String negativeText, String positiveText, View.OnClickListener listener){
        MyAlertDialog myDialog = new MyAlertDialog(getContext()).builder();
        myDialog.setGone().setTitle(title).setMsg(msg).setNegativeButton(negativeText, null).setPositiveButton(positiveText, listener).show();
    }

    private void showSingleChoiceDialog(int titleId, int tag) {
        final MySelectDialog dialog = new MySelectDialog(getContext(), tag);
        // 显示在底部
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setTitle(getString(titleId));
        dialog.setData(itemsListList.get(tag));
    }


}