package zest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookReviewsTest {

    private BookManager getBookManagerWithStubbedFetcher(List<Book> listOfBooks){
        BookRatingsFetcher fetcher = mock(BookRatingsFetcher.class);
        when(fetcher.all()).thenReturn(listOfBooks);
        return new BookManager(fetcher);
    }

    @Test
    public void returnHighRatedBooksOnly(){
        Book book1 = new Book("Book1", 3, "Sandra");
        Book book2 = new Book("Book2", 4, "Emily");
        Book book3 = new Book("Book3", 5, "Josh");
        List<Book> listOfBooks = Arrays.asList(book1, book2, book3);

        BookManager bookManager = getBookManagerWithStubbedFetcher(listOfBooks);

        assertThat(bookManager.highRatedBooks()).containsExactlyInAnyOrder(book2, book3);
    }

    @Test
    public void emptyBookRatings(){
        List<Book> listOfBooks = List.of();
        BookManager bookManager = getBookManagerWithStubbedFetcher(listOfBooks);

        assertThat(bookManager.highRatedBooks()).isEmpty();
    }

    @Test
    public void returnUniqueAuthors(){
        Book book1 = new Book("Book1", 3, "Sandra");
        Book book2 = new Book("Book2", 4, "Emily");
        Book book3 = new Book("Book3", 5, "Josh");
        List<Book> listOfBooks = Arrays.asList(book1, book2, book3);

        BookManager bookManager = getBookManagerWithStubbedFetcher(listOfBooks);

        assertThat(bookManager.uniqueAuthors()).containsExactlyInAnyOrder(book1.getAuthor(), book2.getAuthor(), book3.getAuthor());
    }

    @Test
    public void emptyAuthors(){
        List<Book> listOfBooks = List.of();
        BookManager bookManager = getBookManagerWithStubbedFetcher(listOfBooks);

        assertThat(bookManager.uniqueAuthors()).isEmpty();
    }
}


