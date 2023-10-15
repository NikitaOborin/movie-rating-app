package com.project.movieratingapp.repository.like;

import java.util.Set;

public interface LikeRepository {
    void addLikes(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Set<Long> getLikesByFilmId(Long filmId);

    Set<Long> getLikesByUserId(Long userId);
}