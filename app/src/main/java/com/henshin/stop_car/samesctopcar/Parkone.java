package com.henshin.stop_car.samesctopcar;

import com.henshin.stop_car.user.user;

/**
 * Created by henshin on 2018/3/25.
 */

public class Parkone {
    private String parkoneid;
    private String parkonestate;
    private String userid;
    private String managerid;
    private String parkid;
    public void setParkoneid(String parkoneid)
    {
        this.parkoneid = parkoneid;
    }
    public String getParkoneid()
    {
        return this.parkoneid;
    }
    public void setparkonestate(String parkonestate)
    {
        this.parkonestate = parkonestate;
    }
    public String getparkonestate()
    {
        return this.parkonestate;
    }
    public void setuserid(String userid)
    {
        this.userid = userid;
    }
    public String getuserid()
    {
        return this.userid;
    }
    public void setmanagerid(String managerid)
    {
        this.managerid = managerid;
    }
    public String getmanagerid()
    {
        return this.managerid;
    }
    public void setparkid(String parkid)
    {
        this.parkid = parkid;
    }
    public String getparkid()
    {
        return this.parkid;
    }
}
