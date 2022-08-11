package controller;

import repo.FollowRepo;
import repo.Repository;

public class FollowController {
    private final FollowRepo followRepo = new FollowRepo();

    public void follow(long followingId){
        long followerId = Repository.currentUser.getId();
        boolean isSaved = followRepo.save(followerId,followingId);
        if (isSaved)
            Logger.info("followed successfully!");
        else Logger.error( "sth happened during follow!!!");
    }

    public void unfollow(long followingId){
        long followerId = Repository.currentUser.getId();
        boolean isSaved = followRepo.delete(followerId,followingId);
        if (isSaved)
            Logger.info("unfollowed successfully!");
        else Logger.error( "sth happened during unfollow!!!");
    }
}
