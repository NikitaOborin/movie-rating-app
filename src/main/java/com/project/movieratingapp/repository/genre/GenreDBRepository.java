package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.exception.NotFoundException;
import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class GenreDBRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getListGenre() {
        return jdbcTemplate.query("SELECT * FROM genre", genreRowMapper);
    }

    @Override
    public Genre getGenreByGenreId(Integer genreId) {
        Genre genre;
        List<Genre> listGenres = jdbcTemplate.query("SELECT * FROM genre WHERE genre_id=?", genreRowMapper, genreId);

        if (!listGenres.isEmpty()) {
            genre = listGenres.get(0);
        } else {
            throw new NotFoundException("genre with id = " + genreId + " not found");
        }

        return genre;
    }

    @Override
    public List<Genre> getGenreByFilmId(Long filmId) {
        return jdbcTemplate.query("SELECT g.genre_id, g.name FROM film AS f " +
                "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id=?", genreRowMapper, filmId);
    }

    @Override
    public void updateGenreInDbByFilm(Film film) {
        deleteGenreByFilmId(film.getId());

        List<Genre> genres = getListGenresWithoutDuplicate(film.getGenres());

        for (Genre genre : genres) {
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

    @Override
    public void deleteGenreByFilmId(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", filmId);
    }

    private List<Genre> getListGenresWithoutDuplicate(List<Genre> genres) {
        Set<Genre> genreSet = new HashSet<>(genres);
        List<Genre> genreListWithoutDuplicate = new ArrayList<>(genreSet);

        genreListWithoutDuplicate.sort(new Comparator<Genre>() {
            @Override
            public int compare(Genre o1, Genre o2) {
                return o1.getId() - o2.getId();
            }
        });

        return genreListWithoutDuplicate;
    }

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();

        genre.setId(rs.getInt("genre.genre_id"));
        genre.setName(rs.getString("genre.name"));

        return genre;
    };
}
