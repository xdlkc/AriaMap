package com.xidian.aria.ariamap;

import java.util.List;

public class SpeechDo {
    Integer sn;
    Boolean ls;
    Integer bg;
    Integer ed;
    List<SpeechV2> ws;
    class SpeechV2{
        Integer bg;
        List<SpeechV3> cw;

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
    class SpeechV3{
        Float sc;
        String w;

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
