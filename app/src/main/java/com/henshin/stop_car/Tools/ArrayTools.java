package com.henshin.stop_car.Tools;

import com.henshin.stop_car.seatch.stopcar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by henshin on 2018/2/10.
 */

public class ArrayTools {
    //上传的存储格式为name+/+value中间每一段用#隔开
    //用于处理向服务器传输的信息
    public final static HashMap<String,Object> splitArray(String array)
    {
        HashMap<String,Object> nameValue  = new HashMap<>();
        String[] nameValues = array.split("\\#");
        for(int i=0;i<nameValues.length;i++)
        {
            nameValue.put(nameValues[i].split("\\/")[0],nameValues[i].split("\\/")[1]);
        }
        return nameValue;
    }
    public final static String[] StringToArray(String string)
    {
        return  string.split("\\#");
    }

    //String数组转成List
    public final static List<String> StringToList(String[] string)
    {
        List<String> list = new ArrayList<>();
        for(int i=0;i<string.length;i++)
        {
            list.add(string[i]);
        }
        return list;
    }
    public  static List<String> ArraytoList(ArrayList<stopcar> sc)
    {
        List<String> list = new ArrayList<>();
        for(int i=0;i<sc.size();i++)
        {
            list.add(sc.get(i).getID());
        }
        return list;
    }
    public  static List<Double> ArraytoListscdis(ArrayList<stopcar> sc)
    {
        List<Double> list = new ArrayList<>();
        for(int i=0;i<sc.size();i++)
        {
            list.add(sc.get(i).getDistance());
        }
        return list;
    }
    public static String[] ArrayListToAtring(ArrayList<stopcar> sc)
    {
        int i=0;
        String[] strings = new String[sc.size()];
        for(stopcar stopc:sc)
        {
            strings[i] = stopc.getID();
            i++;
        }
        return strings;
    }
}
