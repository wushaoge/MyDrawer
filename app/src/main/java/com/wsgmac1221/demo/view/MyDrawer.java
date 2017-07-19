package com.wsgmac1221.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wsgmac1221.demo.R;

/**
 * TODO: document your custom view class.
 */
public class MyDrawer extends ViewGroup {

    private final String TAG = "MyDrawer";

    private static final int TYPE_LEFT = 0; //左边滑出
    private static final int TYPE_RIGHT = 1; //右边滑出
    private static final int TYPE_TOP = 2; //上边滑出
    private static final int TYPE_BOTTOM = 3; //下边滑出



    private ViewDragHelper viewDragHelper; //滑动处理的主要类


    /**  自定义属性CUSTOM START **/
    private boolean isOpen = true; //是否打开
    private boolean isKickBack = false;
    private int dragType = 3; //默认从下往上滑
    /**  自定义属性CUSTOM END **/


    private int mContentViewID = -1; //主体viewID
    private View mContentView; //主体view

    private int mDragContentViewID = -1; //要拖动的viewID
    private View mDragContextView; //要拖动的view

    private int mHandlerID = -1; //手柄ViewID
    private View mHandler; //手柄View


    private int myWidth = 0; //父类宽度
    private int myHeight = 0; //父类高度


    private int dragContentWidth = 0; //拖动view的宽度
    private int dragContentHeight = 0; //拖动view的高度

    private int handlerWidth = 0; //手柄宽度
    private int handlerHeight = 0; //手柄高度

    private int surPlusWidth = 0; //减去手柄剩余的宽度
    private int surPlusHeight = 0; //减去手柄剩余的高度

    private DefaultDragHelper defaultDragHelper;


    private MyDrawerListener myDrawerListener = null;

    private MyKickBackListener myKickBackListener;

    //初始化拖动view的坐标
    private int intitLeft = 0;
    private int initRight = 0;
    private int initTop = 0;
    private int initBottom = 0;



    public interface MyDrawerListener{
        /**
         * 隐藏的页面被打开
         */
        void open(); //打开

        /**
         * 隐藏的页面被关闭
         */
        void close(); //关闭

    }

    public interface MyKickBackListener{
        /**
         * 回弹的回调
         */
        void kickBack(); //回弹

    }

    //打开
    public void open(){
        defaultDragHelper.open();
    }
    //关闭
    public void close(){
        defaultDragHelper.close();
    }

    public void setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
    }

    public boolean getIsOpen(){
        return isOpen;
    }

    public void setMyDrawerListener(MyDrawerListener myDrawerListener) {
        this.myDrawerListener = myDrawerListener;
    }

    public void setMyKickBackListener(MyKickBackListener myKickBackListener){
        this.myKickBackListener = myKickBackListener;
    }

    public View getHandlerView(){
        if(mHandlerID != -1){
            return mHandler;
        }
        return null;
    }

    public View getDragContextView(){
        if(mDragContentViewID != -1){
            return mDragContextView;
        }
        return null;
    }


    public MyDrawer(Context context) {
        super(context);
        init(null, 0);
    }

    public MyDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyDrawer, defStyle, 0);

        /**
         *
         * 下面这坨都是自定义属性 详情请看attrs_my_drawer解释 ~\(≧▽≦)/~
         */
        mContentViewID = a.getResourceId(R.styleable.MyDrawer_content,-1);

        mDragContentViewID = a.getResourceId(R.styleable.MyDrawer_drag,-1);

        mHandlerID = a.getResourceId(R.styleable.MyDrawer_handler, -1);

        isOpen = a.getBoolean(R.styleable.MyDrawer_isOpen, false); //默认为关闭

        dragType = a.getInt(R.styleable.MyDrawer_dragtype, TYPE_BOTTOM); //默认为从下边滑出

        defaultDragHelper = new DefaultDragHelper();
        viewDragHelper = ViewDragHelper.create(this, 1.0f, defaultDragHelper);

    }


    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            postInvalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(mContentViewID == -1){
            Log.e(TAG,TAG+"的content属性不能为空");
            return;
        }

        if(mDragContentViewID == -1){
            Log.e(TAG,TAG+"的drag属性不能为空");
            return;
        }

        //手柄可以为空
        if(mHandlerID != -1){
            mHandler = this.findViewById(mHandlerID);
        }

        mContentView = this.findViewById(mContentViewID);
        mDragContextView = this.findViewById(mDragContentViewID);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");


        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);

        //View dragViewTemp = getChildAt(1);
        MarginLayoutParams paramsDrag = (MarginLayoutParams)mDragContextView.getLayoutParams();
        final int contentWidthSpec = getChildMeasureSpec(widthMeasureSpec,paramsDrag.leftMargin+paramsDrag.rightMargin,paramsDrag.width);
        final int contentHeightSpec = getChildMeasureSpec(heightMeasureSpec, paramsDrag.topMargin + paramsDrag.bottomMargin, paramsDrag.height);
        mDragContextView.measure(contentWidthSpec,contentHeightSpec);
        //mDragContextView = dragViewTemp;


        //View contentTemp = getChildAt(0);
        MarginLayoutParams paramsContent = (MarginLayoutParams)mContentView.getLayoutParams();
        final int dragWidthSpec = getChildMeasureSpec(widthMeasureSpec, paramsContent.leftMargin + paramsContent.rightMargin, paramsContent.width);
        final int dragHeightSpec = getChildMeasureSpec(heightMeasureSpec, paramsContent.topMargin + paramsContent.bottomMargin, paramsContent.height);
        mContentView.measure(dragWidthSpec, dragHeightSpec);
        //mContentView = contentTemp;

    }


    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG,"onLayout");
        //super.onLayout(changed,l,t,r,b);
        myWidth = getWidth();
        myHeight = getHeight();

        dragContentWidth = mDragContextView.getMeasuredWidth();
        dragContentHeight = mDragContextView.getMeasuredHeight();

        if(mHandler != null){
            handlerWidth = mHandler.getMeasuredWidth();
            handlerHeight = mHandler.getMeasuredHeight();
        }

        //减去手柄的宽度和高度
        surPlusWidth = dragContentWidth - handlerWidth;
        surPlusHeight = dragContentHeight - handlerHeight;

