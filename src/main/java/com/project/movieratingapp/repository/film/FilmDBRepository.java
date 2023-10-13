package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Mpa;
import com.project.movieratingapp.repository.genre.GenreRepository;
import com.project.movieratingapp.repository.mpa.MpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class FilmDBRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public FilmDBRepository(JdbcTemplate jdbcTemplate, MpaRepository mpaRepository, GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                          "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder );

        genreRepository.updateGenreInDbForFilm(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";
        int i = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                                      film.getDuration(), film.getMpa().getId(), film.getId());

        if (i != 0) {
            genreRepository.updateGenreInDbForFilm(film);
        } else {
            throw new NotFoundException(film + " not found");
        }

        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM film", filmRowMapper);

        for (Film film : films) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
        }

        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film;
        List<Film> films = jdbcTemplate.query("SELECT * FROM film WHERE film_id=?", filmRowMapper, id);

        if (!films.isEmpty()) {
            film = films.get(0);
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
        } else {
            throw new NotFoundException("film with id = " + id + " not found");
        }

        return film;
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
