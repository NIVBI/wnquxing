package com.wnquxing.entity.dto;

import java.io.Serializable;

public class MessageSendDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer messageSend2Type;

    private String meetingNo;

    private Integer messageId;

    private Integer messageType;

    private String sendUserId;

    private String sendUserNickname;

    private String receiveUserId;

    private T messageContent;

    private Integer status;

    private Long sendTime;

    private Integer fileType;

    private String fileName;

    private Long fileSize;

    public Integer getMessageSend2Type() {
        return messageSend2Type;
    }

    public void setMessageSend2Type(Integer messageSend2Type) {
        this.messageSend2Type = messageSend2Type;
    }

    public String getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(String meetingNo) {
        this.meetingNo = meetingNo;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }



    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getSendUserNickname() {
        return sendUserNickname;
    }

    public void setSendUserNickname(String sendUserNickname) {
        this.sendUserNickname = sendUserNickname;
    }

    public T getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(T messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "MessageSendDto{" +
                "messageSend2Type=" + messageSend2Type +
                ", meetingNo='" + meetingNo + '\'' +
                ", messageId=" + messageId +
                ", messageType=" + messageType +
                ", sendUserId=" + sendUserId +
                ", sendUserNickname='" + sendUserNickname + '\'' +
                ", receiveUserId=" + receiveUserId +
                ", messageContent=" + messageContent +
                ", status=" + status +
                ", sendTime=" + sendTime +
                ", fileType=" + fileType +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}