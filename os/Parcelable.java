// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parcelable.java

package android.os;


// Referenced classes of package android.os:
//            Parcel

public interface Parcelable
{
    public static interface Creator
    {

        public abstract Object createFromParcel(Parcel parcel);

        public abstract Object[] newArray(int i);
    }


    public abstract void writeToParcel(Parcel parcel);
}
