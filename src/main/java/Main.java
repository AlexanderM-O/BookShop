
import DAO.PersonDao;
import Domain.PersonEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/*
    todo add connection to database
    todo add Hibernate
    todo add Person Entity
    todo add registration and logging

 */
public class Main {
    private static Logger logger=LoggerFactory.getLogger(Main.class);
    private static SessionFactory factorySession;

    static {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(PersonEntity.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        factorySession = configuration.buildSessionFactory(builder.build());
        logger.info("Connection SUCCESS");
    }

    public static void main(String ...argc) throws Exception {
        PersonEntity person=new PersonEntity();

        person.setName("sana");
        person.setPassword("1234");

        PersonDao dao=new PersonDao(factorySession);
        dao.add(person);
    }

}
