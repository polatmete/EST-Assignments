package zest;

public class MovieStreamingManager {
    private FileStreamService fileStreamService;
    private CacheService cacheService;

    // Constructor to inject the file stream and cache services
    public MovieStreamingManager(FileStreamService fileStreamService, CacheService cacheService) {
        this.fileStreamService = fileStreamService;
        this.cacheService = cacheService;
    }

    // Method to stream a movie by its ID
    public StreamingDetails streamMovie(String movieId) {
        validateMovieId(movieId);
        StreamingDetails details = cacheService.getDetails(movieId);
        if (details == null) {
            MovieMetadata metadata = fileStreamService.retrieveMovie(movieId);
            String streamToken = fileStreamService.generateToken(movieId);  // Assume there's a method to generate a streaming token
            details = new StreamingDetails(movieId, streamToken, metadata);
            cacheService.cacheDetails(movieId, details);
        }
        else if (!validateStreamingToken(movieId, details.getStreamToken())) {
            String newStreamToken = fileStreamService.generateToken(movieId);
            details =  new StreamingDetails(movieId, newStreamToken, details.getMetadata());
            cacheService.refreshCache(movieId, details);
        }
        return details;
    }

    // Additional methods can be added here for other functionalities
    // Updates movie information in the distributed file system and refreshes the cache.
    public void updateMovieMetadata(String movieId, MovieMetadata metadata) {
        validateMovieId(movieId);
        validateMovieMetadata(metadata);
        fileStreamService.updateMetadata(movieId, metadata);
        cacheService.refreshCache(movieId, metadata);
    }

    // Checks the validity of a token against file system records.
    private boolean validateStreamingToken(String movieId, String token) {
        return fileStreamService.validateToken(movieId, token);
    }

    // Checks the correctness of the provided ID.
    private void validateMovieId(String movieId) {
        if (movieId == null || movieId.isEmpty()) {
            throw new IllegalArgumentException("provided ID is invalid");
        }
        if (fileStreamService.retrieveMovie(movieId) == null) {
            throw new IllegalArgumentException("movie not found");
        }
    }

    private void validateMovieMetadata(MovieMetadata movieMetadata) {
        if (movieMetadata == null || movieMetadata.getTitle() == null ||
        movieMetadata.getDescription() == null || movieMetadata.getTitle().isEmpty() ||
        movieMetadata.getDescription().isEmpty()) {
            throw new IllegalArgumentException("invalid metadata provided");
        }
    }
}
