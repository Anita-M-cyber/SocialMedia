package repo;

import model.Comment;
import model.Follow;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class FollowRepo {

    public List<User> getFollowers(User user){
        EntityManager entityManager = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            String jpql = "select f.follower from Follow f where f.following = :user";

            return entityManager.createQuery(jpql, User.class)
                    .setParameter("user", entityManager.find(User.class,user.getId()))
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getFollowings(User user){
        EntityManager entityManager = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            String jpql = "select f.following from Follow f where f.follower = :user";

            return entityManager.createQuery(jpql, User.class)
                    .setParameter("user", entityManager.find(User.class,user.getId()))
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public Boolean save(long followerId , long followingId) {
        EntityManager entityManager = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            User follower = entityManager.find(User.class, followerId);
            User following = entityManager.find(User.class, followingId);
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            follow.setFollowDate(new Date());

            entityManager.persist(follow);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(long followerId , long followingId) {
        EntityManager entityManager = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entityManager = session.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            User follower = entityManager.find(User.class, followerId);
            User following = entityManager.find(User.class, followingId);
            String jpql = "select f from Follow f where f.follower = :follower and f.following = :following";

            Follow follow = entityManager.createQuery(jpql, Follow.class)
                    .setParameter("follower", follower)
                    .setParameter("following", following)
                    .getSingleResult();

            entityManager.remove(follow);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> findAllFollowerByDateBetween(User user , Date fromDate , Date toDate){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            EntityManager entity = session.getEntityManagerFactory().createEntityManager();
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select f.follower from Follow f where f.following = :user and f.followDate between :from and :to", User.class)
                    .setParameter("user", entity.find(User.class,user.getId()))
                    .setParameter("from", fromDate)
                    .setParameter("to", toDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAllFollowingByDateBetween(User user , Date fromDate , Date toDate){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            EntityManager entity = session.getEntityManagerFactory().createEntityManager();
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select f.following from Follow f where f.follower = :user and f.followDate between :from and :to", User.class)
                    .setParameter("user", entity.find(User.class,user.getId()))
                    .setParameter("from", fromDate)
                    .setParameter("to", toDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
