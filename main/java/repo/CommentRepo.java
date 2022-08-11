package repo;

import model.Comment;
import model.Post;
import model.Reaction;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CommentRepo {

    private EntityManager entity;

    public Comment getById(Long commentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.find(Comment.class, commentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Comment> getAllCommentsOfPost(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = entity.find(Post.class, postId);
            return entity.createQuery("SELECT c FROM Comment c WHERE c.post = :post and c.parent is null", Comment.class)
                    .setParameter("post", post)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Boolean save(String text, User user, Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Comment comment = new Comment();
            comment.setCreateDate(new Date());
            comment.setText(text);
            comment.setUser(entity.find(User.class,user.getId()));
            comment.setPost(entity.find(Post.class, postId));
            entity.persist(comment);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean saveOnComment(String text, User user, Long postId, Long commentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Comment comment = new Comment();
            comment.setCreateDate(new Date());
            comment.setText(text);
            comment.setUser(entity.find(User.class,user.getId()));
            comment.setPost(entity.find(Post.class, postId));
            comment.setParent(entity.find(Comment.class, commentId));
            entity.persist(comment);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteAllCommentOfPost(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = entity.find(Post.class, postId);

            List<Comment> comments = entity.createQuery("SELECT c FROM Comment c WHERE c.post = :post ", Comment.class)
                    .setParameter("post", post)
                    .getResultList();

            for (Comment com : comments) {
                entity.remove(com);
            }
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(Long commentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Comment comment = entity.find(Comment.class, commentId);

            List<Comment> comments = entity.createQuery("SELECT c FROM Comment c WHERE c.parent = :comment ", Comment.class)
                    .setParameter("comment", comment)
                    .getResultList();

            for (Comment com : comments) {
                entity.remove(com);
            }

            entity.remove(comment);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Long commentId, String text) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Comment comment = entity.find(Comment.class, commentId);
            comment.setText(text);
            entity.persist(comment);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Comment> findAllByUserAndDateBetween(User user , Date fromDate , Date toDate){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select c from Comment c where c.user = :user and c.createDate between :from and :to", Comment.class)
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
