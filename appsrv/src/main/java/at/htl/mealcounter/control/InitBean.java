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



    }

}
