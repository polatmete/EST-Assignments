package zest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MovieStreamingManagerTest {
    private FileStreamService mockFileStreamService;
    private CacheService mockCacheService;
    private MovieStreamingManager movieStreamingManager;
    private MovieMetadata testMovieMetadata;
    private StreamingDetails testStreamingDetails;
    private static final String testMovieId = "movie-id";

    @BeforeEach
    void init() {
        mockFileStreamService = Mockito.mock(FileStreamService.class);
        mockCacheService = Mockito.mock(CacheService.class);
        movieStreamingManager = new MovieStreamingManager(mockFileStreamService, mockCacheService);
        testMovieMetadata = new MovieMetadata("movie-title", "movie-description");
        testStreamingDetails = new StreamingDetails(testMovieId, "streaming-token", testMovieMetadata);
    }

    @Test
    void streamMovieNoCache() {
        // define return values
        when(mockCacheService.getDetails(testMovieId)).thenReturn(null);
        when(mockFileStreamService.retrieveMovie(testMovieId)).thenReturn(testMovieMetadata);
        when(mockFileStreamService.generateToken(testMovieId)).thenReturn(testStreamingDetails.getStreamToken());
        // call method
        StreamingDetails details = movieStreamingManager.streamMovie(testMovieId);
        // verify results
        verify(mockCacheService, times(1)).getDetails(testMovieId);
        verify(mockFileStreamService, times(1)).retrieveMovie(testMovieId);
        verify(mockFileStreamService, times(1)).generateToken(testMovieId);
        verify(mockCacheService, times(1)).cacheDetails(testMovieId, testStreamingDetails);
        assertEquals(testStreamingDetails, details);
    }

    @Test
    void streamMovieWithCache() {
        // define return values
        when(mockCacheService.getDetails(testMovieId)).thenReturn(testStreamingDetails);
        // call method
        StreamingDetails details = movieStreamingManager.streamMovie(testMovieId);
        // verify results
        verify(mockCacheService, times(1)).getDetails(testMovieId);
        verify(mockFileStreamService, never()).retrieveMovie(testMovieId);
        verify(mockFileStreamService, never()).generateToken(testMovieId);
        verify(mockCacheService, never()).cacheDetails(testMovieId, testStreamingDetails);
        assertEquals(testStreamingDetails, details);
    }
}
