package at.htl.mealcounter.entity;

import java.time.LocalDateTime;

public class NfcIdInfo {

    private String nfcId;
    private LocalDateTime registerDateTime;

    public NfcIdInfo(String nfcId, LocalDateTime registerDateTime) {
        this.nfcId = nfcId;
        this.registerDateTime = registerDateTime;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }
}
