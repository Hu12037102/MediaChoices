package com.xiaobai.media.weight;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.xiaobai.media.R;
import com.xiaobai.media.utils.PhoneUtils;
import com.xiaobai.media.utils.ScreenUtils;


public class TitleView extends RelativeLayout {

    public TextView mTvBack;
    public TextView mTvCenter;
    public TextView mTvSure;
    public ViewGroup mViewRoot;
    private boolean mBackIsFinish;
    private View mLineBottom;
    public AppCompatImageView mAivCenter;

    public void setOnBackViewClickListener(OnBackViewClickListener onBackViewClickListener) {
        this.onBackViewClickListener = onBackViewClickListener;
    }

    private OnBackViewClickListener onBackViewClickListener;

    public void setOnTitleViewClickListener(OnTitleViewClickListener onTitleViewClickListener) {
        this.onTitleViewClickListener = onTitleViewClickListener;
    }

    private OnTitleViewClickListener onTitleViewClickListener;

    public void setOnSureViewClickListener(OnSureViewClickListener onSureViewClickListener) {
        this.onSureViewClickListener = onSureViewClickListener;
    }

    public void setOnCenterClickListener(OnCenterClickListener onCenterClickListener) {
        this.onCenterClickListener = onCenterClickListener;
    }

    private OnCenterClickListener onCenterClickListener;

