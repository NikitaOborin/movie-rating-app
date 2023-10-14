package com.project.movieratingapp.repository.like;

import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;

import java.util.Set;

public interface LikeRepository {
    Film addLikes(Film film, User user);

    Film deleteLike(Film film, User user);

    Set<Long> getLikesForFilm(Film film);

    Set<Long> getLikesForUser(User user);
}
