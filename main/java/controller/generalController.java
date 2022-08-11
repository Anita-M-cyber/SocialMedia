package controller;

import repo.Repository;

import java.util.Scanner;

public class generalController {

    public static void generalElections() {
        Logger.info(" 1.Following Posts \n 2.Create Post \n 3.Activity \n 4.Search \n 5.Direct \n 6.Profile \n 7.logout \n 8.Exit \n");
        System.out.println("your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice == 1) {
            new PostController().followingPosts();
        } else if (choice == 2) {
            new PostController().postCreate();
        } else if (choice == 3) {
            new ActivityController().activityChoice();
        } else if (choice == 4) {
            new SearchController().search();
        } else if (choice == 5) {
            new ChatController().generalDirectory();
        } else if (choice == 6) {
            new ProfileController().showProfile(Repository.currentUser);
        } else if (choice == 7) {
            Repository.currentUser = null;
            throw new RuntimeException("User has been logged out!!");
        } else if (choice == 8) {
            System.exit(0);
        }else {
            Logger.error("wrong input!! \n try again : ");
            generalElections();
        }
    }

}