    private OnSureViewClickListener onSureViewClickListener;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
        initEvent();

    }


    private void initView() {
        View inflate = View.inflate(getContext(), R.layout.view_title, this);
        mViewRoot = inflate.findViewById(R.id.root_view);
        mTvBack = inflate.findViewById(R.id.tv_back);
        mTvCenter = inflate.findViewById(R.id.tv_center);
        mTvSure = inflate.findViewById(R.id.tv_sure);
        mLineBottom = inflate.findViewById(R.id.bottom_line);
        mAivCenter = inflate.findViewById(R.id.aiv_center);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        if (typedArray.getIndexCount() > 0) {
            mViewRoot.setBackgroundColor(typedArray.getColor(R.styleable.TitleView_group_color, ContextCompat.getColor(getContext(), R.color.colorTheme)));
            mTvBack.setText(typedArray.getString(R.styleable.TitleView_back_data));
            mTvBack.setVisibility(typedArray.getBoolean(R.styleable.TitleView_back_text_is_show, true) ? VISIBLE : GONE);
            mBackIsFinish = typedArray.getBoolean(R.styleable.TitleView_back_click_is_finish, true);
            mTvBack.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(R.styleable.TitleView_back_left_drawable), typedArray.getDrawable(R.styleable.TitleView_back_top_drawable),
                    typedArray.getDrawable(R.styleable.TitleView_back_right_drawable), typedArray.getDrawable(R.styleable.TitleView_back_bottom_drawable));
            mTvBack.setCompoundDrawablePadding(R.styleable.TitleView_back_drawable_padding);
            mTvBack.setPadding(typedArray.getDimensionPixelSize(R.styleable.TitleView_back_text_padding_left, ScreenUtils.dp2px(getContext(), 15)), typedArray.getDimensionPixelSize(R.styleable.TitleView_back_text_padding_top, 0),
                    typedArray.getDimensionPixelSize(R.styleable.TitleView_back_text_padding_right, ScreenUtils.dp2px(getContext(), 15)), typedArray.getDimensionPixelSize(R.styleable.TitleView_back_text_padding_bottom, 0));
            mTvBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.TitleView_back_text_size, (int) mTvBack.getTextSize()));
            mTvBack.setTextColor(typedArray.getColor(R.styleable.TitleView_back_text_color, ContextCompat.getColor(getContext(), R.color.color1)));
            mTvCenter.setText(typedArray.getString(R.styleable.TitleView_center_data));
            mTvCenter.setTextColor(typedArray.getColor(R.styleable.TitleView_center_text_color, ContextCompat.getColor(getContext(), R.color.color1)));
            mTvCenter.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.TitleView_center_text_size, (int) mTvCenter.getTextSize()));
            mTvSure.setText(typedArray.getString(R.styleable.TitleView_sure_data));
            mTvSure.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(R.styleable.TitleView_sure_left_drawable), typedArray.getDrawable(R.styleable.TitleView_sure_top_drawable),
                    typedArray.getDrawable(R.styleable.TitleView_sure_right_drawable), typedArray.getDrawable(R.styleable.TitleView_sure_bottom_drawable));
            mTvSure.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_size, ScreenUtils.dp2px(getContext(), 16)));
            mTvSure.setTextColor(typedArray.getColor(R.styleable.TitleView_sure_text_color, ContextCompat.getColor(getContext(), R.color.color1)));
            mTvSure.setCompoundDrawablePadding(R.styleable.TitleView_sure_drawable_padding);
            mTvSure.setPadding(typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_padding_left, ScreenUtils.dp2px(getContext(), 15)), typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_padding_top, 0),
                    typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_padding_right, ScreenUtils.dp2px(getContext(), 15)), typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_padding_bottom, 0));
            mLineBottom.setVisibility(typedArray.getBoolean(R.styleable.TitleView_title_show_bottom_line, false) ? VISIBLE : GONE);
            int widthSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_width_size, 0);
            int heightSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_height_size, 0);

            ViewGroup.MarginLayoutParams sureParams = (MarginLayoutParams) mTvSure.getLayoutParams();
            if (widthSize > 0) {
                sureParams.width = widthSize;
            }
            if (heightSize > 0) {
                sureParams.height = heightSize;
            }
            sureParams.leftMargin = typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_margin_left, 0);
            sureParams.rightMargin = typedArray.getDimensionPixelSize(R.styleable.TitleView_sure_text_margin_right, 0);
            mTvSure.setLayoutParams(sureParams);
            // mTvSure.setBackgroundResource(R.drawable.shape_click_button_view);
            //typedArray.getDimensionPixelSize(R.styleable.Titl_s_l)
            mTvSure.setBackground(typedArray.getDrawable(R.styleable.TitleView_sure_text_background));
            mTvSure.setEnabled(typedArray.getBoolean(R.styleable.TitleView_sure_text_clickable, true));
            mAivCenter.setImageDrawable(typedArray.getDrawable(R.styleable.TitleView_center_src_res));

            typedArray.recycle();
        }
    }

    private void initEvent() {
        mTvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleViewClickListener != null) {
                    onTitleViewClickListener.onBackClick(v);
                }
                if (onBackViewClickListener != null) {
                    onBackViewClickListener.onBackClick(v);
                }
                if (mBackIsFinish && getContext() instanceof Activity) {
                    PhoneUtils.hideSoftKeyboard(v);
                    ((Activity) getContext()).finish();
                }
            }
        });
        mTvSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleViewClickListener != null) {
                    onTitleViewClickListener.onSureClick(v);
                }
                if (onSureViewClickListener != null) {
                    onSureViewClickListener.onSureClick(v);
                }
            }
        });
        mTvCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCenterClickListener != null) {
                    onCenterClickListener.onCenterClick(v);
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultHeightSize = ScreenUtils.dp2px(getContext(), IView.DEFAULT_TITLE_VIEW_HEIGHT);
        switch (heightMeasureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                ScreenUtils.setDefaultRootViewSize(getContext(), mViewRoot, IView.DEFAULT_TITLE_VIEW_HEIGHT);
                break;
            case MeasureSpec.EXACTLY:
                resultHeightSize = heightMeasureSize;
                break;
            default:
                break;
        }
        setMeasuredDimension(widthMeasureSpec, resultHeightSize);
    }

    public interface OnTitleViewClickListener {
        void onBackClick(@NonNull View view);

        void onSureClick(@NonNull View view);
    }

    public interface OnBackViewClickListener {
        void onBackClick(@NonNull View view);
    }

    public interface OnSureViewClickListener {
        void onSureClick(@NonNull View view);
    }

    public interface OnCenterClickListener {
        void onCenterClick(@NonNull View view);
    }
}
