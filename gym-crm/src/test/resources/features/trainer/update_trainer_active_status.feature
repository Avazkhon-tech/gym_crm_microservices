Feature:
  Scenario: Successfully update the trainee's trainers list
    Given a trainer with username "olim.karimov" exists
    When the trainer sends a PATCH request to "/trainers/olim.karimov?isActive=true"
    And the response status should be 200
