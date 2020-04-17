package com.zone24x7.ibrac.recengine.pojo;

public class RecResult<P, O> {

    //Experience payload
    private P recPayload;
    //Object containing other information related to experience generation
    private O recOtherInfo;

    public P getRecPayload() {
        return recPayload;
    }

    public void setRecPayload(P recPayload) {
        this.recPayload = recPayload;
    }

    public O getRecOtherInfo() {
        return recOtherInfo;
    }

    public void setRecOtherInfo(O recOtherInfo) {
        this.recOtherInfo = recOtherInfo;
    }
}
