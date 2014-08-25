// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StateSet.java

package android.util;


public class StateSet
{

    public StateSet()
    {
    }

    public static boolean isWildCard(int stateSetOrSpec[])
    {
        return stateSetOrSpec.length == 0 || stateSetOrSpec[0] == 0;
    }

    public static boolean stateSetMatches(int stateSpec[], int stateSet[])
    {
        if(stateSet == null)
            return stateSpec == null || isWildCard(stateSpec);
        int stateSpecSize = stateSpec.length;
        int stateSetSize = stateSet.length;
        for(int i = 0; i < stateSpecSize; i++)
        {
            int stateSpecState = stateSpec[i];
            if(stateSpecState == 0)
                return true;
            boolean mustMatch;
            if(stateSpecState > 0)
            {
                mustMatch = true;
            } else
            {
                mustMatch = false;
                stateSpecState = -stateSpecState;
            }
            boolean found = false;
            int j = 0;
            do
            {
                if(j >= stateSetSize)
                    break;
                int state = stateSet[j];
                if(state == 0)
                {
                    if(mustMatch)
                        return false;
                    break;
                }
                if(state == stateSpecState)
                {
                    if(mustMatch)
                        found = true;
                    else
                        return false;
                    break;
                }
                j++;
            } while(true);
            if(mustMatch && !found)
                return false;
        }

        return true;
    }

    public static boolean stateSetMatches(int stateSpec[], int state)
    {
        int stateSpecSize = stateSpec.length;
        for(int i = 0; i < stateSpecSize; i++)
        {
            int stateSpecState = stateSpec[i];
            if(stateSpecState == 0)
                return true;
            if(stateSpecState > 0)
            {
                if(state != stateSpecState)
                    return false;
                continue;
            }
            if(state == -stateSpecState)
                return false;
        }

        return true;
    }

    public static int[] trimStateSet(int states[], int newSize)
    {
        int trimmedStates[] = new int[newSize];
        System.arraycopy(states, 0, trimmedStates, 0, newSize);
        return trimmedStates;
    }

    public static final int WILD_CARD[] = new int[0];

}
