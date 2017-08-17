package com.wanny.workease.system.workease_business.customer.main_mvp;

import android.os.Parcel;
import android.os.Parcelable;

import com.wanny.workease.system.workease_business.customer.register_mvp.City;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginEntity;

import java.util.ArrayList;

/**
 * 文件名： WorkInfoEntity
 * 功能：
 * 作者： wanny
 * 时间： 17:26 2017/7/10
 */
public class WorkInfoEntity implements Parcelable {


    private String id;//": "07e13ec8-0d30-4470-8ec4-d9eba53c496c",
    private City city;//'//'": "500000",
    private String recruitNum;//": "20",
    private String price;//": "20",
    private String name;//": "阳光工程",
    private String desc;//": "工程描述...",
    private String detailAddress;//": "施工详细地址",
    private String releaseTime;//": "2017-04-05: 12: 00.0",
    private ArrayList<String> imgs;
    private WoryTypeEntity jobType;
    private LoginEntity user;
    private double pointLat;
    private double pointLon;

    public double getPointLat() {
        return pointLat;
    }

    public void setPointLat(double pointLat) {
        this.pointLat = pointLat;
    }

    public double getPointLon() {
        return pointLon;
    }

    public void setPointLon(double pointLon) {
        this.pointLon = pointLon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecruitNum() {
        return recruitNum;
    }

    public void setRecruitNum(String recruitNum) {
        this.recruitNum = recruitNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public WoryTypeEntity getJobType() {
        return jobType;
    }

    public void setJobType(WoryTypeEntity jobType) {
        this.jobType = jobType;
    }

    public LoginEntity getUser() {
        return user;
    }

    public void setUser(LoginEntity user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public WorkInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.city, flags);
        dest.writeString(this.recruitNum);
        dest.writeString(this.price);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.detailAddress);
        dest.writeString(this.releaseTime);
        dest.writeStringList(this.imgs);
        dest.writeParcelable(this.jobType, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeDouble(this.pointLat);
        dest.writeDouble(this.pointLon);
    }

    protected WorkInfoEntity(Parcel in) {
        this.id = in.readString();
        this.city = in.readParcelable(City.class.getClassLoader());
        this.recruitNum = in.readString();
        this.price = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.detailAddress = in.readString();
        this.releaseTime = in.readString();
        this.imgs = in.createStringArrayList();
        this.jobType = in.readParcelable(WoryTypeEntity.class.getClassLoader());
        this.user = in.readParcelable(LoginEntity.class.getClassLoader());
        this.pointLat = in.readDouble();
        this.pointLon = in.readDouble();
    }

    public static final Creator<WorkInfoEntity> CREATOR = new Creator<WorkInfoEntity>() {
        @Override
        public WorkInfoEntity createFromParcel(Parcel source) {
            return new WorkInfoEntity(source);
        }

        @Override
        public WorkInfoEntity[] newArray(int size) {
            return new WorkInfoEntity[size];
        }
    };
}
