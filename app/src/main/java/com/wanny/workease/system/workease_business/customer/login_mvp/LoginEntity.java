package com.wanny.workease.system.workease_business.customer.login_mvp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： LoginEntity
 * 功能：
 * 作者： wanny
 * 时间： 14:38 2017/5/5
 */
public class LoginEntity implements Parcelable {

    private String userId;//": "07e13ec8-0d30-4470-8ec4-d9eba53c496c",
    private String otherBundId;//": "weixinId",
    private String userName;//": "to",
    private int userState;//": 0,
    private String createTime;//": "2017-03-15 15:11:33.0",
    private int type;//": 1,
    private String mobile;//": "15978946250",
    private String jobTypeName;//": "抹灰工",
    private String senior;//": "精通",
    private String cityName;//": "重庆市",
    private String workyear;//":"11 "


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getSenior() {
        return senior;
    }

    public void setSenior(String senior) {
        this.senior = senior;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.otherBundId);
        dest.writeString(this.userName);
        dest.writeInt(this.userState);
        dest.writeString(this.createTime);
        dest.writeInt(this.type);
        dest.writeString(this.mobile);
        dest.writeString(this.jobTypeName);
        dest.writeString(this.senior);
        dest.writeString(this.cityName);
        dest.writeString(this.workyear);
    }

    public LoginEntity() {

    }

    protected LoginEntity(Parcel in) {
        this.userId = in.readString();
        this.otherBundId = in.readString();
        this.userName = in.readString();
        this.userState = in.readInt();
        this.createTime = in.readString();
        this.type = in.readInt();
        this.mobile = in.readString();
        this.jobTypeName = in.readString();
        this.senior = in.readString();
        this.cityName = in.readString();
        this.workyear = in.readString();
    }

    public static final Creator<LoginEntity> CREATOR = new Creator<LoginEntity>() {
        @Override
        public LoginEntity createFromParcel(Parcel source) {
            return new LoginEntity(source);
        }

        @Override
        public LoginEntity[] newArray(int size) {
            return new LoginEntity[size];
        }
    };
}
