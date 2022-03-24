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
import java.util.List;


@ApplicationScoped
@Transactional
public class ConsumationRepository implements PanacheRepository<Consumation> {

    @Inject
    EntityManager em;

    @Inject
    PersonRepository personRepository;


    public Consumation findByDateAndPerson(LocalDate myDate, Person person) {

        Consumation consumation;

        try {
            consumation = em.createQuery("select c from Consumation c where " +
                            "c.date = :DATE AND " +
                            "c.person.id = :ID", Consumation.class)
                    .setParameter("DATE", myDate)
                    .setParameter("ID", person.getId())
                    .getSingleResult();

        } catch (NoResultException e) {
            consumation = null;
        }

        return consumation;
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
