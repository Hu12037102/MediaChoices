package com.xiaobai.media.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.xiaobai.media.R;
import com.xiaobai.media.utils.ScreenUtils;


public class ItemView extends RelativeLayout {

    public TextView mTvLeft;
    public TextView mTvRight;
    public ViewGroup mRootView;
    public View mTopLine;
    public View mBottomLine;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
        initEvent();
    }

    private void initEvent() {
        mRootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClickItem(v);
                }
            }
        });
    }



    private void initView() {
        View viewInflate = View.inflate(getContext(), R.layout.view_item, this);
        //View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.view_item,this);
        mRootView = viewInflate.findViewById(R.id.root_view);
        mTvLeft = viewInflate.findViewById(R.id.tv_left);
        mTvRight = viewInflate.findViewById(R.id.tv_right);
        mTvRight.setSingleLine(true);
        mTopLine = viewInflate.findViewById(R.id.top_line);
        mBottomLine = viewInflate.findViewById(R.id.bottom_line);
       /* mTvLeft.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) mTvLeft.getLayoutParams();
                mTvRight.setMaxWidth(ScreenUtils.getScreenWidth(getContext()) - (mTvLeft.getMeasuredWidth() + mTvLeft.getPaddingLeft() + mTvLeft.getPaddingRight() + layoutParams.leftMargin
                        + layoutParams.rightMargin + ScreenUtils.dp2px(getContext(), R.dimen.default_margin)));
            }
        });*/
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        if (typedArray != null && typedArray.getIndexCount() > 0) {
            mTvLeft.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(R.styleable.ItemView_title_left_icon),
                    typedArray.getDrawable(R.styleable.ItemView_title_top_icon), typedArray.getDrawable(R.styleable.ItemView_title_right_icon),
                    typedArray.getDrawable(R.styleable.ItemView_title_bottom_icon));
            mTvLeft.setText(typedArray.getString(R.styleable.ItemView_title_data));
            mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ItemView_title_text_size, (int) mTvLeft.getTextSize()));
            mTvLeft.setTextColor(typedArray.getColor(R.styleable.ItemView_title_text_color, mTvLeft.getPaint().getColor()));
            mTvLeft.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(R.styleable.ItemView_title_drawable_padding, 0));
            mTopLine.setVisibility(typedArray.getBoolean(R.styleable.ItemView_show_top_line, false) ? VISIBLE : GONE);
            mTopLine.setBackgroundColor(typedArray.getColor(R.styleable.ItemView_top_line_color, ContextCompat.getColor(getContext(), R.color.colorLine)));
            setLineHeight(mTopLine, typedArray.getDimensionPixelSize(R.styleable.ItemView_top_line_height, ScreenUtils.dp2px(getContext(), 0.5f)));
            setMargin(mTopLine, typedArray.getDimensionPixelSize(R.styleable.ItemView_top_line_margin_left, 0), typedArray.getDimensionPixelSize(R.styleable.ItemView_top_line_margin_top, 0),
                    typedArray.getDimensionPixelSize(R.styleable.ItemView_top_line_margin_right, 0), typedArray.getDimensionPixelSize(R.styleable.ItemView_top_line_margin_bottom, 0));
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(R.styleable.ItemView_content_left_icon),
                    typedArray.getDrawable(R.styleable.ItemView_content_top_icon), typedArray.getDrawable(R.styleable.ItemView_content_right_icon),
                    typedArray.getDrawable(R.styleable.ItemView_content_bottom_icon));
            mTvRight.setText(typedArray.getString(R.styleable.ItemView_content_data));
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ItemView_content_text_size, (int) mTvRight.getTextSize()));
            mTvRight.setTextColor(typedArray.getColor(R.styleable.ItemView_content_text_color, ContextCompat.getColor(getContext(), R.color.colorFF333333)));
            mTvRight.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(R.styleable.ItemView_content_drawable_padding, ScreenUtils.dp2px(getContext(), 5)));
            if (typedArray.getDrawable(R.styleable.ItemView_selector_drawable) != null) {
                mRootView.setBackground(typedArray.getDrawable(R.styleable.ItemView_selector_drawable));
            }
            setMargin(mBottomLine, typedArray.getDimensionPixelSize(R.styleable.ItemView_bottom_line_margin_left, 0), typedArray.getDimensionPixelSize(R.styleable.ItemView_bottom_line_margin_top, 0),
                    typedArray.getDimensionPixelSize(R.styleable.ItemView_bottom_line_margin_right, 0), typedArray.getDimensionPixelSize(R.styleable.ItemView_bottom_line_margin_bottom, 0));
            mBottomLine.setVisibility(typedArray.getBoolean(R.styleable.ItemView_show_bottom_line, false) ? VISIBLE : GONE);
            setLineHeight(mBottomLine, typedArray.getDimensionPixelSize(R.styleable.ItemView_bottom_line_height, ScreenUtils.dp2px(getContext(), 0.5f)));
            mBottomLine.setBackgroundColor(typedArray.getColor(R.styleable.ItemView_bottom_line_color, ContextCompat.getColor(getContext(), R.color.colorLine)));
            mTvLeft.getPaint().setFakeBoldText(typedArray.getBoolean(R.styleable.ItemView_left_text_is_bold, false));
            mTvRight.getPaint().setFakeBoldText(typedArray.getBoolean(R.styleable.ItemView_right_text_is_bold, false));
            boolean isShowLeft = typedArray.getBoolean(R.styleable.ItemView_content_show_left, false);
            mTvRight.setGravity(isShowLeft ? (Gravity.LEFT | Gravity.CENTER_VERTICAL) : (Gravity.RIGHT | Gravity.CENTER_VERTICAL));
            boolean isShowCenter = typedArray.getBoolean(R.styleable.ItemView_right_is_show_center,false);
            mTvRight.setGravity(isShowCenter?(Gravity.CENTER) : (Gravity.RIGHT | Gravity.CENTER_VERTICAL));
            ViewGroup.LayoutParams rightParams = mTvRight.getLayoutParams();
            int width = typedArray.getDimensionPixelSize(R.styleable.ItemView_right_text_width, 0);
            if (width > 0) {
                rightParams.width = width;
            }
            int height = typedArray.getDimensionPixelSize(R.styleable.ItemView_right_text_height, 0);
            if (height > 0) {
                rightParams.height = height;
            }
            mTvRight.setLayoutParams(rightParams);
            Drawable drawable = typedArray.getDrawable(R.styleable.ItemView_right_text_background);
            if (drawable != null) {
                mTvRight.setBackground(drawable);
            }
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,typedArray.getDimensionPixelSize(R.styleable.ItemView_right_text_size,ScreenUtils.dp2px(getContext(),16)));
            typedArray.recycle();

        }
    }


    private void setLineHeight(@NonNull View line, int lineHeight) {
        ViewGroup.LayoutParams lineParams = line.getLayoutParams();
        lineParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lineParams.height = lineHeight;
        line.setLayoutParams(lineParams);
    }

    private void setMargin(@NonNull View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = leftMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.bottomMargin = bottomMargin;
        view.setLayoutParams(layoutParams);
    }

    public void setTitleText(@NonNull CharSequence text) {
        mTvLeft.setText(text);
    }

    public void setTitleText(@StringRes int resString) {
        mTvLeft.setText(resString);
    }

    public void setContentText(@NonNull String text) {
        mTvRight.setText(text);
    }

    public void setContentText(@StringRes int resString) {
        mTvRight.setText(resString);
    }

    public void setTitleIcon(@DrawableRes int leftDrawableRes, @DrawableRes int topDrawableRes,
                             @DrawableRes int rightDrawableRes, @DrawableRes int bottomDrawableRes) {
        mTvLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawableRes, topDrawableRes, rightDrawableRes, bottomDrawableRes);
    }

    public void setTitleIcon(@Nullable Drawable leftDrawable, @Nullable Drawable topDrawable, @Nullable Drawable rightDrawable, @Nullable Drawable bottomDrawable) {
        mTvLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    public void setContentIcon(@DrawableRes int leftDrawableRes, @DrawableRes int topDrawableRes,
                               @DrawableRes int rightDrawableRes, @DrawableRes int bottomDrawableRes) {
        mTvRight.setCompoundDrawablesWithIntrinsicBounds(leftDrawableRes, topDrawableRes, rightDrawableRes, bottomDrawableRes);
    }

    public void setContentIcon(@Nullable Drawable leftDrawable, @Nullable Drawable topDrawable, @Nullable Drawable rightDrawable, @Nullable Drawable bottomDrawable) {
        mTvRight.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultHeight = ScreenUtils.dp2px(getContext(), IView.DEFAULT_ITEM_VIEW_HEIGHT);
        switch (measureHeightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                ScreenUtils.setDefaultRootViewSize(getContext(), mRootView, IView.DEFAULT_ITEM_VIEW_HEIGHT);
                break;
            case MeasureSpec.EXACTLY:
                resultHeight = measureHeightSize;
                break;
            default:
                break;
        }
        setMeasuredDimension(ScreenUtils.screenWidth(getContext()), resultHeight);
    }

    public interface OnItemClickListener {
        void onClickItem(View view);
    }
}
