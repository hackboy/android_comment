// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TypedValue.java

package android.util;

import java.io.PrintStream;

// Referenced classes of package android.util:
//            DisplayMetrics

public class TypedValue
{

    public TypedValue()
    {
    }

    public final float getFloat()
    {
        return Float.intBitsToFloat(data);
    }

    public static float complexToFloat(int complex)
    {
        return (float)(complex & 0xffffff00) * RADIX_MULTS[complex >> 4 & 3];
    }

    public static float complexToDimension(int data, DisplayMetrics metrics)
    {
        return applyDimension(data >> 0 & 0xf, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelOffset(int data, DisplayMetrics metrics)
    {
        return (int)applyDimension(data >> 0 & 0xf, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelSize(int data, DisplayMetrics metrics)
    {
        float value = complexToFloat(data);
        float f = applyDimension(data >> 0 & 0xf, value, metrics);
        int res = (int)(f + 0.5F);
        if(res != 0)
            return res;
        if(value == 0.0F)
            return 0;
        return value <= 0.0F ? -1 : 1;
    }

    public static float complexToDimensionNoisy(int data, DisplayMetrics metrics)
    {
        float res = complexToDimension(data, metrics);
        System.out.println((new StringBuilder()).append("Dimension (0x").append(data >> 8 & 0xffffff).append("*").append(RADIX_MULTS[data >> 4 & 3] / 0.00390625F).append(")").append(DIMENSION_UNIT_STRS[data >> 0 & 0xf]).append(" = ").append(res).toString());
        return res;
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics)
    {
        switch(unit)
        {
        case 0: // '\0'
            return value;

        case 1: // '\001'
            return value * metrics.density;

        case 2: // '\002'
            return value * metrics.scaledDensity;

        case 3: // '\003'
            return value * metrics.xdpi * 0.01388889F;

        case 4: // '\004'
            return value * metrics.xdpi;

        case 5: // '\005'
            return value * metrics.xdpi * 25.4F;
        }
        return 0.0F;
    }

    public float getDimension(DisplayMetrics metrics)
    {
        return complexToDimension(data, metrics);
    }

    public static float complexToFraction(int data, float base, float pbase)
    {
        switch(data >> 0 & 0xf)
        {
        case 0: // '\0'
            return complexToFloat(data) * base;

        case 1: // '\001'
            return complexToFloat(data) * pbase;
        }
        return 0.0F;
    }

    public float getFraction(float base, float pbase)
    {
        return complexToFraction(data, base, pbase);
    }

    public final CharSequence coerceToString()
    {
        int t = type;
        if(t == 3)
            return string;
        else
            return coerceToString(t, data);
    }

    public static final String coerceToString(int type, int data)
    {
        switch(type)
        {
        case 0: // '\0'
            return null;

        case 1: // '\001'
            return (new StringBuilder()).append("@").append(data).toString();

        case 2: // '\002'
            return (new StringBuilder()).append("?").append(data).toString();

        case 4: // '\004'
            return Float.toString(Float.intBitsToFloat(data));

        case 5: // '\005'
            return (new StringBuilder()).append(Float.toString(complexToFloat(data))).append(DIMENSION_UNIT_STRS[data >> 0 & 0xf]).toString();

        case 6: // '\006'
            return (new StringBuilder()).append(Float.toString(complexToFloat(data) * 100F)).append(FRACTION_UNIT_STRS[data >> 0 & 0xf]).toString();

        case 17: // '\021'
            return (new StringBuilder()).append("0x").append(Integer.toHexString(data)).toString();

        case 18: // '\022'
            return data == 0 ? "false" : "true";
        }
        if(type >= 28 && type <= 31)
            return (new StringBuilder()).append("#").append(Integer.toHexString(data)).toString();
        if(type >= 16 && type <= 31)
            return Integer.toString(data);
        else
            return null;
    }

    public void setTo(TypedValue other)
    {
        type = other.type;
        string = other.string;
        data = other.data;
        assetCookie = other.assetCookie;
        resourceId = other.resourceId;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TypedValue{t=0x").append(Integer.toHexString(type));
        sb.append("/d=0x").append(Integer.toHexString(data));
        if(type == 3)
            sb.append(" \"").append(string == null ? "<null>" : string).append("\"");
        if(assetCookie != 0)
            sb.append(" a=").append(assetCookie);
        if(resourceId != 0)
            sb.append(" r=0x").append(Integer.toHexString(resourceId));
        sb.append("}");
        return sb.toString();
    }

    public static final int TYPE_NULL = 0;
    public static final int TYPE_REFERENCE = 1;
    public static final int TYPE_ATTRIBUTE = 2;
    public static final int TYPE_STRING = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_DIMENSION = 5;
    public static final int TYPE_FRACTION = 6;
    public static final int TYPE_FIRST_INT = 16;
    public static final int TYPE_INT_DEC = 16;
    public static final int TYPE_INT_HEX = 17;
    public static final int TYPE_INT_BOOLEAN = 18;
    public static final int TYPE_FIRST_COLOR_INT = 28;
    public static final int TYPE_INT_COLOR_ARGB8 = 28;
    public static final int TYPE_INT_COLOR_RGB8 = 29;
    public static final int TYPE_INT_COLOR_ARGB4 = 30;
    public static final int TYPE_INT_COLOR_RGB4 = 31;
    public static final int TYPE_LAST_COLOR_INT = 31;
    public static final int TYPE_LAST_INT = 31;
    public static final int COMPLEX_UNIT_SHIFT = 0;
    public static final int COMPLEX_UNIT_MASK = 15;
    public static final int COMPLEX_UNIT_PX = 0;
    public static final int COMPLEX_UNIT_DIP = 1;
    public static final int COMPLEX_UNIT_SP = 2;
    public static final int COMPLEX_UNIT_PT = 3;
    public static final int COMPLEX_UNIT_IN = 4;
    public static final int COMPLEX_UNIT_MM = 5;
    public static final int COMPLEX_UNIT_FRACTION = 0;
    public static final int COMPLEX_UNIT_FRACTION_PARENT = 1;
    public static final int COMPLEX_RADIX_SHIFT = 4;
    public static final int COMPLEX_RADIX_MASK = 3;
    public static final int COMPLEX_RADIX_23p0 = 0;
    public static final int COMPLEX_RADIX_16p7 = 1;
    public static final int COMPLEX_RADIX_8p15 = 2;
    public static final int COMPLEX_RADIX_0p23 = 3;
    public static final int COMPLEX_MANTISSA_SHIFT = 8;
    public static final int COMPLEX_MANTISSA_MASK = 0xffffff;
    public int type;
    public CharSequence string;
    public int data;
    public int assetCookie;
    public int resourceId;
    private static final float MANTISSA_MULT = 0.00390625F;
    private static final float RADIX_MULTS[] = {
        0.00390625F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F
    };
    private static final String DIMENSION_UNIT_STRS[] = {
        "px", "dip", "sp", "pt", "in", "mm"
    };
    private static final String FRACTION_UNIT_STRS[] = {
        "%", "%p"
    };

}
