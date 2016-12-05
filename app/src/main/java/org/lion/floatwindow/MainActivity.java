package org.lion.floatwindow;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WindowManager mWindowManager;
    private static final String TAG = "====_MainActivity";
    private LinearLayout mFloatLayout;
    private Button mFloatView;
    private WindowManager.LayoutParams mWmParams;
    private WebView mWebView;
    private final int TOTAL_INDEX = 10;
    private int mCurrentIndex = 0;

    private final int COUNT = 5;
    private int mCurrentCount = 0;
    private boolean mFlowViewShow = true;
    private TextView mTvIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("abcd ");
        }
        mWebView = ((WebView) findViewById(R.id.wv_question));
        mTvIndicator = ((TextView) findViewById(R.id.tv_indicator));
        mockQuestion(mCurrentIndex);
        loadAnswerView(mCurrentIndex);
        findViewById(R.id.tv_next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++mCurrentIndex;
                if (mCurrentIndex < 0) {
                    mCurrentIndex = 0;
                }
                if (mCurrentIndex >= TOTAL_INDEX) {
                    mCurrentIndex = TOTAL_INDEX - 1;
                }
                mTvIndicator.setText((mCurrentIndex + 1) + "/" + TOTAL_INDEX);
                mCurrentCount = 0;
                mockQuestion(mCurrentIndex);
                loadAnswerView(mCurrentIndex);
            }
        });
    }

    private void mockQuestion(int currentIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("<p>")
                    .append("qeustion ")
                    .append("html ")
                    .append("<b>")
                    .append(currentIndex + 1)
                    .append("</b>")
                    .append("</p>");
        }
        sb.append("<br/>").append("<br/>").append("<br/>").append("<br/>").append("<br/>").append("<br/>");

        mWebView.loadData(sb.toString(), "text/html", "UTF-8");
    }

    private void removeAnswerView(int index) {
        mWindowManager.removeView(mFloatLayout);
    }

    private void loadAnswerView(final int index) {
        if (mWindowManager == null) {
            //获取LayoutParams对象
            mWmParams = new WindowManager.LayoutParams();

            //获取的是LocalWindowManager对象
            mWindowManager = this.getWindowManager();
            Log.i(TAG, "mWindowManager1--->" + this.getWindowManager());
            //mWindowManager = getWindow().getWindowManager();
            Log.i(TAG, "mWindowManager2--->" + getWindow().getWindowManager());

            //获取的是CompatModeWrapper对象
            //mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
            Log.i(TAG, "mWindowManager3--->" + mWindowManager);
            mWmParams.format = PixelFormat.RGBA_8888;
            mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mWmParams.gravity = Gravity.BOTTOM;
            mWmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            LayoutInflater inflater = this.getLayoutInflater();//LayoutInflater.from(getApplication());
            mFloatLayout = (LinearLayout) inflater.inflate(getAnswerAreaLayout(), null);
            mWindowManager.addView(mFloatLayout, mWmParams);
        }


        if (mFloatLayout != null) {
            mFloatView = (Button) mFloatLayout.findViewById(R.id.bt);
            mFloatView.setText("index " + index + " count " + mCurrentCount);
            Log.i(TAG, "mFloatView" + mFloatView);
            Log.i(TAG, "mFloatView--parent-->" + mFloatView.getParent());
            Log.i(TAG, "mFloatView--parent--parent-->" + mFloatView.getParent().getParent());

            final TextView tvSmallIndicator = (TextView) mFloatLayout.findViewById(R.id.tv_small_indicator);
            tvSmallIndicator.setText((mCurrentCount + 1) + "/" + COUNT);
            mFloatLayout.findViewById(R.id.iv_prev).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentCount--;
                    if (mCurrentCount < 0) {
                        mCurrentCount = 0;
                    }
                    tvSmallIndicator.setText((mCurrentCount + 1) + "/" + COUNT);
                    mFloatView.setText("index " + index + " count " + mCurrentCount);
                }
            });

            mFloatLayout.findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentCount++;
                    if (mCurrentCount >= COUNT) {
                        mCurrentCount = COUNT - 1;
                    }
                    tvSmallIndicator.setText((mCurrentCount + 1) + "/" + COUNT);
                    mFloatView.setText("index " + index + " count " + mCurrentCount);
                }
            });

            final ImageView ivDown = (ImageView) mFloatLayout.findViewById(R.id.iv_down);
            ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFlowViewShow) {
                        mFloatView.setVisibility(View.GONE);
                        ivDown.setImageResource(R.drawable.up_arrow);
                    } else {
                        mFloatView.setVisibility(View.VISIBLE);
                        ivDown.setImageResource(R.drawable.down_arrow);
                    }
                    mFlowViewShow = !mFlowViewShow;
                }
            });
        }
    }

    private int getAnswerAreaLayout() {
        return R.layout.answer_area;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}

