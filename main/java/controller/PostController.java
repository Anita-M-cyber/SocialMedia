package controller;

import model.Post;
import model.Reaction;
import repo.PostRepo;
import repo.ReactionRepo;
import repo.Repository;
import util.PostType;
import util.ReactionType;
import util.Role;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class PostController {

    Scanner scanner = new Scanner(System.in);
    private final PostRepo postRepo = new PostRepo();
    private final ReactionRepo reactionRepo = new ReactionRepo();

    public void followingPosts() {
        List<Post> posts = postRepo.getAllFollowingPosts(Repository.currentUser);
        System.out.println("Your Following Last Posts : ");
        for (Post post : posts) {
            System.out.println("-------------------------------");
            System.out.println("Post Number : " + post.getId());
            System.out.println("Create By : " + post.getUser().getUsername());
            System.out.println("Date Of Create : " + post.getCreateDate());
            System.out.println("-------------------------------");
        }
        System.out.println("Enter Post Number To Open The Post , Else Enter 0 To Return To General Page : ");
        Long choice = scanner.nextLong();
        if (choice == 0) {
            generalController.generalElections();
        } else {
            Post post = postRepo.getById(choice);
            if (!Objects.isNull(post))
                openPost(post);
            else
                System.out.println("Wrong Number!!");

            followingPosts();
        }
    }

    public void openPost(Post post) {
        reactionRepo.save(post.getId(), Repository.currentUser, ReactionType.VIEW);
        System.out.println("------------------------------");
        System.out.println("Post Number : " + post.getId());
        System.out.println("Create By : " + post.getUser().getUsername());
        System.out.println("Date Of Create : " + post.getCreateDate());
        System.out.println("Post Text : \n + " + post.getText());
        System.out.println("-----------------------------");
        Logger.info("1.Back \n 2.Set Comment \n 3.See Comments \n 4.Like \n 5.Unlike \n");
        if (post.getUser() == Repository.currentUser) {
            Logger.info("6.Edit Post \n 7.Delete Post \n");
            if (Repository.currentUser.getRole() == Role.Business) {
                Logger.info("8.Show Details \n");
            }
        }
        System.out.println("Chose Your Action : ");
        int choice = scanner.nextInt();
        if (choice == 1) {
        } else if (choice == 2) {
            new CommentController().setCommentOnPost(post);
        } else if (choice == 3) {
            new CommentController().showCommentsOfPost(post);
        } else if (choice == 4) {
            new LikeController().setLike(post);
        } else if (choice == 5) {
            new LikeController().setUnlike(post);
        } else if (choice == 6) {
            updatePost(post);
        } else if(choice == 7){
            deletePost(post.getId());
        } else if (choice == 8){
            postDetails(post);
        } else {
            System.out.println("Wrong Input !!");
            openPost(post);
        }
    }

    public void deletePost(Long postId){
        Boolean delete = postRepo.delete(postId);
        if (delete) {
            Logger.info("Post Has Been deleted");
        } else {
            Logger.error("Sth Happened During deleting!!!");
        }
    }

    public void updatePost(Post post){
        PostType type = null;
        if (Repository.currentUser.getRole() == Role.Business) {
            Logger.info(" 1.STORE \n 2.FOOD \n 3.SHOP \n 4.CAR \n ");
            System.out.println("Chose Your Post Type : ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                type = PostType.STORE;
            } else if (choice == 2) {
                type = PostType.FOOD;
            } else if (choice == 3) {
                type = PostType.SHOP;
            } else if (choice == 4) {
                type = PostType.CAR;
            } else {
                Logger.error("wrong input !! try again : ");
                postCreate();
            }
        }

        if (Objects.isNull(type)) {
            type = PostType.NORMAL;
        }

        System.out.println("Enter Your Text : ");
        String text = scanner.nextLine();

        Boolean update = postRepo.update(post.getId() , text , type);
        if (update) {
            Logger.info("Post Has Been Updated");
        } else {
            Logger.error("Sth Happened During Creating!!!");
        }

        openPost(post);
    }

    public void postCreate() {
        PostType type = null;
        if (Repository.currentUser.getRole() == Role.Business) {
            Logger.info(" 1.STORE \n 2.FOOD \n 3.SHOP \n 4.CAR \n ");
            System.out.println("Chose Your Post Type : ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                type = PostType.STORE;
            } else if (choice == 2) {
                type = PostType.FOOD;
            } else if (choice == 3) {
                type = PostType.SHOP;
            } else if (choice == 4) {
                type = PostType.CAR;
            } else {
                Logger.error("wrong input !! try again : ");
                postCreate();
            }
        }

        if (Objects.isNull(type)) {
            type = PostType.NORMAL;
        }

        System.out.println("Enter Your Text : ");
        String text = scanner.nextLine();

        Boolean save = postRepo.save(text, Repository.currentUser, type);
        if (save) {
            Logger.info("Post Has Been Crated Successfully");
        } else {
            Logger.error("Sth Happened During Creating!!!");
        }

        generalController.generalElections();

    }

    public void postDetails(Post post){
        List<Reaction> likes = reactionRepo.getAllLikesOfPost(post.getId());
        List<Reaction> views = reactionRepo.getAllViewsOfPost(post.getId());

        System.out.println("Count Of Views = " + views.size());
        System.out.println("Count Of Likes = " + likes.size());
        System.out.println("People Who Liked Your Post : ");
        for (Reaction reaction : likes){
            System.out.println("User : " + reaction.getUser().getUsername());
        }

        openPost(post);
    }

}
