// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Filter.java

package android.widget;

import android.os.*;

public abstract class Filter
{
    private static class RequestArguments
    {

        CharSequence constraint;
        FilterListener listener;
        FilterResults results;

        private RequestArguments()
        {
        }

    }

    private class ResultsHandler extends Handler
    {

        public void handleMessage(Message msg)
        {
            RequestArguments args = (RequestArguments)msg.obj;
            publishResults(args.constraint, args.results);
            if(args.listener != null)
            {
                int count = args.results == null ? -1 : args.results.count;
                args.listener.onFilterComplete(count);
            }
        }

        final Filter this$0;

        private ResultsHandler()
        {
            this$0 = Filter.this;
            super();
        }

    }

    private class RequestHandler
        implements HandlerInterface
    {

        public void handleMessage(Message msg)
        {
            RequestArguments args = (RequestArguments)msg.obj;
            args.results = performFiltering(args.constraint);
            Message message = mResultHandler.obtainMessage(msg.what);
            message.obj = args;
            message.sendToTarget();
        }

        final Filter this$0;

        private RequestHandler()
        {
            this$0 = Filter.this;
            super();
        }

    }

    public static interface FilterListener
    {

        public abstract void onFilterComplete(int i);
    }

    protected static class FilterResults
    {

        public Object values;
        public int count;

        public FilterResults()
        {
        }
    }


    public Filter()
    {
        HandlerThread thread = new HandlerThread(new RequestHandler(), null, "Filter");
        mThreadHandler = thread.getHandler();
        mResultHandler = new ResultsHandler();
    }

    public final void filter(CharSequence constraint)
    {
        filter(constraint, null);
    }

    public final void filter(CharSequence constraint, FilterListener listener)
    {
        Message message = mThreadHandler.obtainMessage(0xdeadf00d);
        RequestArguments args = new RequestArguments();
        args.constraint = constraint;
        args.listener = listener;
        message.obj = args;
        mThreadHandler.removeMessages(0xdeadf00d);
        mThreadHandler.sendMessage(message);
    }

    protected abstract FilterResults performFiltering(CharSequence charsequence);

    protected abstract void publishResults(CharSequence charsequence, FilterResults filterresults);

    public String convertResultToString(Object resultValue)
    {
        return resultValue != null ? resultValue.toString() : "";
    }

    private static final String THREAD_NAME = "Filter";
    private static final int FILTER_TOKEN = 0xdeadf00d;
    private Handler mThreadHandler;
    private Handler mResultHandler;

}
