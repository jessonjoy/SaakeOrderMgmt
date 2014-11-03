/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.reports;

import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.entity.Vehicle;
import com.saake.invoicer.entity.WorkOrder;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.model.InvoiceItemsData;
import com.saake.invoicer.model.PaymentsData;
import com.saake.invoicer.model.ReportViewOptions;
import com.saake.invoicer.model.WorkOrderReportData;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.Utils;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.naming.InitialContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
public class ReportHelper {
    private static final Log log = LogFactory.getLog(ReportHelper.class);

    private static ReportViewOptions viewOptions = new ReportViewOptions();
            
    public static List<WorkOrderReportData> buildDataListForWorkOrderReport(WorkOrder wo) {    
        List<WorkOrderReportData> dataList = new ArrayList<>();
        if(wo != null){
            WorkOrderReportData dat = new WorkOrderReportData();
            
            dat.setAmount(viewOptions.showTotalAmount? wo.getAmount() : null);
            dat.setWorkOrderDate(wo.getWorkOrderDate());
            dat.setInvoiceDate(wo.getInvoicedTs());
            dat.setWorkOrderItems(convertWorkOrderItems(wo));
            dat.setPayments(viewOptions.showPaymentHistory? convertPayments(wo, dat):null);
            dat.setAddressLine1(wo.getCustomerId().getAddressLine1());
            dat.setAddressLine2(wo.getCustomerId().getAddressLine2());
            dat.setCity(wo.getCustomerId().getCity());
            dat.setCompanyName(wo.getCustomerId().getCompanyName());
            dat.setEmail(wo.getCustomerId().getEmail());
            dat.setFirstName(wo.getCustomerId().getFirstName());
            dat.setLastName(wo.getCustomerId().getLastName());
            dat.setStateProvince(wo.getCustomerId().getStateProvince());
            dat.setMobileNum(wo.getCustomerId().getMobileNum());            
            dat.setNotes(wo.getCustWorkDesc());            
            dat.setWorkOrderId(wo.getWorkOrderId());            
            
            for(InvoiceItemsData datWOItem : dat.getWorkOrderItems()){
                datWOItem.setAmount(viewOptions.showItemAmounts? datWOItem.getAmount() : null);
                datWOItem.setUnitCost(viewOptions.showUnitPrice? datWOItem.getUnitCost(): null);
            }
            dat.setAssignee(viewOptions.showUserAssigned && wo.getAssignedUser() != null? wo.getAssignedUser().getFirstLastName() : null);            
            
            if(Utils.notEmpty(wo.getCustomerId().getCustomerVehicles())){
                Vehicle veh = wo.getCustomerId().getCustomerVehicles().get(0);
                dat.setMake(veh.getMake());            
                dat.setMileage(veh.getMileage());            
                dat.setModel(veh.getModel());            
                dat.setVin(veh.getVin());            
                dat.setYear(veh.getYear());
            }
            
            dataList.add(dat);
<<<<<<< HEAD

=======
>>>>>>> origin/master
        }
        return dataList;
    }
        
