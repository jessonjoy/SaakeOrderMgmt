/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.util;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jn
 */
public class Constants {
    
    public static int COOKIE_AGE = 2592000; //30 days
    public static String COOKIE_NAME = "saake-login";    
    
    public static final List<String> monthsList = Collections.unmodifiableList(
                                    Arrays.asList(new DateFormatSymbols().getMonths()));
    
    public static final List<Integer> vehYearsList = Collections.unmodifiableList(
                                    Utils.getYearsList(40));
    
    public static String contextName = "/saake/orderMgr";

}
