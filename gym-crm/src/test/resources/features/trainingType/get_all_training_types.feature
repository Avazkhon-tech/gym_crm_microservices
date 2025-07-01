Feature: Retrieve all training types

  Scenario: user requests the list of training types
    When I send a GET request to fetch training types from "/training-types"
    Then the response status should be 200
    And the response contains training types
