package com.panda.user.service;

public interface FansService {
    public boolean isFollowing(String writerId, String userId);

    public void follow(String writerId, String userId);

    public void unfollow(String writerId, String userId);
}