    private static <T> void streamPdf(T obj, Boolean download, String template, String type) throws IOException {
        byte[] pdfByteArray = null;
<<<<<<< HEAD
        
        if(Utils.notBlank(template)){
            pdfByteArray = generatePdfFromJasperTemplate(buildDataListForWorkOrderReport((WorkOrder)obj), template);
        }
=======
        String type = "";

         if(obj instanceof WorkOrder){
            type = "WorkOrder";
            pdfByteArray = generatePdfFromJasperTemplate(buildDataListForWorkOrderReport((WorkOrder)obj), "saakeWorkOrder.jasper");
        } 
>>>>>>> origin/master

        if (pdfByteArray != null && pdfByteArray.length > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                baos.write(pdfByteArray);
                ExternalContext ec = JsfUtil.getExternalContext();
                ec.responseReset();
                ec.setResponseContentType("application/pdf");
                ec.setResponseContentLength(baos.size());
                if(download){
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=saake-"+type+".pdf");
                }
                ec.setResponseHeader("Expires", "0");
                ec.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                ec.setResponseHeader("Pragma", "private");

                baos.writeTo(ec.getResponseOutputStream());
                ec.getResponseOutputStream().flush();
            } finally {
                JsfUtil.getFacesContext().responseComplete();

                baos.close();
            }
        }
    }    
    
    public static <T> byte[] generatePdfFromJasperTemplate(List<T> dataList, String template) {
        byte[] pdfByteArray = null;
        try{
            if (Utils.notEmpty(dataList) && Utils.notBlank(template)) {
                try {
                    
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    parameters.put(JRFillParameter.REPORT_FILE_RESOLVER, fileResolver);
                    
                    JasperPrint jasperPrint = fillJasperTemplate(template, "JAVABEAN", dataList, parameters);

                    if (jasperPrint != null) {
                        pdfByteArray = generatePdfBytesFromJasperTemplate(jasperPrint);
<<<<<<< HEAD
                        
//                        JRHtmlInfo jRHtmlInfo = ReportHelper.generateHtmlFromJasperTemplateV2(jasperPrint);
//                        EmailHelper emailHelper = new EmailHelper();
//                        List<ByteArrayDataSource> imgDSList = emailHelper.getImageDataSourceArr(jRHtmlInfo.getImagesData());
//                        
//                        MailServices ms = new MailServices("mail/defaultsession");
//
//                        ms.sendEmail("jessonjoy9@gmail.com"
//                                    , "jessonjoy9@gmail.com",
//                                    "TEST from GF", jRHtmlInfo.getHtmlString(), imgDSList);
                     
                    }                                   
            
=======
                    }
>>>>>>> origin/master
                } catch (Exception e) {
                    throw new Exception("Error exporting pdf", e);
                }
            } else {
                throw new Exception("No data to generate pdf!");
            }
        }catch (Exception e){
            log.error("",e);
            System.out.println(e.getMessage());
            JsfUtil.addErrorMessage("Error exporting pdf for template:"+template);
        }

        return pdfByteArray;
    }
    
    public static <T> JasperPrint fillJasperTemplate(String templateName, String dataSrc, List<T> dataList,
                                                               Map<String, Object> params) throws Exception{
        InputStream is;
        JasperReport jReport = null;
        JasperPrint jPrint = null;

        if (Utils.isEmpty(dataList)) {
            throw new Exception("No data to fill");
        }
        
        if(Utils.notBlank(templateName)){
            try{
//                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com"+Constants.contextName+"/invoicer/reports/"+templateName);
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("reports/"+templateName);

                if (is != null) {
                    jReport = (JasperReport) JRLoader.loadObject(is);
                }

                if (jReport != null) {
                    
                    if ( "JDBC".equalsIgnoreCase(dataSrc)){
                        Connection con = ((DataSource) (new InitialContext().lookup(""))).getConnection();
                        jPrint = JasperFillManager.fillReport(jReport, params, con);
                    }
                    else
                    if ( "JAVABEAN".equalsIgnoreCase(dataSrc)){
                        JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataList);
                        jPrint = JasperFillManager.fillReport(jReport, params, jrDataSource);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

                throw new Exception("Error generating JR Template:"+templateName,e);
            }
        }
        else{
            throw new Exception("No template to fill");
        }

        return jPrint;
    }
     
     public static byte[] generatePdfBytesFromJasperTemplate(JasperPrint jPrint){
        byte[] pdfByteArray = new byte[0];

        try {
            pdfByteArray = JasperExportManager.exportReportToPdf(jPrint);
//            jrHtmlExp.exportReport();
        } catch (JRException e) {
            log.error("Error generating PDF from Jasper Template:"+ jPrint.getName(),e);
        }

        return pdfByteArray;

    }
     
    public static void printJasperReport(JasperPrint jasperPrint) throws Exception {        
        try {

            PrinterJob job = PrinterJob.getPrinterJob();
            /* Create an array of PrintServices */
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            job.defaultPage(pf);
            int selectedService = 0;


            String theUserPrinterName = "\\\\office1\\printer1";
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(theUserPrinterName, null));
            services = PrintServiceLookup.lookupPrintServices(null, attrSet);
                
            job.setPrintService(services[selectedService]);
            
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
            printRequestAttributeSet.add(new Copies(1));

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            /* We set the selected service and pass it as a paramenter */
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        } catch (Exception e) {
           throw new Exception("Error processing printing",e);
//            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
//            try {
//                exporter.exportReport();
//            } catch (JRException e2) {
//                e2.printStackTrace();
//            }
        }
    }
    
    
    public static void printWorkOrder(WorkOrder wo) throws Exception{
        try {
            JasperPrint jasperPrint = 
                    fillJasperTemplate("saakeWorkOrder.jasper", "JAVABEAN", buildDataListForWorkOrderReport(wo), null);

            if (jasperPrint != null) {
                printJasperReport(jasperPrint);
            }
        } catch (Exception e) {
            throw new Exception("Error exporting pdf", e);
        }
    }
    
