package ApiTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResponseObjectListMatcher {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> ResultMatcher containsObjectAsJson(
            ArrayList<T> expectedList) {
        return mvcResult -> {

            String json = mvcResult.getResponse().getContentAsString();
            String actual = objectMapper.writeValueAsString(expectedList);
            assertEquals(json, actual);

        };
    }

    static ResponseObjectListMatcher responseListBody() {
        return new ResponseObjectListMatcher();
    }

}
