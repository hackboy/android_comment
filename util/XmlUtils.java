// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlUtils.java

package android.util;

import java.io.*;
import java.util.*;
import org.kxml2.kdom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xmlpull.v1.*;

// Referenced classes of package android.util:
//            AttributeSet, XmlPullAttributes, Xml

public class XmlUtils
{

    public XmlUtils()
    {
    }

    public static void parse(String xml, ContentHandler contentHandler)
        throws SAXException
    {
        Xml.parse(xml, contentHandler);
    }

    public static void parse(Reader in, ContentHandler contentHandler)
        throws IOException, SAXException
    {
        Xml.parse(in, contentHandler);
    }

    public static void parse(InputStream in, String encoding, ContentHandler contentHandler)
        throws IOException, SAXException
    {
        Xml.parse(in, Xml.findEncodingByName(encoding), contentHandler);
    }

    /**
     * @deprecated Method parserInstance is deprecated
     */

    public static final XmlPullParser parserInstance()
        throws XmlPullParserException
    {
        if(null == sFactory)
            init();
        XmlPullParser parser = sFactory.newPullParser();
        parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        return parser;
    }

    public static final Element getFirstChild(Element e)
    {
        int childCount = e.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            Object o = e.getChild(i);
            if(o instanceof Element)
                return (Element)o;
        }

