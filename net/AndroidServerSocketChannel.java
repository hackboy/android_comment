// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AndroidServerSocketChannel.java

package android.net;

import java.io.FileDescriptor;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

public abstract class AndroidServerSocketChannel extends ServerSocketChannel
{

    public AndroidServerSocketChannel()
    {
        super(SelectorProvider.provider());
    }

    public abstract FileDescriptor getFileDescriptor();
}
