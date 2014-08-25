// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlPullAttributes.java

package android.util;

import org.xmlpull.v1.XmlPullParser;

// Referenced classes of package android.util:
//            AttributeSet, XmlUtils

public class XmlPullAttributes
    implements AttributeSet
{

    public XmlPullAttributes(XmlPullParser parser)
    {
        mParser = parser;
    }

    public int getAttributeCount()
    {
        return mParser.getAttributeCount();
    }

    public String getAttributeName(int index)
    {
        return mParser.getAttributeName(index);
    }

    public String getAttributeValue(int index)
    {
        return mParser.getAttributeValue(index);
    }

    public String getAttributeValue(String namespace, String name)
    {
        return mParser.getAttributeValue(namespace, name);
    }

    public String getPositionDescription()
    {
        return mParser.getPositionDescription();
    }

    public int getAttributeNameResource(int index)
    {
        return 0;
    }

    public int getAttributeListValue(String namespace, String attribute, String options[], int defaultValue)
    {
        return XmlUtils.convertValueToList(getAttributeValue(namespace, attribute), options, defaultValue);
    }

    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue)
    {
        return XmlUtils.convertValueToBoolean(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue)
    {
        return XmlUtils.convertValueToInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeIntValue(String namespace, String attribute, int defaultValue)
    {
        return XmlUtils.convertValueToInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue)
    {
        return XmlUtils.convertValueToUnsignedInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue)
    {
        String s = getAttributeValue(namespace, attribute);
        if(s != null)
            return Float.parseFloat(s);
        else
            return defaultValue;
    }

    public int getAttributeListValue(int index, String options[], int defaultValue)
    {
        return XmlUtils.convertValueToList(getAttributeValue(index), options, defaultValue);
    }

    public boolean getAttributeBooleanValue(int index, boolean defaultValue)
    {
        return XmlUtils.convertValueToBoolean(getAttributeValue(index), defaultValue);
    }

    public int getAttributeResourceValue(int index, int defaultValue)
    {
        return XmlUtils.convertValueToInt(getAttributeValue(index), defaultValue);
    }

    public int getAttributeIntValue(int index, int defaultValue)
    {
        return XmlUtils.convertValueToInt(getAttributeValue(index), defaultValue);
    }

    public int getAttributeUnsignedIntValue(int index, int defaultValue)
    {
        return XmlUtils.convertValueToUnsignedInt(getAttributeValue(index), defaultValue);
    }

    public float getAttributeFloatValue(int index, float defaultValue)
    {
        String s = getAttributeValue(index);
        if(s != null)
            return Float.parseFloat(s);
        else
            return defaultValue;
    }

    public String getIdAttribute()
    {
        return getAttributeValue(null, "id");
    }

    public String getClassAttribute()
    {
        return getAttributeValue(null, "class");
    }

    public int getIdAttributeResourceValue(int defaultValue)
    {
        return getAttributeResourceValue(null, "id", defaultValue);
    }

    public int getStyleAttribute()
    {
        return getAttributeResourceValue(null, "style", 0);
    }

    private XmlPullParser mParser;
}
