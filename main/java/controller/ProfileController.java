package controller;

import model.Post;
import model.User;
import model.chat.Chat;
import repo.FollowRepo;
import repo.PostRepo;
import repo.Repository;
import repo.UserRepo;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProfileController {
    private final FollowRepo followRepo = new FollowRepo();
    private final FollowController followController = new FollowController();
    private final LoginController loginController = new LoginController();
    private final UserRepo userRepo = new UserRepo();
    private final ChatController chatController = new ChatController();
    private final PostController postController = new PostController();
    private final PostRepo postRepo = new PostRepo();

    public void showProfile(User user){
        Scanner scanner = new Scanner(System.in);

        System.out.println("username: " + user.getUsername());
        System.out.print("following: " + followRepo.getFollowings(user).size() + "\t");
        List<User> followers = followRepo.getFollowers(user);
        System.out.println("followers: " + followers.size());
        if (!user.equals(Repository.currentUser)) {
            boolean isFollowing = false;
            for(User follower : followers)
                if (Repository.currentUser.getId().equals(follower.getId())){
                    isFollowing = true;
                    break;
                }
            if (isFollowing)
                System.out.print("unfollow(1)");
            else
                System.out.print("follow(1)");
            System.out.println("  message(2)  main page(3) posts(4)");
            System.out.print("your choice: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                if (isFollowing)
                    followController.unfollow(user.getId());
                else
                    followController.follow(user.getId());
                generalController.generalElections();
            } else if (choice == 2) {
                chatController.createSingleChat(user);
            } else if (choice == 3) {
                generalController.generalElections();
            } else if (choice == 4) {
                showPosts(user);
            } else {
                Logger.error("wrong input!!");
                showProfile(user);
            }
        }else {
            System.out.println("edit Profile(1)  main Page(2) posts(3)");
            System.out.print("your choice: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                editProfile();
            } else if (choice == 2) {
                generalController.generalElections();
            } else if (choice == 3) {
                showPosts(user);
            } else {
                Logger.error("wrong input!!");
                showProfile(user);
            }
        }
    }

    private void editProfile(){
        Scanner scanner = new Scanner(System.in);
        User current = Repository.currentUser;

        System.out.println("enter your new username: ");
        String username = scanner.next();
        System.out.println("enter your new pass");
        String password = loginController.getPass();

        User edited = userRepo.save(current.getId() , username , password , current.getSecurityAnswer(), current.getRole());
        if (!Objects.isNull(Repository.currentUser)) {
            Logger.info("edited successfully");
            Repository.currentUser = edited;
            generalController.generalElections();
        } else{
            Logger.error("wrong input try again!!");
            editProfile();
        }
    }

    private void showPosts(User user){
        Scanner scanner = new Scanner(System.in);
        List<Post> posts = postRepo.getAllUserPosts(user);
        for (Post post : posts){
            System.out.println("Post Id : " + post.getId());
            System.out.println("Post text: " + post.getText() + "...");
            System.out.println("----------------------------------");
        }
        System.out.println("Enter Post Number To Open The Post , Else Enter 0 To Return : ");
        long choice = scanner.nextLong();
        if (choice == 0) {
            generalController.generalElections();
        } else {
            Post post = postRepo.getById(choice);
            if (!Objects.isNull(post))
                postController.openPost(post);
            else {
                System.out.println("Wrong Number!!");
                showProfile(user);
            }
        }
    }
}
