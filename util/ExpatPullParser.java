// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExpatPullParser.java

package android.util;

import java.io.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package android.util:
//            Xml, ExpatParser, ParseException

class ExpatPullParser
    implements XmlPullParser
{
    class ByteDocument extends Document
    {

        int buffer()
            throws IOException
        {
            return in.read(buffer);
        }

        void flush(ExpatParser parser, int length)
            throws XmlPullParserException
        {
            try
            {
                parser.append(buffer, 0, length);
            }
            catch(SAXException e)
            {
                throw new XmlPullParserException("Error parsing document.", ExpatPullParser.this, e);
            }
        }

        final byte buffer[] = new byte[1024];
        final InputStream in;
        final ExpatPullParser this$0;

        ByteDocument(InputStream in, Xml.Encoding encoding)
        {
            this$0 = ExpatPullParser.this;
            super(encoding);
            this.in = in;
        }
    }

    class CharDocument extends Document
    {

        int buffer()
            throws IOException
        {
            return in.read(buffer);
        }

        void flush(ExpatParser parser, int length)
            throws XmlPullParserException
        {
            try
            {
                parser.append(buffer, 0, length);
            }
            catch(SAXException e)
            {
                throw new XmlPullParserException("Error parsing document.", ExpatPullParser.this, e);
            }
        }

        final char buffer[] = new char[512];
        final Reader in;
        final ExpatPullParser this$0;

        CharDocument(Reader in)
        {
            this$0 = ExpatPullParser.this;
            super(Xml.Encoding.UTF_16);
            this.in = in;
        }
    }

    abstract class Document
    {
        private class SaxHandler extends DefaultHandler
        {

            public void startElement(String uri, String localName, String qName, Attributes attributes)
            {
                add(new StartTagEvent(uri, localName, parser, ++depth));
            }

            public void endElement(String uri, String localName, String qName)
            {
                add(new EndTagEvent(uri, localName, depth--));
            }

            public void characters(char ch[], int start, int length)
            {
                if(length == 0)
                    return;
                if(textEvent == null)
                    textEvent = new TextEvent(length, depth);
                textEvent.append(ch, start, length);
            }

            int depth;
            final Document this$1;

            private SaxHandler()
            {
                this$1 = Document.this;
                super();
                depth = 0;
            }

        }


        void pump()
            throws IOException, XmlPullParserException
        {
            if(finished)
                return;
            int length = buffer();
            if(length == -1)
            {
                finished = true;
                try
                {
                    parser.finish();
                }
                catch(SAXException e)
                {
                    throw new XmlPullParserException("Premature end of document.", ExpatPullParser.this, e);
                }
                add(new EndDocumentEvent());
                return;
            }
            if(length == 0)
            {
                return;
            } else
            {
                flush(parser, length);
                return;
            }
        }

        abstract int buffer()
            throws IOException;

        abstract void flush(ExpatParser expatparser, int i)
            throws XmlPullParserException;

        void add(Event event)
        {
            if(textEvent != null)
            {
                last.setNext(textEvent);
                last = textEvent;
                textEvent = null;
            }
            last.setNext(event);
            last = event;
        }

        Event remove()
        {
            return current = current.getNext();
        }

        int next()
            throws XmlPullParserException, IOException
        {
            Event next;
            while((next = current.getNext()) == null) 
                try
                {
                    pump();
                }
                catch(ParseException e)
                {
                    throw new XmlPullParserException("Parsing error.", ExpatPullParser.this, e);
                }
            current = next;
            return current.getType();
        }

        String getEncoding()
        {
            return encoding.expatName;
        }

        int getDepth()
        {
            return currentEvent().getDepth();
        }

        Event currentEvent()
        {
            return current;
        }

        final Xml.Encoding encoding;
        final ExpatParser parser;
        TextEvent textEvent;
        boolean finished;
        Event current;
        Event last;
        final ExpatPullParser this$0;

        Document(Xml.Encoding encoding)
        {
            this$0 = ExpatPullParser.this;
            super();
            textEvent = null;
            finished = false;
            current = new StartDocumentEvent();
            last = current;
            this.encoding = encoding;
            parser = new ExpatParser(encoding, new SaxHandler());
        }
    }

    static class EndDocumentEvent extends Event
    {

        Event getNext()
        {
            throw new IllegalStateException("End of document.");
        }

        void setNext(Event next)
        {
            throw new IllegalStateException("End of document.");
        }

        int getType()
        {
            return 1;
        }

        EndDocumentEvent()
        {
            super(0);
        }
    }

    static class TextEvent extends Event
    {

        int getType()
        {
            return 4;
        }

        StringBuilder getText()
        {
            return builder;
        }

        void append(char text[], int start, int length)
        {
            builder.append(text, start, length);
        }

        final StringBuilder builder;

        public TextEvent(int initialCapacity, int depth)
        {
            super(depth);
            builder = new StringBuilder(initialCapacity);
        }
    }

    static class EndTagEvent extends Event
    {

        String getLocalName()
        {
            return localName;
        }

        String getNamespace()
        {
            return namespace;
        }

        int getType()
        {
            return 3;
        }

        final String namespace;
        final String localName;

        EndTagEvent(String namespace, String localName, int depth)
        {
            super(depth);
            this.namespace = namespace;
            this.localName = localName;
        }
    }

    static class StartTagEvent extends Event
    {

        String getNamespace()
        {
            return namespace;
        }

        String getLocalName()
        {
            return localName;
        }

        int getAttributeCount()
        {
            return attributes.length / 3;
        }

        String getAttributeNamespace(int index)
        {
            return attributes[index * 3];
        }

        String getAttributeName(int index)
        {
            return attributes[index * 3 + 1];
        }

        String getAttributeValue(int index)
        {
            return attributes[index * 3 + 2];
        }

        String getAttributeValue(String namespace, String name)
        {
            if(namespace == null)
                namespace = "";
            String attributes[] = this.attributes;
            int length = attributes.length;
            for(int i = 0; i < length; i += 3)
                if(attributes[i].equals(namespace) && attributes[i + 1].equals(name))
                    return attributes[i + 2];

            return null;
        }

        int getType()
        {
            return 2;
        }

        final String localName;
        final String namespace;
        final String attributes[];

        StartTagEvent(String namespace, String localName, ExpatParser expatParser, int depth)
        {
            super(depth);
            this.namespace = namespace;
            this.localName = localName;
            attributes = expatParser.flattenAttributes();
        }
    }

    static class StartDocumentEvent extends Event
    {

        int getType()
        {
            return 0;
        }

        public StartDocumentEvent()
        {
            super(0);
        }
    }

    static abstract class Event
    {

        void setNext(Event next)
        {
            this.next = next;
        }

        Event getNext()
        {
            return next;
        }

        StringBuilder getText()
        {
            return null;
        }

        String getNamespace()
        {
            return null;
        }

        String getLocalName()
        {
            return null;
        }

        int getAttributeCount()
        {
            return -1;
        }

        String getAttributeNamespace(int index)
        {
            throw new IndexOutOfBoundsException("This is not a start tag.");
        }

        String getAttributeName(int index)
        {
            throw new IndexOutOfBoundsException("This is not a start tag.");
        }

        String getAttributeValue(int index)
        {
            throw new IndexOutOfBoundsException("This is not a start tag.");
        }

        abstract int getType();

        String getAttributeValue(String namespace, String name)
        {
            throw new IndexOutOfBoundsException("This is not a start tag.");
        }

        public int getDepth()
        {
            return depth;
        }

        final int depth;
        Event next;

        Event(int depth)
        {
            next = null;
            this.depth = depth;
        }
    }


    ExpatPullParser()
    {
    }

    public void setFeature(String name, boolean state)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public boolean getFeature(String name)
    {
        throw new UnsupportedOperationException();
    }

    public void setProperty(String name, Object value)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public Object getProperty(String name)
    {
        throw new UnsupportedOperationException();
    }

    public void setInput(Reader in)
        throws XmlPullParserException
    {
        document = new CharDocument(in);
    }

    public void setInput(InputStream in, String encodingName)
        throws XmlPullParserException
    {
        try
        {
            Xml.Encoding encoding = Xml.findEncodingByName(encodingName);
            document = new ByteDocument(in, encoding);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new XmlPullParserException((new StringBuilder()).append("Unsupported encoding: ").append(encodingName).toString());
        }
    }

    public String getInputEncoding()
    {
        return document.getEncoding();
    }

    public void defineEntityReplacementText(String entityName, String replacementText)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public int getNamespaceCount(int depth)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public String getNamespacePrefix(int pos)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public String getNamespaceUri(int pos)
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public String getNamespace(String prefix)
    {
        throw new UnsupportedOperationException();
    }

    public int getDepth()
    {
        return document.getDepth();
    }

    public String getPositionDescription()
    {
        return (new StringBuilder()).append("line ").append(getLineNumber()).append(", column ").append(getColumnNumber()).toString();
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
        if(getEventType() != 4)
            throw new XmlPullParserException("Not on text.");
        String text = getText();
        if(text.length() == 0)
            return true;
        int length = text.length();
        for(int i = 0; i < length; i++)
            if(!Character.isWhitespace(text.charAt(i)))
                return false;

        return true;
    }

    public String getText()
    {
        StringBuilder builder = document.currentEvent().getText();
        return builder != null ? builder.toString() : null;
    }

    public char[] getTextCharacters(int holderForStartAndLength[])
    {
        StringBuilder builder = document.currentEvent().getText();
        int length = builder.length();
        char characters[] = new char[length];
        builder.getChars(0, length, characters, 0);
        holderForStartAndLength[0] = 0;
        holderForStartAndLength[1] = length;
        return characters;
    }

    public String getNamespace()
    {
        return document.currentEvent().getNamespace();
    }

    public String getName()
    {
        return document.currentEvent().getLocalName();
    }

    public String getPrefix()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isEmptyElementTag()
        throws XmlPullParserException
    {
        throw new UnsupportedOperationException();
    }

    public int getAttributeCount()
    {
        return document.currentEvent().getAttributeCount();
    }

    public String getAttributeNamespace(int index)
    {
        return document.currentEvent().getAttributeNamespace(index);
    }

    public String getAttributeName(int index)
    {
        return document.currentEvent().getAttributeName(index);
    }

    public String getAttributePrefix(int index)
    {
        throw new UnsupportedOperationException();
    }

    public String getAttributeType(int index)
    {
        return "CDATA";
    }

    public boolean isAttributeDefault(int index)
    {
        return false;
    }

    public String getAttributeValue(int index)
    {
        return document.currentEvent().getAttributeValue(index);
    }

    public String getAttributeValue(String namespace, String name)
    {
        return document.currentEvent().getAttributeValue(namespace, name);
    }

    public int getEventType()
        throws XmlPullParserException
    {
        return document.currentEvent().getType();
    }

    public int next()
        throws XmlPullParserException, IOException
    {
        return document.next();
    }

    public int nextToken()
        throws XmlPullParserException, IOException
    {
        throw new UnsupportedOperationException();
    }

    public void require(int type, String namespace, String name)
        throws XmlPullParserException, IOException
    {
        if(type != getEventType() || name != null && !name.equals(getName()))
            throw new XmlPullParserException((new StringBuilder()).append("Expected ").append(TYPES[type]).append(getPositionDescription()).toString());
        else
            return;
    }

    public String nextText()
        throws XmlPullParserException, IOException
    {
        if(document.currentEvent().getType() != 2)
            throw new XmlPullParserException("Not on start tag.");
        int next = document.next();
        switch(next)
        {
        case 4: // '\004'
            return getText();

        case 3: // '\003'
            return "";
        }
        throw new XmlPullParserException((new StringBuilder()).append("Unexpected event ID: ").append(next).toString());
    }

    public int nextTag()
        throws XmlPullParserException, IOException
    {
        int eventType = next();
        if(eventType == 4 && isWhitespace())
            eventType = next();
        if(eventType != 2 && eventType != 3)
            throw new XmlPullParserException("Expected start or end tag", this, null);
        else
            return eventType;
    }

    private static final int BUFFER_SIZE = 1024;
    Document document;
}
