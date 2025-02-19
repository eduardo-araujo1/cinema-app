package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.domain.Session;
import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import com.eduardo.cinema_app.dtos.response.MovieSessionDTO;
import com.eduardo.cinema_app.dtos.response.MovieWithSessionsDTO;
import com.eduardo.cinema_app.enums.Genre;
import com.eduardo.cinema_app.exceptions.MovieNotFoundException;
import com.eduardo.cinema_app.mappers.MovieMapper;
import com.eduardo.cinema_app.mappers.SessionMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
import com.eduardo.cinema_app.repositories.SessionRepository;
import com.eduardo.cinema_app.services.MovieService;
import com.eduardo.cinema_app.services.S3StorageService;
import com.eduardo.cinema_app.util.MovieTestUtil;
import com.eduardo.cinema_app.util.SessionTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private S3StorageService s3StorageService;

    @InjectMocks
    private MovieService movieService;

    Movie movieEntity = MovieTestUtil.createMovie();
    MovieRequestDTO movieRequestDTO = MovieTestUtil.createMovieRequestDTO();
    MovieResponseDTO movieResponseDTO = MovieTestUtil.createMovieResponseDTO();

    @Test
    void createMovie_Success() {
        MovieRequestDTO movieRequestDTO = MovieTestUtil.createMovieRequestDTO();
        MultipartFile mockImage = movieRequestDTO.image();
        String expectedImageUrl = "https://s3.amazonaws.com/fake-bucket/spider-man.jpg";
        Movie mappedMovie = MovieTestUtil.createMovie();

        when(s3StorageService.uploadImg(mockImage)).thenReturn(expectedImageUrl);
        when(movieMapper.toDomain(movieRequestDTO, expectedImageUrl)).thenReturn(mappedMovie);
        when(movieRepository.save(any(Movie.class))).thenReturn(mappedMovie);

        movieService.createMovie(movieRequestDTO, mockImage);

        verify(s3StorageService, times(1)).uploadImg(mockImage);
        verify(movieMapper, times(1)).toDomain(movieRequestDTO, expectedImageUrl);
        verify(movieRepository, times(1)).save(mappedMovie);
    }

    @Test
    void findMoviesWithFilters_Success() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Movie movie = MovieTestUtil.createMovie();
        MovieResponseDTO movieResponseDTO = MovieTestUtil.createMovieResponseDTO();
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));

        when(movieRepository.findAllWithFilters(anyString(), any(), eq(pageRequest))).thenReturn(moviePage);
        when(movieMapper.toDto(any(Movie.class))).thenReturn(movieResponseDTO);

        Page<MovieResponseDTO> result = movieService.findMoviesWithFilters("Spider man", Genre.ACAO, page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Spider man", result.getContent().getFirst().title());

        verify(movieRepository, times(1)).findAllWithFilters(anyString(), any(), eq(pageRequest));
        verify(movieMapper, times(1)).toDto(any(Movie.class));
    }

    @Test
    void findMovieWithSessions_Success() {
        Long movieId = 1L;
        Movie movie = MovieTestUtil.createMovie();
        List<Session> sessions = List.of(SessionTestUtil.createSession());
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(sessionRepository.findUpcomingSessionsByMovieId(eq(movieId), any(LocalDateTime.class)))
                .thenReturn(sessions);

        when(sessionMapper.toMovieSessionDTO(any(Session.class))).thenAnswer(invocation -> {
            Session session = invocation.getArgument(0);
            return new MovieSessionDTO(
                    session.getId(),
                    session.getStartTime(),
                    session.getEndTime()
            );
        });

        MovieWithSessionsDTO result = movieService.findMovieWithSessions(movieId);

        assertNotNull(result);
        assertEquals(movie.getId(), result.id());
        assertEquals(movie.getTitle(), result.title());
        assertEquals(movie.getDescription(), result.description());
        assertEquals(movie.getGenre(), result.genre());
        assertEquals(movie.getPosterUrl(), result.posterUrl());
        assertEquals(sessions.size(), result.sessions().size());

        verify(movieRepository, times(1)).findById(movieId);
        verify(sessionRepository, times(1)).findUpcomingSessionsByMovieId(eq(movieId), any(LocalDateTime.class));
        verify(sessionMapper, times(sessions.size())).toMovieSessionDTO(any(Session.class));
    }

    @Test
    void findMovieWithSessions_MovieNotFound() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                () -> movieService.findMovieWithSessions(movieId));

        assertEquals("Filme com o id" + movieId + " não encontrado.", exception.getMessage());

        verify(movieRepository, times(1)).findById(movieId);
        verifyNoInteractions(sessionRepository);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    void updateMovie_Success() {
        Long movieId = 1L;
        Movie existingMovie = MovieTestUtil.createMovie();
        MovieRequestDTO requestDTO = new MovieRequestDTO("Novo Título", "Nova descrição", Genre.ACAO, null);
        MultipartFile mockImage = mock(MultipartFile.class);
        String expectedImageUrl = "https://s3.amazonaws.com/fake-bucket/nova-imagem.jpg";

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(s3StorageService.uploadImg(mockImage)).thenReturn(expectedImageUrl);
        when(movieMapper.toDto(existingMovie)).thenReturn(new MovieResponseDTO(existingMovie.getId(), requestDTO.title(), requestDTO.description(), requestDTO.genre(), expectedImageUrl));

        MovieResponseDTO result = movieService.updateMovie(movieId, requestDTO, mockImage);

        assertNotNull(result);
        assertEquals(requestDTO.title(), result.title());
        assertEquals(requestDTO.description(), result.description());
        assertEquals(requestDTO.genre(), result.genre());
        assertEquals(expectedImageUrl, result.posterUrl());
    }

    @Test
    void updateMovie_MovieNotFound() {
        Long movieId = 1L;
        MovieRequestDTO requestDTO = new MovieRequestDTO("Título", "Descrição", Genre.ACAO, null);
        MultipartFile mockImage = mock(MultipartFile.class);

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class, () ->
                movieService.updateMovie(movieId, requestDTO, mockImage)
        );

        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void deleteMovie_Success() {
        Long movieId = 1L;
        Movie existingMovie = MovieTestUtil.createMovie();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        String filename = existingMovie.getPosterUrl().substring(existingMovie.getPosterUrl().lastIndexOf("/") + 1);
        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).findById(movieId);
        verify(s3StorageService, times(1)).deleteFile(filename);
        verify(movieRepository, times(1)).delete(existingMovie);
    }

    @Test
    void deleteMovie_NotFound() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(movieId));

        verify(movieRepository, times(1)).findById(movieId);
        verify(s3StorageService, times(0)).deleteFile(anyString());
        verify(movieRepository, times(0)).delete(any());
    }



}