//        Log.e(TAG,"宽度===="+myWidth);
//        Log.e(TAG,"高度===="+myHeight);
//        Log.e(TAG,"dragType===="+dragType);
//        Log.e(TAG,"isOPen===="+isOpen);
//
//        Log.e(TAG,"dragContentWidth宽度===="+dragContentWidth);
//        Log.e(TAG,"dragContentHeight高度===="+dragContentHeight);

        //默认里面只有两个view

        //设置contentview的位置
        MarginLayoutParams contentParam = (MarginLayoutParams) mContentView.getLayoutParams();
        mContentView.layout(contentParam.leftMargin, contentParam.topMargin, contentParam.leftMargin+mContentView.getMeasuredWidth(),  contentParam.topMargin+mContentView.getMeasuredHeight());

        //判断滑动方向和默认是否打开来确定滑动view的具体位置
        MarginLayoutParams cParams = (MarginLayoutParams) mDragContextView.getLayoutParams();

//        Log.e(TAG, "cParams.leftMargin====" + cParams.leftMargin);
//        Log.e(TAG, "cParams.rightMargin====" + cParams.rightMargin);
//        Log.e(TAG, "cParams.topMargin====" + cParams.topMargin);
//        Log.e(TAG, "cParams.bottomMargin====" + cParams.bottomMargin);


        //计算坐标真烦
        if(dragType==TYPE_LEFT){
            if(isOpen){
                intitLeft = cParams.leftMargin;
                initRight = intitLeft + dragContentWidth;
            }else{
                intitLeft = -surPlusWidth;
                initRight = intitLeft + dragContentWidth;
            }
            initTop = cParams.topMargin;
            initBottom = initTop + dragContentHeight;
        }else if(dragType==TYPE_RIGHT){
            if(isOpen){
                intitLeft = myWidth-dragContentWidth;
                initRight = intitLeft + dragContentWidth;
            }else{
                intitLeft = myWidth-handlerWidth;
                initRight = intitLeft + dragContentWidth;
            }
            initTop = cParams.topMargin;
            initBottom = initTop + dragContentHeight;
        }else if(dragType==TYPE_TOP){
            if(isOpen){
                initTop = cParams.topMargin;
                initBottom = initTop+dragContentHeight;
            }else{
                initTop = -surPlusHeight;
                initBottom = initTop + dragContentHeight;
            }
            intitLeft = cParams.leftMargin;
            initRight = intitLeft + dragContentWidth;
        }else if(dragType==TYPE_BOTTOM){
            if(isOpen){
                initTop = myHeight - dragContentHeight;
                initBottom = initTop + dragContentHeight;
            }else{
                initTop = myHeight-handlerHeight;
                initBottom = myHeight + surPlusHeight;
            }
            intitLeft = cParams.leftMargin;
            initRight = intitLeft + dragContentWidth;
        }

