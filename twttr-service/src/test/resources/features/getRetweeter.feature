Feature: Get retweeter of a specified tweet
  This feature file describes the behaviour of the REST-API for GET requests at the endpoint /api/tweets/{tweetId}/retweets/authors to get a list of retweeter of the specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who retweeted the specified tweet and the http status-code will be 200
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response status-code will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response status-code will be 404


  Scenario: Get a list of retweeter of a specified tweet
  Requesting a list of retweeter of a specified tweet

    Given the user "max" is authenticated
    And an stored tweet with id 1 got retweeted by users max and john
    When a client sends a GET "/tweets/1/retweets/authors" request for user "max" to get a list of retweeter of the specified tweet
    Then the client will receive the "OK" Status Code
    And the HTTP response body will contain following JSON with a list of users who retweeted the stored tweet
        """
          [
              {
                  "userId": 0,
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "role": "USER"
              },
              {
                  "userId": 2,
                  "firstName": "John",
                  "lastName": "Doe",
                  "role": "USER"
              }
          ]
        """


  Scenario: Unauthorised request to get a list of retweeter from a specified tweet
  The request must contain a valid token of an user

    Given a stored tweet with id 1
    When a client sends a GET "/tweets/1/retweets/authors" request without a valid token to get a list of retweeter of the specified tweet
    Then the client will receive the "UNAUTHORIZED" Status Code


  Scenario: Transmitted token from the request to get a list of retweeter from a specified tweet belongs to a moderator
  Just users can request a list of retweeter from a specified tweet

    Given the moderator "werner" is authenticated
    When a client sends a GET "/tweets/1/retweets/authors" request for moderator "werner" to get a list of retweeter of the specified tweet
    Then the client will receive the "FORBIDDEN" Status Code

  Scenario: Tweet to get the list of retweeter from doesn´t exist
  The user must exist

    Given the user "max" is authenticated
    But there is no tweet with id 9999
    When a client sends a GET "/tweets/9999/retweets/authors" request for user "max" to get a list of retweeter of the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code


  Scenario: Tweet to get the list of retweeter from is in status CANCELED
  The tweet to get the list of retweeter from must be existing

    Given the user "max" is authenticated
    And a stored tweet with id 1 in status CANCELED from user max
    When a client sends a GET "/tweets/1/retweets/authors" request for user "max" to get a list of retweeter of the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code