Feature: Get Trainee Profile

  Scenario: Successfully get trainee profile by username
    Given a trainer with username "olim.karimov" exists
    When the trainer sends a GET request to "/trainers/olim.karimov"
    Then the response status should be 200
    Then the trainer profile is not empty