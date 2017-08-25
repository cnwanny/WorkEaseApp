package com.wanny.workease.system.framework_net.retrofit;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.ServicePhoneResult;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WordPeopleResult;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.RegisterResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 文件名： ApiStores
 * 功能：
 * 作者： wanny
 * 时间： 15:58 2016/8/5
 */
public interface ApiStores {
    //接口地址
    String API_SERVER_URL = "http:139.224.235.180:8080/MigrantApi/";

    String ASID = "663EA8E60B7C428FB301B4990A2C0AE3";

    //登录
//
    @GET("u/login")
    Observable<LoginResult> login(@Query("mobile") String mobile, @Query("password") String password, @Query("type") String type);

    //获取客服热线
    @GET("comm/getCustomers")
    Observable<ServicePhoneResult> getServicePhone();


    //修改密码
    @GET("u/modifyPwd")
    Observable<OrdinalResultEntity> modifyPsd(@Query("userId") String userId, @Query("oldpwd") String oldpwd, @Query("newpwd") String newpwd);


    @GET("u/register")
    Observable<RegisterResult> register(@Query("mobile") String mobile, @Query("password") String password, @Query("type") String type, @Query("userName") String userName, @Query("areaId") String areaId, @Query("jobTypeId") String jobTypeId, @Query("workyear") String workyear, @Query("senior") String senior);
//


    @GET("task/getTaskes")
    Observable<LoginResult> getTaskes(@Query("mobile") String mobile, @Query("password") String password);


    @GET("comm/getJobTypes")
    Observable<WorkTypeResult> getWorkType();


    //获取城市
    @GET("comm/allCity")
    Observable<CityResult> getCity();


    //获取城市
    @GET("  task/deleteTaskById")
    Observable<OrdinalResultEntity> deleteTaskById(@Query("taskId") String taskId);


    @GET("task/getTaskes")
    Observable<WorkResult> getWorkResult(@Query("pageNum") int pageNum);

    @GET("task/getTasksByAreaOrJobtype")
    Observable<WorkResult> getWorkByAreaId(@Query("areaId") String areaId, @Query("jobTypeId") String jobTypeId, @Query("pageNum") int pageNum);


    //u/userInfomation
    //获取用户信息
    @GET("u/userInformation")
    Observable<CustomerInofResult> getUserInfo(@Query("userId") String userId, @Query("userName") String userName);

    //userId
    // userName
    //   mobile
    // areaId
    //        jobTypeId
    //senior

    @GET("u/modify")
    Observable<CustomerInofResult> modifyUserInfo(@Query("userId") String userId, @Query("userName") String userName, @Query("mobile") String mobile, @Query("userState") String userState, @Query("areaId") String areaId, @Query("jobTypeId") String jobTypeId, @Query("senior") String senior, @Query("workyear") String workyear);
    //注册
//     //获取列表
//    @GET("Project/GetBackPriceList")
//    Observable<String> getBackPriceList(@Query("page") int page, @Query("pageSize") int pageSize, @Query("type") int type, @Query("key") String key);


    @GET("u/getPeoples")
    Observable<WordPeopleResult> getWorkInfo(@Query("pageNum") int pageNum, @Query("jobTypeId") String jobTypeId, @Query("userState") String userState);


    @GET("task/releaseTask")
    Observable<OrdinalResultEntity> releaseTask(@Query("userId") String userId, @Query("areaId") String areaId, @Query("jobTypeId") String jobTypeId, @Query("recruitNum") int recruitNum, @Query("price") String price, @Query("name") String name, @Query("desc") String desc, @Query("detailAddress") String detailAddress, @Query("imgs") String imgs, @Query("pointLat") double pointLat, @Query("pointLon") double pointLon);


    @GET("task/editTask")
    Observable<OrdinalResultEntity> modifyTask(@Query("taskId") String taskId, @Query("userId") String userId, @Query("areaId") String areaId, @Query("jobTypeId") String jobTypeId, @Query("recruitNum") int recruitNum, @Query("price") String price, @Query("name") String name, @Query("desc") String desc, @Query("detailAddress") String detailAddress, @Query("imgs") String imgs, @Query("pointLat") double pointLat, @Query("pointLon") double pointLon);


