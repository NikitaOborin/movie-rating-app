package com.project.movieratingapp.repository.film;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        updateGenresByQuery(film);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        setMpaForCurrentFilm(film);
        setGenreForCurrentFilm(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                                      film.getDuration(), film.getMpa().getId(), film.getId());

        updateGenresByQuery(film);
        setMpaForCurrentFilm(film);
        setGenreForCurrentFilm(film);

        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM film", filmRowMapper);

        // вытащить жанры из БД и добавить в энтити
        List<Genre> genres = new ArrayList<>();

        for (Film film : films) {
            setMpaForCurrentFilm(film);
            setGenreForCurrentFilm(film);
        }

        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        return null;
    }

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        film.setMpa(mpa);

        return film;
    };

    private void updateGenresByQuery(Film film) {
        for (Genre genre : film.getGenres()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int genre_id = genre.getId();
            String sqlQueryGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQueryGenre, new String[]{"film_genre_id"});
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setInt(2, genre_id);
                return preparedStatement;
            }, keyHolder);
        }
    }

    private void setMpaForCurrentFilm(Film film) {
        Mpa mpa = new Mpa();
        for (Mpa currentMpa : mpaRepository.getListMpa()) {
            if (currentMpa.getId().equals(film.getMpa().getId())) {
                mpa = currentMpa;
            }
        }
        film.setMpa(mpa);
    }

    private void setGenreForCurrentFilm(Film film) {
        if (!film.getGenres().isEmpty()) {
            List<Genre> genres = new ArrayList<>();
            for (Genre currentGenre : genreRepository.getAllGenre()) {
                for (Genre genre : film.getGenres()) {
                    if (Objects.equals(genre.getId(), currentGenre.getId())) {
                        genres.add(currentGenre);
                    }
                }
            }
            film.setGenres(genres);
        }
    }

}
