// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PopupBox.java

package android.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.test.Assert;
import android.text.*;
import android.text.method.*;
import android.view.View;
import android.view.ViewGroup;

// Referenced classes of package android.widget:
//            LinearLayout, TextView, EditText, Button

/**
 * @deprecated Class PopupBox is deprecated
 */

public class PopupBox extends LinearLayout
{

    public PopupBox(Context context, CharSequence prompt, boolean hasEditable, CharSequence enterLabel, android.view.View.OnClickListener enter, CharSequence cancelLabel, android.view.View.OnClickListener cancel)
    {
        this(context, prompt, ((InputMethod) (hasEditable ? ((InputMethod) (TextInputMethod.getInstance())) : null)), enterLabel, enter, cancelLabel, cancel);
    }

    public PopupBox(Context context, CharSequence prompt, InputMethod editableMethod, CharSequence enterLabel, android.view.View.OnClickListener enter, CharSequence cancelLabel, android.view.View.OnClickListener cancel)
    {
        super(context);
        mEditText = null;
        Assert.assertNotNull(prompt);
        Assert.assertNotNull(enterLabel);
        Assert.assertNotNull(enter);
        setOrientation(1);
        setPadding(10, 6, 10, 6);
        GradientDrawable gd = new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
            Color.rgb(204, 102, 0), Color.rgb(153, 51, 0)
        });
        gd.setCornerRadii(10F, 10F);
        setBackground(gd);
        mPromptText = new TextView(context);
        mPromptText.setTextColor(-1);
        mPromptText.setTypeface(Typeface.DEFAULT_BOLD);
        mPromptText.setTextSize(18F);
        mPromptText.setText(prompt);
        addView(mPromptText, new LinearLayout.LayoutParams(-1, -2, 0.0F));
        if(editableMethod != null)
        {
            mEditText = new EditText(context, ArrowKeyMovementMethod.getInstance(), editableMethod);
            mEditText.setTextColor(0xff661700);
            mEditText.setTextSize(14F);
            GradientDrawable gd2 = new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
                Color.rgb(254, 222, 88), -1
            });
            gd2.setCornerRadii(10F, 10F);
            mEditText.setBackground(gd2);
            mEditText.setPadding(4, 4, 4, 4);
            mEditText.setHiliteColor(Color.rgb(204, 153, 51));
            mEditText.setOnClickListener(enter);
            addView(mEditText, new LinearLayout.LayoutParams(-1, -2, 0.0F));
            mFocusView = mEditText;
        }
        LinearLayout buttonSetView = new LinearLayout(context);
        buttonSetView.setOrientation(0);
        mEnterButton = new Button(context);
        mEnterButton.setTextColor(0xbb444444);
        mEnterButton.setText(enterLabel);
        mEnterButton.setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
        mEnterButton.setTextSize(14F);
        mEnterButton.setFocusable(true);
        mEnterButton.setOnClickListener(enter);
        buttonSetView.addView(mEnterButton, new LinearLayout.LayoutParams(80, 30));
        if(editableMethod == null)
            mFocusView = mEnterButton;
        if(cancelLabel != null && cancelLabel.length() != 0 && cancel != null)
        {
            Button button = new Button(context);
            button.setTextColor(0xbb444444);
            button.setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
            button.setTextSize(14F);
            button.setText(cancelLabel);
            button.setFocusable(true);
            button.setOnClickListener(cancel);
            buttonSetView.addView(button, new LinearLayout.LayoutParams(80, 30));
        }
        addView(buttonSetView, new LinearLayout.LayoutParams(-1, -2));
    }

    public void setEditable(CharSequence editable)
    {
        if(mEditText != null)
        {
            Editable text = mEditText.getText();
            text.replace(0, text.length(), editable);
            Selection.setSelection(text, 0, text.length());
        }
    }

    public CharSequence getText()
    {
        if(mEditText != null)
            return mEditText.getText();
        else
            return "";
    }

    public void requestFocus(int direction)
    {
        mFocusView.requestFocus();
    }

    private EditText mEditText;
    private Button mEnterButton;
    private View mFocusView;
    protected TextView mPromptText;
}
