package com.gamebai.tienlenmiennam.api;

public class PhanHoiGiaoDichZaloPay {
    private String order_url;
    private String app_trans_id;
    private int return_code;
    private String return_message;
    private String zp_trans_token;

    // Getters và Setters

    public PhanHoiGiaoDichZaloPay(String return_message, String order_url, String app_trans_id, int return_code, String zp_trans_token) {
        this.return_message = return_message;
        this.order_url = order_url;
        this.app_trans_id = app_trans_id;
        this.return_code = return_code;
        this.zp_trans_token = zp_trans_token;
    }

    public void setOrder_url(String order_url) {
        this.order_url = order_url;
    }

    public void setApp_trans_id(String app_trans_id) {
        this.app_trans_id = app_trans_id;
    }

    public void setReturn_code(int return_code) {
        this.return_code = return_code;
    }

    public void setReturn_message(String return_message) {
        this.return_message = return_message;
    }

    public void setZp_trans_token(String zp_trans_token) {
        this.zp_trans_token = zp_trans_token;
    }

    public String getOrderUrl() {
        return order_url;
    }

    public String getAppTransId() {
        return app_trans_id;
    }

    public int getReturnCode() {
        return return_code;
    }

    public String getReturnMessage() {
        return return_message;
    }
    public String getZpTransToken() {
        return zp_trans_token;
    }
}
