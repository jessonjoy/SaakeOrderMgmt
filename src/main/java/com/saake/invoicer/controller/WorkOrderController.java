/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.model.ReportViewOptions;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.Vehicle;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import com.saake.invoicer.entity.WorkOrder;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.entity.WorkOrderNotes;
import com.saake.invoicer.model.UserInvoiceVO;
import com.saake.invoicer.model.WorkOrderDataModel;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.sessionbean.WorkOrderFacade;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author jn
 */
@ManagedBean(name = "workOrderCtrl")
@ViewScoped
public class WorkOrderController implements Serializable {
    
    //@Inject
    private Log log = LogFactory.getLog(this.getClass().getName());
    
    private WorkOrderDataModel model;
    private WorkOrder current;
    private WorkOrderItems currentInvItem;
    private ReportViewOptions viewOptions;
    private UserInvoiceVO userInvoiceVO ;
    
//    private SearchWorkOrderVO filterCriteria = new SearchWorkOrderVO();
//    private Orders invoiceOrder;
    private Transaction currentTransaction = new Transaction();
    private WorkOrderNotes currentNotes = new WorkOrderNotes();
//    private Customer orderCustomer;
//    private CustomerVehicle custVehicle = new CustomerVehicle();
    private List<WorkOrder> originalWorkOrderList = null;
    private List<WorkOrder> workOrderList = null;
    private List<Vehicle> suggestVehicleList;
    
    @Inject
    CustomerController custCtrl;
    
    @Inject
    ItemController itemCtrl;
    
    @Inject
    private com.saake.invoicer.sessionbean.WorkOrderFacade woFacade;
    
    @Inject
    private com.saake.invoicer.sessionbean.ItemFacade itemFacade;
    
    private boolean redirect = false;
    private Boolean addNewVechicle = false;
    public Integer noOfRowsToAdd = 1;
    private boolean skipToWizardEnd;

    public WorkOrderController() {
        this.suggestVehicleList = new ArrayList<>();
        this.viewOptions = new ReportViewOptions();
        
        log.info("Inside WorkOrderController!!!");
        Object inv = JsfUtil.getRequestObject("item");

        if (inv != null && inv instanceof WorkOrder) {
            current = (WorkOrder) inv;
        }
//        String action = JsfUtil.getRequestParameter("action");
//        String invoiceId = JsfUtil.getRequestParameter("invoiceId");
//        
//        if("view".equalsIgnoreCase(action)){
//            if(Utils.notBlank(invoiceId)){
//                current = getFacade().find(Long.parseLong(invoiceId));
//            }            
//        }        
    }

    @PostConstruct
    private void initialize() {
        if (JsfUtil.getViewId().contains("create")) {
            initNewWorkOrder();
        } else if (JsfUtil.getViewId().contains("edit")) {
            editWorkOrderInit();
        } else if (JsfUtil.getViewId().contains("view")) {
            viewWorkOrderInit();
        } else if (JsfUtil.getViewId().contains("list")) {
            prepareList();
        }else if (JsfUtil.getViewId().contains("reports/userInvoices")) {
            initUserInvoice();
        }

        redirect = false;
    }

    public String initUserInvoice(){
        if(userInvoiceVO == null){
            userInvoiceVO = new UserInvoiceVO();            
        }
        
        //TODO:
//        userInvoiceVO.setInvoiceList(getInvoiceList());
//        groupUserInvByMonthlyLaborTots();
        return "";
    }
    
    public WorkOrder getSelected() {
        if (current == null) {
            initNewWorkOrder();
        }
        return current;
    }

    public String prepareList() {
        recreateModel();
        return deriveReturnString("list", false);
    }

    public String redirectToList() {
        return "list.jsf?faces-redirect=true";
    }

    public String prepareView() {
        return deriveReturnString("view", true);

    }

    private void recreateModel() {
        workOrderList = null;
        model = null;
        woFacade.clearCache();
    }

