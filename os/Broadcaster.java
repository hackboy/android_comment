// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Broadcaster.java

package android.os;

import java.io.PrintStream;

// Referenced classes of package android.os:
//            Handler, Message

public class Broadcaster
{
    private class Registration
    {

        Registration next;
        Registration prev;
        int senderWhat;
        Handler targets[];
        int targetWhats[];
        final Broadcaster this$0;

        private Registration()
        {
            this$0 = Broadcaster.this;
            super();
        }

    }


    public Broadcaster()
    {
    }

    public void request(int senderWhat, Handler target, int targetWhat)
    {
        Broadcaster broadcaster = this;
        JVM INSTR monitorenter ;
        Registration r;
        int n;
        Handler oldTargets[];
        int oldWhats[];
        int i;
        r = null;
        if(mReg == null)
        {
            r = new Registration();
            r.senderWhat = senderWhat;
            r.targets = new Handler[1];
            r.targetWhats = new int[1];
            r.targets[0] = target;
            r.targetWhats[0] = targetWhat;
            mReg = r;
            r.next = r;
            r.prev = r;
            break MISSING_BLOCK_LABEL_368;
        }
        Registration start = mReg;
        r = start;
        do
        {
            if(r.senderWhat >= senderWhat)
                break;
            r = r.next;
        } while(r != start);
        if(r.senderWhat != senderWhat)
        {
            Registration reg = new Registration();
            reg.senderWhat = senderWhat;
            reg.targets = new Handler[1];
            reg.targetWhats = new int[1];
            reg.next = r;
            reg.prev = r.prev;
            r.prev.next = reg;
            r.prev = reg;
            if(r == mReg && r.senderWhat > reg.senderWhat)
                mReg = reg;
            r = reg;
            n = 0;
            break MISSING_BLOCK_LABEL_350;
        }
        n = r.targets.length;
        oldTargets = r.targets;
        oldWhats = r.targetWhats;
        i = 0;
_L2:
        if(i >= n)
            break MISSING_BLOCK_LABEL_299;
        if(oldTargets[i] != target || oldWhats[i] != targetWhat)
            break MISSING_BLOCK_LABEL_293;
        return;
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        r.targets = new Handler[n + 1];
        System.arraycopy(oldTargets, 0, r.targets, 0, n);
        r.targetWhats = new int[n + 1];
        System.arraycopy(oldWhats, 0, r.targetWhats, 0, n);
        r.targets[n] = target;
        r.targetWhats[n] = targetWhat;
        break MISSING_BLOCK_LABEL_382;
        Exception exception;
        exception;
        throw exception;
    }

    public void cancelRequest(int senderWhat, Handler target, int targetWhat)
    {
        Registration start;
        Registration r;
label0:
        {
            synchronized(this)
            {
                start = mReg;
                r = start;
                if(r != null)
                    break label0;
            }
            return;
        }
        do
        {
            if(r.senderWhat >= senderWhat)
                break;
            r = r.next;
        } while(r != start);
        if(r.senderWhat == senderWhat)
        {
            Handler targets[] = r.targets;
            int whats[] = r.targetWhats;
            int oldLen = targets.length;
            int i = 0;
            do
            {
                if(i >= oldLen)
                    break;
                if(targets[i] == target && whats[i] == targetWhat)
                {
                    r.targets = new Handler[oldLen - 1];
                    r.targetWhats = new int[oldLen - 1];
                    if(i > 0)
                    {
                        System.arraycopy(targets, 0, r.targets, 0, i);
                        System.arraycopy(whats, 0, r.targetWhats, 0, i);
                    }
                    int remainingLen = oldLen - i - 1;
                    if(remainingLen != 0)
                    {
                        System.arraycopy(targets, i + 1, r.targets, i, remainingLen);
                        System.arraycopy(whats, i + 1, r.targetWhats, i, remainingLen);
                    }
                    break;
                }
                i++;
            } while(true);
        }
        broadcaster;
        JVM INSTR monitorexit ;
          goto _L1
        exception;
        throw exception;
_L1:
    }

    public void dumpRegistrations()
    {
        synchronized(this)
        {
            Registration start = mReg;
            System.out.println((new StringBuilder()).append("Broadcaster ").append(this).append(" {").toString());
            if(start != null)
            {
                Registration r = start;
                do
                {
                    System.out.println((new StringBuilder()).append("    senderWhat=").append(r.senderWhat).toString());
                    int n = r.targets.length;
                    for(int i = 0; i < n; i++)
                        System.out.println((new StringBuilder()).append("        [").append(r.targetWhats[i]).append("] ").append(r.targets[i]).toString());

                } while(r != start);
            }
            System.out.println("}");
        }
    }

    public void broadcast(Message msg)
    {
label0:
        {
            synchronized(this)
            {
                if(mReg != null)
                    break label0;
            }
            return;
        }
        int senderWhat = msg.what;
        Registration start = mReg;
        Registration r = start;
        do
        {
            if(r.senderWhat >= senderWhat)
                break;
            r = r.next;
        } while(r != start);
        if(r.senderWhat == senderWhat)
        {
            Handler targets[] = r.targets;
            int whats[] = r.targetWhats;
            int n = targets.length;
            for(int i = 0; i < n; i++)
            {
                Handler target = targets[i];
                Message m = Message.obtain();
                m.copyFrom(msg);
                m.target = target;
                m.what = whats[i];
                target.sendMessage(m);
            }

        }
        broadcaster;
        JVM INSTR monitorexit ;
          goto _L1
        exception;
        throw exception;
_L1:
    }

    private Registration mReg;
}
