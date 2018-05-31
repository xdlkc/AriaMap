package com.xidian.aria.ariamap.dao;

import java.util.List;

/**
 * 讯飞语音识别接口返回数据
 */
public class SpeechDo {
    private Integer sn;
    private Boolean ls;
    private Integer bg;
    private Integer ed;
    private List<SpeechV2> ws;
    public class SpeechV2{
        private Integer bg;
        private List<SpeechV3> cw;

        public Integer getBg() {
            return bg;
        }

        public void setBg(Integer bg) {
            this.bg = bg;
        }

        public List<SpeechV3> getCw() {
            return cw;
        }

        public void setCw(List<SpeechV3> cw) {
            this.cw = cw;
        }
    }
    public class SpeechV3{
        private Float sc;
        private String w;

        public Float getSc() {
            return sc;
        }

        public void setSc(Float sc) {
            this.sc = sc;
        }

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Boolean getLs() {
        return ls;
    }

    public void setLs(Boolean ls) {
        this.ls = ls;
    }

    public Integer getBg() {
        return bg;
    }

    public void setBg(Integer bg) {
        this.bg = bg;
    }

    public Integer getEd() {
        return ed;
    }

    public void setEd(Integer ed) {
        this.ed = ed;
    }

    public List<SpeechV2> getWs() {
        return ws;
    }

    public void setWs(List<SpeechV2> ws) {
        this.ws = ws;
    }
}
