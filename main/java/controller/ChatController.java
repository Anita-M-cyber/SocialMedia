package controller;

import model.User;
import model.chat.Chat;
import model.chat.Members;
import model.chat.Message;
import repo.ChatRepo;
import repo.Repository;
import util.ChatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ChatController {

    Scanner scanner = new Scanner(System.in);

    private final ChatRepo chatRepo = new ChatRepo();

    public void generalDirectory(){
        //Todo Must Show Unread Messages
        Logger.info("1.Create Chat \n2.See Chats \n3.Back \n");
        System.out.println("your choice: ");

        int choice = scanner.nextInt();

        if(choice == 1){
            createChat();
        } else if (choice == 2){
            seeChats();
        }else if (choice == 3){
            generalController.generalElections();
        }else{
            Logger.error("wrong input!! \n try again : ");
            generalDirectory();
        }
    }

    public void createSingleChat(User user){
        List<User> members = new ArrayList<>();
        members.add(Repository.currentUser);
        members.add(user);

        boolean create = chatRepo.createChat(Repository.currentUser , members , ChatType.Single , null);

        if(create)
            System.out.println("Chat Has Been Created !!");
        else
            System.out.println("Sth Happened During Creation !!");
    }

    private void createChat(){
        boolean add = true;
        ChatType type = null;
        String groupName = null;
        List<User> members = new ArrayList<User>();
        members.add(Repository.currentUser);
        System.out.println("Choose Your Chat Type : (Group Or Single)");
        String typeChoice = scanner.next();
        if(typeChoice.equalsIgnoreCase("Group")) {
            type = ChatType.Group;
        } else if(typeChoice.equalsIgnoreCase("Single")) {
            type = ChatType.Single;
        } else {
            Logger.error("Wrong input");
            //Todo
        }

        if (type == ChatType.Group){
            System.out.println("Enter Your Group Name : ");
            groupName = scanner.nextLine();
        }
        while (add) {
            User member = new SearchController().searchUserChat();
            if (Objects.isNull(member))
                return; //Todo What To Do?
            members.add(member);
            if (members.size() > 1 && type == ChatType.Single)
                break;
            if (type == ChatType.Group) {
                Logger.info("Do You Wanna Add More ? (Y/N) : ");
                if (scanner.next().equalsIgnoreCase("N"))
                    add = false;
            }
        }

        boolean create = chatRepo.createChat(Repository.currentUser , members , type , groupName);

        if(create)
            System.out.println("Chat Has Been Created !!");
        else
            System.out.println("Sth Happened During Creation !!");

        generalDirectory();
    }

    private void seeChats(){
        List<Chat> chats = chatRepo.getAllUserChat(Repository.currentUser);
        if (chats.size() == 0){
            Logger.info("No Chat Founded");
            System.out.println("Creating New Chat ? (Y/N) : ");
            if (scanner.next().equalsIgnoreCase("Y"))
                createChat();
            else
                generalDirectory();
        } else {
            for (Chat chat : chats){
                System.out.println("Chat Id : " + chat.getId());
                System.out.println(chat.getChatName());
                System.out.println("----------------------------------");
            }
            System.out.println("Enter Chat Number To Open The Chat , Else Enter 0 To Return : ");
            Long choice = scanner.nextLong();
            if (choice == 0) {
                generalDirectory();
            } else {
                Chat chat = chatRepo.getById(choice);
                if (!Objects.isNull(chat))
                    openChat(chat);
                else
                    System.out.println("Wrong Number!!");

                seeChats();
            }
        }
    }

    private void openChat(Chat chat){
        Logger.info("1.See Messages \n2.See Members \n3.Back");

        System.out.println("your choice : ");
        int choice = scanner.nextInt();

        if(choice == 1){
            seeMessages(chat);
        } else if (choice == 2){
            seeMembers(chat);
        }else if (choice == 3){
            generalDirectory();
        }
        else{
            Logger.error("wrong input!! \n try again : ");
            openChat(chat);
        }
    }

    private void seeMessages(Chat chat){
        Scanner scan = new Scanner(System.in);
        List<Message> messages = chatRepo.getAllMessages(chat);

        for (Message message : messages){
            System.out.println(message.getSender().getUsername() + " : " + message.getMessage());
        }

        Logger.info("1.Send Message \n2.Back \n");
        int choice = scan.nextInt();
        if (choice == 1)
            sendMessage(chat);


        openChat(chat);
    }

    private void seeMembers(Chat chat){
        List<Members> members = chatRepo.getAllMembers(chat);

        for (Members member : members){
            System.out.println(member.getMember().getUsername());
        }

        if (chat.getOwner().equals(Repository.currentUser) && chat.getType() == ChatType.Group) {
            Logger.info("1.Add Member \n 2.Back");
            int choice = scanner.nextInt();
            if(choice == 1)
                addMember(chat);
        }

        openChat(chat);
    }

    private void sendMessage(Chat chat){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Your Message : ");
        String message = scan.nextLine();

        boolean send = chatRepo.sendMessage(chat , message);

        if(send)
            System.out.println("Message Send!!");
        else
            System.out.println("Sth Happened During Sending !!");

        seeMessages(chat);
    }

    private void addMember(Chat chat) {
        boolean add = true;
        List<User> members = new ArrayList<User>();
        while (add) {
            User member = new SearchController().searchUserChat();
            if (Objects.isNull(member))
                return; //Todo What To Do?
            members.add(member);
            Logger.info("Do You Wanna Add More ? (Y/N) : ");
            if (scanner.next().equalsIgnoreCase("N"))
                add = false;
        }

        boolean save = chatRepo.addMembers(chat , members);

        if(save)
            System.out.println("Members Has Been Added !!");
        else
            System.out.println("Sth Happened During Adding !!");

        seeMembers(chat);
    }
}
