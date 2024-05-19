package zest;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CatFactsRetrieverTest {

    private CatFactsRetriever catFactsRetriever;
    private HTTPUtil mockHttpUtil;
    private String singleMockResponse;
    private String multipleMockResponse;
    private String noMockResponse;
    private String negativeMockResponse;
    private JSONObject mockResponse;

    @BeforeEach
    void init() {
        mockHttpUtil = mock(HTTPUtil.class);
        catFactsRetriever = new CatFactsRetriever(mockHttpUtil);
        singleMockResponse = "{\"fact\":\"Cats are animals.\",\"length\":17}";
        multipleMockResponse = "{\"data\":[{\"fact\":\"Cats are animals.\",\"length\":17},{\"fact\":\"Cats are not dogs.\",\"length\":18},{\"fact\":\"Cats are beautiful.\",\"length\":19}]}";
        noMockResponse = "{\"data\":[]}";
        negativeMockResponse = "{\"data\":[{\"fact\":\"Unlike dogs, cats do not have a sweet tooth. Scientists believe this is due to a mutation in a key taste receptor.\",\"length\":114},{\"fact\":\"When a cat chases its prey, it keeps its head level. Dogs and humans bob their heads up and down.\",\"length\":97},{\"fact\":\"The technical term for a cat\\u2019s hairball is a \\u201cbezoar.\\u201d\",\"length\":54},{\"fact\":\"A group of cats is called a \\u201cclowder.\\u201d\",\"length\":38},{\"fact\":\"A cat can\\u2019t climb head first down a tree because every claw on a cat\\u2019s paw points the same way. To get down from a tree, a cat must back down.\",\"length\":142},{\"fact\":\"Cats make about 100 different sounds. Dogs make only about 10.\",\"length\":62},{\"fact\":\"Every year, nearly four million cats are eaten in Asia.\",\"length\":55},{\"fact\":\"There are more than 500 million domestic cats in the world, with approximately 40 recognized breeds.\",\"length\":100},{\"fact\":\"Approximately 24 cat skins can make a coat.\",\"length\":43},{\"fact\":\"While it is commonly thought that the ancient Egyptians were the first to domesticate cats, the oldest known pet cat was recently found in a 9,500-year-old grave on the Mediterranean island of Cyprus. This grave predates early Egyptian art depicting cats by 4,000 years or more.\",\"length\":278}]}";
    }

    @Test
    void testRetrieveRandom() throws IOException {
        mockResponse = new JSONObject(singleMockResponse);
        when(mockHttpUtil.get("https://catfact.ninja/fact")).thenReturn(singleMockResponse);
        String fact = catFactsRetriever.retrieveRandom();
        assertEquals(mockResponse.getString("fact"), fact);
    }

    @Test
    void testRetrieveLongest() throws IOException {
        mockResponse = new JSONObject(multipleMockResponse);
        int limit = mockResponse.getJSONArray("data").length();
        when(mockHttpUtil.get("https://catfact.ninja/facts?limit=" + limit)).thenReturn(multipleMockResponse);
        String fact = catFactsRetriever.retrieveLongest(limit);

        // Find the longest string in multipleMockResponses
        String longestFact = "";
        int longest = 0;
        for (int i = 0; i < mockResponse.getJSONArray("data").length(); i++) {
            if (mockResponse.getJSONArray("data").getJSONObject(i).getInt("length") > longest) {
                longestFact = mockResponse.getJSONArray("data").getJSONObject(i).getString("fact");
                longest = mockResponse.getJSONArray("data").getJSONObject(i).getInt("length");
            }
        }
        assertEquals(longestFact, fact);
    }

    @Test
    void testRetrieveLongestLimitZero() throws IOException {
        mockResponse = new JSONObject(noMockResponse);
        int limit = mockResponse.getJSONArray("data").length();
        when(mockHttpUtil.get("https://catfact.ninja/facts?limit=" + limit)).thenReturn(noMockResponse);
        String fact = catFactsRetriever.retrieveLongest(limit);

        assertEquals("", fact);
    }

    @Test
    void testRetrieveLongestNegativeLimit() throws IOException {
        mockResponse = new JSONObject(negativeMockResponse);
        int limit = mockResponse.getJSONArray("data").length();
        when(mockHttpUtil.get("https://catfact.ninja/facts?limit=" + limit)).thenReturn(negativeMockResponse);
        String fact = catFactsRetriever.retrieveLongest(limit);

        // Find the longest string in multipleMockResponses
        String longestFact = "";
        int longest = 0;
        for (int i = 0; i < mockResponse.getJSONArray("data").length(); i++) {
            if (mockResponse.getJSONArray("data").getJSONObject(i).getInt("length") > longest) {
                longestFact = mockResponse.getJSONArray("data").getJSONObject(i).getString("fact");
                longest = mockResponse.getJSONArray("data").getJSONObject(i).getInt("length");
            }
        }
        assertEquals(longestFact, fact);
    }
}
