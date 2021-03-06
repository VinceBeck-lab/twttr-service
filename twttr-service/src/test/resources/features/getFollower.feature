Feature: Get follower
  This feature file describes the behaviour of the REST-API for GET requests at the endpoint /api/users/{userId}/follower to get a list of follower of the specified user.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who follows the specified user and the http status-code will be 200
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the specified account to get a list of follower from belongs to a moderator, then the http response body contains an appropriate information about the mistake and the http response status-code is 400
  - If the request contains a valid token which belongs to a moderator, then the http status-code will be 403
  - If the specified user doesn´t exist, then the http response status-code will be 404


  Scenario: Get follower of an specified user
  Requesting a list of follower of a specified user

    Given the user "max" is authenticated
    And the user john with id 2 has two followers jane and lena
    When a client sends a GET "/users/2/follower" request for user "max" to get a list of follower from user john
    Then the client will receive the "OK" Status Code
    And the HTTP response body will contain following JSON with a list of users who follows the user john
        """
          [
              {
                  "userId": 3,
                  "firstName": "Jane",
                  "lastName": "Doe",
                  "role": "USER"
              },
              {
                  "userId": 6,
                  "firstName": "Lena",
                  "lastName": "Löchte",
                  "role": "USER"
              }
          ]
        """


  Scenario: Unauthorised request to get a list of follower from a specified user
  The request must contain a valid token of an user

    When a client sends a GET "/users/2/follower" request without a valid token to get a list of follower from user john
    Then the client will receive the "UNAUTHORIZED" Status Code



  Scenario: Account to get the list of follower from belongs to a moderator
  The account to get the list of follower from must be from an user

    Given the user "max" is authenticated
    When a client sends a GET "/users/4/follower" request for user "max" to get a list of follower from a moderator
    Then the client will receive the "BAD_REQUEST" Status Code
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "Account to get the list of follower from belongs to a moderator"
      }
      """

  Scenario: Transmitted token from the request to get a list of follower from a specified user belongs to a moderator
  Just users can request a list of follower from a specified user

    Given the moderator "werner" is authenticated
    When a client sends a GET "/users/2/follower" request for moderator "werner" to get a list of follower from user john
    Then the client will receive the "FORBIDDEN" Status Code


  Scenario: User to get the list of follower from doesn´t exist
  The user to get the list of follower from must be existing

    Given the user "max" is authenticated
    But there is no user with id 9999
    When a client sends a GET "/users/9999/follower" request for user "max" to get a list of the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code