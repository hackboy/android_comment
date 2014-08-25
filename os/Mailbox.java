// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Mailbox.java

package android.os;

import android.util.Log;
import java.util.HashMap;

// Referenced classes of package android.os:
//            MailReceiver, MailboxNotAvailableException, MailSender, Message, 
//            Handler, AsyncResult, Looper

public class Mailbox
{
    public static interface Completion
    {

        public abstract void setAutoComplete(boolean flag);

        public abstract boolean getAutoComplete();

        public abstract boolean isComplete();

        public abstract void complete(Object obj);

        public abstract void completeWithException(Throwable throwable);
    }

    public static interface Receiver
    {

        public abstract Object onNewMail(Message message, Completion completion);
    }


    public static Mailbox createAndPublish(String name, Receiver receiver)
    {
        MailReceiver mr = new MailReceiver();
        mr.setDelegate(receiver);
        mr.publish(name);
        return new Mailbox(name, mr.getMailSender());
    }

    public static Mailbox findPublished(String name)
    {
        MailSender ms = MailSender.findPublished(name);
        if(ms == null)
            return null;
        else
            return new Mailbox(name, ms);
    }

    public static void registerForPublication(String name, Message response)
    {
        if(response == null)
        {
            return;
        } else
        {
            Handler h = new Handler(response.target.getLooper(), name, response) {

                public void handleMessage(Message msg)
                {
                    Mailbox mb = Mailbox.findPublished(name);
                    if(mb != null)
                    {
                        AsyncResult.forMessage(response).result = mb;
                        response.sendToTarget();
                    } else
                    {
                        sendMessageDelayed(obtainMessage(0), 5000L);
                    }
                }

                final String val$name;
                final Message val$response;

            
            {
                name = s;
                response = message;
                super(x0);
            }
            };
            h.obtainMessage(0).sendToTarget();
            return;
        }
    }

    public static void sendToPublished(String name, Message msg)
    {
        sendToPublished(name, msg, null);
    }

    public static void sendToPublished(String name, Message msg, Message response)
    {
        Mailbox mb = findPublished(name);
        if(mb == null)
        {
            if(response == null)
                Log.w("Mailbox", (new StringBuilder()).append("Mailbox ").append(name).append(" not found").toString());
            else
                AsyncResult.forMessage(response).exception = new MailboxNotAvailableException(name);
            return;
        } else
        {
            mb.send(msg, response);
            return;
        }
    }

    public static Mailbox createAnonymous(Receiver receiver)
    {
        MailReceiver mr = new MailReceiver();
        mr.setDelegate(receiver);
        return new Mailbox(null, mr.getMailSender());
    }

    Mailbox(String name, MailSender ms)
    {
        this.name = name;
        this.ms = ms;
    }

    public void send(Message msg)
    {
        send(msg, null);
    }

    public void send(Message msg, final Message response)
    {
        if(response == null)
        {
            ms.send(msg);
        } else
        {
            MailReceiver returnAddress = new MailReceiver(response.target.getLooper());
            returnAddress.setDelegate(new MailReceiver.Listener() {

                public HashMap onNewMail(Message msg, Throwable ex, Completion completion)
                {
                    AsyncResult ar = AsyncResult.forMessage(response, msg.obj, ex);
                    response.setData(msg.peekData());
                    response.sendToTarget();
                    return null;
                }

                public volatile Object onNewMail(Message x0, Throwable x1, Completion x2)
                {
                    return onNewMail(x0, x1, x2);
                }

                final Message val$response;
                final Mailbox this$0;

            
            {
                this$0 = Mailbox.this;
                response = message;
                super();
            }
            });
            ms.send(msg, returnAddress.getMailSender());
        }
    }

    public String getPublishedName()
    {
        return name;
    }

    MailSender ms;
    String name;
    static final int MAILBOX_POLL_MILLIS = 5000;
}
