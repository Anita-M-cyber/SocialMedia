package repo;

import model.User;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Role;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepo {
    public List<User> list() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createSQLQuery("select * from User").addEntity(User.class).list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public User get(long id){
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.get(User.class, id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public User save(long id , String username , String password , String secAnswer , Role role) {
        EntityManager entityManager = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, id);
            if (Objects.isNull(user))
                user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setSecurityAnswer(secAnswer);
            user.setRole(role);

            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User auth(String username, String pass){
        EntityManager entityManager = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            String jpql = "select u from User u where u.username = :username and u.password = :pass";

            return entityManager.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .setParameter("pass",pass).getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public User checkSecAnswer(String username, String secAns){
        EntityManager entityManager = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            String jpql = "select u from User u where u.username = :username and u.securityAnswer = :secAns";

            return entityManager.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .setParameter("secAns",secAns).getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findByUsernameLike(String username){
        EntityManager entityManager = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            String jpql = "select u from User u where u.username like :username";

            return entityManager.createQuery(jpql, User.class)
                    .setParameter("username", "%" + username + "%").getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
