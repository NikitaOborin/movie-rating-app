package com.project.movieratingapp.repository.mpa;

import com.project.movieratingapp.model.Mpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MpaDBRepository implements MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getListMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", mpaRowMapper);
    }

    @Override
    public Mpa getMpaByFilmId(Long film_id) {
        return jdbcTemplate.queryForObject("SELECT m.mpa_id, m.name FROM mpa AS m " +
                                   "INNER JOIN film AS f ON m.mpa_id = f.mpa_id " +
                               "WHERE f.film_id=?", mpaRowMapper, film_id);
    }

    private final RowMapper<Mpa> mpaRowMapper = (rs, rowNum) -> {
        Mpa mpa = new Mpa();

        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("name"));

        return mpa;
    };
}