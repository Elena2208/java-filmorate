package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;


public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User getById(int id);

    User deleteById(int id);

    void addFriend(int firstId, int secondId);

    void deleteFriend(int firstId, int secondId);

    List<User> getFriendsListById(int id);

    List<User> getCommonFriendsList(int firstId, int secondId);
}
