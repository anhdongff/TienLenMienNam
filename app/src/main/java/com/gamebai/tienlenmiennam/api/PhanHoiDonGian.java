package com.gamebai.tienlenmiennam.api;

public class PhanHoiDonGian {
    private String loi;
    private String ok;
    private long created;
    private long updated;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setLoi(String loi) {
        this.loi = loi;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getLoi() {
        return loi;
    }

    public String getOk() {
        return ok;
    }
}
