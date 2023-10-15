package com.project.movieratingapp.repository.mpa;

import com.project.movieratingapp.model.Mpa;

import java.util.List;

public interface MpaRepository {
    List<Mpa> getListMpa();

    Mpa getMpaById(Integer mpaId);

    Mpa getMpaByFilmId(Long film_id);
}
