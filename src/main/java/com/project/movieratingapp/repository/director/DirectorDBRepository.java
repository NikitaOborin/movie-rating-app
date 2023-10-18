package com.project.movieratingapp.repository.director;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Director;
import com.project.movieratingapp.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class DirectorDBRepository implements DirectorRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getDirectors() {
        return jdbcTemplate.query("SELECT * FROM director", directorRowMapper);
    }

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "INSERT INTO director (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"director_id"});

            preparedStatement.setString(1, director.getName());

            return preparedStatement;
        }, keyHolder);

        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        int count = jdbcTemplate.update("UPDATE director SET name=? WHERE director_id=?",
                                        director.getName(), director.getId());

        if (count != 1) {
            throw new NotFoundException("director with id = " + director.getId() + " not found");
        }

        return director;
    }

    @Override
    public Director getDirectorById(Integer directorId) {
        Director director;
        List<Director> directors = jdbcTemplate.query("SELECT * FROM director WHERE director_id=?",
                                                      directorRowMapper, directorId);

        if (directors.isEmpty()) {
            throw new NotFoundException("director with id = " + directorId + " not found");
        }

        director = directors.get(0);

        return director;
    }

    @Override
    public void deleteDirectorById(Integer directorId) {
        jdbcTemplate.update("DELETE FROM director WHERE director_id=?", directorId);
    }

    @Override
    public List<Director> getDirectorsByFilmId(Long filmId) {
        return jdbcTemplate.query("SELECT * FROM director AS d " +
                                     "INNER JOIN film_director AS fd ON d.director_id = fd.director_id " +
                                 "WHERE fd.film_id=?",
                                  directorRowMapper, filmId);
    }

    @Override
    public void updateDirectorInDbByFilm(Film film) {
        deleteDirectorByFilmId(film.getId());

        List<Director> directors = film.getDirectors();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";

        for (Director director : directors) {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"film_director_id"});
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setInt(2, director.getId());

                return preparedStatement;
            }, keyHolder);
        }
    }

    @Override
    public void deleteDirectorByFilmId(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", filmId);
    }

    private RowMapper<Director> directorRowMapper = (rs, rowNum) -> {
        Director director = new Director();

        director.setId(rs.getInt("director.director_id"));
        director.setName(rs.getString("director.name"));

        return director;
    };
}