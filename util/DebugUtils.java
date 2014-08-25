// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DebugUtils.java

package android.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DebugUtils
{

    public DebugUtils()
    {
    }

    public static boolean isObjectSelected(Object object)
    {
        boolean match = false;
        String s = System.getenv("ANDROID_OBJECT_FILTER");
        if(s != null && s.length() > 0)
        {
            String selectors[] = s.split("@");
            if(object.getClass().getSimpleName().matches(selectors[0]))
            {
                for(int i = 1; i < selectors.length; i++)
                {
                    String pair[] = selectors[i].split("=");
                    Class klass = object.getClass();
                    try
                    {
                        Method declaredMethod = null;
                        Class parent = klass;
                        do
                            declaredMethod = parent.getDeclaredMethod((new StringBuilder()).append("get").append(pair[0].substring(0, 1).toUpperCase()).append(pair[0].substring(1)).toString(), (Class[])null);
                        while((parent = klass.getSuperclass()) != null && declaredMethod == null);
                        if(declaredMethod != null)
                        {
                            Object value = declaredMethod.invoke(object, (Object[])null);
                            match |= (value == null ? "null" : value.toString()).matches(pair[1]);
                        }
                    }
                    catch(NoSuchMethodException e)
                    {
                        e.printStackTrace();
                    }
                    catch(IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    catch(InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
        return match;
    }
}
