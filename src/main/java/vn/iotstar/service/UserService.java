package vn.iotstar.service;

import vn.iotstar.model.User;

public interface UserService {
    /** Returns the user when the credentials are right (regardless of activation state), else null. */
    User login(String username, String password);

    User get(String username);

    User getByEmail(String email);

    boolean insert(User user);

    /** Creates an INACTIVE account; it must be activated with an OTP before logging in. */
    boolean register(String username, String password, String email, String fullname, String phone);

    boolean activate(String email);

    boolean resetPassword(String email, String newPassword);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