    //获取自己发布的职位
    @GET("task/getTaskesByUser")
    Observable<WorkResult> getMyWorkResult(@Query("userId") String userId, @Query("pageNum") int pageNum);


//    //获取孩子
//    @FormUrlEncoded
//    @POST("/api/Student/GetParentStudents")
//    Observable<StudentResult> getChild(@Field("ParentId") int ParentId, @Field("sign") String sign);


//    //    //获取课程表
//    @FormUrlEncoded
//    @POST("/api/SchoolClass/GetClassSchedule")
//    Observable<SchedulEntity> getSchedul(@Field("SchoolClassId") String SchoolClassId, @Field("sign") String sign);
//
//
//    //获取短信验证码
//    @FormUrlEncoded
//    @POST("/api/Code/GetCode")
//    Observable<OrdinalResultEntity> getCode(@Field("PhoneNumber") String PhoneNumber, @Field("CodeType") int CodeType, @Field("sign") String sign);
//
//    //    未读通知
//    @FormUrlEncoded
//    @POST("/api/Count/GetTeacherCounts")
//    Observable<NotReadResut> getNotCounts(@Field("TeacherId") String TeacherId, @Field("sign") String sign);
//
//    //
////    //获取轮播图片
//    @GET("/api/Img/GetRotationImgs")
//    Observable<BannerResult> getBanner(@Query("schoolId") int schoolId);
//
//    //    //获取平台图片
//    @FormUrlEncoded
//    @POST("/api/Img/GetPlatformImgs")
//    Observable<PlatImageResult> getPlateImage(@Field("ImgType") int ImgType, @Field("sign") String sign);
//
//    //    //    PageIndex(分页页码)，PageSize(分页大小)，SchoolId(学校Id)
////    //获取新闻
//    @FormUrlEncoded
//    @POST("/api/SchoolNews/GetSchoolNews")
//    Observable<NewsResultEntity> getSchoolNews(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("SchoolId") int SchoolId, @Field("sign") String sign);

