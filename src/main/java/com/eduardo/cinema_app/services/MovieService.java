package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.dtos.response.MovieResponseDTO;
import com.eduardo.cinema_app.enums.Genre;
import com.eduardo.cinema_app.mappers.MovieMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final S3StorageService s3StorageService;
    private final MovieMapper mapper;

    public MovieService(MovieRepository movieRepository, S3StorageService s3StorageService, MovieMapper mapper) {
        this.movieRepository = movieRepository;
        this.s3StorageService = s3StorageService;
        this.mapper = mapper;
    }

    @Transactional
    public void createMovie(MovieRequestDTO dto, MultipartFile image) {
        String imgUrl = "";

        if (dto.image() != null && !dto.image().isEmpty()) {
            imgUrl = s3StorageService.uploadImg(image);
        }

        Movie movie = mapper.toDomain(dto, imgUrl);
        movieRepository.save(movie);
    }

    public Page<MovieResponseDTO> findMoviesWithFilters(String title, Genre genre, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Movie> moviePage = movieRepository.findAllWithFilters(title, genre, pageRequest);
        return moviePage.map(mapper::toDto);
    }

    public MovieResponseDTO findById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));
        return mapper.toDto(movie);
    }

    public MovieResponseDTO updateMovie(Long id, MovieRequestDTO requestDTO, MultipartFile image) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));

        movie.setTitle(requestDTO.title());
        movie.setDescription(requestDTO.description());
        movie.setGenre(requestDTO.genre());

        String newImageUrl = s3StorageService.uploadImg(image);
        movie.setPosterUrl(newImageUrl);

        movieRepository.save(movie);
        return mapper.toDto(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));

        String filename = movie.getPosterUrl().substring(movie.getPosterUrl().lastIndexOf("/") + 1);
        s3StorageService.deleteFile(filename);

        movieRepository.delete(movie);
    }


}
