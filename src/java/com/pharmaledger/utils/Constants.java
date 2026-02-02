package com.pharmaledger.utils;

/**
 * Application constants
 */
public class Constants {
    
    // ==================== ROLE CODES ====================
    public static final String ROLE_ADMIN = "AD";
    public static final String ROLE_PHARMACIST = "PH";
    public static final String ROLE_AUDITOR = "AU";
    
    // ==================== ACTION NAMES ====================
    public static final String ACTION_LOGIN = "Login";
    public static final String ACTION_LOGOUT = "Logout";
    public static final String ACTION_LIST = "List";
    public static final String ACTION_CREATE = "Create";
    public static final String ACTION_UPDATE = "Update";
    public static final String ACTION_DELETE = "Delete";
    public static final String ACTION_SEARCH = "Search";
    public static final String ACTION_DETAIL = "Detail";
    public static final String ACTION_APPROVE = "Approve";
    public static final String ACTION_ASSIGN = "Assign";
    public static final String ACTION_CLOSE = "Close";
    
    // ==================== CONTROLLER MAPPINGS ====================
    public static final String CONTROLLER_LOGIN = "LoginController";
    public static final String CONTROLLER_DRUG = "DrugController";
    public static final String CONTROLLER_BATCH = "BatchController";
    public static final String CONTROLLER_SUPPLIER = "SupplierController";
    public static final String CONTROLLER_LOCATION = "LocationController";
    public static final String CONTROLLER_INVENTORY = "InventoryController";
    public static final String CONTROLLER_INBOUND = "InboundController";
    public static final String CONTROLLER_OUTBOUND = "OutboundController";
    public static final String CONTROLLER_LEDGER = "LedgerController";
    public static final String CONTROLLER_ALERT = "AlertController";
    public static final String CONTROLLER_CASE = "CaseController";
    public static final String CONTROLLER_FORECAST = "ForecastController";
    public static final String CONTROLLER_REPORT = "ReportController";
    
    // ==================== PAGE PATHS ====================
    // Auth pages
    public static final String PAGE_LOGIN = "login.jsp";
    public static final String PAGE_USER_LIST = "userList.jsp";
    public static final String PAGE_ROLE_LIST = "roleList.jsp";
    public static final String PAGE_AUDIT_LOG = "auditLog.jsp";
    
    // Drug pages
    public static final String PAGE_DRUG_LIST = "drugList.jsp";
    public static final String PAGE_DRUG_FORM = "drugForm.jsp";
    public static final String PAGE_DRUG_DETAIL = "drugDetail.jsp";
    
    // Supplier pages
    public static final String PAGE_SUPPLIER_LIST = "supplierList.jsp";
    public static final String PAGE_SUPPLIER_FORM = "supplierForm.jsp";
    
    // Batch pages
    public static final String PAGE_BATCH_LIST = "batchList.jsp";
    public static final String PAGE_BATCH_FORM = "batchForm.jsp";
    
    // Location pages
    public static final String PAGE_LOCATION_LIST = "locationList.jsp";
    public static final String PAGE_LOCATION_FORM = "locationForm.jsp";
    
    // Inventory pages
    public static final String PAGE_INVENTORY_DASHBOARD = "inventoryDashboard.jsp";
    public static final String PAGE_STOCK_SEARCH = "stockSearch.jsp";
    public static final String PAGE_NEAR_EXPIRY_SEARCH = "nearExpirySearch.jsp";
    public static final String PAGE_LOW_STOCK_SEARCH = "lowStockSearch.jsp";
    
    // Inbound/Outbound pages
    public static final String PAGE_INBOUND_LIST = "inboundList.jsp";
    public static final String PAGE_INBOUND_FORM = "inboundForm.jsp";
    public static final String PAGE_OUTBOUND_LIST = "outboundList.jsp";
    public static final String PAGE_OUTBOUND_FORM = "outboundForm.jsp";
    
    // Ledger pages
    public static final String PAGE_LEDGER_LIST = "ledgerList.jsp";
    public static final String PAGE_LEDGER_DETAIL = "ledgerDetail.jsp";
    
    // Alert pages
    public static final String PAGE_RULE_LIST = "ruleList.jsp";
    public static final String PAGE_RULE_FORM = "ruleForm.jsp";
    public static final String PAGE_ALERTS_LIST = "alertsList.jsp";
    public static final String PAGE_ALERT_DETAIL = "alertDetail.jsp";
    public static final String PAGE_ALERT_EXPLAIN = "alertExplain.jsp";
    
    // Case pages
    public static final String PAGE_CASE_LIST = "caseList.jsp";
    public static final String PAGE_CASE_DETAIL = "caseDetail.jsp";
    public static final String PAGE_CASE_ASSIGN = "caseAssign.jsp";
    public static final String PAGE_CASE_CLOSE = "caseClose.jsp";
    
    // Forecast pages
    public static final String PAGE_FORECAST_DASHBOARD = "forecastDashboard.jsp";
    public static final String PAGE_FORECAST_DETAIL = "forecastDetail.jsp";
    public static final String PAGE_REORDER_SUGGEST = "reorderSuggest.jsp";
    public static final String PAGE_REORDER_EXPLAIN = "reorderExplain.jsp";
    
    // Report pages
    public static final String PAGE_REPORT_STOCKOUT = "reportStockout.jsp";
    public static final String PAGE_REPORT_EXPIRY_WASTE = "reportExpiryWaste.jsp";
    public static final String PAGE_REPORT_FORECAST_QUALITY = "reportForecastQuality.jsp";
    public static final String PAGE_REPORT_ALERT_QUALITY = "reportAlertQuality.jsp";
    public static final String PAGE_REPORT_OPS_LATENCY = "reportOpsLatency.jsp";
    
    // ==================== ORDER STATUS ====================
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // ==================== ALERT STATUS ====================
    public static final String ALERT_OPEN = "OPEN";
    public static final String ALERT_ACKNOWLEDGED = "ACKNOWLEDGED";
    public static final String ALERT_RESOLVED = "RESOLVED";
    public static final String ALERT_CLOSED = "CLOSED";
    
    // ==================== TRANSACTION TYPES ====================
    public static final String TXN_INBOUND = "INBOUND";
    public static final String TXN_OUTBOUND = "OUTBOUND";
    public static final String TXN_ADJUSTMENT = "ADJUSTMENT";
    public static final String TXN_TRANSFER = "TRANSFER";
    public static final String TXN_EXPIRED = "EXPIRED";
    
    // ==================== SESSION ATTRIBUTES ====================
    public static final String SESSION_USER = "LOGIN_USER";
    public static final String SESSION_ROLE = "USER_ROLE";
    
    // ==================== REQUEST ATTRIBUTES ====================
    public static final String ATTR_ERROR = "ERROR_MESSAGE";
    public static final String ATTR_SUCCESS = "SUCCESS_MESSAGE";
    public static final String ATTR_LIST = "LIST_DATA";
    public static final String ATTR_ITEM = "ITEM_DATA";
}
