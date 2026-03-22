package ru.ispras.wtpractice.videorent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispras.wtpractice.videorent.entity.Exemplar;

import java.util.List;

public interface ExemplarDAO extends JpaRepository<Exemplar, Long> {
    // Просмотр экземпляров в карточке фильма
    List<Exemplar> findByMovieId(Long movieId);

    // Фильтр по названию носителя
    List<Exemplar> findByMediaTypeNameContainingIgnoreCase(String mediaTypeName);

    // Фильтрация только по доступным экземплярам
    List<Exemplar> findByAvailableIsTrue();

    // Число экземпляров фильма
    long countByMovieId(Long movieId);

    // Число доступных экземпляров
    long countByMovieIdAndAvailableIsTrue(Long movieId);
}
