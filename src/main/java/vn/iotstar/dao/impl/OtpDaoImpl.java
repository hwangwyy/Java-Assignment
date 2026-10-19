package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.iotstar.connection.DBConnection;
import vn.iotstar.dao.OtpDao;
import vn.iotstar.model.Otp;

public class OtpDaoImpl extends DBConnection implements OtpDao {

    @Override
    public void save(Otp otp) {
        try (Connection con = super.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement del = con.prepareStatement("DELETE FROM Otp WHERE email = ? AND purpose = ?");
                    PreparedStatement ins = con.prepareStatement("INSERT INTO Otp(email, purpose, code_hash, "
                            + "expires_at, attempts, created_at) VALUES (?,?,?,?,0,?)")) {
                del.setString(1, otp.getEmail());
                del.setString(2, otp.getPurpose());
                del.executeUpdate();
                ins.setString(1, otp.getEmail());
                ins.setString(2, otp.getPurpose());
                ins.setString(3, otp.getCodeHash());
                ins.setTimestamp(4, otp.getExpiresAt());
                ins.setTimestamp(5, otp.getCreatedAt());
                ins.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Otp get(String email, String purpose) {
        String sql = "SELECT * FROM Otp WHERE email = ? AND purpose = ?";
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, purpose);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Otp o = new Otp();
                    o.setEmail(rs.getString("email"));
                    o.setPurpose(rs.getString("purpose"));
                    o.setCodeHash(rs.getString("code_hash"));
                    o.setExpiresAt(rs.getTimestamp("expires_at"));
                    o.setAttempts(rs.getInt("attempts"));
                    o.setCreatedAt(rs.getTimestamp("created_at"));
                    return o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void increaseAttempts(String email, String purpose) {
        exec("UPDATE Otp SET attempts = attempts + 1 WHERE email = ? AND purpose = ?", email, purpose);
    }

    @Override
    public void delete(String email, String purpose) {
        exec("DELETE FROM Otp WHERE email = ? AND purpose = ?", email, purpose);
    }

    private void exec(String sql, String email, String purpose) {
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, purpose);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
