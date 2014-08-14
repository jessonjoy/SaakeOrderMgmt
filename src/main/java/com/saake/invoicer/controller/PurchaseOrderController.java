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
import com.saake.invoicer.entity.PurchaseOrder;
import com.saake.invoicer.entity.PurchaseOrderItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.PoTransaction;
import com.saake.invoicer.entity.PurchaseOrderNotes;
import com.saake.invoicer.model.PurchaseOrderDataModel;
import com.saake.invoicer.model.UserInvoiceVO;
import com.saake.invoicer.model.PurchaseOrderDataModel;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.sessionbean.PurchaseOrderFacade;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.PurchaseOrderStatusEnum;
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
@ManagedBean(name = "poCtrl")
@ViewScoped
public class PurchaseOrderController implements Serializable {
    
    //@Inject
    private Log log = LogFactory.getLog(this.getClass().getName());
    
    private PurchaseOrderDataModel model;
    private PurchaseOrder current;
    private PurchaseOrderItems currentInvItem;
    private ReportViewOptions viewOptions;
    private UserInvoiceVO userInvoiceVO ;
    
    private PoTransaction currentPoTransaction = new PoTransaction();
    private PurchaseOrderNotes currentNotes = new PurchaseOrderNotes();

    private List<PurchaseOrder> originalPurchaseOrderList = null;
    private List<PurchaseOrder> purchaseOrderList = null;
    
    @Inject
    CustomerController custCtrl;
    
    @Inject
    ItemController itemCtrl;
    
    @Inject
    private com.saake.invoicer.sessionbean.PurchaseOrderFacade woFacade;
    
    @Inject
    private com.saake.invoicer.sessionbean.ItemFacade itemFacade;
    
    private boolean redirect = false;
    private Boolean addNewVechicle = false;
    public Integer noOfRowsToAdd = 1;
    private boolean skipToWizardEnd;

