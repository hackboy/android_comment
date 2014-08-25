// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialerFilter.java

package android.widget;

import android.content.Context;
import android.text.*;
import android.text.method.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import java.util.Map;

// Referenced classes of package android.widget:
//            RelativeLayout, EditText, ImageView

public class DialerFilter extends RelativeLayout
{

    public DialerFilter(Context context)
    {
        super(context);
    }

    public DialerFilter(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mInputFilters = (new InputFilter[] {
            new android.text.InputFilter.AllCaps()
        });
        mHint = (EditText)findViewById(0x1050002);
        if(mHint == null)
            throw new IllegalStateException("DialerFilter must have a child EditText named hint");
        mHint.setFilters(mInputFilters);
        mLetters = mHint;
        mLetters.setInputMethod(TextInputMethod.getInstance());
        mLetters.setMovementMethod(NullMovementMethod.getInstance());
        mLetters.setFocusable(false);
        mPrimary = (EditText)findViewById(0x1050008);
        if(mPrimary == null)
            throw new IllegalStateException("DialerFilter must have a child EditText named primary");
        mPrimary.setFilters(mInputFilters);
        mDigits = mPrimary;
        mDigits.setInputMethod(DialerInputMethod.getInstance());
        mDigits.setMovementMethod(NullMovementMethod.getInstance());
        mDigits.setFocusable(false);
        mIcon = (ImageView)findViewById(0x1050051);
        setFocusable(true);
        KeyCharacterMap kmap = KeyCharacterMap.load(0);
        mIsQwerty = kmap.getKeyboardType() != 1;
        if(mIsQwerty)
            Log.i("DialerFilter", "This device looks to be QWERTY");
        else
            Log.i("DialerFilter", "This device looks to be 12-KEY");
        mIsQwerty = true;
        setMode(1);
    }

    protected void onFocusChanged(boolean focused, int direction)
    {
        super.onFocusChanged(focused, direction);
        if(mIcon != null)
            mIcon.setVisibility(focused ? 0 : 8);
    }

    public boolean isQwertyKeyboard()
    {
        return mIsQwerty;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean handled = false;
        switch(keyCode)
        {
        case 65: // 'A'
            switch(mMode)
            {
            case 1: // '\001'
                handled = mDigits.onKeyDown(keyCode, event);
                handled &= mLetters.onKeyDown(keyCode, event);
                break;

            case 2: // '\002'
                handled = mLetters.onKeyDown(keyCode, event);
                if(mLetters.getText().length() == mDigits.getText().length())
                    setMode(1);
                break;

            case 3: // '\003'
                if(mDigits.getText().length() == mLetters.getText().length())
                {
                    mLetters.onKeyDown(keyCode, event);
                    setMode(1);
                }
                handled = mDigits.onKeyDown(keyCode, event);
                break;

            case 4: // '\004'
                handled = mDigits.onKeyDown(keyCode, event);
                break;

            case 5: // '\005'
                handled = mLetters.onKeyDown(keyCode, event);
                break;
            }
            break;

        default:
            switch(mMode)
            {
            default:
                break;

            case 1: // '\001'
                handled = mLetters.onKeyDown(keyCode, event);
                if(KeyEvent.isModifierKey(keyCode))
                {
                    mDigits.onKeyDown(keyCode, event);
                    handled = true;
                    break;
                }
                KeyCharacterMap kmap = KeyCharacterMap.load(event.getKeyboardDevice());
                boolean isPrint = kmap.isPrintingKey(keyCode);
                if(!isPrint && keyCode != 60 && keyCode != 59)
                    break;
                char c = kmap.getMatch(keyCode, DialerInputMethod.CHARACTERS);
                if(c != 0)
                    handled &= mDigits.onKeyDown(keyCode, event);
                else
                    setMode(2);
                break;

            case 3: // '\003'
            case 4: // '\004'
                handled = mDigits.onKeyDown(keyCode, event);
                break;

            case 2: // '\002'
            case 5: // '\005'
                handled = mLetters.onKeyDown(keyCode, event);
                break;
            }
            break;

        case 19: // '\023'
        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 64: // '@'
            break;
        }
        if(!handled)
            return super.onKeyDown(keyCode, event);
        else
            return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean a = mLetters.onKeyUp(keyCode, event);
        boolean b = mDigits.onKeyUp(keyCode, event);
        return a || b;
    }

    public int getMode()
    {
        return mMode;
    }

