package de.openknowledge.playground.api.rest.security.domain.tweet;

import de.openknowledge.playground.api.rest.security.application.user.UserDTO;

import java.io.Serializable;
import java.util.Date;

public class TweetDTO implements Serializable {
    private Integer tweetId;
    private String content;
    private Date pubDate;
    private UserDTO author;
    private TweetDTO rootTweet;

    public TweetDTO () {
        //for REST
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public TweetDTO getRootTweet() {
        return rootTweet;
    }

    public void setRootTweet(TweetDTO rootTweet) {
        this.rootTweet = rootTweet;
    }
}
