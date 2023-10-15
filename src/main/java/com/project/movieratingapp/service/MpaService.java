package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Mpa;
import com.project.movieratingapp.repository.mpa.MpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaRepository mpaRepository;

    @Autowired
    public MpaService(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    public List<Mpa> getListMpa() {
        return mpaRepository.getListMpa();
    }

    public Mpa getMpaById(Integer mpaId) {
        return mpaRepository.getMpaById(mpaId);
    }
}
