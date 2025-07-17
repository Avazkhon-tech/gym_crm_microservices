Feature:
  Scenario: Successfully update the trainee's trainers list
    Given a trainee with username "aziz.murodov" exists
    When the trainee sends a PATCH request to "/trainees/aziz.murodov?isActive=true"
    And the response status should be 200
