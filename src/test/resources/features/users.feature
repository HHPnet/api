Feature: Users resource in the API

  Scenario: Get user profile given and invalid token
    Given request headers:
    """
    {
      "Authorization": "Bearer INVALID_USER"
    }
    """
    When sending a get request to "/me"
    Then response status should be 401

  Scenario: Get user profile for a non existing user
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_NOTINDATABASE_NOTINDATABASE@EMAIL.COM"
    }
    """
    When sending a get request to "/me"
    Then response status should be 404

  Scenario: Get user profile for an existing user
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

  Scenario: Create an user that already exists
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_INDATABASE_INDATABASE@EMAIL.COM"
    }
    """
    And users information:
      | name       | email                |
      | indatabase | indatabase@email.com |
    When sending a post request to "/me" with given data:
    """
    {
      "name": "indatabase",
      "email": "indatabase@email.com"
    }
    """
    Then response status should be 409

  Scenario: Create an user that does not exist previously
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_NOTINDATABASE_NOTINDATABASE@EMAIL.COM"
    }
    """
    When sending a post request to "/me" with given data:
    """
    {
      "name": "notindatabase",
      "email": "notindatabase@email.com"
    }
    """
    Then response status should be 201
    And the response content should contains:
    """
    {
      "name": "notindatabase",
      "email": "notindatabase@email.com"
    }
    """

  Scenario: Create an user that does not exist previously using different email that given in the token
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_NOTINDATABASE_NOTINDATABASE@EMAIL.COM"
    }
    """
    When sending a post request to "/me" with given data:
    """
    {
      "name": "notindatabase",
      "email": "notindatabase1@email.com"
    }
    """
    Then response status should be 401

  Scenario: Update user information for a non existing user
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_NOTINDATABASE_NOTINDATABASE@EMAIL.COM"
    }
    """
    When sending a put request to "/me" with given data:
    """
    {
      "name": "notindatabase",
      "email": "notindatabase1@email.com"
    }
    """
    Then response status should be 401

  Scenario: Update user information for an existing user
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_INDATABASE_INDATABASE@EMAIL.COM"
    }
    """
    And users information:
      | name       | email                |
      | indatabase | indatabase@email.com |
    When sending a put request to "/me" with given data:
    """
    {
      "name": "indatabase_edit",
      "email": "indatabase@email.com"
    }
    """
    Then response status should be 202
    And the response content should contains:
    """
    {
      "name": "indatabase_edit",
      "email": "indatabase@email.com"
    }
    """

  Scenario: Update user information for an existing user using an already existing email
    Given request headers:
    """
    {
      "Authorization": "Bearer USERTOKEN_INDATABASE_INDATABASE@EMAIL.COM"
    }
    """
    And users information:
      | name        | email                 |
      | indatabase  | indatabase@email.com  |
      | indatabase1 | indatabase1@email.com |
    When sending a put request to "/me" with given data:
    """
    {
      "name": "indatabase_edit",
      "email": "indatabase1@email.com"
    }
    """
    Then response status should be 404