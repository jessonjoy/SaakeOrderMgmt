/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jn
 */
public class JRHtmlInfo {
    String htmlString;
    Map imagesData = new HashMap();

    public JRHtmlInfo(String htmlString, Map imagesData) {
        this.htmlString = htmlString;
        this.imagesData = imagesData;
    }
    
    public String getHtmlString() {
        return htmlString;
    }

    public void setHtmlString(String htmlString) {
        this.htmlString = htmlString;
    }

    public Map getImagesData() {
        return imagesData;
    }

    public void setImagesData(Map imagesData) {
        this.imagesData = imagesData;
    }
    
    
}
