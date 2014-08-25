// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharSequences.java

package android.util;


public class CharSequences
{

    public CharSequences()
    {
    }

    public static CharSequence forAsciiBytes(byte bytes[])
    {
        return new CharSequence(bytes) {

            public char charAt(int index)
            {
                return (char)bytes[index];
            }

            public int length()
            {
                return bytes.length;
            }

            public CharSequence subSequence(int start, int end)
            {
                return CharSequences.forAsciiBytes(bytes, start, end);
            }

            public String toString()
            {
                return new String(bytes);
            }

            final byte val$bytes[];

            
            {
                bytes = abyte0;
                super();
            }
        };
    }

    public static CharSequence forAsciiBytes(byte bytes[], int start, int end)
    {
        validate(start, end, bytes.length);
        return new CharSequence(bytes, start, end) {

            public char charAt(int index)
            {
                return (char)bytes[index + start];
            }

            public int length()
            {
                return end - start;
            }

            public CharSequence subSequence(int newStart, int newEnd)
            {
                newStart -= start;
                newEnd -= start;
                CharSequences.validate(newStart, newEnd, length());
                return CharSequences.forAsciiBytes(bytes, newStart, newEnd);
            }

            public String toString()
            {
                return new String(bytes, start, length());
            }

            final byte val$bytes[];
            final int val$start;
            final int val$end;

            
            {
                bytes = abyte0;
                start = i;
                end = j;
                super();
            }
        };
    }

    static void validate(int start, int end, int length)
    {
        if(start < 0)
            throw new IndexOutOfBoundsException();
        if(end < 0)
            throw new IndexOutOfBoundsException();
        if(end > length)
            throw new IndexOutOfBoundsException();
        if(start > end)
            throw new IndexOutOfBoundsException();
        else
            return;
    }

    static String toString(CharSequence charSequence)
    {
        return (new StringBuilder()).append(charSequence).toString();
    }

    public static boolean equals(CharSequence a, CharSequence b)
    {
        if(a.length() != b.length())
            return false;
        int length = a.length();
        for(int i = 0; i < length; i++)
            if(a.charAt(i) != b.charAt(i))
                return false;

        return true;
    }
}
