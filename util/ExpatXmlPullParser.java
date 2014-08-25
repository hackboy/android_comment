// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExpatXmlPullParser.java

package android.util;

import java.io.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class ExpatXmlPullParser
    implements XmlPullParser
{

    ExpatXmlPullParser()
    {
        mCurrAttr = new String[32];
    }

    public void setFeature(String s, boolean flag)
        throws XmlPullParserException
    {
    }

    public boolean getFeature(String name)
    {
        return false;
    }

    public void setProperty(String s, Object obj)
        throws XmlPullParserException
    {
    }

    public Object getProperty(String name)
    {
        return null;
    }

    public void setInput(Reader reader)
        throws XmlPullParserException
    {
    }

    public void setInput(InputStream inputStream, String inputEncoding)
        throws XmlPullParserException
    {
        mInputStream = inputStream;
        mInputEncoding = inputEncoding;
        if(mNativeParser == 0)
            mNativeParser = nativeNewParser();
        nativeSetInputStream(mNativeParser, mInputStream, mInputStorage);
    }

    public String getInputEncoding()
    {
        return mInputEncoding;
    }

    public void defineEntityReplacementText(String s, String s1)
        throws XmlPullParserException
    {
    }

    public int getNamespaceCount(int depth)
        throws XmlPullParserException
    {
        return 0;
    }

    public String getNamespacePrefix(int pos)
        throws XmlPullParserException
    {
        throw new IndexOutOfBoundsException();
    }

    public String getNamespaceUri(int pos)
        throws XmlPullParserException
    {
        throw new IndexOutOfBoundsException();
    }

    public String getNamespace(String prefix)
    {
        return null;
    }

    public int getDepth()
    {
        ensureCurrFields();
        return mCurrDepth;
    }

    public String getPositionDescription()
    {
        return "";
    }

    public int getLineNumber()
    {
        return -1;
    }

    public int getColumnNumber()
    {
        return -1;
    }

    public boolean isWhitespace()
        throws XmlPullParserException
    {
        switch(mEventType)
        {
        case 7: // '\007'
            return true;

        case 4: // '\004'
        case 5: // '\005'
            return mCurrIsWhitespace;

        case 6: // '\006'
        default:
            throw new RuntimeException("isWhitespace");
        }
    }

    public String getText()
    {
        ensureCurrFields();
        return mCurrText;
    }

    public char[] getTextCharacters(int holderForStartAndLength[])
    {
        throw new RuntimeException("getTextCharacters");
    }

    public String getNamespace()
    {
        if(mEventType == 2 || mEventType == 3)
            return "";
        else
            return null;
    }

    public String getName()
    {
        switch(mEventType)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 6: // '\006'
            ensureCurrFields();
            return mCurrName;

        case 4: // '\004'
        case 5: // '\005'
        default:
            return null;
        }
    }

    public String getPrefix()
    {
        return null;
    }

    public boolean isEmptyElementTag()
        throws XmlPullParserException
    {
        if(mEventType != 2)
            throw new RuntimeException("isEmptyElementTag");
        else
            return false;
    }

    public int getAttributeCount()
    {
        ensureCurrFields();
        return mEventType != 2 ? -1 : mCurrAttrCount;
    }

    public String getAttributeNamespace(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("getAttributeNamespace");
        else
            return "";
    }

    public String getAttributeName(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("getAttributeName");
        else
            return mCurrAttr[(index << 1) + 0];
    }

    public String getAttributePrefix(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("getAttributePrefix");
        else
            return null;
    }

    public String getAttributeType(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("getAttributeType");
        else
            return "CDATA";
    }

    public boolean isAttributeDefault(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("isAttributeDefault");
        else
            return false;
    }

    public String getAttributeValue(int index)
    {
        ensureCurrFields();
        if(index < 0 || index >= mCurrAttrCount)
            throw new RuntimeException("getAttributeValue");
        else
            return mCurrAttr[(index << 1) + 1];
    }

    public String getAttributeValue(String namespace, String name)
    {
        if(mEventType != 2)
            throw new IndexOutOfBoundsException("getAttributeValue");
        if(name == null)
            return null;
        int n = mCurrAttrCount;
        String pairs[] = mCurrAttr;
        for(int i = 0; i < n; i++)
            if(name.equals(pairs[i << 1]))
                return pairs[(i << 1) + 1];

        return null;
    }

    public int getEventType()
        throws XmlPullParserException
    {
        return mEventType;
    }

    public int next()
        throws XmlPullParserException, IOException
    {
        return nextToken();
    }

    public int nextToken()
        throws XmlPullParserException, IOException
    {
        int eventType = nativeNextToken(mNativeParser);
        if(eventType < 0)
        {
            throw new RuntimeException();
        } else
        {
            mEventType = eventType;
            dirtyCurrFields();
            return eventType;
        }
    }

    public void require(int i, String s, String s1)
        throws XmlPullParserException, IOException
    {
    }

    public String nextText()
        throws XmlPullParserException, IOException
    {
        if(getEventType() != 2)
            throw new XmlPullParserException("parser must be on START_TAG to read next text", this, null);
        int eventType = next();
        if(eventType == 4)
        {
            String result = getText();
            eventType = next();
            if(eventType == 4)
            {
                StringBuilder sb = new StringBuilder(result);
                do
                {
                    sb.append(getText());
                    eventType = next();
                } while(eventType == 4);
                result = sb.toString();
            }
            if(eventType != 3)
                throw new XmlPullParserException("event TEXT it must be immediately followed by END_TAG", this, null);
            else
                return result;
        }
        if(eventType == 3)
            return "";
        else
            throw new XmlPullParserException("parser must be on START_TAG or TEXT to read text", this, null);
    }

    public int nextTag()
        throws XmlPullParserException, IOException
    {
        int eventType = next();
        if(eventType == 4 && isWhitespace())
            eventType = next();
        if(eventType != 2 && eventType != 3)
            throw new XmlPullParserException("expected start or end tag", this, null);
        else
            return eventType;
    }

    private void ensureCurrFields()
    {
        if(mCurrIsDirty)
        {
            nativeSetCurrFields(mNativeParser);
            mCurrIsDirty = false;
        }
    }

    private void dirtyCurrFields()
    {
        mCurrIsDirty = true;
    }

    private static native int nativeNewParser();

    private static native void nativeFreeParser(int i);

    private static native void nativeSetInputStream(int i, InputStream inputstream, byte abyte0[]);

    private static native int nativeNextToken(int i);

    private native void nativeSetCurrFields(int i);

    private int mNativeParser;
    private int mEventType;
    private boolean mCurrIsDirty;
    private int mCurrDepth;
    private String mCurrText;
    private String mCurrName;
    private String mCurrAttr[];
    private int mCurrAttrCount;
    private boolean mCurrIsWhitespace;
    private InputStream mInputStream;
    private String mInputEncoding;
    private final byte mInputStorage[] = new byte[8192];
}
