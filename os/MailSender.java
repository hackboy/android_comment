// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MailSender.java

package android.os;


// Referenced classes of package android.os:
//            DeadObjectException, IMailbox, MailReceiver, Envelope, 
//            RemoteMailException, ServiceManagerNative, IServiceManager, IBinder, 
//            Parcel, Message

class MailSender
    implements IMailbox
{

    public static MailSender findPublished(String name)
    {
        return fromBinderInterface(ServiceManagerNative.getDefault().checkService(name));
        DeadObjectException e;
        e;
        return null;
    }

    public static MailSender fromBinderInterface(IBinder remote)
    {
        if(remote == null)
            return null;
        IMailbox mb = (IMailbox)remote.queryLocalInterface("android.os.IMailbox");
        if(mb instanceof MailReceiver)
            return ((MailReceiver)mb).getMailSender();
        if(mb instanceof MailSender)
            return (MailSender)mb;
        else
            return new MailSender(remote);
    }

    MailSender(IBinder remote)
    {
        mRemote = remote;
        if(remote instanceof MailReceiver)
            localMR = (MailReceiver)remote;
    }

    public void publish(String name)
    {
        try
        {
            ServiceManagerNative.getDefault().addService(name, asBinder());
        }
        catch(DeadObjectException e) { }
    }

    public void send(Message msg)
    {
        send(msg, null, 0);
    }

    public void send(Message msg, MailSender returnAddress)
    {
        send(msg, returnAddress, 0);
    }

    public void send(Message msg, MailSender returnAddress, int replyWhat)
    {
        Envelope e = new Envelope(msg, null, returnAddress, replyWhat);
        sendEnvelope(e);
    }

    public IBinder asBinder()
    {
        return mRemote;
    }

    public void sendEnvelope(Envelope e)
    {
        Throwable exception = null;
        Parcel p = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        if(localMR != null)
        {
            Parcel.checkCanWriteValue(e.getMessage().obj);
            Parcel.checkCanWriteMap(e.getMessage().peekData());
            localMR.sendEnvelope(e);
        } else
        {
            e.writeToParcel(p);
            try
            {
                p.setDataPosition(0);
                boolean success = mRemote.transact(1, p, reply, 0);
                if(success)
                {
                    String exString = reply.readString();
                    if(exString != null)
                        exception = new RemoteMailException(exString);
                } else
                {
                    exception = new RemoteMailException("Unsupported transact code / method call");
                }
            }
            catch(Throwable ex)
            {
                exception = ex;
            }
            if(exception != null)
            {
                IMailbox ra = e.getReturnAddress();
                if(ra != null)
                {
                    Message msg = Message.obtain();
                    msg.what = e.getReplyWhat();
                    ra.sendEnvelope(new Envelope(msg, exception));
                }
            }
            e.getMessage().recycle();
        }
        p.recycle();
        reply.recycle();
    }

    IBinder mRemote;
    MailReceiver localMR;
}
