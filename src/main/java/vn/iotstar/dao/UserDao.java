package vn.iotstar.dao;

import vn.iotstar.model.User;

public interface UserDao {
    User get(String username);

    User getByEmail(String email);

    boolean insert(User user);

    boolean activate(String email);

    boolean updatePassword(String email, String passwordHash);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
