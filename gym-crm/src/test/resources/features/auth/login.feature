Feature: User authentication

  Scenario: Successful login
    Given a user "aziz.murodov" with password "1234"
    When  the user posts credentials to "/auth/login"
    Then  the response status should be 200
    And the body contains a non‑blank JWT and refresh token

  Scenario: Unsuccessful login with wrong password
    Given a user "aziz.murodov" with password "wrong-password"
    When the user posts credentials to "/auth/login"
    Then the response status should be 401
    And the body should contain the message "Username or password is incorrect"

  Scenario: Unsuccessful login with non-existing username
    Given a user "non-existing user" with password "password"
    When the user posts credentials to "/auth/login"
    Then the response status should be 401
    And the body should contain the message "Username or password is incorrect"

  Scenario: Unsuccessful login with invalid empty fields
    Given a user "" with password ""
    When the user posts credentials to "/auth/login"
    Then the response status should be 400
    And the body contains contains a list of errors