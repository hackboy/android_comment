// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeSpinnerHelper.java

package android.widget;

import android.content.Context;
import android.text.method.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

// Referenced classes of package android.widget:
//            DoubleDigitManager, TextView, TextSwitcher, ImageView, 
//            LinearLayout, ViewSwitcher

class TimeSpinnerHelper
{
    public static final class AnimationType extends Enum
    {

        public static final AnimationType[] values()
        {
            return (AnimationType[])$VALUES.clone();
        }

        public static AnimationType valueOf(String name)
        {
            return (AnimationType)Enum.valueOf(android/widget/TimeSpinnerHelper$AnimationType, name);
        }

        public static final AnimationType Up;
        public static final AnimationType Down;
        public static final AnimationType Press;
        private static final AnimationType $VALUES[];

        static 
        {
            Up = new AnimationType("Up", 0);
            Down = new AnimationType("Down", 1);
            Press = new AnimationType("Press", 2);
            $VALUES = (new AnimationType[] {
                Up, Down, Press
            });
        }

        private AnimationType(String s, int i)
        {
            super(s, i);
        }
    }

    public static interface CallBack
    {

        public abstract void arrowUp();

        public abstract void arrowDown();

        public abstract boolean singleDigitIntermediate(int i);

        public abstract void singleDigitFinal(int i);

        public abstract boolean twoDigitsFinal(int i, int j);

        public abstract void aMPressed();

        public abstract void pMPressed();
    }


