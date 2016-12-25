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

    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_NOTINDATABASE_NOTINDATABASE@EMAIL.COM"
    }
    """
    When sending a get request to "/me"
    Then response status should be 404

    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_INDATABASE_INDATABASE@EMAIL.COM"
    }
    """
    And users information:
      | name       | email                |
      | indatabase | indatabase@email.com |
    When sending a get request to "/me"
    Then response status should be 200
    And the response content should contains:
    """
    {
      "name": "indatabase",
      "email": "indatabase@email.com"
    }
    """
