package repo;

import model.User;
import model.chat.Chat;
import model.chat.Members;
import model.chat.Message;
import model.chat.MessageReceiver;
import org.hibernate.Session;
import util.ChatType;
import util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatRepo {

    private EntityManager entity;

    public Chat getById(Long chatId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.find(Chat.class , chatId);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean createChat(User owner , List<User> members , ChatType type , String groupName){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            Chat chat = new Chat();
            chat.setCreateDate(new Date());
            chat.setOwner(entity.find(User.class , owner.getId()));
            chat.setType(type);
            if(type == ChatType.Group)
                chat.setChatName(groupName);
            else
                chat.setChatName(owner.getUsername() + " / " + members.get(1).getUsername());
            entity.persist(chat);

            for (User user : members){
                Members member = new Members();
                member.setChat(chat);
                member.setMember(entity.find(User.class , user.getId()));
                entity.persist(member);
            }

            entity.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Chat> getAllUserChat(User user){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            return entity.createQuery("select m.chat from Members m where m.Member =: user")
                    .setParameter("user" , entity.find(User.class,user.getId())).getResultList();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public Boolean addMembers(Chat chat , List<User> members){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            for (User user : members){
                Members member = new Members();
                member.setChat(entity.find(Chat.class , chat.getId()));
                member.setMember(entity.find(User.class , user.getId()));
                entity.persist(member);
            }

            entity.getTransaction().commit();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Members> getAllMembers(Chat chat){
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();
            return entity.createQuery("select m from Members m where m.chat =: chat")
                    .setParameter("chat" , entity.find(Chat.class , chat.getId()))
                    .getResultList();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getAllMessages(Chat chat){
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            List<Message> messages = entity.createQuery("select m from Message m where m.chat =: chat order by m.sendDate asc")
                    .setParameter("chat" , entity.find(Chat.class , chat.getId()))
                    .getResultList();

            for (Message message : messages){
                MessageReceiver receiver = null;
                try {
                     receiver = entity.createQuery("select r from MessageReceiver r where r.message =: message and r.receiver =:user and r.received =: received", MessageReceiver.class)
                            .setParameter("message", message)
                            .setParameter("received", false)
                            .setParameter("user", entity.find(User.class, Repository.currentUser.getId())).getSingleResult();

                }catch (Exception e){
                    if (Objects.isNull(receiver)) {
                        receiver = new MessageReceiver();
                    }
                }
                if (!Objects.isNull(receiver)) {
                    receiver.setReceived(true);
                    receiver.setReceiverDate(new Date());

                }
            }

            entity.getTransaction().commit();

            return messages;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Boolean sendMessage(Chat chat , String msg){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            entity = session.getEntityManagerFactory().createEntityManager();
            entity.getTransaction().begin();

            Message message = new Message();
            message.setMessage(msg);
            message.setChat(entity.find(Chat.class , chat.getId()));
            message.setSendDate(new Date());
            message.setSender(entity.find(User.class , Repository.currentUser.getId()));
            entity.persist(message);

            List<User> members = entity.createQuery("select m.Member from Members m where m.chat =: chat")
                    .setParameter("chat" ,entity.find(Chat.class , chat.getId()))
                    .getResultList();

            for (User user : members){
                MessageReceiver receiver = new MessageReceiver();
                receiver.setReceiver(user);
                receiver.setMessage(message);
                receiver.setReceived(false);
                entity.persist(receiver);
            }

            entity.getTransaction().commit();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
