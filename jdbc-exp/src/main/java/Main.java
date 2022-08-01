import entities.Car;
import entities.Owner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.tree.RowMapper;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = meta.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

        List<Owner> list = getAllOwner(session);
        list.stream().forEach(System.out::println);

        sessionFactory.close();
    }

    private static Owner getOwner(Session session, String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Owner> query = builder.createQuery(Owner.class);
        Root<Owner> root = query.from(Owner.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        Owner owner = session.createQuery(query).getSingleResult();
        return owner;
    }

    private static void addCar(Session session, Owner owner, Car car) {
        Transaction transaction = session.beginTransaction();
        car.setOwner(owner);
        session.save(car);
        transaction.commit();
    }

    private static List<Owner> getAllOwner(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Owner> query = builder.createQuery(Owner.class);
        Root<Owner> root = query.from(Owner.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    private static void addOwner(Session session, String name) {
        Transaction transaction = session.beginTransaction();
        Owner owner = new Owner(name);
        session.save(owner);
        transaction.commit();
    }
}
