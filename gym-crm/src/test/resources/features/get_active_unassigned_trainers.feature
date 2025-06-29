Feature: Get active unassigned trainers for a trainee

  Scenario: Trainee requests list of active unassigned trainers
    Given I am a trainee with username "aziz.murodov"
    When I want to get unassigned trainers for a trainee from "/trainees/aziz.murodov/unassigned-trainers"
    Then the response status is 200
    And the response contains a list of unassigned trainers