    //    //课程表
//    @FormUrlEncoded
//    @POST("/api/SchoolClass/GetClassSchedule")
//    Observable<FoodResult> getMySchedul(@Field("SchoolClassId") int SchoolClassId);
//
//

//
//    //获取班级圈
//    @FormUrlEncoded
//    @POST("/api/ParentTeacherAppraise/ReviewTeacherAppraises")
//    Observable<OrdinalResultEntity> recallParent(@Field("Id") String Id, @Field("ParentId") String ParentId, @Field("ParentReviewContent") String ParentReviewContent, @Field("sign") String sign);
//
//
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/GetClassCircles")
//    Observable<FriendsResult> getFriendsClass(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("SchoolClassId") String SchoolClassId, @Field("sign") String sign);
//
//    //家校互评
//    @FormUrlEncoded
//    @POST("/api/ParentTeacherAppraise/GetTeacherAppraises")
//    Observable<ParentAndTeacherResult> getParentTeacherAppraise(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("TeacherId") String TeaacherId, @Field("SchoolClassId") String SchoolClassId, @Field("sign") String sign);
//
////    @Multipart
//    @POST("UploadServlet")
//    Call<ResponseBody> upload(@Part("description") RequestBody description,
//                              @Part MultipartBody.Part file);

//
//    //获取家庭作业
//    @FormUrlEncoded
//    @POST("/api/StudentWork/GetParentStudentWorks")
//    Observable<HomeWorkResult> getHomeWord(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("StudentId") int  StudentId, @Field("sign") String sign);
//
//    //设置班级通知已读
//    @FormUrlEncoded
//    @POST("/api/ClassNotice/SetClassNoticeReaded")
//    Observable<HomeWorkResult> readClassNotic(@Field("Id") int Id, @Field("AppUserType") int AppUserType, @Field("StudentId") int  StudentId,  @Field("TeacherId") int  TeacherId ,  @Field("sign") String sign);
//
//    //设置作业为已读
//    @FormUrlEncoded
//    @POST("/api/StudentWork/SetStudentWorkReaded")
//    Observable<HomeWorkResult> readHomework(@Field("Id") int Id, @Field("StudentId") int StudentId, @Field("sign") String sign);
//
//
//    //家校互评
//    @FormUrlEncoded
//    @POST("/api/ParentTeacherAppraise/GetParentAppraises   ")
//    Observable<ParentAndTeacherResult> getParentTeacherAppraise(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("StudentId") int  StudentId, @Field("sign") String sign);
//
//    @FormUrlEncoded
//    @POST("/api/Hx/GetHxContacts")
//    Observable<ContactResult> getHxContact(@Field("SchoolClassId") int PageIndex, @Field("sign") String sign);
//
//    //获取班级通知
//    @FormUrlEncoded
//    @POST("/api/ClassNotice/GetClassNotices")
//    Observable<ClassNotificResult> getClassNoti(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("SchoolClassId") int  classId, @Field("sign") String sign);
//
//
//
//    @GET("/api/Area/Get")
//    Observable<CityResult> getCity(@Query("dataid") String id,@Query("sign") String sign);
//
//
//
//    //通过区域获取学校
//    @FormUrlEncoded
//    @POST("/api/School/GetSchools")
//    Observable<SchoolResult> getSchoolByCity(@Field("AreaId") String DataID, @Field("sign") String sign);
//
//
//    //通过学校获取班级
//    @FormUrlEncoded
//    @POST("/api/SchoolClass/GetClassBySchoolId")
//    Observable<ApplyClassResult> getClassBySchool(@Field("SchoolId") int SchoolId, @Field("sign") String sign);
//
//
//
//
//    @FormUrlEncoded
//    @POST("/api/Register/Pregister")
//    Observable<OrdinalResultEntity> register(@Field("PhoneNumber") String PhoneNumber,@Field("Pwd") String Pwd,@Field("sign") String sign);
//
//    //重置密码
//    @FormUrlEncoded
//    @POST("/api/Register/ParentModifyPwd")
//    Observable<OrdinalResultEntity> resetPsd(@Field("PhoneNumber") String PhoneNumber,@Field("Pwd") String Pwd,@Field("sign") String sign);
//
//
//
//
//    //申请请假
//    @FormUrlEncoded
//    @POST("/api/StudentAskLeave/AddAskLeave")
//    Observable<OrdinalResultEntity> addLeave(@Field("ParentId") int ParentId,@Field("ParentName") String ParentName, @Field("StudentId") int StudentId,@Field("SchoolClassId") String SchoolClassId,@Field("Content") String Content , @Field("sign") String sign);
//
//
//    //获取请假列表
//    @FormUrlEncoded
//    @POST("/api/StudentAskLeave/GetParentAskLeaves")
//    Observable<ClassNotificResult> getLeaveList(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("StudentId") int  StudentId, @Field("sign") String sign);
//
//
//
//
//
//    //入班申请
//    @FormUrlEncoded
//    @POST("/api/ParentStudentApply/AddApply")
//    Observable<OrdinalResultEntity> applyClass(@Field("StudentIName") String StudentIName, @Field("ParentAppellative") String ParentAppellative, @Field("SchoolClassId") String  SchoolClassId, @Field("ParentId") String  ParentId  ,@Field("ParentName") String  ParentName,@Field("sign") String sign);
//
//
//
//    @FormUrlEncoded
//    @POST("/api/ParentTeacherAppraise/ReviewTeacherAppraises")
//    Observable<OrdinalResultEntity> recallParent(@Field("Id") String Id, @Field("ParentId") String ParentId, @Field("ParentReviewContent") String  ParentReviewContent,@Field("sign") String sign);
//
//
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/GetClassCircles")
//    Observable<FriendsResult> getFriendsClass(@Field("PageIndex") int PageIndex, @Field("PageSize") int PageSize, @Field("SchoolClassId") String  SchoolClassId, @Field("sign") String sign);
//
//
//
//    //新增班级
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/AddClassCircle")
//    Observable<FriendsResult> getFriendsClass(@Field("SchoolClassId") String SchoolClassId, @Field("AppUserType") int AppUserType, @Field("CreateUserName") String  CreateUserName,@Field("ImgSrc") String  ImgSrc,@Field("Content") String  Content, @Field("sign") String sign);
//
//    //删除班级圈
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/DeleteClassCircle")
//    Observable<OrdinalResultEntity> deleteFriends(@Field("ClassCircleId") int ClassCircleId, @Field("AppUserType") int AppUserType, @Field("CreateUserId") int  CreateUserId, @Field("sign") String sign);
//
//    //班级圈评论  //ClassCircleId(班级圈Id)，AppUserType(1：教师，2：家长),CreateUserId(创建者Id),CreateUserName(创建者姓名)，CommentId（被回复的评论ID）CommentUserName(被回复的评论的创建者)，Content（内容）
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/AddClassCircleComment")
//    Observable<OrdinalResultEntity> addFriendComment(@Field("ClassCircleId") int ClassCircleId, @Field("AppUserType") int AppUserType, @Field("CreateUserId") int  CreateUserId, @Field("CreateUserName") String  CreateUserName, @Field("CommentId") int  CommentId, @Field("CommentUserName") String  CommentUserName,@Field("Content") String  Content,  @Field("sign") String sign);
//    //朋友圈点赞 ClassCircleId(班级圈Id)，AppUserType(1：教师，2：家长),CreateUserId(创建者Id),CreateUserName(创建者姓名)
//    @FormUrlEncoded
//    @POST("/api/ClassCircle/AddClassCirclePraise")
//    Observable<OrdinalResultEntity> friendsPraise(@Field("ClassCircleId") int ClassCircleId, @Field("AppUserType") int AppUserType, @Field("CreateUserId") int  CreateUserId,@Field("CreateUserName") String  CreateUserName,@Field("sign") String sign);
//


