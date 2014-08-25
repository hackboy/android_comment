// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MailReceiver.java

package android.os;

import android.util.Log;

// Referenced classes of package android.os:
//            BinderNative, MailSender, Envelope, IMailbox, 
//            Looper, Message, Parcel, Mailbox, 
//            IBinder, Handler

class MailReceiver extends BinderNative
    implements IMailbox
{
    class MyHandler extends Handler
    {

        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
            case 1: // '\001'
                Envelope e = (Envelope)msg.obj;
                dispatchEnvelope(e);
                break;
            }
        }

        final MailReceiver this$0;

        MyHandler(Looper l)
        {
            this$0 = MailReceiver.this;
            super(l);
        }
    }

    public class Completion
        implements Mailbox.Completion
    {

        public void setAutoComplete(boolean autoComplete)
        {
            this.autoComplete = autoComplete;
        }

        public boolean getAutoComplete()
        {
            return autoComplete;
        }

        public boolean isComplete()
        {
            return isComplete;
        }

        public void complete(Object result)
        {
            internalComplete(result, null);
        }

        public void completeWithException(Throwable exception)
        {
            internalComplete(null, exception);
        }

        public boolean isResponse()
        {
            throw new RuntimeException("unimplemented");
        }

        private void internalComplete(Object result, Throwable exception)
        {
            if(isComplete)
            {
                Log.w("MailReceiver", "Redundent mail completion at", new Throwable());
                return;
            }
            isComplete = true;
            IMailbox ra = envelope.getReturnAddress();
            if(ra != null)
            {
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = envelope.getReplyWhat();
                ra.sendEnvelope(new Envelope(msg, exception));
            } else
            if(exception != null)
                Log.w("MailReceiver", "Uncaught exception processing mail message", exception);
            if(!autoComplete)
                envelope.getMessage().recycle();
        }

        private Envelope envelope;
        private boolean isComplete;
        private boolean autoComplete;
        final MailReceiver this$0;

        private Completion(Envelope e)
        {
            this$0 = MailReceiver.this;
            super();
            isComplete = false;
            autoComplete = true;
            envelope = e;
        }

    }

    static interface Listener
    {

        public abstract Object onNewMail(Message message, Throwable throwable, Mailbox.Completion completion);
    }


    public MailReceiver()
    {
        this(Looper.myLooper());
    }

    public MailReceiver(Looper l)
    {
        attachInterface(this, "android.os.IMailbox");
        handler = new MyHandler(l);
        mailSender = new MailSender(this);
    }

    public void publish(String name)
    {
        mailSender.publish(name);
    }

    public void setDelegate(Listener l)
    {
        _flddelegate = l;
    }

    public void setDelegate(Mailbox.Receiver l)
    {
        delegateNoEx = l;
    }

    public MailSender getMailSender()
    {
        return mailSender;
    }

    public IBinder asBinder()
    {
        return this;
    }

    public void sendEnvelope(Envelope e)
    {
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = e;
        handler.sendMessage(msg);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
    {
        switch(code)
        {
        case 1: // '\001'
            String exception = null;
            try
            {
                sendEnvelope(new Envelope(data));
            }
            catch(Throwable ex)
            {
                exception = ex.toString();
            }
            reply.writeString(exception);
            return true;
        }
        return false;
    }

    private void dispatchEnvelope(Envelope e)
    {
        Object ret;
        Completion complete;
        Throwable exception = null;
        ret = null;
        complete = new Completion(e);
        if(_flddelegate != null)
            ret = _flddelegate.onNewMail(e.getMessage(), e.getException(), complete);
        else
        if(delegateNoEx != null)
            ret = delegateNoEx.onNewMail(e.getMessage(), complete);
        if(complete.getAutoComplete())
            complete.complete(ret);
        if(complete.getAutoComplete())
            e.getMessage().recycle();
        break MISSING_BLOCK_LABEL_148;
        Throwable ex;
        ex;
        complete.completeWithException(ex);
        if(complete.getAutoComplete())
            e.getMessage().recycle();
        break MISSING_BLOCK_LABEL_148;
        Exception exception1;
        exception1;
        if(complete.getAutoComplete())
            e.getMessage().recycle();
        throw exception1;
    }

    private MyHandler handler;
    private Listener _flddelegate;
    private Mailbox.Receiver delegateNoEx;
    private MailSender mailSender;
    private static final int EVENT_ON_SEND_ENVELOPE = 1;

}
