package com.eduardo.cinema_app.util;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import com.eduardo.cinema_app.enums.Genre;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MovieTestUtil {

    public static Long MOVIE_ID = 1L;
    public static String TITLE = "Spider man";
    public static String DESCRIPTION = "Filmaço do spider";
    public static Genre GENRE = Genre.ACAO;
    public static String POSTER_URL = "imagem-spider-man.jpg";


    public static Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(MOVIE_ID);
        movie.setTitle(TITLE);
        movie.setDescription(DESCRIPTION);
        movie.setGenre(GENRE);
        movie.setPosterUrl(POSTER_URL);
        return movie;
    }

    public static MovieRequestDTO createMovieRequestDTO() {
        return new MovieRequestDTO(TITLE,DESCRIPTION,GENRE, createMockMultipartFile());
    }

    public static MovieResponseDTO createMovieResponseDTO() {
        return new MovieResponseDTO(MOVIE_ID,TITLE,DESCRIPTION,GENRE,POSTER_URL);
    }

    public static MultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
                "image",
                "spider-man.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteúdo fake da imagem".getBytes()
        );
    }
}
