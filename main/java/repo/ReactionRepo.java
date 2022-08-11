package repo;

import model.Comment;
import model.Post;
import model.Reaction;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;
import util.ReactionType;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReactionRepo {

    private EntityManager entity;

    public Reaction getById(Long reactionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.find(Reaction.class, reactionId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Reaction> getAllViewsOfPost(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.createQuery("select r from Reaction r where r.reactionTo = :post and r.type = :type order by r.createDate desc ", Reaction.class)
                    .setParameter("post", entity.find(Post.class,postId))
                    .setParameter("type", ReactionType.VIEW)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Reaction> getAllLikesOfPost(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.createQuery("select r from Reaction r where r.reactionTo = :post and r.type = :type order by r.createDate desc ", Reaction.class)
                    .setParameter("post", entity.find(Post.class,postId))
                    .setParameter("type", ReactionType.LIKE)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean save(Long postId, User user, ReactionType type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            List<Reaction> reactions = entity.createQuery("select r from Reaction r where r.reactionTo = :post and r.user =:user and r.type = :type order by r.createDate desc ", Reaction.class)
                    .setParameter("post", entity.find(Post.class, postId))
                    .setParameter("user" , entity.find(User.class , user.getId()))
                    .setParameter("type", type).getResultList();
            Reaction reaction = null;
            if (reactions.size() > 0)
                reaction = reactions.get(0);
            if (!Objects.isNull(reaction)){
                return false;
            }
            reaction = new Reaction();
            reaction.setCreateDate(new Date());
            reaction.setReactionTo(entity.find(Post.class, postId));
            reaction.setUser(entity.find(User.class,user.getId()));
            reaction.setType(type);
            entity.persist(reaction);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean unlike(Post post , User user){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Reaction reaction = entity.createQuery("select r from Reaction r where r.reactionTo = :post and r.user =:user and r.type = :type order by r.createDate desc ", Reaction.class)
                    .setParameter("post", entity.find(Post.class, post.getId()))
                    .setParameter("user" , entity.find(User.class , user.getId()))
                    .setParameter("type", ReactionType.LIKE).getSingleResult();
            entity.remove(reaction);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean delete(Long reactionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Reaction reaction = entity.find(Reaction.class, reactionId);
            entity.remove(reaction);
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteAllReactionsOfPost(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Post post = entity.find(Post.class, postId);

            List<Reaction> reactions = entity.createQuery("SELECT r FROM Reaction r WHERE r.reactionTo = :post ", Reaction.class)
                    .setParameter("post", post)
                    .getResultList();

            for (Reaction rec : reactions) {
                entity.remove(rec);
            }
            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reaction> findAllByUserAndBetween(User user , Date fromDate , Date toDate){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select r from Reaction r where r.user = :user and r.createDate between :from and :to", Reaction.class)
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
