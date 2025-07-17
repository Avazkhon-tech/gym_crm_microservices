Feature:
  Scenario:
    Given a user "aziz.murodov" with password "1234"
    When the user posts credentials to "/auth/login"
    And the body contains a non‑blank JWT and refresh token
    And the response status should be 200
    When the user "aziz.murodov" posts the refreshToken to "/auth/refresh-token"
    And the response status should be 200
    And the body contains a non‑blank JWT and refresh token