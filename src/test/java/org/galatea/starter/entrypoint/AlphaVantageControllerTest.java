package org.galatea.starter.entrypoint;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import junitparams.JUnitParamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.ASpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@RequiredArgsConstructor
@Slf4j
// We don't load the entire spring application context for this test.
@WebMvcTest(AlphaVantageController.class)
// Use this runner since we want to parameterize certain tests.
// See runner's javadoc for more usage.
@RunWith(JUnitParamsRunner.class)
public class AlphaVantageControllerTest extends ASpringTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testAlphaVantageEndpoint() throws Exception {
    String stock = "TSLA";
    String stockLabel = "stock";

//    given(this.mockHalService.processText(paramVal)).willReturn(result);

    this.mvc.perform(
        get("/avp").param(stockLabel, stock).accept(MediaType.APPLICATION_JSON_VALUE));
  }

}
