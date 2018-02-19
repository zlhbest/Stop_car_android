package com.henshin.stop_car.Tools;

import java.util.HashMap;

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
}
