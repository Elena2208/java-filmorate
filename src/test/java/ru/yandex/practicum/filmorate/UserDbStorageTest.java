package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    final User user1 = User.builder()
            .email("elena@yandex.ru")
            .login("elena111")
            .name("E")
            .birthday(LocalDate.of(1990, 2, 22))
            .build();
    final User user2 = User.builder()
            .email("max@yandex.ru")
            .login("max111")
            .name("M")
            .birthday(LocalDate.of(1985, 5, 11))
            .build();

    final User user3 = User.builder()
            .email("Nik@yandex.ru")
            .login("Nik2020")
            .name("N")
            .birthday(LocalDate.of(1988, 6, 23))
            .build();

    @Test
    void createTest() {
        userDbStorage.create(user1);
        User userNew = userDbStorage.getById(1);
        assertThat(user1).isEqualToComparingFieldByFieldRecursively(userNew);
    }

    @Test
    void getById() {
        userDbStorage.create(user1);
        User userNew = userDbStorage.getById(1);
        assertThat(user1).isEqualToComparingFieldByFieldRecursively(userNew);
    }

    @Test
    void getAll() {
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        List<User> users = (List<User>) userDbStorage.findAll();
        AssertionsForInterfaceTypes.assertThat(users).isNotEmpty();
    }

    @Test
    void updateTest() {
        userDbStorage.create(user1);
        user1.setName("Nik");
        userDbStorage.update(user1);
        AssertionsForClassTypes.assertThat(userDbStorage.getById((int) user1.getId()))
                .hasFieldOrPropertyWithValue("name", "Nik");
    }

    @Test
    void deleteById() {
        userDbStorage.create(user1);
        userDbStorage.deleteById(1);
        Assertions.assertThatThrownBy(() -> userDbStorage.getById(1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addFriendTest() {
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).isEmpty());
        userDbStorage.addFriend(1, 2);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).size() == 1);
        userDbStorage.addFriend(2, 1);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).size() == 2);
    }

    @Test
    void deleteFriendTest() {
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).isEmpty());
        userDbStorage.addFriend(1, 2);
        userDbStorage.addFriend(2, 1);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).size() == 2);
        userDbStorage.deleteFriend(1, 2);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).size() == 1);
        userDbStorage.deleteFriend(2, 1);
        assertThat(userDbStorage.getFriendsListById((int) user1.getId()).isEmpty());
    }

    @Test
    void getCommonFriendsListTest() {
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        userDbStorage.addFriend(1, 3);
        userDbStorage.addFriend(2, 3);
        userDbStorage.getCommonFriendsList(1, 2);
        assertThat(userDbStorage.getCommonFriendsList(1, 2).size() == 1);
    }
}
