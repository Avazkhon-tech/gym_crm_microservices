Feature: User authentication

  Scenario: Successful password change
    Given a user "aziz.murodov" with current password "1234" and new password "123439982"
    When the user sends a change‑password request to "/auth/password"
    Then  the response status should be 200
    And the body should contain the message "OK"

  Scenario: Successful password change
    Given a user "aziz.murodov" with current password "in-correct-old-password" and new password "123439982"
    When the user sends a change‑password request to "/auth/password"
    Then  the response status should be 400
    And the body should contain the message "Old password is incorrect"

  Scenario: Successful password change
    Given a user "username" with current password "" and new password ""
    When the user sends a change‑password request to "/auth/password"
    Then  the response status should be 400
    And the body contains contains a list of errors