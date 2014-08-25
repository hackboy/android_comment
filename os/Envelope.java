// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Envelope.java

package android.os;


// Referenced classes of package android.os:
//            RemoteMailException, Message, Parcelable, Parcel, 
//            MailSender

class Envelope
    implements Parcelable
{

    public Envelope(Message msg, Throwable exception, MailSender returnAddress, int replyWhat)
    {
        this.msg = msg;
        this.returnAddress = returnAddress;
        this.replyWhat = replyWhat;
        this.exception = exception;
        if(msg == null)
            this.msg = Message.obtain();
    }

    public Envelope(Message msg, Throwable exception, MailSender returnAddress)
    {
        this(msg, exception, returnAddress, 0);
    }

    public Envelope(Message msg, Throwable exception)
    {
        this(msg, exception, null, 0);
    }

    public Envelope(Parcel source)
    {
        msg = Message.obtain();
        int version = source.readInt();
        if(version != 1)
            throw new RuntimeException((new StringBuilder()).append("unsupported envelope version ").append(version).toString());
        msg.what = source.readInt();
        msg.arg1 = source.readInt();
        msg.arg2 = source.readInt();
        String exceptionString = source.readString();
        if(exceptionString != null)
            exception = new RemoteMailException(exceptionString);
        msg.obj = source.readValue();
        msg.setData(source.readHashMap());
        IBinder inBinder = source.readStrongBinder();
        returnAddress = MailSender.fromBinderInterface(inBinder);
        replyWhat = source.readInt();
    }

    public Envelope()
    {
        msg = new Message();
    }

    public Message getMessage()
    {
        return msg;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void setMessage(Message msg)
    {
        if(msg != null)
            this.msg = msg;
    }

    public MailSender getReturnAddress()
    {
        return returnAddress;
    }

    public void setReturnAddress(MailSender ra)
    {
        returnAddress = ra;
    }

    public void setReplyWhat(int replyWhat)
    {
        this.replyWhat = replyWhat;
    }

    public int getReplyWhat()
    {
        return replyWhat;
    }

    public void writeToParcel(Parcel dest)
    {
        dest.writeInt(1);
        dest.writeInt(msg.what);
        dest.writeInt(msg.arg1);
        dest.writeInt(msg.arg2);
        if(exception == null)
            dest.writeString(null);
        else
            dest.writeString(exception.toString());
        dest.writeValue(msg.obj);
        dest.writeMap(msg.peekData());
        if(returnAddress == null)
            dest.writeStrongBinder(null);
        else
            dest.writeStrongBinder(returnAddress.asBinder());
        dest.writeInt(replyWhat);
    }

    static final int CURRENT_ENVELOPE_VERSION = 1;
    private Message msg;
    private MailSender returnAddress;
    private int replyWhat;
    private Throwable exception;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Envelope createFromParcel(Parcel source)
        {
            return new Envelope(source);
        }

        public Envelope[] newArray(int size)
        {
            return new Envelope[size];
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
