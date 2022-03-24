package at.htl.mealcounter.control;

import at.htl.mealcounter.entity.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PersonRepository implements PanacheRepository<Person> {

    @Inject
    EntityManager em;

//    public void readFromCsv() {
//
//        URL url = Thread.currentThread().getContextClassLoader().getResource("names.csv");
//        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)) {
//            stream.skip(1)
//                    .map(s -> s.split(";"))
//                    .map(a -> new Person(
//                            a[0],
//                            a[1],
//                            Integer.parseInt(a[2])))
//                    .peek(out::println)
//                    .forEach(em::merge);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public Person findByPersonId(Person person) {
        return em.createQuery("select p from Person p where p.id = :id", Person.class)
                .setParameter("id", person.getId()).getSingleResult();
    }

    public List<Person> findByEntryYear(int entryYear) {
        return em.createQuery("select p from Person p where p.entryYear = :entryYear", Person.class)
                .setParameter("entryYear", entryYear).getResultList();
    }
    public Person findPersonByNfcCard(String id) {
        return em.createQuery("select p from Person p where p.nfcCard.nfcId = :id", Person.class)
                .setParameter("id", id).getResultStream().findFirst().orElse(null);
    }
}
