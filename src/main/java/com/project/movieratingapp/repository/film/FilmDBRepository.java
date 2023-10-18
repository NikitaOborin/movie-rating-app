package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Mpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class FilmDBRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM film", filmRowMapper);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                          "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder );

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";
        int countRow =  jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                                   film.getDuration(), film.getMpa().getId(), film.getId());

        if (countRow != 1) {
            throw new NotFoundException("film with id = " + film.getId() + " not found");
        }

        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        Film film;
        List<Film> films = jdbcTemplate.query("SELECT * FROM film WHERE film_id=?", filmRowMapper, filmId);

        if (!films.isEmpty()) {
            film = films.get(0);
        } else {
            throw new NotFoundException("film with id = " + filmId + " not found");
        }

        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
                          "FROM film AS f " +
                              "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                          "GROUP BY f.film_id " +
                          "ORDER BY COUNT(l.user_id) DESC " +
                          "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, filmRowMapper, count);
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortByYear(Integer directorId) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
                          "FROM film AS f " +
                              "INNER JOIN film_director AS fd ON f.film_id = fd.film_id " +
                          "WHERE fd.director_id=? " +
                          "ORDER BY EXTRACT(YEAR FROM f.release_date)";

        return jdbcTemplate.query(sqlQuery, filmRowMapper, directorId);
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortByLikes(Integer directorId) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
                          "FROM film AS f " +
                              "INNER JOIN film_director AS fd ON f.film_id = fd.film_id " +
                              "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                          "WHERE fd.director_id=? " +
                          "GROUP BY f.film_id " +
                          "ORDER BY COUNT(l.user_id) DESC";

        return jdbcTemplate.query(sqlQuery, filmRowMapper, directorId);
    }

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        Mpa mpa = new Mpa();

        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        mpa.setId(rs.getInt("mpa_id"));
        film.setMpa(mpa);

        return film;
    };
}