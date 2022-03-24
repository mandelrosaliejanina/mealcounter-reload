package at.htl.mealcounter.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "M_CONSUMATION")
public class Consumation extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "DATE")
    @JsonbDateFormat(value = "dd/MM/yyyy")
    private LocalDateTime date;

    @Column(name = "HASCONSUMED")
    private boolean hasConsumed;

    public Consumation() {
    }

    public Consumation(Person person, LocalDateTime date, boolean hasConsumed) {
        this.person = person;
        this.date = date;
        this.hasConsumed = hasConsumed;
    }

    //region getter and setter


    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isHasConsumed() {
        return hasConsumed;
    }

    public void setHasConsumed(boolean hasConsumed) {

        if(this.hasConsumed = false){
            this.hasConsumed = hasConsumed;
        }else{
            throw new IllegalArgumentException("Schüler hat bereits sein essen bekommen");
        }
    }
    //endregion


    @Override
    public String toString() {
        return "Consumation{" +
                "person=" + person +
                ", date=" + date +
                ", hasConsumed=" + hasConsumed +
                '}';
    }
}
