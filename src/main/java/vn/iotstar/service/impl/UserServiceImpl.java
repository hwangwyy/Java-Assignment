package vn.iotstar.service.impl;

import java.sql.Date;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && PasswordUtil.verify(password, user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public boolean insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        User user = new User(email, username, fullname, PasswordUtil.hash(password), null, Constant.ROLE_USER,
                phone, today);
        user.setActive(false);
        return userDao.insert(user);
    }

    @Override
    public boolean activate(String email) {
        return userDao.activate(email);
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        return userDao.updatePassword(email, PasswordUtil.hash(newPassword));
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }
}
