package controller;

import model.Comment;
import model.Post;
import model.Reaction;
import model.User;
import repo.*;
import util.DateUtils;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ActivityController {
    private final PostRepo postRepo = new PostRepo();
    private final PostController postController = new PostController();
    private final CommentRepo commentRepo = new CommentRepo();
    private final CommentController commentController = new CommentController();
    private final FollowRepo followRepo = new FollowRepo();
    private final ReactionRepo reactionRepo = new ReactionRepo();

    public void activityChoice() {
        Logger.info(" 1.Today \n 2.Yesterday \n 3.Last Week \n 4.Last Month \n 5.Last Year \n 6.Back \n");
        System.out.println("your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        Date toDate = DateUtils.getEndOfDay(new Date());

        if (choice == 1) {
            Date fromDate = DateUtils.getStartOfDay(new Date());
            showActivity(fromDate , toDate);
        } else if (choice == 2) {
            Date fromDate = DateUtils.getDateBefore(new Date() , 1 , ChronoUnit.DAYS);
            fromDate = DateUtils.getStartOfDay(fromDate);
            showActivity(fromDate , toDate);
        } else if (choice == 3) {
            Date fromDate = DateUtils.getDateBefore(new Date() , 7 ,ChronoUnit.DAYS);
            fromDate = DateUtils.getStartOfDay(fromDate);
            showActivity(fromDate , toDate);
        } else if (choice == 4) {
            Date fromDate = DateUtils.getDateBefore(new Date() , 30 , ChronoUnit.DAYS);
            fromDate = DateUtils.getStartOfDay(fromDate);
            showActivity(fromDate , toDate);
        } else if (choice == 5) {
            Date fromDate = DateUtils.getDateBefore(new Date() , 365 , ChronoUnit.DAYS);
            fromDate = DateUtils.getStartOfDay(fromDate);
            showActivity(fromDate , toDate);
        } else if (choice == 6) {
            generalController.generalElections();
        } else {
            Logger.error("wrong input!! \n try again : ");
            activityChoice();
        }

        activityChoice();
    }

    public void showActivity(Date fromDate , Date toDate) {

        Logger.info("From : " + fromDate + "\nTo : " + toDate);

        User currentUser = Repository.currentUser;
        List<Post> posts = postRepo.findAllPostsByUserAndBetween(currentUser,fromDate,toDate);
        System.out.println("Posts created: ");
        System.out.println("-----------------------------");
        for (Post post : posts) {
            List<Reaction> likes = reactionRepo.getAllLikesOfPost(post.getId());
            List<Reaction> views = reactionRepo.getAllViewsOfPost(post.getId());

            System.out.println("Count Of Views = " + views.size());
            System.out.println("Count Of Likes = " + likes.size());
            System.out.println("People Who Liked Your Post : ");
            for (Reaction reaction : likes){
                System.out.println("User : " + reaction.getUser().getUsername());
            }
        }
        System.out.println("-----------------------------");
        List<Comment> comments = commentRepo.findAllByUserAndDateBetween(currentUser,fromDate,toDate);
        System.out.println("Comments created: ");
        System.out.println("-----------------------------");
        for (Comment comment : comments)
            commentController.showDetails(comment);
        System.out.println("-----------------------------");
        List<User> following = followRepo.findAllFollowingByDateBetween(currentUser,fromDate,toDate);
        System.out.println("People that you followed: ");
        System.out.println("-----------------------------");
        for (User user : following)
            System.out.println(user.getUsername());
        System.out.println("-----------------------------");
        List<User> follower = followRepo.findAllFollowerByDateBetween(currentUser,fromDate,toDate);
        System.out.println("People that followed you: ");
        System.out.println("-----------------------------");
        for (User user : follower)
            System.out.println(user.getUsername());
        System.out.println("-----------------------------");
        List<Reaction> reactions = reactionRepo.findAllByUserAndBetween(currentUser,fromDate,toDate);
        System.out.println("Your reactions: ");
        System.out.println("-----------------------------");
        for (Reaction reaction : reactions)
            System.out.println("type: " + reaction.getType() + "  post id : " + reaction.getReactionTo().getId());
        System.out.println("-----------------------------");
    }

}
