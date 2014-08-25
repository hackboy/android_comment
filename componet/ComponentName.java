// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2008-1-13 21:16:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ComponentName.java

package android.content;

import android.os.Parcel;
import android.os.Parcelable;

// Referenced classes of package android.content:Context
//组件名称，和特定包关联,主要是简化intent设定目标的行为,使用起来不顺啊
public final class ComponentName implements Parcelable{
    //包名+类名
    public ComponentName(String pkg, String cls)
    {
        if(pkg == null)
            throw new NullPointerException("package name is null");
        if(cls == null)
        {
            throw new NullPointerException("class name is null");
        } else
        {
            mPackage = pkg;
            mClass = cls;
            return;
        }
    }
    //context+class
    public ComponentName(Context pkg, String cls)
    {
        if(cls == null)
        {
            throw new NullPointerException("class name is null");
        } else
        {
            mPackage = pkg.getPackageName();
            mClass = cls;
            return;
        }
    }

    public ComponentName(Context pkg, Class cls)
    {
        if(cls == null)
        {
            throw new NullPointerException("class name is null");
        } else
        {
            mPackage = pkg.getPackageName();
            mClass = cls.getName();
            return;
        }
    }

    public String getPackageName()
    {
        return mPackage;
    }

    public String getClassName()
    {
        return mClass;
    }

    public String flattenToString()
    {
        return (new StringBuilder()).append(mPackage).append("/").append(mClass).toString();
    }
    //如何如何
    public static ComponentName unflattenFromString(String str)
    {
        int sep = str.indexOf('/');
        //"/"不包含或者位于字符串结尾
        if(sep < 0 || sep + 1 >= str.length())
            return null;
        //这种形式===>com.cloudcity/Main
        String pkg = str.substring(0, sep);
        String cls = str.substring(sep + 1);
        //这是干毛
        if(cls.length() > 0 && cls.charAt(0) == '.')
            cls = (new StringBuilder()).append(pkg).append(cls).toString();
        return new ComponentName(pkg, cls);
    }

    public String toShortString()
    {
        return (new StringBuilder()).append("{").append(mPackage).append("/").append(mClass).append("}").toString();
    }

    public String toString()
    {
        return (new StringBuilder()).append("ComponentInfo{").append(mPackage).append("/").append(mClass).append("}").toString();
    }

    public boolean equals(Object obj)
    {
        ComponentName other = (ComponentName)obj;
        return mPackage.equals(other.mPackage) && mClass.equals(other.mClass);
        ClassCastException e;
        e;
        return false;
    }

    public int hashCode()
    {
        return mPackage.hashCode() + mClass.hashCode();
    }

    public void writeToParcel(Parcel out)
    {
        out.writeString(mPackage);
        out.writeString(mClass);
    }

    public static void writeToParcel(ComponentName c, Parcel out)
    {
        if(c != null)
        {
            out.writeInt(1);
            c.writeToParcel(out);
        } else
        {
            out.writeInt(0);
        }
    }

    public static ComponentName readFromParcel(Parcel in)
    {
        return in.readInt() == 0 ? null : new ComponentName(in);
    }

    public ComponentName(Parcel in)
    {
        mPackage = in.readString();
        mClass = in.readString();
    }

    private ComponentName()
    {
        mPackage = null;
        mClass = null;
    }

    private ComponentName(String pkg, Parcel in)
    {
        mPackage = pkg;
        mClass = in.readString();
    }
    //为毛线要在这个地方定义，蛋疼
    private final String mPackage;
    private final String mClass;
    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public ComponentName createFromParcel(Parcel in)
        {
            return new ComponentName(in);
        }

        public ComponentName[] newArray(int size)
        {
            return new ComponentName[size];
        }

        public volatile Object[] newArray(int x0)
        {
            return newArray(x0);
        }

        public volatile Object createFromParcel(Parcel x0)
        {
            return createFromParcel(x0);
        }

    };

}