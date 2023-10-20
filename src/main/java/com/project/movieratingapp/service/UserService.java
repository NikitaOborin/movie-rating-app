package com.project.movieratingapp.service;

import com.project.movieratingapp.model.Event;
import com.project.movieratingapp.model.Film;
import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.director.DirectorRepository;
import com.project.movieratingapp.repository.event.EventRepository;
import com.project.movieratingapp.repository.film.FilmRepository;
import com.project.movieratingapp.repository.friendship.FriendshipRepository;
import com.project.movieratingapp.repository.genre.GenreRepository;
import com.project.movieratingapp.repository.like.LikeRepository;
import com.project.movieratingapp.repository.mpa.MpaRepository;
import com.project.movieratingapp.repository.user.UserRepository;
import com.project.movieratingapp.util.EventType;
import com.project.movieratingapp.util.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final LikeRepository likeRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final DirectorRepository directorRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserService(@Qualifier("userDBRepository") UserRepository userRepository,
                       @Qualifier("filmDBRepository") FilmRepository filmRepository,
                       FriendshipRepository friendshipRepository,
                       LikeRepository likeRepository,
                       GenreRepository genreRepository,
                       MpaRepository mpaRepository,
                       DirectorRepository directorRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.friendshipRepository = friendshipRepository;
        this.likeRepository = likeRepository;
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
        this.directorRepository = directorRepository;
        this.eventRepository = eventRepository;
    }

    public List<User> getUsers() {
        log.info("UserService: getUsers(): start");
        List<User> users = userRepository.getUsers();

        for (User user : users) {
            user.setFriends(friendshipRepository.getFriendsByUserId(user.getId()));
            user.setFilmLikes(likeRepository.getLikesByUserId(user.getId()));
        }

        return users;
    }

    public User addUser(User user) {
        log.info("UserService: addUser(): start with user={}", user);
        return userRepository.addUser(user);
    }

    public User updateUser(User user) {
        log.info("UserService: updateUser(): start with user={}", user);
        return userRepository.updateUser(user);
    }

    public User getUserById(Long userId) {
        log.info("UserService: getUserById(): start with id={}", userId);
        User user = userRepository.getUserById(userId);

        user.setFriends(friendshipRepository.getFriendsByUserId(userId));
        user.setFilmLikes(likeRepository.getLikesByUserId(userId));

        return user;
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("UserService: addFriend(): start with id={}, friendId={}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (friend.getFriends().containsKey(userId)) {
            friend.getFriends().put(userId, true);
            user.getFriends().put(friendId, true);

            friendshipRepository.addFriendship(user, friendId);
            friendshipRepository.addFriendship(friend, userId);
        } else {
            user.getFriends().put(friendId, false);

            friendshipRepository.addFriendship(user, friendId);
        }

        eventRepository.addEventForUserByEntityId(userId, friendId, EventType.FRIEND.toString(),
                                                                    OperationType.ADD.toString());

        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        log.info("UserService: deleteFriend(): start with id={}, friendId={}", userId, friendId);
        User user = getUserById(userId);

        friendshipRepository.deleteFriendship(userId, friendId);

        if (user.getFriends().get(friendId).equals(true)) {
            friendshipRepository.deleteFriendship(friendId, userId);
        }

        user.setFriends(friendshipRepository.getFriendsByUserId(userId));

        eventRepository.addEventForUserByEntityId(userId, friendId, EventType.FRIEND.toString(),
                                                                    OperationType.REMOVE.toString());

        return user;
    }

    public List<User> getUserFriends(Long userId) {
        log.info("UserService: getUserFriends(): start with id={}", userId);
        userRepository.getUserById(userId);

        Map<Long, Boolean> getFriendsByUserId = friendshipRepository.getFriendsByUserId(userId);
        Set<Long> userFriendsId = getFriendsByUserId.keySet();

        List<User> userFriends = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            userFriends.add(userRepository.getUserById(friendId));
        }

        return userFriends;
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        log.info("UserService: getMutualFriends(): start with id={}, otherId={}", userId, otherId);
        List<User> mutualFriends = new ArrayList<>();
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);
        Set<Long> userFriendsId = user.getFriends().keySet();
        Set<Long> otherUserFriendsId = otherUser.getFriends().keySet();

        for (Long friendId : userFriendsId) {
            for (Long otherFriendId : otherUserFriendsId) {
                if (friendId.equals(otherFriendId)) {
                    mutualFriends.add(getUserById(otherFriendId));
                }
            }
        }

        return mutualFriends;
    }

    public List<Film> getFilmsRecommendationsByUserId(Long userId) {
        log.info("UserService: getFilmsRecommendationsByUserId(): start with userId={}=", userId);
        List<Film> filmsRecommendation = filmRepository.getFilmsRecommendation(userId);

        for (Film film : filmsRecommendation) {
            film.setGenres(genreRepository.getGenreByFilmId(film.getId()));
            film.setMpa(mpaRepository.getMpaByFilmId(film.getId()));
            film.setLikes(likeRepository.getLikesByFilmId(film.getId()));
            film.setDirectors(directorRepository.getDirectorsByFilmId(film.getId()));
        }

        return filmsRecommendation;
    }

    public List<Event> getEventFeedByUserId(Long userId) {
        userRepository.getUserById(userId);

        return eventRepository.getEventFeedByUserId(userId);
    }
}