    public PurchaseOrderController() {
        this.viewOptions = new ReportViewOptions();
        
        log.info("Inside PurchaseOrderController!!!");
        Object inv = JsfUtil.getRequestObject("item");

        if (inv != null && inv instanceof PurchaseOrder) {
            current = (PurchaseOrder) inv;
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
            initNewPurchaseOrder();
        } else if (JsfUtil.getViewId().contains("edit")) {
            editPurchaseOrderInit();
        } else if (JsfUtil.getViewId().contains("view")) {
            viewPurchaseOrderInit();
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
    
    public PurchaseOrder getSelected() {
        if (current == null) {
            initNewPurchaseOrder();
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
        purchaseOrderList = null;
        model = null;
        woFacade.clearCache();
    }

    public void initNewPurchaseOrder() {
        current = new PurchaseOrder();
        current.setPurchaseOrderItems(new ArrayList<PurchaseOrderItems>());
        addNewItemToPurchaseOrder();
        current.setPurchaseOrderDate(new Date());
        current.setAmount(0.0);
        current.setStatus(PurchaseOrderStatusEnum.DRAFT.name());
    }

    public String prepareCreate() {
        initNewPurchaseOrder();
        return deriveReturnString("create", false);
    }

    public String save() {
        if (current != null) {
            if (validated()) {
                if (Utils.notEmpty(current.getPurchaseOrderItems())) {
                    List<PurchaseOrderItems> emptyList = new ArrayList<>();
                    for (PurchaseOrderItems items : current.getPurchaseOrderItems()) {
                        if (items.isEmptyForUse()) {
                            emptyList.add(items);
                        }
                    }
                    current.getPurchaseOrderItems().removeAll(emptyList);
                }
                
                if(currentNotes != null && Utils.notBlank(currentNotes.getNotes())){
                    currentNotes.setPurchaseOrderId(current);
                    current.addNote(currentNotes);
                }

                if (current.getPurchaseOrderId() != null) {
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
            
            getFacade().createPurchaseOrder(current);
            JsfUtil.addSuccessMessage("Purchase Order Created!");
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
            if (Utils.notEmpty(current.getPurchaseOrderItems())) {
                for (PurchaseOrderItems woItm : current.getPurchaseOrderItems()) {
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
                       
            current = getFacade().updatePurchaseOrder(current);

            JsfUtil.addRequestObject("item", current);
            JsfUtil.addSuccessMessage("Purchase Order Updated!");

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

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public String softDelete() {
        try {
            getFacade().softDelete(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public void addNewRowsToPurchaseOrder(Integer noOfRowsToAdd ) {
        for(int i = 0; i< noOfRowsToAdd; i++){
            addNewItemToPurchaseOrder();
        }
    }
    
    public String addNewItemToPurchaseOrder() {
        PurchaseOrderItems it = new PurchaseOrderItems();
        it.setAmount(0.00);
        it.setDiscount(0.00);
        it.setUnitPrice(0.00);
        it.setItem(new Item());
        it.getItem().setUnitPrice(0.00);
        it.setPurchaseOrderId(current); 
        it.setQuantity(1);
        current.getPurchaseOrderItems().add(it);
        return null;
    }

    public String removeOrderItem(PurchaseOrderItems ordItm) {
        if (ordItm != null) {
            if (current.getPurchaseOrderItems().size() > 1) {
                current.getPurchaseOrderItems().remove(ordItm);
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

        current.setAmount(current.getAmount());
        return null;
    }

    public void calculateOrderItemPrice(PurchaseOrderItems ordItm) {
        if (ordItm != null && ordItm.getQuantity() != null) {
            ordItm.setAmount((ordItm.getUnitPrice() * ordItm.getQuantity()) - (ordItm.getDiscount() != null ? ordItm.getDiscount() * ordItm.getQuantity() : 0));
        }

        calOrderPrice();
    }
    
    public void populateItemOnPurchaseOrder(PurchaseOrderItems ordItm) {
        if(ordItm != null){
            if(ordItm.getItem() != null){
                ordItm.setUnitPrice(ordItm.getItem().getUnitPrice());
                ordItm.setDescription(ordItm.getItem().getDescription());
            }
        }
        
        calculateOrderItemPrice(ordItm);
    }    

    private PurchaseOrderFacade getFacade() {
        return woFacade;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        if (purchaseOrderList == null) {
            purchaseOrderList = woFacade.findAll();
            originalPurchaseOrderList = new ArrayList(purchaseOrderList);
        }
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public PurchaseOrderFacade getWoFacade() {
        return woFacade;
    }

    public void setWoFacade(PurchaseOrderFacade woFacade) {
        this.woFacade = woFacade;
    }

    public PurchaseOrder getCurrent() {
        if (current == null) {
            initNewPurchaseOrder();
        }
        return current;
    }

    public void setCurrent(PurchaseOrder current) {
        this.current = current;
    }

    private void editPurchaseOrderInit() {
        viewPurchaseOrderInit();
    }

    private void viewPurchaseOrderInit() {
        String id = JsfUtil.getRequestParameter("id");

        if (Utils.notBlank(id)) {
            current = getFacade().getPurchaseOrder(Integer.parseInt(id));
        }
        else{
            Object woCopy = JsfUtil.getAttributeFromSession("woCopy");
            if(woCopy != null && woCopy instanceof PurchaseOrder){
                current = (PurchaseOrder) woCopy;
                JsfUtil.removeAttributeFromSession("woCopy");
            }
        }             
    }

    public void filterList() {
//        if (!filterCriteria.empty()) {
//            purchaseOrderList.clear();
//
//            for (PurchaseOrder inv : originalPurchaseOrderList) {
//                if (Utils.notBlank(filterCriteria.getPurchaseOrderPeriod())) {
//                    if ("today".equalsIgnoreCase(filterCriteria.getPurchaseOrderPeriod())) {
//                        if (inv.getPurchaseOrderDate() != null && (inv.getPurchaseOrderDate().equals(new Date()))) {
//                            purchaseOrderList.add(inv);
//                        }
//                    } else if ("yest".equalsIgnoreCase(filterCriteria.getPurchaseOrderPeriod())) {
////                            if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
////                                purchaseOrderList.add(inv);
////                            }
//                    }
//                } else if (Utils.notBlank(filterCriteria.getStatus())) {
//                    if (Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getCustomer() != null && filterCriteria.getCustomer().getCustomerId() != null) {
//                    if (inv.getCustomerId() != null && inv.getCustomerId().getCustomerId() != null
//                            && inv.getCustomerId().getCustomerId().equals(filterCriteria.getCustomer().getCustomerId())) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null && filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()
//                            && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null && filterCriteria.getToDate() != null) {
//                    if (inv.getPurchaseOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getPurchaseOrderDate()) || filterCriteria.getFromDate().equals(inv.getPurchaseOrderDate()))
//                            && (filterCriteria.getToDate().before(inv.getPurchaseOrderDate()) || filterCriteria.getToDate().equals(inv.getPurchaseOrderDate()))) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null) {
//                    if (inv.getPurchaseOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getPurchaseOrderDate()) || filterCriteria.getFromDate().equals(inv.getPurchaseOrderDate()))) {
//                        purchaseOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToDate() != null) {
//                    if (inv.getPurchaseOrderDate() != null
//                            && (filterCriteria.getToDate().before(inv.getPurchaseOrderDate()) || filterCriteria.getToDate().equals(inv.getPurchaseOrderDate()))) {
//                        purchaseOrderList.add(inv);
//                    }
//                }
//            }
//        }
    }

    public void resetSearch() {
        purchaseOrderList = new ArrayList(originalPurchaseOrderList);
//        filterCriteria = new SearchPurchaseOrderVO();
    }
//
//    public SearchPurchaseOrderVO getFilterCriteria() {
//        return filterCriteria;
//        return filterCriteria;
//    }

//    public void setFilterCriteria(SearchPurchaseOrderVO filterCriteria) {
//        this.filterCriteria = filterCriteria;
//    }

    public PurchaseOrderDataModel getModel() {
        if (model == null) {
            model = new PurchaseOrderDataModel(getPurchaseOrderList());
        }
        return model;
    }

    public void setModel(PurchaseOrderDataModel model) {
        this.model = model;
    }

    public void addNewCustomerToPurchaseOrder() {
        log.info("inside addNewCustomerToPurchaseOrder");

        Customer cust = custCtrl.create(custCtrl.getCurrent());
        current.setCustomerId(cust);

//        if (Utils.notEmpty(cust.getCustomerVehicles())) {
//            current.setVehicle(cust.getCustomerVehicles().get(cust.getCustomerVehicles().size() - 1));
//        } else {
//            current.setVehicle(new Vehicle());
//        }
    }

    public void createNewItemAndAddToPurchaseOrder() {
        log.info("inside createNewItemAndAddToPurchaseOrder");

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

    public void generatePurchaseOrderPdfAction() {
        JsfUtil.addAttributeInSession("invoice", current);
    }

    public void downloadPdf() {
//        try {
//            ReportHelper.downloadPurchaseOrderPDF(current);
//        } catch (IOException ex) {
//            Logger.getLogger(PurchaseOrderController.class.getName()).log(Level.SEVERE, "Error downloading pdf", ex);
//        }
    }

    public void viewPdf() {
//        try {
//            ReportHelper.viewPurchaseOrderPDF(current, viewOptions);
//        } catch (IOException ex) {
//            Logger.getLogger(PurchaseOrderController.class.getName()).log(Level.SEVERE, "Error viewing pdf", ex);
//        }
    }

    public void printPurchaseOrder() {
//        try {
//            ReportHelper.printPurchaseOrder(current);
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage("Error printing invoice");
//            log.error("Error printing invoice", e);
//        }
    }

    public String copyPurchaseOrder() {
        try {
            if(current != null){
                current = PurchaseOrder.copy(current, new PurchaseOrder());
                current.setPurchaseOrderDate(new Date());
                current.setCreateTs(new Date());
                current.setUpdateTs(null);
                current.setUpdatedBy(null);
                current.setPurchaseOrderId(null);
                current.setStatus(PurchaseOrderStatusEnum.DRAFT.name());
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
            if (addId && current != null && current.getPurchaseOrderId() != null) {
                id = "&id=" + current.getPurchaseOrderId();
            }

            return viewString + "?faces-redirect=true" + id;
        } else {
            return viewString;
        }
    }

    public PurchaseOrderItems getCurrentInvItem() {
        return currentInvItem;
    }

    public void setCurrentInvItem(PurchaseOrderItems currentInvItem) {
        this.currentInvItem = currentInvItem;
    }

    public void redirectToView(Integer id) {
        try {
            if (id == null || id == 0) {
                id = current.getPurchaseOrderId();
            }
            JsfUtil.getExternalContext().redirect(JsfUtil.getExternalContext().getRequestContextPath()+"/po/view.jsf?id=" + id) ;
        } catch (IOException ex) {
            Logger.getLogger(PurchaseOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public List<Vehicle> suggestVehicle(String inp) {
//
//        suggestVehicleList.clear();
//
//        if (current == null || current.getCustomerId() == null || Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
//            JsfUtil.addInfoMessage("No Vehicles found");
//            return null;
//        }
//
//        if (Utils.isBlank(inp)) {
//            suggestVehicleList.addAll(current.getCustomerId().getCustomerVehicles());
//        }
//
//
//        return suggestVehicleList;
//    }

//    public void identifyVehicle() {
//
//        if (current != null || current.getCustomerId() != null) {
//            if (Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
//                initNewVehicleToPurchaseOrder();
//            } else {
//                if (current.getCustomerId().getCustomerVehicles().size() == 1) {
//                    current.setVehicle(current.getCustomerId().getCustomerVehicles().get(0));
//                }
//            }
//        }
//    }

    public void changeCustomer() {
        current.setCustomerId(null);
//        current.setVehicle(null);
        addNewVechicle = false;
    }

//    public void initNewVehicleToPurchaseOrder() {
////        current.setVehicle(new Vehicle());
//        addNewVechicle = true;
//    }

//    public boolean isAddNewVechicle() {
//        return addNewVechicle;
//    }
//
//    public void setAddNewVechicle(Boolean addNewVechicle) {
//        this.addNewVechicle = addNewVechicle;
//    }

    private boolean validated() {
        boolean validated = true;
        if (Utils.isEmpty(current.getPurchaseOrderItems())) {
            validated = false;
            JsfUtil.addErrorMessage("Please add items to the order.");
        }
        if (current.getCustomerId() == null) {
            validated = false;
            JsfUtil.addErrorMessage("Please select a customer.");
        }
//        if (current.getCustomerId() != null && (current.getVehicle() == null || current.getVehicle().isEmpty())) {
//            validated = false;
//            JsfUtil.addErrorMessage("Please select or add a vehicle.");
//        }

        return validated;
    }

//    public boolean getChangeVehicleIndicator() {
//        return current != null && current.getVehicle() != null
//                && current.getCustomerId() != null && Utils.notEmpty(current.getCustomerId().getCustomerVehicles());
//    }
//
//    public void changeVehicle() {
//        addNewVechicle = false;
//        current.setVehicle(null);
//    }    
    
//    public void assignPurchaseOrder(){
//        if(current.getAssignedUser() != null){
//            current = getFacade().assignPurchaseOrder(current);
//        }
//    }
//    
//    public void convertToInvoice(){
//        try {
//            getFacade().convertToInvoice(current);         
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//        }
//    }
    
    public void addNewItemToPurchaseOrder(PurchaseOrderItems ordItm) {
        ordItm.reset();
        
        ordItm.setAddItem(true);
        
        calculateOrderItemPrice(ordItm);

    }
    
    public void undoAddItemToPurchaseOrder(PurchaseOrderItems ordItm) {
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
//                    totAmt = current.getTransactionAmount() - currentPoTransaction.getAmount();
//                }
//                else{
//                   totAmt = current.getTransactionAmount() + currentPoTransaction.getAmount();
//                }
//                
//                if (totAmt > current.getAmount()) {
//                    JsfUtil.addErrorMessage("Payment amount exceeds invoice amount.");
//                    return;
//                }
                
                if(Utils.isEmpty(current.getTransactions())){
                    current.setTransactions(new HashSet<PoTransaction>()); 
                }
                current.getTransactions().add(currentPoTransaction);
                
                current = getFacade().postTransaction(currentPoTransaction);
               
                current = getFacade().find(current.getPurchaseOrderId());              
                
                JsfUtil.addInfoMessage("Payment posted successfully");

            }            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void postTransactionAndAddAgain() {
        postTransaction();
        if(!JsfUtil.isErrorRaised()){
            currentPoTransaction = new PoTransaction();
        }
    }

    public void postTransactionAndClose() {
        postTransaction();
        
        if(!JsfUtil.isErrorRaised()){
            redirectToView(current.getPurchaseOrderId());
        }
    }

    public void initCurrentTransaction() {
        currentPoTransaction = new PoTransaction();
        Double currentAmt = current.getAmount() - current.getTransactionAmount();
        currentPoTransaction.setAmount(Math.abs(currentAmt));
        
        if(currentAmt < 0.0){
            currentPoTransaction.setTransType(TransTypeEnum.REFUND.getValue());
        }
        else{
            currentPoTransaction.setTransType(TransTypeEnum.PAYMENT.getValue());
        }
        
        currentPoTransaction.setTransDate(new Date());
        currentPoTransaction.setPurchaseOrderId(current);        
    }
    
    public void initPurchaseOrderTransaction() {
        initCurrentTransaction();
        if(current.getTransactions() == null){
            current.setTransactions(new HashSet<PoTransaction>());
            current.getTransactions().add(currentPoTransaction);
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

    public PoTransaction getCurrentPoTransaction() {
        return currentPoTransaction;
    }

    public void setCurrentPoTransaction(PoTransaction currentPoTransaction) {
        this.currentPoTransaction = currentPoTransaction;
    }

    public boolean isSkipToWizardEnd() {
        return skipToWizardEnd;
    }

    public void setSkipToWizardEnd(boolean skipToWizardEnd) {
        this.skipToWizardEnd = skipToWizardEnd;
    }       

    private boolean validateTransaction() {
        boolean validated = true;
        if (currentPoTransaction != null) {
            if (currentPoTransaction.getAmount() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment amount.");
            }
            else if (currentPoTransaction.getAmount() <= 0.0) {
                validated = false;
                JsfUtil.addErrorMessage("Payment amount cannot be less than 0");              
            }
            if (currentPoTransaction.getTransDate() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment date.");
            }
            if (currentPoTransaction.getTransType() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment type.");
            }
            if (currentPoTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.REFUND.getValue())) {
                //refund cannot be greater than amount already paid
                if(currentPoTransaction.getAmount() > current.getTransactionAmount()){
                    validated = false;
                    JsfUtil.addErrorMessage("Refund amount cannot exceed total paid amount.");
                }
            }
            else if (currentPoTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.PAYMENT.getValue())) {
                //total payment cannot be greater than invoiced amount 
                if(currentPoTransaction.getAmount() + current.getTransactionAmount() > current.getAmount()){
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
        if (currentPoTransaction != null) {
            if (currentPoTransaction.getAmount() < 0.0) {
                validated = false;
                JsfUtil.addErrorMessage("Payment amount cannot be less than 0");              
            }
            
            if (currentPoTransaction.getTransType().equalsIgnoreCase(TransTypeEnum.PAYMENT.getValue())) {
                //total payment cannot be greater than invoiced amount 
                if(currentPoTransaction.getAmount() > current.getAmount()){
                    //validated = false;
                    JsfUtil.addInfoMessage("Payment amount exceeds invoiced amount.");
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
//                    if(inv.getPurchaseOrder() != null && inv.getPurchaseOrder().getAssignedUser() != null && 
//                            inv.getPurchaseOrder().getAssignedUser().getUserId().equals(userInvoiceVO.getUser().getUserId())){
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
//            User user = inv.getPurchaseOrder().getAssignedUser();
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
//                else
//                if(current.getVehicle() == null){
//                    nextStep = false;
//                    JsfUtil.addErrorMessage("Please select or add a vehicle.");  
//                }                
            }
            else
            if("woPmtTab".equalsIgnoreCase(event.getNewStep())){
                if(Utils.isEmpty(current.getTransactions())){
                    initPurchaseOrderTransaction();
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
                current.setNotes(new HashSet<PurchaseOrderNotes>());
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
    
    public void initPurchaseOrderNotes(){
        log.info("Inside initPurchaseOrderNotes");
        currentNotes = new PurchaseOrderNotes();
    }

    public PurchaseOrderNotes getCurrentNotes() {
        return currentNotes;
    }

    public void setCurrentNotes(PurchaseOrderNotes currentNotes) {
        this.currentNotes = currentNotes;
    }       
}
