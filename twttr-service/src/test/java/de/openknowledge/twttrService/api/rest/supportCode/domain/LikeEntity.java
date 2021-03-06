package de.openknowledge.twttrService.api.rest.supportCode.domain;

public class LikeEntity {
    private Integer tweetId, userId;

    public LikeEntity(Integer tweetId, Integer userId) {
        this.tweetId = tweetId;
        this.userId = userId;
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