    public void initNewWorkOrder() {
        current = new WorkOrder();
        current.setWorkOrderItems(new ArrayList<WorkOrderItems>());
        addNewItemToWorkOrder();
        current.setWorkOrderDate(new Date());
        current.setAmount(0.0);
        current.setLaborAmt(0.0);
        current.setLaborAdjAmt(0.0);
        current.setAdvanceAmt(0.0);
        current.setStatus(WorkOrderStatusEnum.DRAFT.name());
    }

    public String prepareCreate() {
        initNewWorkOrder();
        return deriveReturnString("create", false);
    }

    public String save() {
        if (current != null) {
            if (validated()) {
                if (Utils.notEmpty(current.getWorkOrderItems())) {
                    List<WorkOrderItems> emptyList = new ArrayList<>();
                    for (WorkOrderItems items : current.getWorkOrderItems()) {
                        if (items.isEmptyForUse()) {
                            emptyList.add(items);
                        }
                    }
                    current.getWorkOrderItems().removeAll(emptyList);
                }

                if (current.getWorkOrderId() != null) {
                    return update();
                } else {
                    return create();
                }
            }
        }

        return null;
    }

    public String create() {
        try {
            
            getFacade().createWorkOrder(current);
            JsfUtil.addSuccessMessage("Work Order Created!");
            redirect = true;
            return prepareView();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();

        if (current != null) {
            if (Utils.notEmpty(current.getWorkOrderItems())) {
                for (WorkOrderItems woItm : current.getWorkOrderItems()) {
                    if (woItm.getItem() == null) {
                        woItm.setItem(new Item(0.00));
                    }
                }
            }
        }
        JsfUtil.addAttributeInRequest("item", current);

        redirect = true;

        return deriveReturnString("edit", true);

    }

    public String update() {
        try {
                       
            current = getFacade().updateWorkOrder(current);

            JsfUtil.addRequestObject("item", current);
            JsfUtil.addSuccessMessage("Work Order Updated!");

            redirect = true;
            return prepareView();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String delete() {
        try {
            getFacade().remove(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WorkOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public String softDelete() {
        try {
            getFacade().softDelete(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WorkOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public void addNewRowsToWorkOrder(Integer noOfRowsToAdd ) {
        for(int i = 0; i< noOfRowsToAdd; i++){
            addNewItemToWorkOrder();
        }
    }
    
    public String addNewItemToWorkOrder() {
        WorkOrderItems it = new WorkOrderItems();
        it.setAmount(0.00);
        it.setDiscount(0.00);
        it.setUnitPrice(0.00);
        it.setItem(new Item());
        it.getItem().setUnitPrice(0.00);
        it.setWorkOrderId(current); 
        it.setQuantity(1);
        current.getWorkOrderItems().add(it);
        return null;
    }

    public String removeOrderItem(WorkOrderItems ordItm) {
        if (ordItm != null) {
            if (current.getWorkOrderItems().size() > 1) {
                current.getWorkOrderItems().remove(ordItm);
            }

            calOrderPrice();
        }

        return null;
    }

    public String calOrderPrice() {
        Double amt = current.getItemTotalAmount();

        if (amt != null) {
            if (current.getDiscount() != null && current.getDiscount() > 0) {
                amt = amt - current.getDiscount();
            }
            current.setAmount(amt);
        } else {
            current.setAmount(0.0);
        }

        current.setAmount(current.getAmount() + current.getLaborAmt());
        return null;
    }

    public void calculateOrderItemPrice(WorkOrderItems ordItm) {
        if (ordItm != null && ordItm.getQuantity() != null) {
            ordItm.setAmount((ordItm.getUnitPrice() * ordItm.getQuantity()) - (ordItm.getDiscount() != null ? ordItm.getDiscount() * ordItm.getQuantity() : 0));
        }

        calOrderPrice();
    }
    
    public void populateItemOnWorkOrder(WorkOrderItems ordItm) {
        if(ordItm != null){
            if(ordItm.getItem() != null){
                ordItm.setUnitPrice(ordItm.getItem().getUnitPrice());
                ordItm.setDescription(ordItm.getItem().getDescription());
            }
        }
        
        calculateOrderItemPrice(ordItm);
    }    

    private WorkOrderFacade getFacade() {
        return woFacade;
    }

    public List<WorkOrder> getWorkOrderList() {
        if (workOrderList == null) {
            workOrderList = woFacade.findAll();
            originalWorkOrderList = new ArrayList(workOrderList);
        }
        return workOrderList;
    }

    public void setWorkOrderList(List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public WorkOrderFacade getWoFacade() {
        return woFacade;
    }

    public void setWoFacade(WorkOrderFacade woFacade) {
        this.woFacade = woFacade;
    }

    public WorkOrder getCurrent() {
        if (current == null) {
            initNewWorkOrder();
        }
        return current;
    }

    public void setCurrent(WorkOrder current) {
        this.current = current;
    }

    private void editWorkOrderInit() {
        viewWorkOrderInit();
    }

    private void viewWorkOrderInit() {
        String id = JsfUtil.getRequestParameter("id");

        if (Utils.notBlank(id)) {
            current = getFacade().getWorkOrder(Integer.parseInt(id));
        }
        else{
            Object woCopy = JsfUtil.getAttributeFromSession("woCopy");
            if(woCopy != null && woCopy instanceof WorkOrder){
                current = (WorkOrder) woCopy;
                JsfUtil.removeAttributeFromSession("woCopy");
            }
        }

        if (current != null) {
            if (current.getVehicle() == null) {
                initNewVehicleToWorkOrder();
            }
        }        
    }

    public void filterList() {
//        if (!filterCriteria.empty()) {
//            workOrderList.clear();
//
//            for (WorkOrder inv : originalWorkOrderList) {
//                if (Utils.notBlank(filterCriteria.getWorkOrderPeriod())) {
//                    if ("today".equalsIgnoreCase(filterCriteria.getWorkOrderPeriod())) {
//                        if (inv.getWorkOrderDate() != null && (inv.getWorkOrderDate().equals(new Date()))) {
//                            workOrderList.add(inv);
//                        }
//                    } else if ("yest".equalsIgnoreCase(filterCriteria.getWorkOrderPeriod())) {
////                            if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
////                                workOrderList.add(inv);
////                            }
//                    }
//                } else if (Utils.notBlank(filterCriteria.getStatus())) {
//                    if (Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getCustomer() != null && filterCriteria.getCustomer().getCustomerId() != null) {
//                    if (inv.getCustomerId() != null && inv.getCustomerId().getCustomerId() != null
//                            && inv.getCustomerId().getCustomerId().equals(filterCriteria.getCustomer().getCustomerId())) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null && filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()
//                            && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null && filterCriteria.getToDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getWorkOrderDate()) || filterCriteria.getFromDate().equals(inv.getWorkOrderDate()))
//                            && (filterCriteria.getToDate().before(inv.getWorkOrderDate()) || filterCriteria.getToDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getWorkOrderDate()) || filterCriteria.getFromDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getToDate().before(inv.getWorkOrderDate()) || filterCriteria.getToDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                }
//            }
//        }
    }

    public void resetSearch() {
        workOrderList = new ArrayList(originalWorkOrderList);
//        filterCriteria = new SearchWorkOrderVO();
    }
//
//    public SearchWorkOrderVO getFilterCriteria() {
//        return filterCriteria;
//        return filterCriteria;
//    }

//    public void setFilterCriteria(SearchWorkOrderVO filterCriteria) {
//        this.filterCriteria = filterCriteria;
//    }

    public WorkOrderDataModel getModel() {
        if (model == null) {
            model = new WorkOrderDataModel(getWorkOrderList());
        }
        return model;
    }

    public void setModel(WorkOrderDataModel model) {
        this.model = model;
    }

    public void addNewCustomerToWorkOrder() {
        log.info("inside addNewCustomerToWorkOrder");

        Customer cust = custCtrl.create(custCtrl.getCurrent());
        current.setCustomerId(cust);

        if (Utils.notEmpty(cust.getCustomerVehicles())) {
            current.setVehicle(cust.getCustomerVehicles().get(cust.getCustomerVehicles().size() - 1));
        } else {
            current.setVehicle(new Vehicle());
        }
    }

    public void createNewItemAndAddToWorkOrder() {
        log.info("inside createNewItemAndAddToWorkOrder");

        currentInvItem.setItem(itemCtrl.create(itemCtrl.getCurrent()));

        calculateOrderItemPrice(currentInvItem);
    }

    public ItemController getItemCtrl() {
        return itemCtrl;
    }

    public void setItemCtrl(ItemController itemCtrl) {
        this.itemCtrl = itemCtrl;
    }

    public CustomerController getCustCtrl() {
        return custCtrl;
    }

    public void setCustCtrl(CustomerController custCtrl) {
        this.custCtrl = custCtrl;
    }

    public void generateWorkOrderPdfAction() {
        JsfUtil.addAttributeInSession("invoice", current);
    }

    public void downloadPdf() {
        try {
            ReportHelper.downloadWorkOrderPDF(current);
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, "Error downloading pdf", ex);
        }
    }

    public void viewPdf() {
        try {
            ReportHelper.viewWorkOrderPDF(current, viewOptions);
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, "Error viewing pdf", ex);
        }
    }

    public void printWorkOrder() {
        try {
            ReportHelper.printWorkOrder(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice", e);
        }
    }

    public String copyWorkOrder() {
        try {
            if(current != null){
                current = WorkOrder.copy(current, new WorkOrder());
                current.setWorkOrderDate(new Date());
                current.setCreateTs(new Date());
                current.setUpdateTs(null);
                current.setUpdatedBy(null);
                current.setWorkOrderId(null);
                current.setInvoicedTs(null);
                current.setIsInvoiced("N");
                current.setStatus(WorkOrderStatusEnum.DRAFT.name());
                current.setPaymentStatus(null);
                current.setTransactions(null);
                
                redirect = false;
                
                JsfUtil.addAttributeInSession("woCopy", current);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice", e);
        }
        return deriveReturnString("edit", true);

    }

    private String deriveReturnString(String viewString, boolean addId) {
        if (redirect) {
            String id = "";
            if (addId && current != null && current.getWorkOrderId() != null) {
                id = "&id=" + current.getWorkOrderId();
            }

            return viewString + "?faces-redirect=true" + id;
        } else {
            return viewString;
        }
    }

    public WorkOrderItems getCurrentInvItem() {
        return currentInvItem;
    }

    public void setCurrentInvItem(WorkOrderItems currentInvItem) {
        this.currentInvItem = currentInvItem;
    }

    public void redirectToView(Integer id) {
        try {
            if (id == null || id == 0) {
                id = current.getWorkOrderId();
            }
            JsfUtil.getExternalContext().redirect(JsfUtil.getExternalContext().getRequestContextPath()+"/workorder/view.jsf?id=" + id) ;
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Vehicle> suggestVehicle(String inp) {

        suggestVehicleList.clear();

        if (current == null || current.getCustomerId() == null || Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
            JsfUtil.addInfoMessage("No Vehicles found");
            return null;
        }

        if (Utils.isBlank(inp)) {
            suggestVehicleList.addAll(current.getCustomerId().getCustomerVehicles());
        }


        return suggestVehicleList;
    }

    public void identifyVehicle() {

        if (current != null || current.getCustomerId() != null) {
            if (Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
                initNewVehicleToWorkOrder();
            } else {
                if (current.getCustomerId().getCustomerVehicles().size() == 1) {
                    current.setVehicle(current.getCustomerId().getCustomerVehicles().get(0));
                }
            }
        }
    }

    public void changeCustomer() {
        current.setCustomerId(null);
        current.setVehicle(null);
        addNewVechicle = false;
    }

    public void initNewVehicleToWorkOrder() {
        current.setVehicle(new Vehicle());
        addNewVechicle = true;
    }

    public boolean isAddNewVechicle() {
        return addNewVechicle;
    }

    public void setAddNewVechicle(Boolean addNewVechicle) {
        this.addNewVechicle = addNewVechicle;
    }

    private boolean validated() {
        boolean validated = true;
        if (Utils.isEmpty(current.getWorkOrderItems())) {
            validated = false;
            JsfUtil.addErrorMessage("Please add items to the order.");
        }
        if (current.getCustomerId() == null) {
            validated = false;
            JsfUtil.addErrorMessage("Please select a customer.");
        }
        if (current.getCustomerId() != null && (current.getVehicle() == null || current.getVehicle().isEmpty())) {
            validated = false;
            JsfUtil.addErrorMessage("Please select or add a vehicle.");
        }

        return validated;
    }

    public boolean getChangeVehicleIndicator() {
        return current != null && current.getVehicle() != null
                && current.getCustomerId() != null && Utils.notEmpty(current.getCustomerId().getCustomerVehicles());
    }

    public void changeVehicle() {
        addNewVechicle = false;
        current.setVehicle(null);
    }    
    
    public void assignWorkOrder(){
        if(current.getAssignedUser() != null){
            current = getFacade().assignWorkOrder(current);
        }
    }
    
    public void convertToInvoice(){
        try {
            getFacade().convertToInvoice(current);         
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public void addNewItemToWorkOrder(WorkOrderItems ordItm) {
        ordItm.reset();
        
        ordItm.setAddItem(true);
        
        calculateOrderItemPrice(ordItm);

    }
    
    public void undoAddItemToWorkOrder(WorkOrderItems ordItm) {
        ordItm.reset();
                
        calculateOrderItemPrice(ordItm);    
    }

    public Integer getNoOfRowsToAdd() {
        return noOfRowsToAdd;
    }

    public void setNoOfRowsToAdd(Integer noOfRowsToAdd) {
        this.noOfRowsToAdd = noOfRowsToAdd;
    }

    public ReportViewOptions getViewOptions() {
        return viewOptions;
    }

    public void setViewOptions(ReportViewOptions viewOptions) {
        this.viewOptions = viewOptions;
    }      
    
    public void postTransaction() {
        try {
              
            if(validateTransaction()){
//                if(current.getAmount().equals(current.getTransactionAmount())){
//                    current.setPaymentStatus(PaymentStatusEnum.PAID.name());
//                }
//                else if(current.getAmount() > current.getTransactionAmount()){
//                    current.setPaymentStatus(PaymentStatusEnum.PARTIALPAY.name());
//                }
//                else if(current.getAmount() < current.getTransactionAmount()){
//                    current.setPaymentStatus(PaymentStatusEnum.OVERPAY.name());
//                }  
//                
//                Double totAmt;
//                if(current.getStatus().equals(PaymentStatusEnum.OVERPAY.name())){
//                    totAmt = current.getTransactionAmount() - currentTransaction.getAmount();
//                }
//                else{
//                   totAmt = current.getTransactionAmount() + currentTransaction.getAmount();
//                }
//                
//                if (totAmt > current.getAmount()) {
//                    JsfUtil.addErrorMessage("Payment amount exceeds invoice amount.");
//                    return;
//                }
                
                if(Utils.isEmpty(current.getTransactions())){
                    current.setTransactions(new HashSet<Transaction>()); 
                }
                current.getTransactions().add(currentTransaction);
                
                current = getFacade().postTransaction(currentTransaction);
               
                current = getFacade().find(current.getWorkOrderId());              
                
                JsfUtil.addInfoMessage("Payment posted successfully");

            }            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void postTransactionAndAddAgain() {
        postTransaction();
        if(!JsfUtil.isErrorRaised()){
            currentTransaction = new Transaction();
        }
    }

    public void postTransactionAndClose() {
        postTransaction();
        
        if(!JsfUtil.isErrorRaised()){
            redirectToView(current.getWorkOrderId());
        }
    }

    public void initCurrentTransaction() {
        currentTransaction = new Transaction();
        Double currentAmt = current.getAmount() - current.getTransactionAmount();
        currentTransaction.setAmount(Math.abs(currentAmt));
        
        if(currentAmt < 0.0){
            currentTransaction.setTransType(TransTypeEnum.REFUND.getValue());
        }
        else{
            currentTransaction.setTransType(TransTypeEnum.PAYMENT.getValue());
        }
        
        currentTransaction.setTransDate(new Date());
        currentTransaction.setWorkOrderId(current);        
    }
    
    public void initWorkOrderTransaction() {
        initCurrentTransaction();
        if(current.getTransactions() == null){
            current.setTransactions(new HashSet<Transaction>());
            current.getTransactions().add(currentTransaction);
        }
    }

    public List<SelectItem> getTransTypeList() {
        log.info("Inside get transTypeList");
        List<SelectItem> list = new ArrayList();

        for (TransTypeEnum tr : TransTypeEnum.values()) {
            list.add(new SelectItem(tr.getValue(), tr.getValue()));
        }
        return list;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public boolean isSkipToWizardEnd() {
        return skipToWizardEnd;
    }

    public void setSkipToWizardEnd(boolean skipToWizardEnd) {
        this.skipToWizardEnd = skipToWizardEnd;
    }       

    private boolean validateTransaction() {
        boolean validated = true;
        if (currentTransaction != null) {
            if (currentTransaction.getAmount() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment amount.");
            }
            else if (currentTransaction.getAmount() <= 0.0) {
                validated = false;
                JsfUtil.addErrorMessage("Payment amount cannot be less than 0");              
            }
            if (currentTransaction.getTransDate() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment date.");
            }
            if (currentTransaction.getTransType() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment type.");
            }
            if (currentTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.REFUND.getValue())) {
                //refund cannot be greater than amount already paid
                if(currentTransaction.getAmount() > current.getTransactionAmount()){
                    validated = false;
                    JsfUtil.addErrorMessage("Refund amount cannot exceed total paid amount.");
                }
            }
            else if (currentTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.PAYMENT.getValue())) {
                //total payment cannot be greater than invoiced amount 
                if(currentTransaction.getAmount() + current.getTransactionAmount() > current.getAmount()){
                    validated = false;
                    JsfUtil.addErrorMessage("Payment amount exceeds invoiced amount.");
                }
            }
        } else {
            validated = false;
        }
        return validated;
    }   
    
    private boolean validateAndProcessTransForCreateFlow() {
        boolean validated = true;
        if (currentTransaction != null) {
            if (currentTransaction.getAmount() < 0.0) {
                validated = false;
                JsfUtil.addErrorMessage("Payment amount cannot be less than 0");              
            }
            
            if (currentTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.PAYMENT.getValue())) {
                //total payment cannot be greater than invoiced amount 
                if(currentTransaction.getAmount() > current.getAmount()){
                    validated = false;
                    JsfUtil.addErrorMessage("Payment amount exceeds invoiced amount.");
                }
            }
        }
        
        if(current.getTransactionAmount().equals(current.getAmount())){
            current.setPaymentStatus(PaymentStatusEnum.PAID.name());
        }
        else if(current.getTransactionAmount() < current.getAmount()){
            current.setPaymentStatus(PaymentStatusEnum.PARTIALPAY.name());
        }
        else if(current.getTransactionAmount() > current.getAmount()){
            current.setPaymentStatus(PaymentStatusEnum.OVERPAY.name());
        }           
        return validated;
    }   
    
    public String filterUserInvoices(){
//        List<Invoice> list = new ArrayList<>();
//        
//        if(!userInvoiceVO.empty()){        
//            Boolean addItem = false;
//            for(Invoice inv : getInvoiceList()){
//                addItem = false;
//                if(userInvoiceVO.getUser() != null){
//                    if(inv.getWorkOrder() != null && inv.getWorkOrder().getAssignedUser() != null && 
//                            inv.getWorkOrder().getAssignedUser().getUserId().equals(userInvoiceVO.getUser().getUserId())){
//                        log.info("add item true for " + userInvoiceVO.getUser() );
//                        addItem = true;                    
//                    }
//                }                
//                if((userInvoiceVO.getUser() == null || addItem ) && userInvoiceVO.getMonth()!= null){
//                    log.info("month is not null");
//                    //addItem = false;
//                    if(inv.getInvoiceDate()!= null && 
//                            (userInvoiceVO.getMonth().equalsIgnoreCase(Utils.getMonthNameFromDate(inv.getInvoiceDate())))){
//                        addItem = true;                                            
//                    }
//                    else{
//                         addItem = false;   
//                    }
//                }       
//                
//                if(addItem){
//                    log.info("adding item...");
//                    list.add(inv); 
//                }
//            }
//        
//            userInvoiceVO.setInvoiceList(list);
//        }          
//        
        return "";
    }
    
    public void groupUserInvByMonthlyLaborTots(){  
        
//        Multimap<User, UserInvMonLbrTot> userToInvMonTot = ArrayListMultimap.create();
//                
//        for(Invoice inv : getInvoiceList()){           
//            User user = inv.getWorkOrder().getAssignedUser();
//            String month = Utils.getMonthNameFromDate(inv.getInvoiceDate());
//
////            userToInvMonTot.put(user,new UserInvMonLbrTot(user, month, inv));
//        }               
    }
    
    public String onFlowProcess(FlowEvent event) {
//        if(skipToWizardEnd) {
//            skipToWizardEnd = false;   //reset in case user goes back
//            return "confirm";
//        }
//        else {
            boolean nextStep = true;
            log.info("Inside inFlowProcess: "+event.getNewStep());
            if("woCustTab".equalsIgnoreCase(event.getOldStep())){
                if(current.getCustomerId() == null){
                    nextStep = false;
                    JsfUtil.addErrorMessage("Please select or add a customer.");  
                }
                else
                if(current.getVehicle() == null){
                    nextStep = false;
                    JsfUtil.addErrorMessage("Please select or add a vehicle.");  
                }                
            }
            else
            if("woPmtTab".equalsIgnoreCase(event.getNewStep())){
                if(Utils.isEmpty(current.getTransactions())){
                    initWorkOrderTransaction();
                }
            }
            else
            if("woAsgnTab".equalsIgnoreCase(event.getNewStep())){
                nextStep = validateAndProcessTransForCreateFlow();                
            }
            return nextStep ? event.getNewStep(): event.getOldStep();
//        }
    }
    
    public void addNotes(){
        if(!currentNotes.isEmpty()){
            if(current.getNotes() == null){
                current.setNotes(new HashSet<WorkOrderNotes>());
            }

            currentNotes.setCreatedOn(new Date());        
            current.getNotes().add(currentNotes);     
            current.setUpdateTs(new Date());

            woFacade.edit(current);                
        }
        else{
            JsfUtil.addErrorMessage("No notes added to save!");
        }
    }
    
    public void initWorkOrderNotes(){
        log.info("Inside initWorkOrderNotes");
        currentNotes = new WorkOrderNotes();
    }

    public WorkOrderNotes getCurrentNotes() {
        return currentNotes;
    }

    public void setCurrentNotes(WorkOrderNotes currentNotes) {
        this.currentNotes = currentNotes;
    }       
}
