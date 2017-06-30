package com.wanny.workease.system.framework_mvpbasic;

/**
 * 文件名： BaseOperateImp
 * 功能：
 * 作者： wanny
 * 时间： 9:35 2017/3/9
 */
public interface BaseOperateImp<T> {
    void success(T t);
    void fail(String errorMessage);
    void loadIng(String title);
    void hide();
}
