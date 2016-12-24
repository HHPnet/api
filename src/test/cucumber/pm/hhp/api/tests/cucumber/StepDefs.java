package pm.hhp.api.tests.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import gherkin.deps.com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.IdentityHashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:cucumber.xml", initializers = ConfigFileApplicationContextInitializer.class)
public class StepDefs {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  private IdentityHashMap<String, String> headers = new IdentityHashMap<>();

  private ResultActions response;

  @Before
  public void setup()  {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply(springSecurity())
            .build();
  }

  @After
  public void tearDown() {
    mockMvc = null;
    headers.clear();
  }

  protected ResultActions performWithHeaders(MockHttpServletRequestBuilder requestBuilder) throws Exception {
    return mockMvc.perform(setHeaders(requestBuilder));

  }

  private MockHttpServletRequestBuilder setBodyData(MockHttpServletRequestBuilder method, String data) throws JSONException {
    return method.contentType(MediaType.APPLICATION_JSON_VALUE).content(new JSONObject(data)
            .toString());
  }

  private MockHttpServletRequestBuilder setHeaders(MockHttpServletRequestBuilder method) throws JSONException {
    if (!headers.isEmpty()) {
      headers.entrySet().forEach(e -> {
        method.header(e.getKey(), e.getValue());
      });
    }

    return method;
  }

  @When("^sending a get request to \"([^\"]*)\"$")
  public void sendingAGetRequestTo(String url) throws Throwable {
    response = performWithHeaders(get(url));
  }

  @When("^sending a post request to \"([^\"]*)\" with given data:$")
  public void sendingAPostRequestToWithGivenData(String url, String data) throws Throwable {
    response = performWithHeaders(setBodyData(post(url), data));
  }

  @Then("^response status should be (\\d+)$")
  public void responseStatusShouldBe(int status) throws Throwable {
    assertThat(response.andReturn().getResponse().getStatus()).isEqualTo(status);
  }

  @Then("^the response content should contains:$")
  public void theResponseContentShouldContains(String expected) throws Throwable {
    String serverResponse = response.andReturn().getResponse().getContentAsString();
    try {
      JSONAssert.assertEquals(expected, serverResponse, false);
    } catch (Throwable t) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      fail(" JSON expected:\n" + expected
              + "\nJSON server response:\n" + gson.toJson(new JsonParser().parse(serverResponse)));
    }
  }

  @Given("^request headers:$")
  public void requestHeaders(String requestHeaders) throws Throwable {
    new JsonParser().parse(requestHeaders)
            .getAsJsonObject()
            .entrySet()
            .forEach(header -> headers.put(header.getKey(), header.getValue().toString().replace("\"", "")));
  }
}
