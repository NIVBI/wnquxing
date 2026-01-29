package com.wnquxing.entity.dto;

import java.io.Serializable;

public class TokenUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private String nickname;
    private Integer sex;
    private String userId;
    private String curNickname;
    private Boolean admin;
    private String myMeetingNo;
    private String curMeetingNo;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMyMeetingNo() {
        return myMeetingNo;
    }

    public void setMyMeetingNo(String myMeetingNo) {
        this.myMeetingNo = myMeetingNo;
    }

    public String getCurMeetingNo() {
        return curMeetingNo;
    }

    public void setCurMeetingNo(String curMeetingNo) {
        this.curMeetingNo = curMeetingNo;
    }

    public String getCurNickname() {
        return curNickname;
    }

    public void setCurNickname(String curNickname) {
        this.curNickname = curNickname;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenUserInfoDTO(String token, String nickname, String userId, Boolean admin) {
        this.token = token;
        this.nickname = nickname;
        this.userId = userId;
        this.admin = admin;
    }

    public TokenUserInfoDTO() {
    }
}
