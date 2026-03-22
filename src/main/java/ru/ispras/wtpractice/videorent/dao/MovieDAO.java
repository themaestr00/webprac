package ru.ispras.wtpractice.videorent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispras.wtpractice.videorent.entity.Movie;

import java.time.LocalDate;
import java.util.List;

public interface MovieDAO extends JpaRepository<Movie, Long> {
    // Фильтрация фильмов по названию или его части на странице фильмов
    List<Movie> findByNameContainingIgnoreCase(String name);

    // Фильтрация фильмов по имени режиссера или его части на странице фильмов
    List<Movie> findByDirectorContainingIgnoreCase(String director);

    // Фильтрация фильмов по имени компании или его части на странице фильмов
    List<Movie> findByCompanyContainingIgnoreCase(String company);

    // Фильтрация фильмов по дате выпуска на странице фильмов
    List<Movie> findByReleaseDateBetween(LocalDate releaseDateAfter, LocalDate releaseDateBefore);
}
