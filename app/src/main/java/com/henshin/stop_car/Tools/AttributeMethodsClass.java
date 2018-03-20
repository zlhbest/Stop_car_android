package com.henshin.stop_car.Tools;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by henshin on 2018/3/11.
 */

public class AttributeMethodsClass {
    public static String GetCalloutStringByAttributeFunction(Map<String, Object> attr)
    {
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            Set<String> keys = attr.keySet();
            for (String key : keys)
            {
                Object value = attr.get(key);
                if ((value instanceof GregorianCalendar))
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.CHINA);
                    value = simpleDateFormat.format(((GregorianCalendar)value).getTime());
                }
                stringBuilder.append(key + " : " + value + "\n");
            }
            result = stringBuilder.toString();
        }
        catch (Exception localException) {}
        return result;
    }

    public static boolean isNumberFunction(String string)
    {
        boolean result = false;
        Pattern pattern = Pattern.compile("^[-+]?[0-9]");
        if (pattern.matcher(string).matches()) {
            result = true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('^');
        stringBuilder.append('[');
        stringBuilder.append("-+");
        stringBuilder.append("]?[");
        stringBuilder.append("0-9]+(");
        stringBuilder.append('\\');
        stringBuilder.append(".[0-9");
        stringBuilder.append("]+)");
        stringBuilder.append("?$");
        Pattern pattern1 = Pattern.compile(stringBuilder.toString());
        if (pattern1.matcher(string).matches()) {
            result = true;
        }
        return result;
    }
}