    public TimeSpinnerHelper(Context context, LinearLayout group, ImageView upArrow, ImageView downArrow, TextSwitcher textView, CallBack callBack)
    {
        mIsDoubleDigitAware = false;
        mShowUpArrow = true;
        mShowDownArrow = true;
        mSwitchedViewFactory = new ViewSwitcher.ViewFactory() {

            public View makeView()
            {
                TextView view = new TextView(mContext);
                TextView existingView = (TextView)mTextView.getCurrentView();
                if(existingView != null)
                    view.setTextColor(existingView.getNormalTextColor());
                return view;
            }

            final TimeSpinnerHelper this$0;

            
            {
                this$0 = TimeSpinnerHelper.this;
                super();
            }
        };
        mContext = context;
        mGroup = group;
        mUpArrow = upArrow;
        mDownArrow = downArrow;
        mTextView = textView;
        mTextView.setFactory(mSwitchedViewFactory);
        animateTopToBottom();
        mCallBack = callBack;
        mUpArrow.setVisibility(4);
        mDownArrow.setVisibility(4);
        mGroup.setFocusable(true);
        mGroup.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            public void onFocusChanged(View v, boolean hasFocus)
            {
                boolean shouldShowUp = hasFocus && mShowUpArrow;
                boolean shouldShowDown = hasFocus && mShowDownArrow;
                mUpArrow.setVisibility(shouldShowUp ? 0 : 4);
                mDownArrow.setVisibility(shouldShowDown ? 0 : 4);
            }

            final TimeSpinnerHelper this$0;

            
            {
                this$0 = TimeSpinnerHelper.this;
                super();
            }
        });
        mDoubleDigitManager = new DoubleDigitManager(1200L, new DoubleDigitManager.CallBack() {

            public boolean singleDigitIntermediate(int digit)
            {
                return mCallBack.singleDigitIntermediate(digit);
            }

            public void singleDigitFinal(int digit)
            {
                mCallBack.singleDigitFinal(digit);
            }

            public boolean twoDigitsFinal(int digit1, int digit2)
            {
                return mCallBack.twoDigitsFinal(digit1, digit2);
            }

            final TimeSpinnerHelper this$0;

            
            {
                this$0 = TimeSpinnerHelper.this;
                super();
            }
        });
        mGroup.setKeyListener(new android.view.View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() != 0)
                    return false;
                if(keyCode == 19)
                {
                    mCallBack.arrowUp();
                    return true;
                }
                if(keyCode == 20)
                {
                    mCallBack.arrowDown();
                    return true;
                }
                KeyCharacterMap keyMap = KeyCharacterMap.load(event.getKeyboardDevice());
                boolean consumed = false;
                char c = keyMap.getNumber(keyCode);
                if(c >= '0' && c <= '9')
                {
                    int digit = c - 48;
                    if(mIsDoubleDigitAware)
                        mDoubleDigitManager.reportDigit(digit);
                    else
                        mCallBack.singleDigitFinal(c - 48);
                    consumed = true;
                }
                if(keyMap.getMatch(keyCode, TimeSpinnerHelper.sAmChars) != 0)
                {
                    mCallBack.aMPressed();
                    consumed = true;
                } else
                if(keyMap.getMatch(keyCode, TimeSpinnerHelper.sPmChars) != 0)
                {
                    mCallBack.pMPressed();
                    consumed = true;
                }
                return consumed;
            }

            final TimeSpinnerHelper this$0;

            
            {
                this$0 = TimeSpinnerHelper.this;
                super();
            }
        });
    }

    public int getBaseline()
    {
        return mGroup.getBaseline();
    }

    public void setIsDoubleDigitAware(boolean isDoubleDigitAware)
    {
        mIsDoubleDigitAware = isDoubleDigitAware;
    }

    public void show()
    {
        mGroup.setVisibility(0);
    }

    public void hide()
    {
        mGroup.setVisibility(8);
    }

    public void takeFocus()
    {
        mGroup.requestFocus();
    }

    public void setShowUpArrow(boolean showUpArrow)
    {
        mShowUpArrow = showUpArrow;
        if(mGroup.isFocused())
            mUpArrow.setVisibility(showUpArrow ? 0 : 4);
    }

    public void setShowDownArrow(boolean showDownArrow)
    {
        mShowDownArrow = showDownArrow;
        if(mGroup.isFocused())
            mDownArrow.setVisibility(showDownArrow ? 0 : 4);
    }

    public void setDisplay(String value, AnimationType animationType)
    {
        TextView existingView = (TextView)mTextView.getCurrentView();
        if(existingView != null && value.contentEquals(existingView.getText()))
            return;
        static class _cls5
        {

            static final int $SwitchMap$android$widget$TimeSpinnerHelper$AnimationType[];

            static 
            {
                $SwitchMap$android$widget$TimeSpinnerHelper$AnimationType = new int[AnimationType.values().length];
                try
                {
                    $SwitchMap$android$widget$TimeSpinnerHelper$AnimationType[AnimationType.Down.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$widget$TimeSpinnerHelper$AnimationType[AnimationType.Up.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$widget$TimeSpinnerHelper$AnimationType[AnimationType.Press.ordinal()] = 3;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        switch(_cls5..SwitchMap.android.widget.TimeSpinnerHelper.AnimationType[animationType.ordinal()])
        {
        case 1: // '\001'
            animateTopToBottom();
            break;

        case 2: // '\002'
            animateBottomToTop();
            break;

        case 3: // '\003'
            animateKeyPressed();
            break;
        }
        mTextView.setText(value);
    }

    public void setTextColor(int color)
    {
        ((TextView)mTextView.getCurrentView()).setTextColor(color);
    }

    private void animateTopToBottom()
    {
        mTextView.setInAnimation(AnimationUtils.loadAnimation(mContext, 0x103001c));
        mTextView.setOutAnimation(AnimationUtils.loadAnimation(mContext, 0x103001d));
    }

    private void animateBottomToTop()
    {
        mTextView.setInAnimation(AnimationUtils.loadAnimation(mContext, 0x103001b));
        mTextView.setOutAnimation(AnimationUtils.loadAnimation(mContext, 0x103001e));
    }

    private void animateKeyPressed()
    {
        mTextView.setInAnimation(AnimationUtils.loadAnimation(mContext, 0x1030004));
        mTextView.setOutAnimation(AnimationUtils.loadAnimation(mContext, 0x1030005));
    }

    private boolean mIsDoubleDigitAware;
    private DoubleDigitManager mDoubleDigitManager;
    private boolean mShowUpArrow;
    private boolean mShowDownArrow;
    private final Context mContext;
    private final LinearLayout mGroup;
    private final ImageView mUpArrow;
    private final ImageView mDownArrow;
    private final TextSwitcher mTextView;
    private final CallBack mCallBack;
    private static final long DOUBLE_DIGIT_TIMEOUT_MILLIS = 1200L;
    private static final char sAmChars[] = {
        'a', 'A'
    };
    private static final char sPmChars[] = {
        'p', 'P'
    };
    private ViewSwitcher.ViewFactory mSwitchedViewFactory;












}
