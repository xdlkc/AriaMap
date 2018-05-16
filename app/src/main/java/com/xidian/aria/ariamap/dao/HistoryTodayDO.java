package com.xidian.aria.ariamap.dao;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by lkc on 2018/5/13.
 * 存储历史上的今天接口返回数据
 */

public class HistoryTodayDO {
    private String reason;
    private ArrayList<Detail> result;
    public class Detail{
        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }

        String day;
        String date;
        String title;
        String e_id;
    }
    private Integer error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ArrayList<Detail> getResult() {
        return result;
    }

    public void setResult(ArrayList<Detail> result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }
}
