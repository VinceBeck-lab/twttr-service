package de.openknowledge.twttrService.api.rest.stepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.twttrService.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.twttrService.api.rest.supportCode.SharedDomain;
import de.openknowledge.twttrService.api.rest.supportCode.domain.ErrorMessage;
import de.openknowledge.twttrService.api.rest.supportCode.domain.NewTweet;
import de.openknowledge.twttrService.api.rest.supportCode.domain.TweetDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class CreateTweetSteps {

    private SharedDomain domain;
    public CreateTweetSteps(SharedDomain domain) {
        this.domain = domain;
    }



    @When("a client sends a POST {string} request for (user|moderator) {string} to create the following tweet")
    public void a_client_sends_a_POST_request_for_user_to_create_the_following_tweet(String additionalPath, String userName, NewTweet newTweet) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a POST {string} request for user {string} to create new tweet with {int} character")
    public void a_client_sends_a_POST_request_for_user_to_create_new_tweet_with_character(String additionalPath, String userName, int numberCharacters) {
        StringBuilder buffer = new StringBuilder(numberCharacters);
        for (int i = 0; i < numberCharacters; i++) {
            buffer.append("x");
        }
        NewTweet newTweet = new NewTweet(buffer.toString());

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request for user {string} without a valid token to create the following tweet")
    public void a_client_sends_a_POST_request_for_user_without_a_valid_token_to_create_the_following_tweet(String additionalPath, String userName, NewTweet newTweet) {
        String randomToken = "xxx";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @Then("the HTTP response body contains following JSON of a list of error messages:")
    public void the_HTTP_response_body_contains_following_JSON_of_a_list_of_error_messages(String expectedJson) {
        try {
            List<ErrorMessage> errors = new ObjectMapper().readValue(expectedJson, new TypeReference<List<ErrorMessage>>() {});
            domain.getResponse().then()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("size()", Matchers.equalTo(1))
                    .body("[0].errorMessage", Matchers.equalTo(errors.get(0).getErrorMessage()));
        } catch (IOException e) {
            throw new PendingException();
        }
    }

    @Then("the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system")
    public void checkCreatedTweetResponse(TweetDTO expectedTweet) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("tweetId", Matchers.notNullValue(Integer.class))
                .body("content", Matchers.equalTo(expectedTweet.getContent()))
                .body("pubDate", Matchers.notNullValue(Date.class))
                .body("author.userId", Matchers.equalTo(expectedTweet.getAuthor().getUserId()))
                .body("author.firstName", Matchers.equalTo(expectedTweet.getAuthor().getFirstName()))
                .body("author.lastName", Matchers.equalTo(expectedTweet.getAuthor().getLastName()))
                .body("author.role", Matchers.equalTo(expectedTweet.getAuthor().getRole().toString()))
                .body("rootTweet", Matchers.isEmptyOrNullString());
    }
}