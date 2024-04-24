package zest;

import java.util.List;

public class BookRatingsFetcher {
    private DatabaseConnection dbConnection;

    public BookRatingsFetcher(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Book> all() {
    // This would normally fetch data from a database...The implementation is skipped.
    // ...
        return null;
    }

    public void close() {
        dbConnection.close();
    }
}
