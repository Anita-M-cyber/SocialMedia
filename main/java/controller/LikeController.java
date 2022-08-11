package controller;

import model.Post;
import repo.ReactionRepo;
import repo.Repository;
import util.ReactionType;

public class LikeController {

    private final ReactionRepo reactionRepo = new ReactionRepo();

    public void setLike(Post post) {
        Boolean save = reactionRepo.save(post.getId() , Repository.currentUser , ReactionType.LIKE);
        if (save) {
            Logger.info("Like Has Been Crated Successfully");
        } else {
            Logger.error("Sth Happened During Creating!!!");
        }
        new PostController().openPost(post);
    }

    public void setUnlike(Post post){
        Boolean unlike = reactionRepo.unlike(post , Repository.currentUser);
        if (unlike) {
            Logger.info("Unlike Has Been Done");
        } else {
            Logger.error("Sth Happened During Unlike!!!");
        }
        new PostController().openPost(post);
    }
}
