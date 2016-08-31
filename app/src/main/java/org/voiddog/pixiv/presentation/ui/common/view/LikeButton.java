package org.voiddog.pixiv.presentation.ui.common.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import org.voiddog.pixiv.R;

/**
 * Created by qigengxin on 16/8/31.
 */
public class LikeButton extends ViewGroup{

    private Drawable mLikeIcon, mLikedIcon;
    private ImageView mIvLike, mIvLiked;
    private LikeAnimator mAnimator;
    private boolean mLiked = false;
    private OnLikeListener mLikeListener;
    private OnClickListener mCustomClickListener;

    public LikeButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setOnLikeListener(OnLikeListener likeListener){
        mLikeListener = likeListener;
    }

    /**
     * 设置喜欢状态
     * @param liked 是否喜欢
     */
    public void setLikeState(boolean liked){
        mLiked = liked;
        mAnimator.cancelAnimation();

        resetView();

        if(liked){
            mIvLike.setVisibility(GONE);
            mIvLiked.setVisibility(VISIBLE);
        }
        else{
            mIvLike.setVisibility(VISIBLE);
            mIvLiked.setVisibility(GONE);
        }
    }

    public boolean isLiked(){
        return mLiked;
    }

    void resetView(){
        mIvLiked.setScaleX(1);
        mIvLiked.setScaleY(1);
        mIvLiked.setVisibility(VISIBLE);

        mIvLike.setScaleX(1);
        mIvLike.setScaleY(1);
        mIvLike.setVisibility(VISIBLE);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mCustomClickListener = l;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = getChildCount() - 1; i >= 0; i--){
            View view = getChildAt(i);
            view.layout(getPaddingLeft(), getPaddingTop()
                    , getPaddingLeft() + view.getMeasuredWidth()
                    , getPaddingTop() + view.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED){
            widthSize = Math.max(mLikedIcon.getIntrinsicWidth(), mLikeIcon.getIntrinsicWidth());
        }

        if(heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            heightSize = Math.max(mLikedIcon.getIntrinsicHeight(), mLikeIcon.getIntrinsicHeight());
        }

        setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight()
                , heightSize + getPaddingTop() + getPaddingBottom());

        measureChildren(
                MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        );
    }

    void init(Context context, AttributeSet attrs, int defStyleAttr){
        mLikedIcon = getResources().getDrawable(R.drawable.ic_button_liked);
        mLikeIcon = getResources().getDrawable(R.drawable.ic_button_like);

        mIvLike = new ImageView(context);
        mIvLike.setImageDrawable(mLikeIcon);
        mIvLike.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mIvLiked = new ImageView(context);
        mIvLiked.setImageDrawable(mLikedIcon);
        mIvLiked.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIvLiked.setVisibility(GONE);

        addView(mIvLiked);
        addView(mIvLike);

        mAnimator = new LikeAnimator();

        super.setOnClickListener(new LikeClickListener());
        setClipToPadding(false);
    }

    class LikeClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(isLiked()){
                mAnimator.playUnLike();
            }
            else{
                mAnimator.playLike();
            }
            mLiked = !mLiked;
            if(mLikeListener != null){
                mLikeListener.onLikeChanged(mLiked);
            }
            if(mCustomClickListener != null){
                mCustomClickListener.onClick(v);
            }
        }
    }

    class LikeAnimator implements ValueAnimator.AnimatorUpdateListener{

        private boolean liked = false;
        private ValueAnimator mValueAnimator;

        public LikeAnimator(){
            mValueAnimator = new ValueAnimator();
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.setDuration(200);
            mValueAnimator.setInterpolator(new DecelerateInterpolator(2));
        }

        public void cancelAnimation(){
            mValueAnimator.cancel();
        }

        public void playLike(){
            liked = true;
            mValueAnimator.cancel();
            mValueAnimator.setFloatValues(1, 0);
            mValueAnimator.start();
        }

        public void playUnLike(){
            liked = false;
            mValueAnimator.cancel();
            mValueAnimator.setFloatValues(0, 1);
            mValueAnimator.start();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            mIvLike.setScaleX(value);
            mIvLike.setScaleY(value);

            mIvLiked.setVisibility(VISIBLE);
            mIvLike.setVisibility(VISIBLE);

            if(liked){
                mIvLiked.scrollTo(0, 0);
                float v = 1.0f - value;
                mIvLiked.setScaleX(v);
                mIvLiked.setScaleY(v);
            }
            else {
                mIvLiked.setScaleX(1.0f - value);
                mIvLiked.setScaleY(1.0f - value);
            }
            postInvalidate();
        }
    }

    public interface OnLikeListener{
        void onLikeChanged(boolean liked);
    }
}
