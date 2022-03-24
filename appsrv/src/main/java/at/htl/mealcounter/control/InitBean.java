package at.htl.mealcounter.control;


import at.htl.mealcounter.entity.Consumation;
import at.htl.mealcounter.entity.NfcCard;
import at.htl.mealcounter.entity.Person;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class InitBean {


    @Inject
    EntityManager em;

    @Inject
    PersonRepository personRepository;

    @Inject
    ConsumationRepository consumationRepository;

    @Inject
    NfcRepository nfcRepository;

    @Transactional
    public void startUp(@Observes StartupEvent startupEvent) {

        NfcCard nfcCard1 = new NfcCard("abc");
        nfcRepository.persist(nfcCard1);

        Person rosi = new Person(nfcRepository.findByNfcId("0603e7e30000"),"Rosalie","Mandel",2012);
        personRepository.persist(rosi);
        Person sandy = new Person(nfcRepository.findByNfcId("060367a5fe00"),"Sandy","Tang",2012);
        personRepository.persist(sandy);
        Person kelly = new Person(nfcRepository.findByNfcId("060317f2fb00"),"Kelly","Tran",2012);
        personRepository.persist(kelly);
        Person franz = new Person(nfcRepository.findByNfcId("0bcb245"),"Franz","Birker",2012);
        personRepository.persist(franz);
        Person paula = new Person(nfcRepository.findByNfcId("abncj435"),"Paula","Taler",2012);
        personRepository.persist(paula);
        Person susi = new Person(nfcRepository.findByNfcId("235313jn"),"Susi","Maus",2012);
        personRepository.persist(susi);

        Consumation consumation = new Consumation(paula, LocalDateTime.now().minusDays(1), true);
        consumationRepository.merge(consumation);
        Consumation consumation1 = new Consumation(franz, LocalDateTime.now(), true);
        consumationRepository.merge(consumation1);
        Consumation consumation2 = new Consumation(susi, LocalDateTime.now(), true);
        consumationRepository.merge(consumation2);


    }

}
