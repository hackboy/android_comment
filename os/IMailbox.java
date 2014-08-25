// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IMailbox.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, Envelope

public interface IMailbox
    extends IInterface
{

    public abstract void sendEnvelope(Envelope envelope);

    public static final String descriptor = "android.os.IMailbox";
    public static final int SEND_ENVELOPE = 1;
}
