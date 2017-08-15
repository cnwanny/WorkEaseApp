package com.wanny.workease.system.workease_business.customer.user_mvp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： CustomerInfo
 * 功能：
 * 作者： wanny
 * 时间： 15:13 2017/8/3
 */
public class CustomerInfo implements Parcelable {

    private String userId;//": "07e13ec8-0d30-4470-8ec4-d9eba53c496c",
    private String otherBundId;//": "weixinId",
    private int userState;//": 0,
    private String createTime;//": "2017-03-15 15:11:33.0",
    private int type;//": 1,
    private String mobile;//": "15978946250",
    private String senior;//": "",
    private String areaId;//": ""
    private String userName;
    private String jobTypeName;
    private String cityName;
    private String workyear;

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWorkyear() {
        return workyear;
    }

    public void setWorkyear(String workyear) {
        this.workyear = workyear;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    //    "userId": "cf0bf5d1-9fbf-42ed-ac59-5c38f2080829",
//            "otherBundId": "",
//            "userName": "王鹏飞",
//            "password": "0",
//            "userState": 0,
//            "createTime": "2017-07-21 14:55:42.0",
//            "type": 0,
//            "mobile": "18223103860",
//            "jobTypeId": "101",
//            "senior": "一般",
//            "areaId": "500100"


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtherBundId() {
        return otherBundId;
    }

    public void setOtherBundId(String otherBundId) {
        this.otherBundId = otherBundId;
    }



    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getSenior() {
        return senior;
    }

    public void setSenior(String senior) {
        this.senior = senior;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public CustomerInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.otherBundId);
        dest.writeInt(this.userState);
        dest.writeString(this.createTime);
        dest.writeInt(this.type);
        dest.writeString(this.mobile);
        dest.writeString(this.senior);
        dest.writeString(this.areaId);
        dest.writeString(this.userName);
        dest.writeString(this.jobTypeName);
        dest.writeString(this.cityName);
        dest.writeString(this.workyear);
    }

    protected CustomerInfo(Parcel in) {
        this.userId = in.readString();
        this.otherBundId = in.readString();
        this.userState = in.readInt();
        this.createTime = in.readString();
        this.type = in.readInt();
        this.mobile = in.readString();
        this.senior = in.readString();
        this.areaId = in.readString();
        this.userName = in.readString();
        this.jobTypeName = in.readString();
        this.cityName = in.readString();
        this.workyear = in.readString();
    }

    public static final Creator<CustomerInfo> CREATOR = new Creator<CustomerInfo>() {
        @Override
        public CustomerInfo createFromParcel(Parcel source) {
            return new CustomerInfo(source);
        }

        @Override
        public CustomerInfo[] newArray(int size) {
            return new CustomerInfo[size];
        }
    };
}
