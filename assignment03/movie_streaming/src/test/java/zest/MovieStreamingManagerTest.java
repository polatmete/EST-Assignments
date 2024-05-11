package zest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.refEq;

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
    void nullMovieId() {
        assertThrows(IllegalArgumentException.class, () -> movieStreamingManager.streamMovie(null));
        verify(mockFileStreamService, never()).retrieveMovie(any());
        verify(mockCacheService, never()).getDetails(any());
        verify(mockFileStreamService, never()).generateToken(any());
        verify(mockCacheService, never()).cacheDetails(any(), any());
        verify(mockFileStreamService, never()).validateToken(any(), any());
        verify(mockCacheService, never()).refreshCache(any(), (StreamingDetails) any());
    }

    @Test
    void emptyMovieId() {
        assertThrows(IllegalArgumentException.class, () -> movieStreamingManager.streamMovie(""));
        verify(mockFileStreamService, never()).retrieveMovie(any());
        verify(mockCacheService, never()).getDetails(any());
        verify(mockFileStreamService, never()).generateToken(any());
        verify(mockCacheService, never()).cacheDetails(any(), any());
        verify(mockFileStreamService, never()).validateToken(any(), any());
        verify(mockCacheService, never()).refreshCache(any(), (StreamingDetails) any());
    }

    @Test
    void inexistentMovieId() {
        // define return values
        when(mockFileStreamService.retrieveMovie(testMovieId)).thenReturn(null);
        // verify results
        assertThrows(IllegalArgumentException.class, () -> movieStreamingManager.streamMovie(testMovieId));
        verify(mockFileStreamService, times(1)).retrieveMovie(testMovieId);
        verify(mockCacheService, never()).getDetails(any());
        verify(mockFileStreamService, never()).generateToken(any());
        verify(mockCacheService, never()).cacheDetails(any(), any());
        verify(mockFileStreamService, never()).validateToken(any(), any());
        verify(mockCacheService, never()).refreshCache(any(), (StreamingDetails) any());
    }

    @Test
    void streamMovieNoCache() {
        // define return values
        when(mockFileStreamService.retrieveMovie(testMovieId)).thenReturn(testMovieMetadata);
        when(mockCacheService.getDetails(testMovieId)).thenReturn(null);
        when(mockFileStreamService.generateToken(testMovieId)).thenReturn(testStreamingDetails.getStreamToken());
        // call method
        StreamingDetails details = movieStreamingManager.streamMovie(testMovieId);
        // verify results
        verify(mockFileStreamService, times(2)).retrieveMovie(testMovieId);
        verify(mockCacheService, times(1)).getDetails(testMovieId);
        verify(mockFileStreamService, times(1)).generateToken(testMovieId);
        verify(mockCacheService, times(1)).cacheDetails(eq(testMovieId), refEq(testStreamingDetails));
        verify(mockFileStreamService, never()).validateToken(any(), any());
        verify(mockCacheService, never()).refreshCache(any(), (StreamingDetails) any());
        assertThat(testStreamingDetails).usingRecursiveComparison().isEqualTo(details);
    }

    @Test
    void streamMovieWithCacheInvalidToken() {
        // define variables
        StreamingDetails testInvalidStreamingDetails = new StreamingDetails(testMovieId, "invalid-streaming-token", testMovieMetadata);
        // define return values
        when(mockFileStreamService.retrieveMovie(testMovieId)).thenReturn(testMovieMetadata);
        when(mockCacheService.getDetails(testMovieId)).thenReturn(testInvalidStreamingDetails);
        when(mockFileStreamService.validateToken(testMovieId, testInvalidStreamingDetails.getStreamToken())).thenReturn(false);
        when(mockFileStreamService.generateToken(testMovieId)).thenReturn(testStreamingDetails.getStreamToken());
        // call method
        StreamingDetails details = movieStreamingManager.streamMovie(testMovieId);
        // verify results
        verify(mockFileStreamService, times(1)).retrieveMovie(testMovieId);
        verify(mockCacheService, times(1)).getDetails(testMovieId);
        verify(mockFileStreamService, times(1)).generateToken(testMovieId);
        verify(mockCacheService, never()).cacheDetails(eq(testMovieId), refEq(testStreamingDetails));
        verify(mockFileStreamService, times(1)).validateToken(testMovieId, testInvalidStreamingDetails.getStreamToken());
        verify(mockCacheService, times(1)).refreshCache(eq(testMovieId), refEq(testStreamingDetails));
        assertThat(testStreamingDetails).usingRecursiveComparison().isEqualTo(details);
    }

    @Test
    void streamMovieWithCacheValidToken() {
        // define return values
        when(mockFileStreamService.retrieveMovie(testMovieId)).thenReturn(testMovieMetadata);
        when(mockCacheService.getDetails(testMovieId)).thenReturn(testStreamingDetails);
        when(mockFileStreamService.validateToken(testMovieId, testStreamingDetails.getStreamToken())).thenReturn(true);
        // call method
        StreamingDetails details = movieStreamingManager.streamMovie(testMovieId);
        // verify results
        verify(mockFileStreamService, times(1)).retrieveMovie(testMovieId);
        verify(mockCacheService, times(1)).getDetails(testMovieId);
        verify(mockFileStreamService, never()).generateToken(testMovieId);
        verify(mockCacheService, never()).cacheDetails(eq(testMovieId), refEq(testStreamingDetails));
        verify(mockFileStreamService, times(1)).validateToken(testMovieId, testStreamingDetails.getStreamToken());
        verify(mockCacheService, never()).refreshCache(eq(testMovieId), refEq(testStreamingDetails));
        assertThat(testStreamingDetails).usingRecursiveComparison().isEqualTo(details);
    }
}
