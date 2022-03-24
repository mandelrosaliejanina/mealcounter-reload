package at.htl.mealcounter.control;


import at.htl.mealcounter.entity.NfcCard;
import at.htl.mealcounter.entity.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class NfcRepository implements PanacheRepository<NfcCard> {

    @Inject
    EntityManager em;

    @Inject
    PersonRepository personRepository;
//
//    @Transactional
//    public void checkedNfcCard(String nfcId) {
//        if(personRepository.findByNfcId(nfcId) == null) {
//            NfcCard nfcCard = new NfcCard();
//            em.persist(nfcCard);
//        }
//    }


    public NfcCard findByNfcId(String nfcId) {
        NfcCard nfcCard = null;

         var nfcCards = em.createQuery("select n from NfcCard n where n.nfcId = :id", NfcCard.class)
                .setParameter("id", nfcId).getResultList();
        if(nfcCards.size() > 0) {
            nfcCard = nfcCards.get(0);
        }
        return nfcCard;
    }


    public boolean cardExists(String nfcId) {
        return em.createQuery("select count(n) from NfcCard n where n.nfcId = :id", Long.class)
                .setParameter("id", nfcId)
                .getSingleResult() == 1;
    }

    public Person findCardOwner(String nfcId){
        return em.createQuery("select p from Person p where p.nfcCard.nfcId = :id", Person.class)
                .setParameter("id", nfcId)
                .getSingleResult();
    }

    public boolean hasCardOwner(String nfcId){
        return em.createQuery("select count(p) from Person p where p.nfcCard.nfcId = :id", Long.class)
                .setParameter("id", nfcId)
                .getSingleResult() == 1;
    }

    public NfcCard merge(NfcCard nfcCard){
        return em.merge(nfcCard);
    }
}
