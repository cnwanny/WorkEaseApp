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
    private String otherBundId;//": "tangdexiang",
    private String userName;//": "to",
    private String password;//": "123444",
    private String userState;//": 0,
    private String createTime;//": "2017-03-15 15:11:33.0",
    private String type;//": 1,
    private String mobile;//": "15978946250",
    private String jobTypeId;//": "stype",
    private String senior;//": "",
    private String areaId;//": ""

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(String jobTypeId) {
        this.jobTypeId = jobTypeId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.otherBundId);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.userState);
        dest.writeString(this.createTime);
        dest.writeString(this.type);
        dest.writeString(this.mobile);
        dest.writeString(this.jobTypeId);
        dest.writeString(this.senior);
        dest.writeString(this.areaId);
    }

    public LoginEntity() {
    }

    protected LoginEntity(Parcel in) {
        this.userId = in.readString();
        this.otherBundId = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.userState = in.readString();
        this.createTime = in.readString();
        this.type = in.readString();
        this.mobile = in.readString();
        this.jobTypeId = in.readString();
        this.senior = in.readString();
        this.areaId = in.readString();
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
