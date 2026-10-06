package vn.iotstar.dao;

import vn.iotstar.model.User;

public interface UserDao {
    User get(String username);

    boolean insert(User user);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