//        Log.e(TAG,"intitLeft===="+intitLeft);
//        Log.e(TAG,"initRight===="+initRight);
//        Log.e(TAG,"initTop===="+initTop);
//        Log.e(TAG, "initBottom====" + initBottom);
         mDragContextView.layout(intitLeft, initTop, initRight, initBottom);


    }




    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }


    private class DefaultDragHelper extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if(child == mDragContextView){
                return true;
            }
            return false;
        }

        //手指释放的时候回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if(dragType==TYPE_LEFT){
                int top = mDragContextView.getLeft();
                int critical = (int)(- surPlusWidth/2.0f);
                if(top>=critical){
                    //打开
                    open();
                }else{
                    close();
                }
            }

            if(dragType==TYPE_RIGHT){
                if(mDragContextView.getLeft()<=myWidth-dragContentWidth+(surPlusWidth/2.0f)){
                    //打开
                    open();
                }else{
                    close();
                }
            }

            if(dragType==TYPE_TOP){
                int top = mDragContextView.getTop();
                int critical = (int)(- surPlusHeight/2.0f);
                if(top>=critical){
                    //打开
                    open();
                }else{
                    close();
                }
            }

            if(dragType==TYPE_BOTTOM){
                int top = mDragContextView.getTop();
                int critical = (int)(myHeight - surPlusHeight/2.0f - handlerHeight);
                if(top<=critical){
                    //打开
                    open();
                    isKickBack = true;
                    Log.d(TAG, "onViewReleased: 回弹了");
                }else{
                    close();
                }
            }

        }


        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(dragType==TYPE_LEFT){
                if(left<-surPlusWidth){
                    return -surPlusWidth;
                }else if(left>0){
                    return 0;
                }
                return left;
            }
            if(dragType==TYPE_RIGHT){
                if(left<myWidth-dragContentWidth){ //说明已经拖到左边了
                    return myWidth-dragContentWidth;
                }else if(left>myWidth - handlerWidth){ //说明已经拖到右边了
                    return myWidth - handlerWidth;
                }
                return left;
            }
            return intitLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(dragType==TYPE_TOP){
                if(top<-surPlusHeight){
                    return -surPlusHeight;
                }else if(top>0){
                    return 0;
                }
                return top;
            }
            if(dragType==TYPE_BOTTOM){
                if(top<myHeight-dragContentHeight){ //说明已经拖到顶部了
                    return myHeight-dragContentHeight;
                }else if(top>myHeight - handlerHeight){ //说明已经拖到底部了
                    return myHeight - handlerHeight;
                }
                return top;
            }
            return initTop;
        }

        @Override
        public void onViewDragStateChanged(int state) {
//            Log.d(TAG, "onViewDragStateChanged: state"+state);
//            Log.d(TAG, "onViewDragStateChanged: mDragContextView.getTop()"+mDragContextView.getTop());
//            Log.d(TAG, "onViewDragStateChanged: (myHeight-dragContentHeight)"+(myHeight-dragContentHeight));
            if (state == 0 && mDragContextView.getTop()-(myHeight-dragContentHeight)<100) {
                if(myDrawerListener != null && getIsOpen() == false){
                    myDrawerListener.open();
                }
                isOpen = true;
                Log.d(TAG, "onViewDragStateChanged: 打开了");
            }else if (state == 0 && mDragContextView.getTop() > myHeight-dragContentHeight){
                Log.d(TAG, "onViewDragStateChanged: 关闭了");
                if(myDrawerListener != null && getIsOpen() == true){
                    myDrawerListener.close();
                }
                isOpen = false;
            }
            if(state == 0 && isKickBack){
                if (myKickBackListener != null) {
                    myKickBackListener.kickBack();
                }
                isKickBack = false;
            }
            super.onViewDragStateChanged(state);
        }

        //没有这个方法拖动view里面的控件就不能点击了
        @Override
        public int getViewVerticalDragRange(View child) {
            return myHeight;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return myHeight;
        }


        //打开
        private void open(){
            int finalLeft = 0;
            int finalTop = 0;

            if(dragType==TYPE_LEFT){
                finalLeft = 0;
                finalTop = initTop;
            }

            if(dragType==TYPE_RIGHT){
                finalLeft = myWidth - dragContentWidth;
                finalTop = initTop;
            }

            if(dragType==TYPE_TOP){
                finalLeft = intitLeft;
                finalTop = 0;
            }

            if(dragType==TYPE_BOTTOM){
                finalLeft = intitLeft;
                finalTop = myHeight - dragContentHeight;
            }

            viewDragHelper.smoothSlideViewTo(mDragContextView,finalLeft,finalTop);
            invalidate();
        }

        //关闭
        private void close(){
            int finalLeft = 0;
            int finalTop = 0;

            if(dragType==TYPE_LEFT){
                finalLeft = -surPlusWidth;
                finalTop = initTop;
            }

            if(dragType==TYPE_RIGHT){
                finalLeft = myWidth - handlerWidth;
                finalTop = initTop;
            }

            if(dragType==TYPE_TOP){
                finalLeft = intitLeft;
                finalTop = -surPlusHeight;
            }

            if(dragType==TYPE_BOTTOM){
                finalLeft = intitLeft;
                finalTop = myHeight - handlerHeight;
            }

            viewDragHelper.smoothSlideViewTo(mDragContextView,finalLeft,finalTop);
            invalidate();
        }


    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        boolean flag=viewDragHelper.shouldInterceptTouchEvent(event);
        return flag;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }


}
