package com.saake.invoicer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wxx244
 * Date: 6/15/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailNotifInfo {
    private List<String> originalToAddressList = new ArrayList<String>();
    private List<String> toAddressList = new ArrayList<String>();
    private List<String> ccAddressList = new ArrayList<String>();
    private String fromAddress;
    private String msg ;
    private String subject ;
    private String[] attachments ;
    private String[] attachmentsNames ;
    private List dataList = new ArrayList();

    private String template ;
    
    public List<String> getToAddressList() {
        return toAddressList;
    }

    public void setToAddressList(List<String> toAddressList) {
        this.toAddressList = toAddressList;
    }

    public List<String> getCcAddressList() {
        return ccAddressList;
    }

    public void setCcAddressList(List<String> ccAddressList) {
        this.ccAddressList = ccAddressList;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public String[] getAttachmentsNames() {
        return attachmentsNames;
    }

    public void setAttachmentsNames(String[] attachmentsNames) {
        this.attachmentsNames = attachmentsNames;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public List<String> getOriginalToAddressList() {
        return originalToAddressList;
    }

    public void setOriginalToAddressList(List<String> originalToAddressList) {
        this.originalToAddressList = originalToAddressList;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
        
}
