package com.henshin.stop_car.Tools;

import android.content.Context;

import com.esri.arcgisruntime.data.Feature;
import com.henshin.stop_car.R;
import com.henshin.stop_car.seatch.mianstop;
import com.henshin.stop_car.seatch.stop;
import com.henshin.stop_car.seatch.stopcar;
import com.henshin.stop_car.user.user;

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
    public static String StringArrayToString(String[] parkid)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<parkid.length;i++)
        {
            stringBuilder.append(parkid[i]+"#");
        }
        return stringBuilder.toString();
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
    public static user ArrayTouser(String result, Context context)
    {
        String[] results = result.split("#");
        user userforyou = new user();
        userforyou.setAa001(Integer.parseInt(results[1]));
        userforyou.setAa004(results[2]);
        userforyou.setAa005(results[3]);
        userforyou.setAa006(Float.parseFloat(results[4]));
        userforyou.setAa007(results[5]);
        userforyou.setAa008(results[6]);
        userforyou.setAa009(results[7]);
        String Aa010 = context.getResources().getString(R.string.url)+results[8].replace("\r\n","");
        userforyou.setAa010(Aa010);
        return userforyou;
    }
    public static stop FeatureToStop(String result)
    {
        stop Stop = new stop();
        String[] results = result.split("#");
        Stop.setFID(results[7]);
        Stop.setname(results[6]);
        Stop.setallnumber(Integer.parseInt(results[4])+Integer.parseInt(results[2])+"");
        Stop.setemptynumber(results[4]);
        Stop.setmanagerid(results[0]);
        Stop.setlocation(results[5]);
        Stop.setintroduce(results[3]);
        if(results[1].equals("0"))
        {
            Stop.setpeice("免费");
        }
        else
        {
            Stop.setpeice(results[1]);
        }
        return Stop;
    }
    public static mianstop FeatureToMainStop(Feature feature)
    {
        mianstop Stop = new mianstop();
        Stop.setName(feature.getAttributes().get("NAME").toString());
        Stop.setFid(feature.getAttributes().get("OBJECTID").toString());
        return Stop;
    }
}
