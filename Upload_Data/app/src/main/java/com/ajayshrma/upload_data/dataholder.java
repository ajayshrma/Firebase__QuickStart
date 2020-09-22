package com.ajayshrma.upload_data;

public class dataholder {

    String mname,maddress,mphoneNo,mimage;

    public dataholder(String mname, String maddress, String mphoneNo) {
        this.mname = mname;
        this.maddress = maddress;
        this.mphoneNo = mphoneNo;
        this.mimage = mimage;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMaddress() {
        return maddress;
    }

    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }

    public String getMphoneNo() {
        return mphoneNo;
    }

    public void setMphoneNo(String mphoneNo) {
        this.mphoneNo = mphoneNo;
    }

    public String getMimage() {
        return mimage;
    }

    public void setMimage(String mimage) {
        this.mimage = mimage;
    }
}
