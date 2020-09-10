package org.galatea.starter.entrypoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import org.apache.commons.collections4.IterableUtils;


/**
 * REST Controller that listens to http endpoints and allows the caller to send text to be
 * processed.
 */
@RequiredArgsConstructor
@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@RestController
public class AlphaVantageController extends BaseRestController {

  /**
   * Send the received text to the HalService to be processed and send the result out.
   */
  // @GetMapping to link http GET request to this method
  // @RequestParam to take a parameter from the url

  @GetMapping(value = "${webservice.alphavantagepath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ArrayList<ArrayList> alphaVantageEndpoint(
      @RequestParam(value = "stock") final String stock,
      @RequestParam(value = "days", defaultValue = "10") Integer numberOfDays) throws IOException{

    ResponseEntity<String> response = getAlphaVantageResponse(stock);

    JsonNode allJSONData = responseToJSON(response);

    JsonNode stockDataJSON = allJSONData.get("Time Series (Daily)");

    ArrayList<ArrayList> stockPriceDateResponse = formatStockJSON(stockDataJSON, numberOfDays);

    return stockPriceDateResponse;

  }

  private ResponseEntity<JsonNode> getAlphaVantageResponse(String stock){

    //    Create REST call for Alphavantage
    RestTemplate restTemplate = new RestTemplate();
    final String alphaVantageUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&symbol=";
    final String alphaAPI = "&apikey=randomapikey";

//    GET Instance (Actual Call) to Alphavantage with Stock Data
    ResponseEntity<JsonNode> response
        = restTemplate.getForEntity(alphaVantageUrl + stock + alphaAPI, JsonNode.class);

    return response;

  }

  private JsonNode responseToJSON(ResponseEntity<String> response) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode allJSONData = mapper.readTree(response.getBody());

    return allJSONData;
  }

  private ArrayList<ArrayList> formatStockJSON(JsonNode stockDataJSON, Integer numberOfDays){

    //    Get all stock dates
    Iterator<Map.Entry<String, JsonNode>> dates = stockDataJSON.fields();
    ArrayList<ArrayList> marketDates = new ArrayList<>();

//    Find Number of Stock Dates
    Integer datesLength = IterableUtils.size(stockDataJSON);

    if (numberOfDays > datesLength){
      numberOfDays = datesLength;
    }

//    Limit stock dates to numberOfDays
    for (int i = 0; i < numberOfDays; i++) {
      ArrayList<String> stockDateTuple = new ArrayList<>();
      Map.Entry<String, JsonNode> date = dates.next();
      String   fieldName  = date.getKey();
      String stockPrice = stockDataJSON.get(fieldName).get("4. close").asText();
      stockDateTuple.add(fieldName);
      stockDateTuple.add(stockPrice);
      marketDates.add(stockDateTuple);
    }

    return marketDates;
  }


}
