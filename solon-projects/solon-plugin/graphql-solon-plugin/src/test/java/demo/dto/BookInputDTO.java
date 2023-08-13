package demo.dto;

import demo.entity.BookEntity;

public class BookInputDTO extends BookEntity {

    private AuthorInputDTO author;

    public AuthorInputDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInputDTO author) {
        this.author = author;
    }
}
