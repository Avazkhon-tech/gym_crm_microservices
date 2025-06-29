Feature: Update trainee profile

  Scenario: Trainee updates their profile successfully
    Given a trainee to update with username "crazy.man" exists
    When I update the profile with new details
    Then the response status is 200
    And the response contains updated info
