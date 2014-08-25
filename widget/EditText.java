// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EditText.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.text.Editable;
import android.text.Selection;
import android.text.method.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import java.util.Map;

// Referenced classes of package android.widget:
//            TextView

public class EditText extends TextView
{

    public EditText(Context context)
    {
        this(context, null, null, ArrowKeyMovementMethod.getInstance(), ((InputMethod) (TextInputMethod.getInstance())), null, 0x1010021);
    }

    public EditText(Context context, MovementMethod movement, InputMethod input)
    {
        this(context, null, null, movement, input, null, 0x1010021);
    }

    public EditText(Context context, MovementMethod movement, InputMethod input, TransformationMethod transformation)
    {
        this(context, null, null, movement, input, transformation, 0x1010021);
    }

    public EditText(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams, ArrowKeyMovementMethod.getInstance(), inputMethodFromAttrs(context, attrs), null, 0x1010021);
    }

    public EditText(Context context, AttributeSet attrs, Map inflateParams, MovementMethod movement, InputMethod input, TransformationMethod transformation, int defStyle)
    {
        super(context, attrs, inflateParams, movement, input, transformation, defStyle);
    }

    public Editable getText()
    {
        return (Editable)super.getText();
    }

    public void setText(CharSequence text, TextView.BufferType type)
    {
        super.setText(text, TextView.BufferType.EDITABLE);
    }

    protected static InputMethod inputMethodFromAttrs(Context context, AttributeSet attrs)
    {
        boolean numeric;
        CharSequence digits;
        boolean phone;
        boolean autotext;
        android.text.method.TextInputMethod.Capitalize cap;
        Class c;
        InstantiationException ex;
        boolean defaultAutoText = false;
        int defaultAutoCap = 4;
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.TextView);
        if(a.getBoolean(23, false))
        {
            defaultAutoText = false;
            defaultAutoCap = 4;
        }
        a = context.obtainStyledAttributes(attrs, android.R.styleable.EditText);
        CharSequence inputMethod = a.getString(5);
        numeric = a.getBoolean(0, false);
        digits = a.getString(3);
        phone = a.getBoolean(4, false);
        autotext = a.getBoolean(2, defaultAutoText);
        int autocap = a.getInt(1, defaultAutoCap);
        if(autocap == 1)
            cap = android.text.method.TextInputMethod.Capitalize.SENTENCES;
        else
        if(autocap == 2)
            cap = android.text.method.TextInputMethod.Capitalize.WORDS;
        else
        if(autocap == 3)
            cap = android.text.method.TextInputMethod.Capitalize.CHARACTERS;
        else
            cap = android.text.method.TextInputMethod.Capitalize.NONE;
        a.recycle();
        if(inputMethod == null)
            break MISSING_BLOCK_LABEL_206;
        try
        {
            c = Class.forName(inputMethod.toString());
        }
        // Misplaced declaration of an exception variable
        catch(InstantiationException ex)
        {
            throw new RuntimeException(ex);
        }
        return (InputMethod)c.newInstance();
        ex;
        throw new RuntimeException(ex);
        ex;
        throw new RuntimeException(ex);
        if(digits != null)
            return DigitsInputMethod.getInstance(digits.toString());
        if(phone)
            return DialerInputMethod.getInstance();
        if(numeric)
            return DigitsInputMethod.getInstance();
        else
            return TextInputMethod.getInstance(autotext, cap);
    }

    public int getSelectionStart()
    {
        return Selection.getSelectionStart(getText());
    }

    public int getSelectionEnd()
    {
        return Selection.getSelectionEnd(getText());
    }

    public void setSelection(int start, int stop)
    {
        Selection.setSelection(getText(), start, stop);
    }

    public void setSelection(int index)
    {
        Selection.setSelection(getText(), index);
    }

    public void selectAll()
    {
        Selection.selectAll(getText());
    }

    public void extendSelection(int index)
    {
        Selection.extendSelection(getText(), index);
    }

    public boolean onMotionEvent(MotionEvent event)
    {
        if(event.getAction() == 0 && isFocusable())
        {
            requestFocus();
            return true;
        } else
        {
            return false;
        }
    }

    public volatile CharSequence getText()
    {
        return getText();
    }
}
