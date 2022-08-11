package controller;

import model.User;
import repo.UserRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchController {
    private final UserRepo userRepo = new UserRepo();
    private final ProfileController profileController = new ProfileController();
    Scanner scanner = new Scanner(System.in);

    public void search(){
        System.out.print("enter username you're looking for: ");
        String username = scanner.next();
        List<User> foundUsers = userRepo.findByUsernameLike(username);
        Map<Integer,User> userMap = new HashMap<>();
        int i = 0;
        for (User u : foundUsers) {
            i++;
            userMap.put(i, u);
            System.out.println(i + "- " + u.getUsername());
        }
        System.out.print("which one you want: ");
        int choice = scanner.nextInt();
        while (!userMap.containsKey(choice)) {
            Logger.error("wrong choice!! try again");
            System.out.print("which one you want: ");
            choice = scanner.nextInt();
            //todo If Want To Return
        }

        User wanted = userMap.get(choice);
        profileController.showProfile(wanted);
    }

    public User searchUserChat(){
        System.out.print("enter username you're looking for: ");
        String username = scanner.next();
        List<User> foundUsers = userRepo.findByUsernameLike(username);
        Map<Integer,User> userMap = new HashMap<>();
        int i = 0;
        for (User u : foundUsers) {
            i++;
            userMap.put(i, u);
            System.out.println(i + "- " + u.getUsername());
        }
        System.out.print("which one you want: ");
        int choice = scanner.nextInt();
        while(!userMap.containsKey(choice)) {
            Logger.error("wrong choice!!");
            Logger.info("If You Wanna Go Back Enter 0 !!");
            System.out.print("which one you want: ");
            choice = scanner.nextInt();
            if(choice == 0){
                return null;
            }
        }

        return userMap.get(choice);
    }
}
