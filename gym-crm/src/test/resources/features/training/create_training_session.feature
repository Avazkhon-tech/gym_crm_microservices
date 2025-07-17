Feature: Create a new training session

  Scenario: Successfully create a training session
    Given a training session with valid details
    When the client sends a POST request to "/trainings"
    Then the response status should be 200
