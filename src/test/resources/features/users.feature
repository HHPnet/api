Feature: Users resource in the API

  Scenario: Register user in the API
    Given request headers:
    """
    {
      "Authorization": "Bearer INVALID_USER"
    }
    """
    When sending a get request to "/me"
    Then response status should be 401