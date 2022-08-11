package controller;

import model.Comment;
import model.Post;
import repo.CommentRepo;
import repo.Repository;

import java.util.List;
import java.util.Scanner;

public class CommentController {

    Scanner scanner = new Scanner(System.in);
    private final CommentRepo commentRepo = new CommentRepo();

    public void setCommentOnPost(Post post) {
        System.out.println("Enter Your Comment : ");
        String text = scanner.nextLine();
        Boolean save = commentRepo.save(text , Repository.currentUser , post.getId());
        if (save) {
            Logger.info("Comment Has Been Crated Successfully");
        } else {
            Logger.error("Sth Happened During Creating!!!");
        }
        new PostController().openPost(post);
    }

    public void showCommentsOfPost(Post post){
        List<Comment> comments = commentRepo.getAllCommentsOfPost(post.getId());
        for (Comment comment : comments){
            System.out.println("-----------------------------");
            System.out.println("Comment By : " + comment.getUser().getUsername());
            System.out.println("Created Date : " + comment.getCreateDate());
            System.out.println("Text : " + comment.getText());
            System.out.println("-------------------------------");
        }
        //todo Show Child Comment - Edit - Delete Must Complete In Swing Phase
        new PostController().openPost(post);
    }

    public void showDetails(Comment comment){
        System.out.println("-----------------------------");
        System.out.println("Created Date : " + comment.getCreateDate());
        System.out.println("Text : " + comment.getText());
        System.out.println("-------------------------------");
    }
}
