/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.reports;

import com.saake.invoicer.model.EmailNotifInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.activation.FileDataSource;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jn
 */
public class EmailHelper {

//    public static sendEmail()
     private void buildEmailBody(EmailNotifInfo info) throws Exception {
//        if("JR".equalsIgnoreCase(info.getTemplateType())){
            Map<String, Object> paramMap = new HashMap();
//            paramMap.put("HOME_URL", ElmHelper.buildElmReqURL(req.getElmReqId()));

            JasperPrint jasperPrint = ReportHelper.fillJasperTemplate(info.getTemplate(), "JAVABEAN", info.getDataList(), paramMap);

            if(jasperPrint != null){
                info.setMsg(ReportHelper.generateHtmlFromJasperTemplate(jasperPrint));
            }
//        }
//        else{
//            //TODO: pass it through a method to replace variables.
//            info.setMsg(notif.getEmailBody());
//        }
    }
     
    public List<ByteArrayDataSource> getImageDataSourceArr(Map imagesData) {
        List<ByteArrayDataSource> dsList = new ArrayList<>();
        
        Set<Entry> entries = imagesData.entrySet();
        for (Entry entry : entries) {
            dsList.add(new ByteArrayDataSource((String) entry.getKey(),(byte[]) entry.getValue(),"image/gif"));
        }
        return dsList;
    }
}
