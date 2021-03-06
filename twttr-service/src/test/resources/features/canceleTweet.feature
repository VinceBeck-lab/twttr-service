Feature: Cancel a tweet
  This feature file describes the behaviour of the REST-API for DELETE requests at the endpoint /api/tweets/{tweetId} to cancel a specified tweets.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of the user who is the author of the specified tweet, then the http response status-code will be 204
  - If the request contains a token from another user than the author, then the http response status-code will be 403
  - If the request contains a valid token which belongs to a moderator, then the http response status-code will be 204
  - If the request doesn´t contain a valid token, then the http response status-code will be 401
  - If the specified tweet doesn´t exist or is already in status "CANCELED", then the http response status-code will be 404

  Scenario: Cancel a tweet
  Request to set a specified tweet in status CANCELED

    Given the user "max" is authenticated
    And a stored tweet with id 1 from user max
    When a client sends a DELETE "/tweets/1" request for user "max" to cancel the specified tweet
    Then the client will receive the "NO_CONTENT" Status Code


  Scenario: Another user than the author wants to cancel a tweet
  To cancel a tweet the requesting user must be the author of it

    Given the user "john" is authenticated
    And a stored tweet with id 1 from user max
    When a client sends a DELETE "/tweets/1" request for user "john" to cancel the specified tweet
    Then the client will receive the "FORBIDDEN" Status Code



  Scenario: Moderator cancel tweet
  Moderators can cancel the tweets from every user

    Given the moderator "werner" is authenticated
    And a stored tweet with id 1 from user max
    When a client sends a DELETE "/tweets/1" request for moderator "werner" to cancel the specified tweet
    Then the client will receive the "NO_CONTENT" Status Code



  Scenario: Unauthorised request to cancel a tweet
  The request to cancel a tweet must contain a valid token

    Given a stored tweet with id 1
    When a client sends a DELETE "/tweets/1" request without a valid token to cancel the specified tweet
    Then the client will receive the "UNAUTHORIZED" Status Code



  Scenario: Tweet to cancel doesn´t exist
  The tweet to cancel must exist

    Given the user "max" is authenticated
    But there is no tweet with id 9999
    When a client sends a DELETE "/tweets/9999" request for user "max" to cancel the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code


  Scenario: Tweet to delete is in status CANCELED
  The tweet to cancel must be in status PUBLISH

    Given the user "max" is authenticated
    And a stored tweet with id 1 in status CANCELED from user max
    When a client sends a DELETE "/tweets/1" request for user "max" to cancel the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code