    public void setMode(int newMode)
    {
        switch(newMode)
        {
        case 1: // '\001'
            makeDigitsPrimary();
            mLetters.setVisibility(0);
            mDigits.setVisibility(0);
            break;

        case 4: // '\004'
            makeDigitsPrimary();
            mLetters.setVisibility(8);
            mDigits.setVisibility(0);
            break;

        case 5: // '\005'
            makeLettersPrimary();
            mLetters.setVisibility(0);
            mDigits.setVisibility(8);
            break;

        case 3: // '\003'
            makeDigitsPrimary();
            mLetters.setVisibility(4);
            mDigits.setVisibility(0);
            break;

        case 2: // '\002'
            makeLettersPrimary();
            mLetters.setVisibility(0);
            mDigits.setVisibility(4);
            break;
        }
        int oldMode = mMode;
        mMode = newMode;
        onModeChange(oldMode, newMode);
    }

    private void makeLettersPrimary()
    {
        if(mPrimary == mDigits)
            swapPrimaryAndHint(true);
    }

    private void makeDigitsPrimary()
    {
        if(mPrimary == mLetters)
            swapPrimaryAndHint(false);
    }

    private void swapPrimaryAndHint(boolean makeLettersPrimary)
    {
        Editable lettersText = mLetters.getText();
        Editable digitsText = mDigits.getText();
        android.text.method.InputMethod lettersInput = mLetters.getInputMethod();
        android.text.method.InputMethod digitsInput = mDigits.getInputMethod();
        if(makeLettersPrimary)
        {
            mLetters = mPrimary;
            mDigits = mHint;
        } else
        {
            mLetters = mHint;
            mDigits = mPrimary;
        }
        mLetters.setInputMethod(lettersInput);
        mLetters.setText(lettersText);
        lettersText = mLetters.getText();
        Selection.setSelection(lettersText, lettersText.length());
        mDigits.setInputMethod(digitsInput);
        mDigits.setText(digitsText);
        digitsText = mDigits.getText();
        Selection.setSelection(digitsText, digitsText.length());
        mPrimary.setFilters(mInputFilters);
        mHint.setFilters(mInputFilters);
    }

    public CharSequence getLetters()
    {
        if(mLetters.getVisibility() == 0)
            return mLetters.getText();
        else
            return "";
    }

    public CharSequence getDigits()
    {
        if(mDigits.getVisibility() == 0)
            return mDigits.getText();
        else
            return "";
    }

    public CharSequence getFilterText()
    {
        if(mMode != 4)
            return getLetters();
        else
            return getDigits();
    }

    public void append(String text)
    {
        switch(mMode)
        {
        case 1: // '\001'
            mDigits.getText().append(text);
            mLetters.getText().append(text);
            break;

        case 3: // '\003'
        case 4: // '\004'
            mDigits.getText().append(text);
            break;

        case 2: // '\002'
        case 5: // '\005'
            mLetters.getText().append(text);
            break;
        }
    }

    public void clearText()
    {
        Editable text = mLetters.getText();
        text.clear();
        text = mDigits.getText();
        text.clear();
        if(mIsQwerty)
            setMode(1);
        else
            setMode(4);
    }

    public void setLettersWatcher(TextWatcher watcher)
    {
        CharSequence text = mLetters.getText();
        Spannable span = (Spannable)text;
        span.setSpan(watcher, 0, text.length(), 18);
    }

    public void setDigitsWatcher(TextWatcher watcher)
    {
        CharSequence text = mDigits.getText();
        Spannable span = (Spannable)text;
        span.setSpan(watcher, 0, text.length(), 18);
    }

    public void setFilterWatcher(TextWatcher watcher)
    {
        if(mMode != 4)
            setLettersWatcher(watcher);
        else
            setDigitsWatcher(watcher);
    }

    public void removeFilterWatcher(TextWatcher watcher)
    {
        Spannable text;
        if(mMode != 4)
            text = mLetters.getText();
        else
            text = mDigits.getText();
        text.removeSpan(watcher);
    }

    protected void onModeChange(int i, int j)
    {
    }

    public static final int DIGITS_AND_LETTERS = 1;
    public static final int DIGITS_AND_LETTERS_NO_DIGITS = 2;
    public static final int DIGITS_AND_LETTERS_NO_LETTERS = 3;
    public static final int DIGITS_ONLY = 4;
    public static final int LETTERS_ONLY = 5;
    EditText mLetters;
    EditText mDigits;
    EditText mPrimary;
    EditText mHint;
    InputFilter mInputFilters[];
    ImageView mIcon;
    int mMode;
    private boolean mIsQwerty;
}
