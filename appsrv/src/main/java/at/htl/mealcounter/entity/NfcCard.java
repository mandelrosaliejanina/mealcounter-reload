package at.htl.mealcounter.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "M_NFC_CARD")
public class NfcCard extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long dbId;

    @Column(name = "NFC_ID")
    public String nfcId;

    @Column(name = "REGISTER_DATE_TIME")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDateTime registerDateTime;

    public NfcCard(String nfcId) {
        this.nfcId = nfcId;
        this.registerDateTime = LocalDateTime.now();
    }

    public NfcCard() {

    }


    public NfcCard(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public NfcCard(String nfcId, LocalDateTime registerDateTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NfcCard nfcCard = (NfcCard) o;
        return Objects.equals(nfcId, nfcCard.nfcId) && Objects.equals(registerDateTime, nfcCard.registerDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nfcId, registerDateTime);
    }
}
