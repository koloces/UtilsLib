package com.koloce.kulibrary.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.koloce.kulibrary.R;


/**
 * Created by koloces on 2019/3/4
 * 获取验证码倒计时工具
 */
public class CountDownTimerHelper extends CountDownTimer {
    private TextView mTextView;
    private String canClickStrHint;//可以点击时候的文字提示
    private int canClickTextColor;//可以点击时候的文字颜色
    private int canClickTextBgId;//可以点击时候的文字背景
    private int cannotClickTextColor;//不可以点击时候的文字颜色
    private int cannotClickTextBgId;//不可以点击时候的文字背景

    private CountDownTimerHelper(Builder builder) {
        super(builder.maxTime, 1000);
        this.mTextView = builder.textView;
        this.canClickStrHint = builder.canClickStrHint;
        this.canClickTextColor = builder.canClickTextColor;
        this.canClickTextBgId = builder.canClickTextBgId;
        this.cannotClickTextColor = builder.cannotClickTextColor;
        this.cannotClickTextBgId = builder.cannotClickTextBgId;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); // 设置不可点击
        millisUntilFinished -= 1;
        mTextView.setText(millisUntilFinished / 1000 + "s"); // 设置倒计时时间

        if (cannotClickTextBgId != -1){
            mTextView.setBackgroundResource(cannotClickTextBgId);
        }
        if (cannotClickTextColor != -1){
            mTextView.setTextColor(cannotClickTextColor);
        } else {
            mTextView.setTextColor(mTextView.getResources().getColor(R.color.line_color));
        }
    }

    @Override
    public void onFinish() {
        mTextView.setText(canClickStrHint);
        mTextView.setClickable(true);// 重新获得点击

        if (canClickTextBgId != -1){
            mTextView.setBackgroundResource(canClickTextBgId);
        }
        if (canClickTextColor != -1){
            mTextView.setTextColor(canClickTextColor);
        }
    }

    public static class Builder{
        private TextView textView;
        private int maxTime = 60000;
        private String canClickStrHint;//可以点击时候的文字提示
        private int canClickTextColor = -1;//可以点击时候的文字颜色
        private int canClickTextBgId = -1;//可以点击时候的文字背景
        private int cannotClickTextColor = -1;//不可以点击时候的文字颜色
        private int cannotClickTextBgId = -1;//不可以点击时候的文字背景

        public Builder setTextView(TextView textView) {
            this.textView = textView;
            return this;
        }

        public Builder(TextView textView, String canClickStrHint) {
            this.textView = textView;
            this.canClickStrHint = canClickStrHint;
        }

        /**
         * 以秒为单位
         * @param maxTime
         * @return
         */
        public Builder setMaxTime(int maxTime) {
            this.maxTime = maxTime * 1000;
            return this;
        }

        public Builder setCanClickTextColor(int canClickTextColor) {
            this.canClickTextColor = canClickTextColor;
            return this;
        }

        public Builder setCanClickTextBgId(int canClickTextBgId) {
            this.canClickTextBgId = canClickTextBgId;
            return this;
        }

        public Builder setCannotClickTextColor(int cannotClickTextColor) {
            this.cannotClickTextColor = cannotClickTextColor;
            return this;
        }

        public Builder setCannotClickTextBgId(int cannotClickTextBgId) {
            this.cannotClickTextBgId = cannotClickTextBgId;
            return this;
        }

        public CountDownTimerHelper build(){
            return new CountDownTimerHelper(this);
        }
    }
}
