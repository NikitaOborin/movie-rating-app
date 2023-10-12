package com.project.movieratingapp.repository.genre;

import com.project.movieratingapp.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
      Genre genre = new Genre();
      genre.setId(rs.getInt("genre_id"));
      genre.setName(rs.getString("name"));
      return genre;
    };
}
