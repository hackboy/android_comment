// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AndroidLocalSocketImplFactory.java

package android.net;

import java.net.SocketImpl;
import java.net.SocketImplFactory;

// Referenced classes of package android.net:
//            AndroidSocketImpl

public class AndroidLocalSocketImplFactory
    implements SocketImplFactory
{

    public AndroidLocalSocketImplFactory()
    {
    }

    public SocketImpl createSocketImpl()
    {
        return new AndroidSocketImpl(false);
    }
}
