Feature: User authentication

  Scenario: Successful login
    Given a user "aziz.murodov" with password "1234"
    When  the user posts valid credentials to "/auth/login"
    Then  the response status is 200
    And the body contains a non‑blank JWT and refresh token

  Scenario: Unsuccessful login
    Given a user "aziz.murodov" with password "1234"
    When the user posts invalid credentials to "/auth/login"
    Then the response status is 401
    And the body contains contains a message "Username or password is incorrect"