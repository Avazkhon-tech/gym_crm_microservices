Feature:
  Scenario: Successfully update the trainee's trainers list
    Given a trainee with username "aziz.murodov" exists
    When the trainee sends a GET request to "/trainees/aziz.murodov/unassigned-trainers"
    And the response contains a list of unassigned trainers
    And the response status should be 200
    Given a list of unassigned trainers for the trainee exists
    When the trainee sends a PUT request to "/trainees/aziz.murodov/trainers"
    And the response status should be 200
