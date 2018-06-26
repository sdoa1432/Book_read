package com.yuexun.book_read.model;

/**
 * Created by yuexun on 2018/6/8.
 */

public class ChapterInfoModel {
    public int chapterId;
    public String chapterTitle;
    public boolean isVip;

    @Override
    public String toString() {
        return "ChapterInfoModel{" +
                "chapterId=" + chapterId +
                ", chapterTitle='" + chapterTitle + '\'' +
                ", isVip=" + isVip +
                '}';
    }
}
