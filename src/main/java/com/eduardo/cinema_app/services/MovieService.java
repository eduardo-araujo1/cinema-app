package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.Movie;
import com.eduardo.cinema_app.dtos.request.MovieRequestDTO;
import com.eduardo.cinema_app.mappers.MovieMapper;
import com.eduardo.cinema_app.repositories.MovieRepository;
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


}
