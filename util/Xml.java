// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Xml.java

package android.util;

import java.io.*;
import org.xml.sax.*;
import org.xmlpull.v1.XmlPullParser;

// Referenced classes of package android.util:
//            ExpatParser, ParseException, ExpatPullParser, ExpatXmlPullParser

public class Xml
{
    public static final class Encoding extends Enum
    {

        public static final Encoding[] values()
        {
            return (Encoding[])$VALUES.clone();
        }

        public static Encoding valueOf(String name)
        {
            return (Encoding)Enum.valueOf(android/util/Xml$Encoding, name);
        }

        public static final Encoding US_ASCII;
        public static final Encoding UTF_8;
        public static final Encoding UTF_16;
        public static final Encoding ISO_8859_1;
        final String expatName;
        private static final Encoding $VALUES[];

        static 
        {
            US_ASCII = new Encoding("US_ASCII", 0, "US-ASCII");
            UTF_8 = new Encoding("UTF_8", 1, "UTF-8");
            UTF_16 = new Encoding("UTF_16", 2, "UTF-16");
            ISO_8859_1 = new Encoding("ISO_8859_1", 3, "ISO-8859-1");
            $VALUES = (new Encoding[] {
                US_ASCII, UTF_8, UTF_16, ISO_8859_1
            });
        }

        private Encoding(String s, int i, String expatName)
        {
            super(s, i);
            this.expatName = expatName;
        }
    }


    public Xml()
    {
    }

    public static void parse(String xml, ContentHandler contentHandler)
        throws SAXException
    {
        ExpatParser parser = new ExpatParser(contentHandler);
        contentHandler.setDocumentLocator(parser.locator);
        contentHandler.startDocument();
        try
        {
            parser.append(xml);
            parser.finish();
        }
        catch(ParseException e)
        {
            throw new SAXParseException(e.getMessage(), null, null, parser.line(), parser.column(), e);
        }
        contentHandler.endDocument();
    }

    public static void parse(Reader in, ContentHandler contentHandler)
        throws IOException, SAXException
    {
        ExpatParser parser = new ExpatParser(contentHandler);
        contentHandler.setDocumentLocator(parser.locator);
        contentHandler.startDocument();
        try
        {
            parser.parse(in);
        }
        catch(ParseException e)
        {
            throw new SAXParseException(e.getMessage(), null, null, parser.line(), parser.column(), e);
        }
        contentHandler.endDocument();
    }

    public static void parse(InputStream in, Encoding encoding, ContentHandler contentHandler)
        throws IOException, SAXException
    {
        ExpatParser parser = new ExpatParser(encoding, contentHandler);
        contentHandler.setDocumentLocator(parser.locator);
        contentHandler.startDocument();
        try
        {
            parser.parse(in);
        }
        catch(ParseException e)
        {
            throw new SAXParseException(e.getMessage(), null, null, parser.line(), parser.column(), e);
        }
        contentHandler.endDocument();
    }

    /**
     * @deprecated Method newPullParser is deprecated
     */

    public static XmlPullParser newPullParser()
    {
        return new ExpatPullParser();
    }

    /**
     * @deprecated Method oldPullParser is deprecated
     */

    public static XmlPullParser oldPullParser()
    {
        return new ExpatXmlPullParser();
    }

    public static Encoding findEncodingByName(String encodingName)
        throws UnsupportedEncodingException
    {
        if(encodingName == null)
            return Encoding.UTF_8;
        Encoding arr$[] = Encoding.values();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            Encoding encoding = arr$[i$];
            if(encoding.expatName.equalsIgnoreCase(encodingName))
                return encoding;
        }

        throw new UnsupportedEncodingException(encodingName);
    }
}
