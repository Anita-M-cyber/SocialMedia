package repo;

import model.Post;
import model.Reaction;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;
import util.PostType;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostRepo {

    private EntityManager entity;
    private final FollowRepo followRepo = new FollowRepo();
    private final CommentRepo commentRepo = new CommentRepo();
    private final ReactionRepo reactionRepo = new ReactionRepo();

    public Post getById(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.find(Post.class, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> getAllUserPosts(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.createQuery("SELECT p FROM Post p WHERE p.user = :user", Post.class)
                    .setParameter("user", entity.find(User.class,user.getId()))
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> getAllFollowingPosts(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            String jpql = "select f.following from Follow f where f.follower = :user";
            List<User> following = entity.createQuery(jpql, User.class)
                    .setParameter("user", entity.find(User.class,user.getId()))
                    .getResultList();

            return entity.createQuery("SELECT p FROM Post p WHERE p.user in :user order by p.createDate desc ", Post.class)
                    .setParameter("user", following)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean save(String text, User user, PostType type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = new Post();
            post.setCreateDate(new Date());
            post.setText(text);
            post.setUser(entity.find(User.class,user.getId()));
            post.setType(type);
            entity.persist(post);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(Long postId) {
        reactionRepo.deleteAllReactionsOfPost(postId);
        commentRepo.deleteAllCommentOfPost(postId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = entity.find(Post.class, postId);
            entity.remove(post);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Long postId, String text, PostType type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = entity.find(Post.class, postId);
            post.setText(text);
            post.setType(type);
            entity.persist(post);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> findAllPostsByUserAndBetween(User user, Date fromDate , Date toDate){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select p from Post p where p.user = :user and p.createDate between :from and :to", Post.class)
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
