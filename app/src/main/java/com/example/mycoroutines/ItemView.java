package com.example.mycoroutines;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * time：2020/6/24 0024
 * author：hexun
 * describe：
 */
public class ItemView extends LinearLayout {

    TextView tvContent;

    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tvContent = new TextView(getContext());
        tvContent.setLines(1);
        tvContent.setEllipsize(TextUtils.TruncateAt.END);
        tvContent.setTextSize(12);
        tvContent.setTextColor(Color.WHITE);
        tvContent.setGravity(Gravity.CENTER_VERTICAL);
        tvContent.setBackgroundResource(R.drawable.bac);
        int dp8 = ScreenUtils.dip2px(getContext(), 8f);
        int dp32 = ScreenUtils.dip2px(getContext(), 32f);
        tvContent.setPadding(dp8, 0, dp8, 0);
        addView(tvContent, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp32));
    }

    public void setTvContent(String str) {
        tvContent.setText(str);
    }

    public View getTvContent() {
        return tvContent;
    }

}
