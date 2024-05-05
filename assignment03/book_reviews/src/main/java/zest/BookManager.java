package zest;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BookManager {

    private BookRatingsFetcher fetcher;

    public BookManager(BookRatingsFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public List<Book> highRatedBooks() {
        try {
            List<Book> allBooks = fetcher.all();
            return allBooks.stream()
                    .filter(book -> book.getRating() >= 4)
                    .collect(toList());
        } finally {
            fetcher.close();
        }
    }

    public List<String> uniqueAuthors(){
        try {
            List<Book> allBooks = fetcher.all();
            List<String> authors = new ArrayList<>();
            for (Book book : allBooks) {
                if (!authors.contains(book.getAuthor())) {
                    authors.add(book.getAuthor());
                }
            }
            return authors;
        } finally {
            fetcher.close();
        }
    }
}