    //    String URL = "http://pg.17gp.com";
//    String returnPath = URL + "/app/images/ReturnPrice.jpg";
//    String returnedPath = URL + "/app/images/ReturnedPrice.jpg";
//    String aboutHtml = "http://pg.17gp.com/app/AppHtml/index.html";

//    //通过Post
//    @FormUrlEncoded
//    @POST("/api/UserInfo/Login")
//    Observable<Loginentity> loginGetData(@Field("account") String account, @Field("password") String password);
//
//    //    @FormUrlEncoded
//    @POST("/api/Communitys/AddOrUpdate")
//    Observable<CreateBulidEntity> addBuild(@Body AddBuildEntity entity);
//
//
////    @POST("/api/Communitys/AddOrUpdate")
////    Observable<CreateBulidEntity> updateBuild(@Body AddBuildEntity entity);
//
//
//    //查询楼盘数据
//    @GET("/api/Communitys/GetCommunityById")
//    Observable<ResultEntity> getCommunityById(@Query("id") String buildGuid);
//
//    @FormUrlEncoded
//    @POST("/api/Communitys/AddCommunityLocation")
//    Observable<AddLocationEntity> addBuildLocation(@Field("id") String id, @Field("lng") double lng, @Field("lat") double lat);
//
//
//    @GET("/api/Communitys/RemoveCommunityLocation")
//    Observable<AddLocationEntity> removeBuildLocation(@Query("id") String buildGuid);
//
//
//    //创建楼栋
//    @POST("/api/Buildings/AddBuildings")
//    Observable<AddBuildsEntity> addLouDongs(@Body ArrayList<AddLoudongSEntity> entity);
//
//    //更新或者修改楼栋信息
//    @POST("/api/Buildings/AddOrUpdate")
//    Observable<OrdinalResultEntity> updateAndAddLouDong(@Body UpdateLoudongEntity entity);
//
//
//    //获取楼栋列表
//    @GET("/api/Buildings/GetBuildingList")
//    Observable<BuildsListEntity> getBuilds(@Query("communityGuid") String communityGuid, @Query("key") String key);
//
//
//    @POST("/api/Buildings/AddRooms")
//    Observable<AddroomEntity> addRooms(@Body ArrayList<AddLoudongSEntity> entity);
//
//    //添加楼栋的位置
//    @FormUrlEncoded
//    @POST("/api/Buildings/AddBuildingLocation")
//    Observable<AddLocationEntity> addLouDongLocation(@Field("id") String id, @Field("lng") double lng, @Field("lat") double lat);
//
//
//    //移除楼栋位置信息
//    @GET("/api/Buildings/RemoveBuildingLocation")
//    Observable<AddLocationEntity> removeLouDongLocation(@Query("id") String buildGuid);
//
//    //添加房号的位置
//    @FormUrlEncoded
//    @POST(" /api/Buildings/AddRoomLocation")
//    Observable<AddLocationEntity> addRoomLocation(@Field("id") String id, @Field("lng") double lng, @Field("lat") double lat);
//
//
//    //移除房号位置信息
//    @GET("/api/Buildings/RemoveRoomLocation")
//    Observable<AddLocationEntity> removeRoomLocation(@Query("id") String buildGuid);
//
//
//    @FormUrlEncoded
//    @POST("/api/Buildings/UpdateTotalFloor")
//    Observable<OrdinalResultEntity> updateTotalFloor(@Field("id") String id, @Field("name") String name, @Field("floors") int lat);
//
//
//    @FormUrlEncoded
//    @POST("/api/Buildings/UpdateElevatorCount")
//    Observable<OrdinalResultEntity> updateElevator(@Field("id") String id, @Field("name") String name, @Field("floors") int floors, @Field("elevators") int elevators);
//
//    //移除房号位置信息
//    @GET("/api/Buildings/GetRooms")
//    Observable<GetRoomsEntity> getRooms(@Query("buildingGuid") String buildingGuid, @Query("key") String key);
//
//
//    @GET("/api/Communitys/GetCommunityList")
//    Observable<SearchBuildEntity> getSearchBuilds(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("cityName") String cityName, @Query("key") String key, @Query("logUser") String logUser);
//
//
//    //通过当前的位置获取附近的楼盘
//    @GET("/api/Communitys/GetAroundCommunityList")
//    Observable<SearchBuildEntity> getBuildsByLocation(@Query("pageSize") int pageSize, @Query("cityName") String cityName, @Query("lng") double lng, @Query("lat") double lat, @Query("redius") double redius, @Query("logUser") String logUser);
//
//
//    //app更新获取最新版本
//    @GET("/api/AppVersion/GetLatestVersion")
//    Observable<AppUpdateResult> getAppInfo();

//    //获取
////    @GET("Lnquiry/GetBackListRecord")
////    Observable<ReplyPriceModel> loadReplyData(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize , @Query("user") String user, @Query("beginTime") String beginTime, @Query("endTime") String endTime , @Query("keyword") String keyword , @Query("status") int status, @Query("sourceOrg") String sourceOrg);
//
//    //获取新建询价 /  @Query("beginTime") String beginTime, @Query("endTime") String endTime , @Query("keyword") String keyword , @Query("status") int status, @Query("sourceOrg") String sourceOrg
//    @GET("Lnquiry/GetBackRecordAndProvident")
//    Observable<ReplyPriceModel> loadReplyData(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("user") String user);
//
//    //获取全部的信息
//    @GET("Lnquiry/GetAllRecordAndProvident")
//    Observable<ReplyPriceModel> getAllReplyData(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("user") String user);
//
//    //询价记录
//    @GET("Lnquiry/GetDetails")
//    Observable<PriceInputModel> getDetailsData(@Query("rid") int rid, @Query("user") String user);
//
//    //回价接口
//    @FormUrlEncoded
//    @POST("Lnquiry/PostBackPrice")
//    Observable<ReplyPriceResultEntity> startReplyPrice(@Field("ID") int ID, @Field("Price") float Price, @Field("BackUserAccount") String BackUserAccount, @Field("BackUserName") String BackUserName, @Field("BackUserTel") String BackUserTel, @Field("TotalPrice") float TotalPrice, @Field("Remark") String Remark);
//
//   //获取用户发布的数据
//    @GET("Lnquiry/GetMyCenterData")
//    Observable<UserCenterModel> getUserData(@Query("userAccount") String userAccount);
//    //提交百度推送
//    @FormUrlEncoded
//    @POST("Login/AddBaiDu")
//    Observable<BaiduPushModel> setBaiduPush(@Field("UserAccount") String UserAccount, @Field("ChanelId") String ChanelId, @Field("Type") String Type);
//
//
//    //修改密码
//    @FormUrlEncoded
//    @POST("Login/ChangePassword")
//    Observable<ModifypasswordModel> modifyPassword(@Field("Account") String account, @Field("OldPwd") String OldPwd, @Field("NewPwd") String NewPwd);
//
//    //获取版本
//    @GET("Login/GetVersion")
//    Observable<VersionModel> getVersion();
//
//    @FormUrlEncoded
//    @POST("Login/Post")
//    Call<String> login(@Field("Account") String account, @Field("Password") String password, @Field("Client") String client, @Field("Application") String application);
//
//   //抢单详情页面
//    @GET("Lnquiry/Claimed")
//    Observable<GetTaskSuccessModel> postGetTask(@Query("rid") String rid, @Query("user") String user);
//    //抢单详情页面
//    @GET("Lnquiry/GetProvidentDetails")
//    Observable<GetTaskDetailModel> getTaskDetail(@Query("rid") String rid, @Query("user") String user);

}
