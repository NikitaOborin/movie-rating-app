package com.project.movieratingapp.service;

import com.project.movieratingapp.model.User;
import com.project.movieratingapp.repository.UserInMemoryRepository;
import com.project.movieratingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {
    private User user1;
    private User user2;
    private User user3;
    private UserService userService;
    @BeforeEach
    void createUsers() {
        UserRepository userRepository = new UserInMemoryRepository();
        userService = new UserService(userRepository);

        user1 = new User();
        user1.setName("user1Name");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("user1Login");
        user1.setEmail("user1@mail.ru");

        user2 = new User();
        user2.setName("user2Name");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("user2Login");
        user2.setEmail("user2@mail.ru");

        user3 = new User();
        user3.setName("user3Name");
        user3.setBirthday(LocalDate.of(2003, 3, 3));
        user3.setLogin("user3Login");
        user3.setEmail("user3@mail.ru");

        userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.addUser(user3);
    }

    @Test
    void shouldSuccessAddFriend() {
        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(user1.getFriends().size(), 1, "user не имеет друзей");
        assertEquals(user2.getFriends().size(), 1, "otherUser не имеет друзей");
    }

    @Test
    void shouldSuccessDeleteFriend() {
        userService.addFriend(user1.getId(), user2.getId());
        userService.deleteFriend(user1.getId(), user2.getId());
        assertEquals(user1.getFriends().size(), 0, "user не имеет друзей");
        assertEquals(user2.getFriends().size(), 0, "otherUser не имеет друзей");
    }

    @Test
    void shouldSuccessGetMutualFriends() {
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user2.getId(), user3.getId());
        List<User> mutualFriends = userService.getMutualFriends(user1.getId(), user3.getId());
        assertEquals(mutualFriends.get(0), user2, "user и user2 не имеют общих друзей");
    }
}