        return null;
    }

    public static final Element getFirstChild(Element e, String childName)
    {
        int childCount = e.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            Object o = e.getChild(i);
            if(!(o instanceof Element))
                continue;
            Element child = (Element)o;
            if(childName.equals(child.getName()))
                return child;
        }

        return null;
    }

    public static final XmlSerializer serializerInstance()
        throws XmlPullParserException
    {
        if(null == sFactory)
            init();
        return sFactory.newSerializer();
    }

    public static final AttributeSet getAttributeSet(XmlPullParser parser)
    {
        return (AttributeSet)parser;
        Exception e;
        e;
        return new XmlPullAttributes(parser);
    }

    public static final int convertValueToList(CharSequence value, String options[], int defaultValue)
    {
        if(null != value)
        {
            for(int i = 0; i < options.length; i++)
                if(value.equals(options[i]))
                    return i;

        }
        return defaultValue;
    }

    public static final boolean convertValueToBoolean(CharSequence value, boolean defaultValue)
    {
        boolean result = false;
        if(null == value)
            return defaultValue;
        if(value.equals("1") || value.equals("true") || value.equals("TRUE"))
            result = true;
        return result;
    }

    public static final int convertValueToInt(CharSequence charSeq, int defaultValue)
    {
        if(null == charSeq)
            return defaultValue;
        String nm = charSeq.toString();
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;
        if('-' == nm.charAt(0))
        {
            sign = -1;
            index++;
        }
        if('0' == nm.charAt(index))
        {
            if(index == len - 1)
                return 0;
            char c = nm.charAt(index + 1);
            if('x' == c || 'X' == c)
            {
                index += 2;
                base = 16;
            } else
            {
                index++;
                base = 8;
            }
        } else
        if('#' == nm.charAt(index))
        {
            index++;
            base = 16;
        }
        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    public static final int convertValueToUnsignedInt(String value, int defaultValue)
    {
        if(null == value)
            return defaultValue;
        else
            return parseUnsignedIntAttribute(value);
    }

    public static final int parseUnsignedIntAttribute(CharSequence charSeq)
    {
        String value = charSeq.toString();
        int index = 0;
        int len = value.length();
        int base = 10;
        if('0' == value.charAt(index))
        {
            if(index == len - 1)
                return 0;
            char c = value.charAt(index + 1);
            if('x' == c || 'X' == c)
            {
                index += 2;
                base = 16;
            } else
            {
                index++;
                base = 8;
            }
        } else
        if('#' == value.charAt(index))
        {
            index++;
            base = 16;
        }
        return (int)Long.parseLong(value.substring(index), base);
    }

    public static final void writeMapXml(Map val, OutputStream out)
        throws XmlPullParserException, IOException
    {
        XmlSerializer serializer = serializerInstance();
        serializer.setOutput(out, "utf-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeMapXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeListXml(List val, OutputStream out)
        throws XmlPullParserException, IOException
    {
        XmlSerializer serializer = serializerInstance();
        serializer.setOutput(out, "utf-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeListXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeMapXml(Map val, String name, XmlSerializer out)
        throws XmlPullParserException, IOException
    {
        if(val == null)
        {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        Set s = val.entrySet();
        Iterator i = s.iterator();
        out.startTag(null, "map");
        if(name != null)
            out.attribute(null, "name", name);
        java.util.Map.Entry e;
        for(; i.hasNext(); writeValueXml(e.getValue(), (String)e.getKey(), out))
            e = (java.util.Map.Entry)i.next();

        out.endTag(null, "map");
    }

    public static final void writeListXml(List val, String name, XmlSerializer out)
        throws XmlPullParserException, IOException
    {
        if(val == null)
        {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "list");
        if(name != null)
            out.attribute(null, "name", name);
        int N = val.size();
        for(int i = 0; i < N; i++)
            writeValueXml(val.get(i), null, out);

        out.endTag(null, "list");
    }

    public static final void writeByteArrayXml(byte val[], String name, XmlSerializer out)
        throws XmlPullParserException, IOException
    {
        if(val == null)
        {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "byte-array");
        if(name != null)
            out.attribute(null, "name", name);
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        StringBuilder sb = new StringBuilder(val.length * 2);
        for(int i = 0; i < N; i++)
        {
            int b = val[i];
            int h = b >> 4;
            sb.append(h < 10 ? 48 + h : (97 + h) - 10);
            h = b & 0xff;
            sb.append(h < 10 ? 48 + h : (97 + h) - 10);
        }

        out.text(sb.toString());
        out.endTag(null, "byte-array");
    }

    public static final void writeIntArrayXml(int val[], String name, XmlSerializer out)
        throws XmlPullParserException, IOException
    {
        if(val == null)
        {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "int-array");
        if(name != null)
            out.attribute(null, "name", name);
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for(int i = 0; i < N; i++)
        {
            out.startTag(null, "item");
            out.attribute(null, "value", Integer.toString(val[i]));
            out.endTag(null, "item");
        }

        out.endTag(null, "int-array");
    }

    public static final void writeValueXml(Object v, String name, XmlSerializer out)
        throws XmlPullParserException, IOException
    {
        if(v == null)
        {
            out.startTag(null, "null");
            if(name != null)
                out.attribute(null, "name", name);
            out.endTag(null, "null");
            return;
        }
        if(v instanceof String)
        {
            out.startTag(null, "string");
            if(name != null)
                out.attribute(null, "name", name);
            out.text(v.toString());
            out.endTag(null, "string");
            return;
        }
        String typeStr;
        if(v instanceof Integer)
            typeStr = "int";
        else
        if(v instanceof Long)
            typeStr = "long";
        else
        if(v instanceof Float)
            typeStr = "float";
        else
        if(v instanceof Double)
            typeStr = "double";
        else
        if(v instanceof Boolean)
        {
            typeStr = "boolean";
        } else
        {
            if(v instanceof byte[])
            {
                writeByteArrayXml((byte[])(byte[])v, name, out);
                return;
            }
            if(v instanceof int[])
            {
                writeIntArrayXml((int[])(int[])v, name, out);
                return;
            }
            if(v instanceof Map)
            {
                writeMapXml((Map)v, name, out);
                return;
            }
            if(v instanceof List)
            {
                writeListXml((List)v, name, out);
                return;
            }
            if(v instanceof CharSequence)
            {
                out.startTag(null, "string");
                if(name != null)
                    out.attribute(null, "name", name);
                out.text(v.toString());
                out.endTag(null, "string");
                return;
            } else
            {
                throw new RuntimeException((new StringBuilder()).append("writeValueXml: unable to write value ").append(v).toString());
            }
        }
        out.startTag(null, typeStr);
        if(name != null)
            out.attribute(null, "name", name);
        out.attribute(null, "value", v.toString());
        out.endTag(null, typeStr);
    }

    public static final HashMap readMapXml(InputStream in)
        throws XmlPullParserException, IOException
    {
        XmlPullParser parser = parserInstance();
        parser.setInput(in, null);
        return (HashMap)readValueXml(parser, new String[1]);
    }

    public static final ArrayList readListXml(InputStream in)
        throws XmlPullParserException, IOException
    {
        XmlPullParser parser = parserInstance();
        parser.setInput(in, null);
        return (ArrayList)readValueXml(parser, new String[1]);
    }

    public static final HashMap readThisMapXml(XmlPullParser parser, String endTag, String name[])
        throws XmlPullParserException, IOException
    {
        HashMap map = new HashMap();
        int eventType = parser.getEventType();
        do
        {
            XmlPullParser _tmp = parser;
            if(eventType == 2)
            {
                Object val = readThisValueXml(parser, name);
                if(name[0] != null)
                    map.put(name[0], val);
                else
                    throw new XmlPullParserException((new StringBuilder()).append("Map value without name attribute: ").append(parser.getName()).toString());
            } else
            {
                XmlPullParser _tmp1 = parser;
                if(eventType == 3)
                    if(parser.getName().equals(endTag))
                        return map;
                    else
                        throw new XmlPullParserException((new StringBuilder()).append("Expected ").append(endTag).append(" end tag at: ").append(parser.getName()).toString());
            }
            eventType = parser.next();
            XmlPullParser _tmp2 = parser;
        } while(eventType != 1);
        throw new XmlPullParserException((new StringBuilder()).append("Document ended before ").append(endTag).append(" end tag").toString());
    }

    public static final ArrayList readThisListXml(XmlPullParser parser, String endTag, String name[])
        throws XmlPullParserException, IOException
    {
        ArrayList list = new ArrayList();
        int eventType = parser.getEventType();
        do
        {
            XmlPullParser _tmp = parser;
            if(eventType == 2)
            {
                Object val = readThisValueXml(parser, name);
                list.add(val);
            } else
            {
                XmlPullParser _tmp1 = parser;
                if(eventType == 3)
                    if(parser.getName().equals(endTag))
                        return list;
                    else
                        throw new XmlPullParserException((new StringBuilder()).append("Expected ").append(endTag).append(" end tag at: ").append(parser.getName()).toString());
            }
            eventType = parser.next();
            XmlPullParser _tmp2 = parser;
        } while(eventType != 1);
        throw new XmlPullParserException((new StringBuilder()).append("Document ended before ").append(endTag).append(" end tag").toString());
    }

    public static final int[] readThisIntArrayXml(XmlPullParser parser, String endTag, String name[])
        throws XmlPullParserException, IOException
    {
        int num;
        try
        {
            num = Integer.parseInt(parser.getAttributeValue(null, "num"));
        }
        catch(NullPointerException e)
        {
            throw new XmlPullParserException("Need num attribute in byte-array");
        }
        catch(NumberFormatException e)
        {
            throw new XmlPullParserException("Not a number in num attribute in byte-array");
        }
        int array[] = new int[num];
        int i = 0;
        int eventType = parser.getEventType();
        do
        {
            XmlPullParser _tmp = parser;
            if(eventType == 2)
            {
                if(parser.getName().equals("item"))
                    try
                    {
                        array[i] = Integer.parseInt(parser.getAttributeValue(null, "value"));
                    }
                    catch(NullPointerException e)
                    {
                        throw new XmlPullParserException("Need value attribute in item");
                    }
                    catch(NumberFormatException e)
                    {
                        throw new XmlPullParserException("Not a number in value attribute in item");
                    }
                else
                    throw new XmlPullParserException((new StringBuilder()).append("Expected item tag at: ").append(parser.getName()).toString());
            } else
            {
                XmlPullParser _tmp1 = parser;
                if(eventType == 3)
                {
                    if(parser.getName().equals(endTag))
                        return array;
                    if(parser.getName().equals("item"))
                        i++;
                    else
                        throw new XmlPullParserException((new StringBuilder()).append("Expected ").append(endTag).append(" end tag at: ").append(parser.getName()).toString());
                }
            }
            eventType = parser.next();
            XmlPullParser _tmp2 = parser;
        } while(eventType != 1);
        throw new XmlPullParserException((new StringBuilder()).append("Document ended before ").append(endTag).append(" end tag").toString());
    }

    public static final Object readValueXml(XmlPullParser parser, String name[])
        throws XmlPullParserException, IOException
    {
        int eventType = parser.getEventType();
        do
        {
            XmlPullParser _tmp = parser;
            if(eventType == 2)
                return readThisValueXml(parser, name);
            XmlPullParser _tmp1 = parser;
            if(eventType == 3)
                throw new XmlPullParserException((new StringBuilder()).append("Unexpected end tag at: ").append(parser.getName()).toString());
            XmlPullParser _tmp2 = parser;
            if(eventType == 4)
                throw new XmlPullParserException((new StringBuilder()).append("Unexpected text: ").append(parser.getText()).toString());
            eventType = parser.next();
            XmlPullParser _tmp3 = parser;
        } while(eventType != 1);
        throw new XmlPullParserException("Unexpected end of document");
    }

    private static final Object readThisValueXml(XmlPullParser parser, String name[])
        throws XmlPullParserException, IOException
    {
        String tagName;
label0:
        {
            String valueName;
            Object res;
label1:
            {
label2:
                {
label3:
                    {
                        valueName = parser.getAttributeValue(null, "name");
                        tagName = parser.getName();
                        if(tagName.equals("null"))
                        {
                            res = null;
                            break label1;
                        }
                        if(!tagName.equals("string"))
                            break label2;
                        String value = "";
                        do
                        {
                            int eventType;
                            do
                            {
                                XmlPullParser _tmp = parser;
                                if((eventType = parser.next()) == 1)
                                    break label3;
                                XmlPullParser _tmp1 = parser;
                                if(eventType == 3)
                                    if(parser.getName().equals("string"))
                                    {
                                        name[0] = valueName;
                                        return value;
                                    } else
                                    {
                                        throw new XmlPullParserException((new StringBuilder()).append("Unexpected end tag in <string>: ").append(parser.getName()).toString());
                                    }
                                XmlPullParser _tmp2 = parser;
                                if(eventType != 4)
                                    break;
                                value = (new StringBuilder()).append(value).append(parser.getText()).toString();
                            } while(true);
                            XmlPullParser _tmp3 = parser;
                        } while(eventType != 2);
                        throw new XmlPullParserException((new StringBuilder()).append("Unexpected start tag in <string>: ").append(parser.getName()).toString());
                    }
                    throw new XmlPullParserException("Unexpected end of document in <string>");
                }
                if(tagName.equals("int"))
                    res = Integer.valueOf(parser.getAttributeValue(null, "value"));
                else
                if(tagName.equals("long"))
                    res = Long.valueOf(parser.getAttributeValue(null, "value"));
                else
                if(tagName.equals("float"))
                    res = new Float(parser.getAttributeValue(null, "value"));
                else
                if(tagName.equals("double"))
                    res = new Double(parser.getAttributeValue(null, "value"));
                else
                if(tagName.equals("boolean"))
                {
                    res = Boolean.valueOf(parser.getAttributeValue(null, "value"));
                } else
                {
                    if(tagName.equals("int-array"))
                    {
                        parser.next();
                        res = readThisIntArrayXml(parser, "int-array", name);
                        name[0] = valueName;
                        return res;
                    }
                    if(tagName.equals("map"))
                    {
                        parser.next();
                        res = readThisMapXml(parser, "map", name);
                        name[0] = valueName;
                        return res;
                    }
                    if(tagName.equals("list"))
                    {
                        parser.next();
                        res = readThisListXml(parser, "list", name);
                        name[0] = valueName;
                        return res;
                    } else
                    {
                        throw new XmlPullParserException((new StringBuilder()).append("Unknown tag: ").append(tagName).toString());
                    }
                }
            }
            do
            {
                XmlPullParser _tmp4 = parser;
                if((eventType = parser.next()) == 1)
                    break label0;
                XmlPullParser _tmp5 = parser;
                if(eventType == 3)
                    if(parser.getName().equals(tagName))
                    {
                        name[0] = valueName;
                        return res;
                    } else
                    {
                        throw new XmlPullParserException((new StringBuilder()).append("Unexpected end tag in <").append(tagName).append(">: ").append(parser.getName()).toString());
                    }
                XmlPullParser _tmp6 = parser;
                if(eventType == 4)
                    throw new XmlPullParserException((new StringBuilder()).append("Unexpected text in <").append(tagName).append(">: ").append(parser.getName()).toString());
                XmlPullParser _tmp7 = parser;
            } while(eventType != 2);
            throw new XmlPullParserException((new StringBuilder()).append("Unexpected start tag in <").append(tagName).append(">: ").append(parser.getName()).toString());
        }
        throw new XmlPullParserException((new StringBuilder()).append("Unexpected end of document in <").append(tagName).append(">").toString());
    }

    private static final void init()
        throws XmlPullParserException
    {
        sFactory = XmlPullParserFactory.newInstance("org.kxml2.io.KXmlParser,org.kxml2.io.KXmlSerializer", null);
    }

    public static final void beginDocument(XmlPullParser parser, String firstElementName)
        throws XmlPullParserException, IOException
    {
_L3:
        parser;
        if((type = parser.next()) == 2) goto _L2; else goto _L1
_L1:
        parser;
        if(type == 1) goto _L2; else goto _L3
_L2:
        parser;
        if(type != 2)
            throw new XmlPullParserException("No start tag found");
        if(!parser.getName().equals(firstElementName))
            throw new XmlPullParserException((new StringBuilder()).append("Unexpected start tag: found ").append(parser.getName()).append(", expected ").append(firstElementName).toString());
        else
            return;
    }

    public static final void nextElement(XmlPullParser parser)
        throws XmlPullParserException, IOException
    {
_L3:
        parser;
        if((type = parser.next()) == 2) goto _L2; else goto _L1
_L1:
        parser;
        if(type == 1) goto _L2; else goto _L3
_L2:
    }

    private static XmlPullParserFactory sFactory;
    public static final int NaN_Int = 0x80000000;
}
