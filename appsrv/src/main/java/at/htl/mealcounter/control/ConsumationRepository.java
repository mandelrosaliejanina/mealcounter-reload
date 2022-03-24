package at.htl.mealcounter.control;

import at.htl.mealcounter.entity.Consumation;
import at.htl.mealcounter.entity.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@ApplicationScoped
@Transactional
public class ConsumationRepository implements PanacheRepository<Consumation> {

    @Inject
    EntityManager em;

    @Inject
    PersonRepository personRepository;


    public boolean canConsumeToday(Person person) {

        Consumation consumation;
        LocalDate ld = LocalDate.now();

        try {
            consumation = em.createQuery("select c from Consumation c where " +
                            "c.person.id = :ID and " +
                            "c.date >= :DATE", Consumation.class)
                    .setParameter("DATE", LocalDateTime.of(ld, LocalTime.MIDNIGHT))
                    .setParameter("ID", person.getId())
                    .getResultStream().findFirst().orElse(null);

        } catch (NoResultException e) {
            consumation = null;
        }
        System.out.println(consumation);
        System.out.println(consumation == null);
        return consumation == null;
    }

    public List<Consumation> findByNfcId(String nfcId) {
        var query = em.createQuery("select c from Consumation c join  c.person p join p.nfcCard nfc where nfc.nfcId = :nfcId",
                Consumation.class ).setParameter("nfcId", nfcId);
        return query.getResultList();
    }

    public void merge(Consumation consumation) {
        em.merge(consumation);
    }

}
