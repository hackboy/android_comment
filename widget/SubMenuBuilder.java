// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SubMenuBuilder.java

package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import java.util.Map;

// Referenced classes of package android.widget:
//            MenuBuilder

public class SubMenuBuilder extends MenuBuilder
    implements SubMenu
{
    private class SubMenuView extends MenuBuilder.MenuView
    {

        public boolean dispatchKeyEvent(KeyEvent event)
        {
            if(event.isDown())
            {
                int keyCode = event.getKeyCode();
                if(event.getRepeatCount() == 0 && keyCode == 21)
                {
                    closeJustMe();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }

        protected final void closeJustMe()
        {
            if(mCallback != null)
                mCallback.onCloseSubMenu(SubMenuBuilder.this);
        }

        final SubMenuBuilder this$0;

        public SubMenuView(Context context, AttributeSet attrs, Map inflateParams)
        {
            this$0 = SubMenuBuilder.this;
            super(SubMenuBuilder.this, context, attrs, inflateParams);
        }
    }


    private SubMenuBuilder(Context context)
    {
        super(context);
    }

    public SubMenuBuilder(Context context, Menu parentMenu)
    {
        this(context);
        mParentMenu = parentMenu;
    }

    public Menu getParentMenu()
    {
        return mParentMenu;
    }

    public View createView(String name, AttributeSet attrs, Map inflateParams)
    {
        if(name.equals("MenuView"))
        {
            SubMenuView subView = new SubMenuView(mContext, attrs, inflateParams);
            subView.setPadding(subView.getPaddingLeft(), subView.getPaddingTop(), subView.getPaddingRight(), 6);
            return subView;
        } else
        {
            return super.createView(name, attrs, inflateParams);
        }
    }

    public SubMenu addSubMenu(int group, int id, CharSequence title)
    {
        throw new UnsupportedOperationException("Attempt to add a sub-menu to a sub-menu.");
    }

    public SubMenu addSubMenu(int group, int id, View title)
    {
        throw new UnsupportedOperationException("Attempt to add a sub-menu to a sub-menu.");
    }

    public SubMenu addSubMenu(int group, int id, int title)
    {
        throw new UnsupportedOperationException("Attempt to add a sub-menu to a sub-menu.");
    }

    private Menu mParentMenu;
}
