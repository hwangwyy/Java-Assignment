package vn.iotstar.dao;

import vn.iotstar.model.Otp;

public interface OtpDao {
    /** Replaces any existing OTP for the same (email, purpose). */
    void save(Otp otp);

    Otp get(String email, String purpose);

    void increaseAttempts(String email, String purpose);

    void delete(String email, String purpose);
}
