package com.henshin.stop_car.Tools;

import com.esri.arcgisruntime.geometry.Point;
import com.henshin.stop_car.seatch.stopcar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by henshin on 2018/3/14.
 */

public class Select {
    public static void SelectAndPaixu(ArrayList<stopcar> sc,Point conter)
    {
        for(int i=0;i<sc.size();i++)
        {
            double dis = drawCircle.getDistance(sc.get(i).getPoint(),conter);
            sc.get(i).setDistance(dis);
        }
        Select.paixu(sc);
    }
    public static   void paixu(ArrayList<stopcar> sc)
    {
        for(int i=0;i<sc.size()-1;i++)
        {//外层循环控制排序趟数
            for(int j=0;j<sc.size()-1-i;j++)
            {//内层循环控制每一趟排序多少次
                if(sc.get(j).getDistance()>sc.get(j+1).getDistance())
                {
                    stopcar temp=sc.get(j);
                    sc.set(j,sc.get(j+1));
                    sc.set(j+1,temp);
                }
            }
        }
    }
}
