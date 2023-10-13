package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class GenreDBRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM genre", genreRowMapper);
    }

    @Override
    public List<Genre> getGenreByFilmId(Long film_id) {
        return jdbcTemplate.query("SELECT g.genre_id, g.name FROM film AS f " +
                "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id=?", genreRowMapper, film_id);
    }

    @Override
    public void updateGenreInDbForFilm(Film film) {
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

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
      Genre genre = new Genre();

      genre.setId(rs.getInt("genre.genre_id"));
      genre.setName(rs.getString("genre.name"));

      return genre;
    };
}