//    public static void viewWorkOrderPDF(WorkOrder wo) throws IOException {
//        streamPdf(wo, Boolean.FALSE, "wo");
//    }
//
//    public static void downloadWorkOrderPDF(WorkOrder wo) throws IOException {
//        streamPdf(wo, Boolean.TRUE, "wo");
//    }
       
    private static FileResolver fileResolver = new FileResolver() {
        @Override
        public File resolveFile(String fileName) {
            URI uri;
            File file = null;
            try {
                URL urlImg = this.getClass().getResource("/reports/"+fileName);
                
                if(urlImg != null){
                    uri = new URI(urlImg.getPath());
                    file = new File(uri.getPath());
                }                
            } catch (URISyntaxException e) {
                log.error("",e);
            }
            return file;
        }
    };

    private static List<InvoiceItemsData> convertWorkOrderItems(WorkOrder workOrder) {
       List<InvoiceItemsData> list = new ArrayList<>();
        
       if(workOrder.getWorkOrderItems() != null && Utils.notEmpty(workOrder.getWorkOrderItems())){
        for(WorkOrderItems invItem : workOrder.getWorkOrderItems()){
             InvoiceItemsData itData = new InvoiceItemsData();

             itData.setAmount(invItem.getAmount());
             itData.setDescription(invItem.getDescription());
             itData.setQuantity(invItem.getQuantity());
             itData.setUnitCost(invItem.getUnitPrice());

             list.add(itData);
         }
       }
        
        InvoiceItemsData itData = new InvoiceItemsData();
        itData.setDescription("Labor for Services");
        itData.setQuantity(1);
        itData.setAmount(workOrder.getLaborAmt());
        list.add(itData);    
        
        return list;
    }

    public static void viewPDF(String type, WorkOrder current, ReportViewOptions viewOptions) throws IOException {
        String template = "";
        if(type.equalsIgnoreCase("wo")){
            template = "saakeWorkOrder.jasper";            
            viewWorkOrderPDF(current, viewOptions, template);
        } 
        else if(type.equalsIgnoreCase("inv")){
            template = "saakeInvoice.jasper";
            viewInvoicePDF(current, viewOptions, template);
        } 
        
    }
        
    public static void viewWorkOrderPDF(WorkOrder wo, ReportViewOptions viewOptions1, String template) throws IOException {        
        viewOptions = viewOptions1;
        streamPdf(wo, Boolean.FALSE,template,"wo");    
    }    

    public static void viewInvoicePDF(WorkOrder wo, ReportViewOptions viewOptions1, String template) throws IOException {        
        viewOptions = viewOptions1;
        streamPdf(wo, Boolean.FALSE,template,"inv");    
    }

    private static List<PaymentsData> convertPayments(WorkOrder workOrder, WorkOrderReportData dat) {
        List<PaymentsData> list = new ArrayList<>();
        
       if(Utils.notEmpty(workOrder.getTransactions())){
        for(Transaction invItem : workOrder.getTransactions()){
             PaymentsData itData = new PaymentsData();

             itData.setAmount(invItem.getAmount());
             itData.setComments(invItem.getComments());
             itData.setPaymentDate(invItem.getTransDate());

             list.add(itData);
             
             dat.setTotPaidAmt(dat.getTotPaidAmt() + itData.getAmount());
         }
       }              
        
        return list;

    }

}
