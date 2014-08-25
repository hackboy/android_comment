// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExpatParser.java

package android.util;

import java.io.*;
import org.xml.sax.*;

// Referenced classes of package android.util:
//            Xml

class ExpatParser
    implements Attributes
{
    private class ExpatLocator
        implements Locator
    {

        public String getPublicId()
        {
            return null;
        }

        public String getSystemId()
        {
            return null;
        }

        public int getLineNumber()
        {
            return line();
        }

        public int getColumnNumber()
        {
            return column();
        }

        final ExpatParser this$0;

        private ExpatLocator()
        {
            this$0 = ExpatParser.this;
            super();
        }

    }


    ExpatParser(ContentHandler contentHandler)
    {
        this(Xml.Encoding.UTF_16, contentHandler);
    }

    ExpatParser(Xml.Encoding encoding, ContentHandler contentHandler)
    {
        locator = new ExpatLocator();
        inStartElement = false;
        attributeCount = -1;
        this.encoding = encoding;
        pointer = initialize(encoding.expatName);
        this.contentHandler = contentHandler;
    }

    Xml.Encoding getEncoding()
    {
        return encoding;
    }

    native int initialize(String s);

    void startElement(String uri, String localName, int attributeCount)
        throws SAXException
    {
        inStartElement = true;
        this.attributeCount = attributeCount;
        String qName = "" != uri ? "" : localName;
        contentHandler.startElement(uri, localName, qName, this);
        inStartElement = false;
        this.attributeCount = -1;
        break MISSING_BLOCK_LABEL_66;
        Exception exception;
        exception;
        inStartElement = false;
        this.attributeCount = -1;
        throw exception;
    }

    void endElement(String uri, String localName)
        throws SAXException
    {
        String qName = "" != uri ? "" : localName;
        contentHandler.endElement(uri, localName, qName);
    }

    void text(char text[], int length)
        throws SAXException
    {
        contentHandler.characters(text, 0, length);
    }

    void startNamespace(String prefix, String uri)
        throws SAXException
    {
        contentHandler.startPrefixMapping(prefix, uri);
    }

    void endNamespace(String prefix)
        throws SAXException
    {
        contentHandler.endPrefixMapping(prefix);
    }

    static String notNull(String s)
    {
        return s != null ? s : "";
    }

    void append(String xml)
        throws SAXException
    {
        append(pointer, xml, false);
    }

    native void append(int i, String s, boolean flag)
        throws SAXException;

    void append(char xml[], int offset, int length)
        throws SAXException
    {
        append(pointer, xml, offset, length);
    }

    native void append(int i, char ac[], int j, int k)
        throws SAXException;

    void append(byte xml[], int offset, int length)
        throws SAXException
    {
        append(pointer, xml, offset, length);
    }

    void parse(InputStream in)
        throws IOException, SAXException
    {
        byte buffer[] = new byte[1024];
        int length;
        while((length = in.read(buffer)) != -1) 
            append(pointer, buffer, 0, length);
        finish();
    }

    void parse(Reader in)
        throws IOException, SAXException
    {
        char buffer[] = new char[512];
        int length;
        while((length = in.read(buffer)) != -1) 
            append(pointer, buffer, 0, length);
        finish();
    }

    native void append(int i, byte abyte0[], int j, int k)
        throws SAXException;

    private void release()
    {
        if(!released)
        {
            released = true;
            release(pointer);
        }
    }

    void finish()
        throws SAXException
    {
        append(pointer, "", true);
        release();
        break MISSING_BLOCK_LABEL_25;
        Exception exception;
        exception;
        release();
        throw exception;
    }

    native void release(int i);

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        release();
        break MISSING_BLOCK_LABEL_18;
        Exception exception;
        exception;
        release();
        throw exception;
    }

    static native void staticInitialize(String s);

    int line()
    {
        return line(pointer);
    }

    native int line(int i);

    int column()
    {
        return column(pointer);
    }

    native int column(int i);

    public int getLength()
    {
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        else
            return attributeCount;
    }

    public String getURI(int index)
    {
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        if(index < 0 || index >= attributeCount)
            return null;
        else
            return getURI(pointer, index);
    }

    public String getLocalName(int index)
    {
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        else
            return index >= 0 && index < attributeCount ? getLocalName(pointer, index) : null;
    }

    public String getQName(int index)
    {
        if(index < 0 || index >= attributeCount)
        {
            return null;
        } else
        {
            String uri = getURI(index);
            return "" != uri ? "" : getLocalName(pointer, index);
        }
    }

    public String getType(int index)
    {
        return index >= 0 && index < attributeCount ? "CDATA" : null;
    }

    public String getValue(int index)
    {
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        else
            return index >= 0 && index < attributeCount ? getValue(pointer, index) : null;
    }

    public int getIndex(String uri, String localName)
    {
        if(uri == null)
            throw new NullPointerException("uri");
        if(localName == null)
            throw new NullPointerException("local name");
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        else
            return getIndex(pointer, uri, localName);
    }

    public int getIndex(String qName)
    {
        return getIndex("", qName);
    }

    public String getType(String uri, String localName)
    {
        if(uri == null)
            throw new NullPointerException("uri");
        if(localName == null)
            throw new NullPointerException("local name");
        else
            return getIndex(uri, localName) != -1 ? "CDATA" : null;
    }

    public String getType(String qName)
    {
        return getIndex(qName) != -1 ? "CDATA" : null;
    }

    public String getValue(String uri, String localName)
    {
        if(uri == null)
            throw new NullPointerException("uri");
        if(localName == null)
            throw new NullPointerException("local name");
        if(!inStartElement)
            throw new IllegalStateException("Attributes can only be used within the scope of startElement().");
        else
            return getValue(pointer, uri, localName);
    }

    public String getValue(String qName)
    {
        return getValue("", qName);
    }

    String[] flattenAttributes()
    {
        return flattenAttributes(pointer);
    }

    native String getURI(int i, int j);

    native String getLocalName(int i, int j);

    native String getValue(int i, int j);

    native int getIndex(int i, String s, String s1);

    native String getValue(int i, String s, String s1);

    native String[] flattenAttributes(int i);

    private static final int BUFFER_SIZE = 1024;
    static final String TAG = android/util/ExpatParser.getSimpleName();
    private static final String CDATA = "CDATA";
    private final int pointer;
    private final Xml.Encoding encoding;
    private final ContentHandler contentHandler;
    final Locator locator;
    boolean inStartElement;
    int attributeCount;
    private static final String OUTSIDE_START_ELEMENT = "Attributes can only be used within the scope of startElement().";
    volatile boolean released;

    static 
    {
        staticInitialize("");
    }
}
