// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MenuBuilder.java

package android.widget;

import android.R;
import android.content.*;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.method.KeyCharacterMap;
import android.util.AttributeSet;
import android.view.*;
import java.util.List;
import java.util.Map;

// Referenced classes of package android.widget:
//            WidgetInflate, SubMenuBuilder, TextView, LinearLayout, 
//            ImageView

public class MenuBuilder
    implements Menu, android.view.ViewInflate.Factory
{
    protected static class Divider extends ImageView
        implements ItemHolder
    {

        public void setItem(ItemImpl item)
        {
            mItem = item;
            item.setShown(false);
        }

        public ItemImpl getItem()
        {
            return mItem;
        }

        public void setInvoker(ItemInvoker iteminvoker)
        {
        }

        private ItemImpl mItem;

        public Divider(Context context, AttributeSet attrs, Map inflateParams)
        {
            super(context, attrs, inflateParams);
            setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    protected static class MenuItemView extends LinearLayout
        implements ItemHolder
    {

        public void setItem(ItemImpl item)
        {
            mItem = item;
        }

        public ItemImpl getItem()
        {
            return mItem;
        }

        public void setInvoker(ItemInvoker invoker)
        {
            mInvoker = invoker;
        }

        protected void onFocusChanged(boolean focused, int direction)
        {
            super.onFocusChanged(focused, direction);
            mItem.sendSelectionChange(focused);
        }

        public int[] createDrawableState()
        {
            if(mPressed)
                return PRESSED_STATE_SET;
            else
                return super.createDrawableState();
        }

        public boolean performClick()
        {
            mPressed = true;
            refreshDrawableState();
            invalidate();
            Message message = Message.obtain(mHandler, 462);
            mHandler.sendMessageDelayed(message, 125L);
            super.performClick();
            return true;
        }

        public boolean onMotionEvent(MotionEvent ev)
        {
            switch(ev.getAction())
            {
            default:
                break;

            case 1: // '\001'
                if(mPressed)
                {
                    performClick();
                    mInvoker.invokeItem(mItem);
                }
                break;

            case 0: // '\0'
                mPressed = true;
                refreshDrawableState();
                break;

            case 3: // '\003'
                mPressed = false;
                refreshDrawableState();
                break;

            case 2: // '\002'
                int x = (int)ev.getX();
                int y = (int)ev.getY();
                if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
                {
                    if(mPressed)
                    {
                        mPressed = false;
                        refreshDrawableState();
                    }
                    break;
                }
                if(!mPressed)
                {
                    mPressed = true;
                    refreshDrawableState();
                }
                break;
            }
            return true;
        }

        private static final int DO_CLICK_MSG = 462;
        private final Handler mHandler = new Handler() {

            public void handleMessage(Message msg)
            {
                if(msg.what == 462)
                {
                    mPressed = false;
                    refreshDrawableState();
                    invalidate();
                }
            }

            final MenuItemView this$0;

                
                {
                    this$0 = MenuItemView.this;
                    super();
                }
        };
        private ItemImpl mItem;
        private boolean mPressed;
        private ItemInvoker mInvoker;


        public MenuItemView(Context context, AttributeSet attrs, Map inflateParams)
        {
            super(context, attrs, inflateParams);
        }
    }

    protected static interface ItemHolder
    {

        public abstract void setInvoker(ItemInvoker iteminvoker);

        public abstract void setItem(ItemImpl itemimpl);

        public abstract ItemImpl getItem();
    }

    public class SubMenuItemImpl extends ItemImpl
    {

        public void setSelected(boolean selected)
        {
            super.setSelected(selected);
            mArrowView.setSelected(selected);
        }

        private ImageView mArrowView;
        final MenuBuilder this$0;

        public SubMenuItemImpl(int id, int group, int order, CharSequence titleStr, View titleView, View v)
        {
            this$0 = MenuBuilder.this;
            super(id, group, order, titleStr, titleView, v);
            mArrowView = (ImageView)v.findViewById(0x1050078);
            mArrowView.setVisibility(0);
        }

        SubMenuItemImpl(int id, int group, int order, View v)
        {
            this$0 = MenuBuilder.this;
            super(id, group, order, v);
        }
    }

    protected class ItemImpl
        implements android.view.Menu.Item
    {

        public int getId()
        {
            return mId;
        }

        public int getGroup()
        {
            return mGroup;
        }

        public void setTitle(CharSequence title)
        {
            mTitle = title;
            if(mTitleView != null)
                mTitleView.setText(title == null ? "" : title);
        }

        public CharSequence getTitle()
        {
            return mTitle;
        }

        public void setIntent(Intent intent)
        {
            mIntent = intent;
        }

        public Intent getIntent()
        {
            return mIntent;
        }

        public void setShortcut(int numberKey, int modifier, int alphaKey)
        {
            mShortcutNumberKey = numberKey;
            mShortcutModifier = modifier;
            mShortcutAlphaKey = alphaKey;
            shortcutsChanged();
        }

        public void setNumericShortcut(int numberKey)
        {
            mShortcutNumberKey = numberKey;
            shortcutsChanged();
        }

        public void setAlphabeticShortcut(int modifier, int alphaKey)
        {
            mShortcutModifier = modifier;
            mShortcutAlphaKey = alphaKey;
            shortcutsChanged();
        }

        public int getShortcutNumericKey()
        {
            return mShortcutNumberKey;
        }

        public int getShortcutAlphabeticModifier()
        {
            return mShortcutModifier;
        }

        public int getShortcutAlphabeticKey()
        {
            return mShortcutAlphaKey;
        }

        public void setCheckable(boolean checkable)
        {
            int oldFlags = mFlags;
            mFlags = mFlags & -2 | (checkable ? 1 : 0);
            if(oldFlags != mFlags)
                if(checkable)
                {
                    if(mCheckView != null)
                        mCheckView.setImageState((mFlags & 2) == 0 ? android.R.styleable.MenuItemUncheckedState : android.R.styleable.MenuItemCheckedState, true);
                } else
                if(mCheckView != null)
                    mCheckView.setImageState(null, false);
        }

        public boolean isCheckable()
        {
            return (mFlags & 0x10001) == 1;
        }

        public void setChecked(boolean checked)
        {
            int oldFlags = mFlags;
            mFlags = mFlags & -3 | (checked ? 2 : 0);
            if(oldFlags != mFlags && mCheckView != null)
                if(checked)
                {
                    if((oldFlags & 4) != 0)
                    {
                        uncheckGroup(mGroup);
                        mFlags |= 2;
                    }
                    mCheckView.setImageState(android.R.styleable.MenuItemCheckedState, true);
                } else
                {
                    mCheckView.setImageState(android.R.styleable.MenuItemUncheckedState, true);
                }
        }

        public void setSelected(boolean selected)
        {
            if(mCheckView != null)
                mCheckView.setSelected(selected);
            if(mTitleView != null)
                mTitleView.setSelected(selected);
            if(mShortcutView != null)
                mShortcutView.setSelected(selected);
            int oldFlags = mFlags;
            mFlags = mFlags & 0xffffffef | (selected ? 0x10 : 0);
            if(oldFlags != mFlags && mCheckView != null)
                if(isChecked())
                    mCheckView.setImageState(android.R.styleable.MenuItemCheckedState, true);
                else
                    mCheckView.setImageState(android.R.styleable.MenuItemUncheckedState, true);
        }

        void setExclusiveCheck(boolean exclusive)
        {
            mFlags = mFlags & -5 | (exclusive ? 4 : 0);
        }

        public boolean isChecked()
        {
            return (mFlags & 0x10002) == 2;
        }

        boolean isEnabled()
        {
            return (mFlags & 0x10008) == 0;
        }

        public void setShown(boolean shown)
        {
            int oldFlags = mFlags;
            mFlags = mFlags & -9 | (shown ? 0 : 8);
            if(oldFlags != mFlags)
                if(shown)
                {
                    mView.setVisibility(0);
                    if(mShortcutsChanged)
                        mMenuView.shortcutsChanged();
                } else
                {
                    mView.setVisibility(8);
                }
        }

        public boolean isShown()
        {
            return (mFlags & 8) == 0;
        }

        public boolean isSelected()
        {
            return (mFlags & 0x10) != 0;
        }

        public boolean isFocused()
        {
            return (mFlags & 0x10) != 0;
        }

        public boolean hasSubMenu()
        {
            return mMenu != null;
        }

        public SubMenu getSubMenu()
        {
            return mMenu;
        }

        public void setClickListener(android.view.Menu.OnClickListener clickListener)
        {
            mClickListener = clickListener;
        }

        public void setSelectionListener(android.view.Menu.OnSelectionListener selectionListener)
        {
            mSelectionListener = selectionListener;
        }

        public void setCallback(Runnable callback)
        {
            mItemCallback = callback;
        }

        public boolean isSeparator()
        {
            return (mFlags & 0x10000) != 0;
        }

        public void invoke()
        {
            if(mClickListener != null && mClickListener.onClick(this))
                return;
            if(mCallback != null && mCallback.onMenuItemSelected(MenuBuilder.this, this))
                return;
            if(mItemCallback != null)
                mItemCallback.run();
            else
            if(mIntent != null)
                mView.getContext().startActivity(mIntent);
        }

        public void sendSelectionChange(boolean selected)
        {
            setSelected(selected);
            if(mSelectionListener != null)
                mSelectionListener.onSelection(this, selected);
        }

        public final View getView()
        {
            return mView;
        }

        void applyShortcut(KeyEvent event, boolean force)
        {
            if((mShortcutsChanged || force) && mTitleView != null)
            {
                KeyCharacterMap kmap = KeyCharacterMap.load(event.getKeyboardDevice());
                mShortcutsChanged = false;
                if(kmap.getKeyboardType() != 1)
                {
                    if(mShortcutView != null)
                    {
                        char c = kmap.getDisplayLabel(mShortcutAlphaKey);
                        if(c != 0)
                            mShortcutView.setText(String.valueOf(Character.toLowerCase(c)));
                        else
                            mShortcutView.setText("");
                        mShortcutView.setVisibility(0);
                    }
                    mIndexView.setVisibility(8);
                } else
                {
                    if(mShortcutView != null)
                    {
                        char c = kmap.getDisplayLabel(mShortcutNumberKey);
                        if(c != 0)
                            mIndexView.setText(String.valueOf(c));
                        else
                            mIndexView.setText("");
                        mShortcutView.setVisibility(8);
                    }
                    mIndexView.setVisibility(0);
                }
            }
        }

        private void applyStyle()
        {
            MenuView mv = mMenuView;
            int textColor = mv.mItemTextColor;
            int selectedTextColor = mv.mItemSelectedTextColor;
            int normalTextColor = mv.mItemNormalTextColor;
            int size = mv.mItemTextSize;
            Typeface style = mv.mItemTextStyle;
            if(mIndexView != null)
                MenuBuilder.setTextView(mIndexView, textColor, selectedTextColor, normalTextColor, size, style);
            if(mTitleView != null)
                MenuBuilder.setTextView(mTitleView, textColor, selectedTextColor, normalTextColor, size, style);
            if(mShortcutView != null)
                MenuBuilder.setTextView(mShortcutView, withAlpha(textColor, 127), withAlpha(selectedTextColor, 127), withAlpha(normalTextColor, 127), size, MenuBuilder.SHORTCUT_TEXT_STYLE);
        }

        private void shortcutsChanged()
        {
            mShortcutsChanged = true;
            mMenuView.shortcutsChanged();
        }

        private final int mId;
        private final int mGroup;
        private final int mOrder;
        private CharSequence mTitle;
        private Intent mIntent;
        private int mShortcutNumberKey;
        private int mShortcutModifier;
        private int mShortcutAlphaKey;
        private Runnable mItemCallback;
        private android.view.Menu.OnClickListener mClickListener;
        private android.view.Menu.OnSelectionListener mSelectionListener;
        private View mView;
        private TextView mIndexView;
        private ImageView mCheckView;
        private TextView mTitleView;
        private SubMenu mMenu;
        private TextView mShortcutView;
        private boolean mShortcutsChanged;
        private int mFlags;
        private static final int CHECKABLE = 1;
        private static final int CHECKED = 2;
        private static final int EXCLUSIVE = 4;
        private static final int HIDDEN = 8;
        private static final int SELECTED = 16;
        private static final int SEPARATOR = 0x10000;
        private static final int SHORTCUT_ALPHA = 127;
        final MenuBuilder this$0;






        ItemImpl(int id, int group, int order, CharSequence titleStr, View titleView, View v)
        {
            this$0 = MenuBuilder.this;
            super();
            mId = id;
            mGroup = group;
            mOrder = order;
            mTitle = titleStr;
            mView = v;
            mIndexView = (TextView)v.findViewById(0x105005e);
            mCheckView = (ImageView)v.findViewById(0x105005f);
            mTitleView = (TextView)v.findViewById(0x1050012);
            mShortcutView = (TextView)v.findViewById(0x1050060);
            mShortcutsChanged = true;
            Drawable bg = mMenuView.mItemBackground;
            if(bg != null)
                mView.setBackground(bg.getConstantState().newDrawable());
            if(mHaveCheckables && mCheckView != null)
                mCheckView.setVisibility(0);
            applyStyle();
            if(titleView == null)
            {
                setTitle(titleStr);
            } else
            {
                ViewGroup g = (ViewGroup)v;
                int idx = g.indexOfChild(mTitleView);
                g.removeView(mTitleView);
                g.addView(titleView, idx, mTitleView.getLayoutParams());
            }
        }

        ItemImpl(int id, int group, int order, View v)
        {
            this$0 = MenuBuilder.this;
            super();
            mId = id;
            mGroup = group;
            mOrder = order;
            mView = v;
            mFlags = 0x10000;
        }
    }

    protected class MenuView extends LinearLayout
        implements ItemInvoker
    {

        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
            mParent.requestTransparentRegion(this);
            boolean qwerty = mQwertyInitiated;
            boolean qwertyChanged = qwerty != mQwertyMode;
            boolean updateShortcuts = mShortcutsChanged || qwertyChanged;
            if(updateShortcuts)
            {
                mShortcutsChanged = false;
                mQwertyMode = mShortcutsChanged;
            }
            prepareItems();
            boolean gaveFocus = false;
            ItemImpl item = mDefaultItem;
            if(item != null)
            {
                View v = item.getView();
                if(v.getVisibility() == 0 && item.isEnabled())
                {
                    v.requestFocus();
                    gaveFocus = true;
                }
            }
            KeyEvent fakeKey = new KeyEvent(0L, 0L, 0, 0, 0);
            int N = getChildCount();
            for(int i = 0; i < N; i++)
            {
                View v = getChildAt(i);
                item = ((ItemHolder)v).getItem();
                if(!gaveFocus)
                {
                    if(v.getVisibility() == 0 && item.isEnabled())
                    {
                        v.requestFocus();
                        gaveFocus = true;
                    }
                } else
                if(!updateShortcuts)
                    break;
                if(updateShortcuts)
                    item.applyShortcut(fakeKey, qwertyChanged);
            }

            if(!gaveFocus)
                requestFocus();
        }

        public void windowFocusChanged(boolean hasWindowFocus)
        {
            super.windowFocusChanged(hasWindowFocus);
        }

        public boolean dispatchKeyEvent(KeyEvent event)
        {
            boolean handled = false;
            if(event.isDown())
            {
                int keyCode = event.getKeyCode();
                if(event.getRepeatCount() == 0)
                {
                    if(keyCode == 4 || keyCode == 3)
                    {
                        close();
                        return true;
                    }
                    if(keyCode == 23 || keyCode == 64)
                    {
                        invokeSelection();
                        return true;
                    }
                    if(keyCode == 22 && mHasSubMenu)
                    {
                        ItemImpl currentItem = currentItem();
                        if(currentItem.hasSubMenu())
                            invokeItem(currentItem);
                        return true;
                    }
                }
                if(performShortcut(keyCode, event, 0))
                    return true;
            }
            if(!handled)
                handled = super.dispatchKeyEvent(event);
            return handled;
        }

        boolean performShortcut(int keyCode, KeyEvent event, int flags)
        {
            boolean qwerty = mQwertyInitiated;
            int meta = event.getMetaState();
            int N = getChildCount();
            for(int i = 0; i < N; i++)
            {
                ItemImpl item = ((ItemHolder)getChildAt(i)).getItem();
                if(item.isSeparator() || !item.isShown())
                    continue;
                boolean hit = false;
                if(item.hasSubMenu() && item.getSubMenu().performShortcut(keyCode, event, flags))
                    hit = true;
                if(!hit)
                    if(qwerty)
                    {
                        if(item.getShortcutAlphabeticKey() == keyCode && item.getShortcutAlphabeticModifier() == meta)
                            hit = true;
                    } else
                    {
                        hit = item.getShortcutNumericKey() == keyCode;
                    }
                if(!hit)
                    continue;
                if(item.isEnabled())
                {
                    item.invoke();
                    if((flags & 1) == 0)
                        close();
                }
                return true;
            }

            return false;
        }

        boolean isShortcutKey(int keyCode, int metaState)
        {
            boolean qwerty = mQwertyInitiated;
            int N = getChildCount();
            boolean hit = false;
            for(int i = 0; i < N; i++)
            {
                ItemImpl item = ((ItemHolder)getChildAt(i)).getItem();
                if(item.isSeparator())
                    continue;
                if(item.hasSubMenu() && item.getSubMenu().isShortcutKey(keyCode, metaState))
                    return true;
                if(qwerty)
                {
                    if(item.getShortcutAlphabeticKey() == keyCode && item.getShortcutAlphabeticModifier() == metaState)
                        hit = true;
                } else
                {
                    hit = item.getShortcutNumericKey() == keyCode;
                }
            }

            return hit;
        }

        boolean performIdentifierAction(int id, int flags)
        {
            ItemImpl item = (ItemImpl)findItem(id);
            if(item == null)
                return false;
            if(item.isEnabled())
            {
                item.invoke();
                if((flags & 1) == 0)
                    close();
            }
            return true;
        }

        private final void invokeSelection()
        {
            ItemImpl item = currentItem();
            invokeItem(item);
        }

        public final void invokeItem(ItemImpl item)
        {
            if(item == null)
                return;
            item.invoke();
            if(item.hasSubMenu())
                mCallback.onSubMenuSelected((SubMenuBuilder)item.getSubMenu());
            else
                close();
        }

        ItemImpl currentItem()
        {
            ItemHolder holder = (ItemHolder)findFocus();
            if(null == holder)
                return null;
            else
                return holder.getItem();
        }

        protected final void close()
        {
            if(mCallback != null)
                mCallback.onCloseMenu(MenuBuilder.this);
        }

        private void shortcutsChanged()
        {
            mShortcutsChanged = true;
        }

        private void prepareItems()
        {
            int N = getChildCount();
            boolean separatorNeeded = false;
            ItemImpl lastSeparator = null;
            for(int i = 0; i < N; i++)
            {
                ItemImpl item = ((ItemHolder)getChildAt(i)).getItem();
                if(item.isSeparator())
                {
                    item.setShown(separatorNeeded);
                    if(separatorNeeded)
                        lastSeparator = item;
                    separatorNeeded = false;
                    continue;
                }
                if(!separatorNeeded)
                    separatorNeeded = item.isShown();
            }

            if(!separatorNeeded && lastSeparator != null)
                lastSeparator.setShown(false);
        }

        Drawable mItemBackground;
        int mItemTextColor;
        int mItemSelectedTextColor;
        int mItemNormalTextColor;
        int mItemTextSize;
        Typeface mItemTextStyle;
        private boolean mShortcutsChanged;
        protected boolean mQwertyInitiated;
        private boolean mQwertyMode;
        final MenuBuilder this$0;


        public MenuView(Context context, AttributeSet attrs, Map inflateParams)
        {
            this$0 = MenuBuilder.this;
            super(context, attrs, inflateParams);
            android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.MenuView);
            mItemBackground = a.getDrawable(3);
            mItemSelectedTextColor = a.getColor(4, mItemTextColor);
            mItemNormalTextColor = a.getColor(5, 0xff000000);
            mItemTextColor = a.getColor(0, mItemNormalTextColor);
            mItemTextSize = a.getDimensionPixelSize(1, 16);
            mItemTextStyle = Typeface.defaultFromStyle(a.getInt(2, 0));
            a.recycle();
            mShortcutsChanged = true;
        }
    }

    public static interface ItemInvoker
    {

        public abstract void invokeItem(ItemImpl itemimpl);
    }

    public static interface Callback
    {

        public abstract boolean onMenuItemSelected(MenuBuilder menubuilder, android.view.Menu.Item item);

        public abstract void onCloseMenu(MenuBuilder menubuilder);

        public abstract boolean onSubMenuSelected(SubMenuBuilder submenubuilder);

        public abstract void onCloseSubMenu(SubMenuBuilder submenubuilder);
    }


    public MenuBuilder(Context context)
    {
        mHaveCheckables = false;
        mHasSubMenu = false;
        mContext = context;
        mResources = context.getResources();
        mFactory = new WidgetInflate(context);
        mFactory.setFactory(this);
        mRootView = mFactory.inflate(0x104000f, null, null);
        mMenuView = (MenuView)mRootView.findViewById(0x1050006);
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public View getView()
    {
        return mRootView;
    }

    public View getMenuView()
    {
        return mMenuView;
    }

    public android.view.Menu.Item add(int group, int id, CharSequence title)
    {
        MenuItemView view = addInternal(group, id, title, null, 0x1040010, false);
        return view.getItem();
    }

    public android.view.Menu.Item add(int group, int id, CharSequence title, Runnable callback)
    {
        MenuItemView view = addInternal(group, id, title, null, 0x1040010, false);
        ItemImpl item = view.getItem();
        item.mItemCallback = callback;
        return item;
    }

    public SubMenu addSubMenu(int group, int id, CharSequence title)
    {
        SubMenuBuilder subMenu = new SubMenuBuilder(mContext, this);
        subMenu.setQwertyMode(mMenuView.mQwertyInitiated);
        add(group, id, title, subMenu);
        return subMenu;
    }

    private android.view.Menu.Item add(int group, int id, CharSequence title, SubMenu subMenu)
    {
        MenuItemView view = addInternal(group, id, title, null, 0x1040023, true);
        ItemImpl item = view.getItem();
        item.mMenu = subMenu;
        mHasSubMenu = true;
        return item;
    }

    public android.view.Menu.Item add(int group, int id, int title)
    {
        return add(group, id, mResources.getText(title));
    }

    public android.view.Menu.Item add(int group, int id, int title, Runnable callback)
    {
        return add(group, id, mResources.getText(title), callback);
    }

    public SubMenu addSubMenu(int group, int id, int title)
    {
        SubMenuBuilder subMenu = new SubMenuBuilder(mContext, this);
        subMenu.setQwertyMode(mMenuView.mQwertyInitiated);
        add(group, id, title, subMenu);
        return subMenu;
    }

    private android.view.Menu.Item add(int group, int id, int title, SubMenu subMenu)
    {
        return add(group, id, mResources.getText(title), subMenu);
    }

    public android.view.Menu.Item add(int group, int id, View title)
    {
        MenuItemView view = addInternal(group, id, null, title, 0x1040010, false);
        return view.getItem();
    }

    public android.view.Menu.Item add(int group, int id, View title, Runnable callback)
    {
        MenuItemView view = addInternal(group, id, null, title, 0x1040010, false);
        ItemImpl item = view.getItem();
        item.mItemCallback = callback;
        return item;
    }

    public SubMenu addSubMenu(int group, int id, View title)
    {
        SubMenuBuilder subMenu = new SubMenuBuilder(mContext, this);
        subMenu.setQwertyMode(mMenuView.mQwertyInitiated);
        add(group, id, title, subMenu);
        return subMenu;
    }

    private android.view.Menu.Item add(int group, int id, View title, SubMenu subMenu)
    {
        MenuItemView view = addInternal(group, id, null, title, 0x1040023, true);
        ItemImpl item = view.getItem();
        item.mMenu = subMenu;
        mHasSubMenu = true;
        return item;
    }

    public int addIntentOptions(int group, int id, ComponentName caller, Intent specifics[], Intent intent, int flags, android.view.Menu.Item outSpecificItems[])
    {
        PackageManager pm = mContext.getPackageManager();
        List lri = pm.queryIntentActivityOptions(caller, specifics, intent, 0);
        int N = lri == null ? 0 : lri.size();
        if((flags & 4) == 0)
            removeGroup(group);
        if((flags & 1) == 0)
            addSeparator(group, id);
        for(int i = 0; i < N; i++)
        {
            android.content.PackageManager.ResolveInfo ri = (android.content.PackageManager.ResolveInfo)lri.get(i);
            Intent rintent = new Intent(ri.specificIndex >= 0 ? specifics[ri.specificIndex] : intent);
            rintent.setComponent(new ComponentName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.className));
            android.view.Menu.Item item = add(group, id, pm.getResolveLabel(ri));
            item.setIntent(rintent);
            if(outSpecificItems != null && ri.specificIndex >= 0)
                outSpecificItems[ri.specificIndex] = item;
        }

        if(N > 0 && (flags & 2) == 0)
            addSeparator(group, id);
        return N;
    }

    public android.view.Menu.Item addSeparator(int group, int id)
    {
        int order = categoryToOrder(group);
        Divider view = getDivider();
        ItemImpl item = new ItemImpl(id, group, order, view);
        view.setItem(item);
        mMenuView.addView(view, findNextOrderIndex(order), mDividerParams);
        return item;
    }

    protected Divider getDivider()
    {
        return (Divider)mFactory.inflate(0x1040011, null, null);
    }

    public ImageView getDividerView()
    {
        return getDivider();
    }

    public void removeItem(int id)
    {
        int i = findItemIndex(id);
        if(i >= 0)
        {
            if(mDefaultItem != null && mDefaultItem.getView() == mMenuView.getChildAt(i))
                mDefaultItem = null;
            mMenuView.removeViewAt(i);
        }
    }

    public void removeGroup(int group)
    {
        int i = findGroupIndex(group);
        if(i >= 0)
            for(; i < mMenuView.getChildCount(); mMenuView.removeViewAt(i))
            {
                View view = mMenuView.getChildAt(i);
                ItemImpl item = ((ItemHolder)view).getItem();
                if(item.mGroup != group)
                    break;
                if(mDefaultItem != null && mDefaultItem.getView() == view)
                    mDefaultItem = null;
            }

    }

    public void removeItemAt(int index)
    {
        mMenuView.removeViewAt(index);
    }

    public void clear()
    {
        mMenuView.removeAllViews();
    }

    public void setItemCheckable(int id, boolean checkable)
    {
        android.view.Menu.Item item = findItem(id);
        if(item != null)
            item.setCheckable(checkable);
    }

    public void setItemChecked(int id, boolean checked)
    {
        android.view.Menu.Item item = findItem(id);
        if(item != null)
            item.setChecked(checked);
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive)
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mGroup == group)
            {
                item.setCheckable(checkable);
                item.setExclusiveCheck(exclusive);
            }
        }

    }

    public void setItemShown(int id, boolean shown)
    {
        android.view.Menu.Item item = findItem(id);
        if(item != null)
            item.setShown(shown);
    }

    public void setGroupShown(int group, boolean shown)
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mGroup == group)
                item.setShown(shown);
        }

    }

    public boolean hasShownItems()
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.isShown())
                return true;
        }

        return false;
    }

    public void setDefaultItem(int id)
    {
        mDefaultItem = id < 0 ? null : (ItemImpl)findItem(id);
    }

    public android.view.Menu.Item findItem(int id)
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mId == id)
                return item;
            if(!item.hasSubMenu())
                continue;
            android.view.Menu.Item possibleItem = item.getSubMenu().findItem(id);
            if(possibleItem != null)
                return possibleItem;
        }

        return null;
    }

    public int findItemIndex(int id)
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mId == id)
                return i;
        }

        return -1;
    }

    public int findGroupIndex(int group)
    {
        int order = categoryToOrder(group);
        int size = size();
        for(int i = 0; i < size; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mOrder == order)
                return i;
        }

        return -1;
    }

    public int size()
    {
        return mMenuView.getChildCount();
    }

    public android.view.Menu.Item get(int index)
    {
        return ((ItemHolder)mMenuView.getChildAt(index)).getItem();
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags)
    {
        return mMenuView.performShortcut(keyCode, event, flags);
    }

    public boolean isShortcutKey(int keyCode, int metaState)
    {
        return mMenuView.isShortcutKey(keyCode, metaState);
    }

    public boolean performIdentifierAction(int id, int flags)
    {
        return mMenuView.performIdentifierAction(id, flags);
    }

    public void setQwertyMode(boolean isQwerty)
    {
        mMenuView.mQwertyInitiated = isQwerty;
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(!item.isSeparator() && item.hasSubMenu())
                item.getSubMenu().setQwertyMode(isQwerty);
        }

    }

    public View createView(String name, AttributeSet attrs, Map inflateParams)
    {
        if(name.equals("MenuItemView"))
            return new MenuItemView(mContext, attrs, inflateParams);
        if(name.equals("MenuView"))
            return new MenuView(mContext, attrs, inflateParams);
        if(name.equals("Divider"))
            return new Divider(mContext, attrs, inflateParams);
        else
            return null;
    }

    private static int categoryToOrder(int value)
    {
        int index = (value & 0xffff0000) >> 16;
        if(index < sCategoryToOrder.length)
            return sCategoryToOrder[index] << 16 | value & 0xffff;
        else
            return value;
    }

    private int findNextOrderIndex(int order)
    {
        for(int i = mMenuView.getChildCount() - 1; i >= 0; i--)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mOrder <= order)
                return i + 1;
        }

        return 0;
    }

    private MenuItemView addInternal(int group, int id, CharSequence titleStr, View titleView, int itemViewResId, boolean isSubMenu)
    {
        int order = categoryToOrder(group);
        MenuItemView view = (MenuItemView)mFactory.inflate(itemViewResId, null, null);
        ItemImpl item;
        if(isSubMenu)
            item = new SubMenuItemImpl(id, group, order, titleStr, titleView, view);
        else
            item = new ItemImpl(id, group, order, titleStr, titleView, view);
        view.setInvoker(mMenuView);
        view.setItem(item);
        mMenuView.addView(view, findNextOrderIndex(order), mItemParams);
        mMenuView.shortcutsChanged();
        return view;
    }

    private void uncheckGroup(int group)
    {
        int N = mMenuView.getChildCount();
        for(int i = 0; i < N; i++)
        {
            ItemImpl item = ((ItemHolder)mMenuView.getChildAt(i)).getItem();
            if(item.mGroup == group)
                item.setChecked(false);
        }

    }

    public View getCurrentItemView()
    {
        return mMenuView.currentItem().getView();
    }

    private static void setTextView(TextView v, int textColor, int selectedTextColor, int normalTextColor, int size, Typeface style)
    {
        v.setTextColor(textColor);
        v.setSelectedTextColor(selectedTextColor);
        v.setNormalTextColor(normalTextColor);
        v.setTextSize(size);
        v.setTypeface(style);
        v.setMaxLines(1);
    }

    private int withAlpha(int textColor, int alpha)
    {
        return alpha << 24 | 0xffffff & textColor;
    }

    private static final int sCategoryToOrder[] = {
        1, 4, 5, 3, 2, 0
    };
    private static final Typeface SHORTCUT_TEXT_STYLE;
    protected final Context mContext;
    private final Resources mResources;
    private final WidgetInflate mFactory;
    private final View mRootView;
    private final MenuView mMenuView;
    protected Callback mCallback;
    private ItemImpl mDefaultItem;
    private boolean mHaveCheckables;
    private static final LinearLayout.LayoutParams mItemParams = new LinearLayout.LayoutParams(-1, -2);
    private static final LinearLayout.LayoutParams mDividerParams = new LinearLayout.LayoutParams(-1, -2);
    private boolean mHasSubMenu;
    private Divider mDivider;

    static 
    {
        SHORTCUT_TEXT_STYLE = Typeface.DEFAULT;
    }








}
