package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vn.iotstar.connection.DBConnection;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.model.Product;

public class ProductDaoImpl extends DBConnection implements ProductDao {
    private static final String BASE_SELECT = "SELECT p.product_id, p.product_name, p.description, p.price, "
            + "p.image, p.created_date, p.cate_id, c.cate_name "
            + "FROM Product p LEFT JOIN Category c ON p.cate_id = c.cate_id";

    @Override
    public void insert(Product product) {
        String sql = "INSERT INTO Product(product_name, description, price, image, cate_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setInt(5, product.getCategoryId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Product product) {
        String sql = "UPDATE Product SET product_name = ?, description = ?, price = ?, image = ?, cate_id = ? "
                + "WHERE product_id = ?";
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setInt(5, product.getCategoryId());
            ps.setInt(6, product.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Product WHERE product_id = ?";
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product get(int id) {
        String sql = BASE_SELECT + " WHERE p.product_id = ?";
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getAll() {
        return search(null, null);
    }

    @Override
    public List<Product> search(String keyword, Integer cateId) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = appendFilters(sql, cateId, keyword);
        sql.append(" ORDER BY p.created_date DESC, p.product_id DESC");

        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> getLatest(int limit) {
        return getPage(null, null, 1, limit);
    }

    @Override
    public List<Product> getPage(Integer cateId, String keyword, int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, pageSize);
        int offset = (safePage - 1) * safeSize;

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = appendFilters(sql, cateId, keyword);
        sql.append(" ORDER BY p.created_date DESC, p.product_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(safeSize);

        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int count(Integer cateId, String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product p");
        List<Object> params = appendFilters(sql, cateId, keyword);
        try (Connection con = super.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countByCategory(int cateId) {
        return count(cateId, null);
    }

    private static List<Object> appendFilters(StringBuilder sql, Integer cateId, String keyword) {
        List<Object> params = new ArrayList<>();
        List<String> clauses = new ArrayList<>();
        if (cateId != null && cateId > 0) {
            clauses.add("p.cate_id = ?");
            params.add(cateId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            clauses.add("p.product_name LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }
        if (!clauses.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", clauses));
        }
        return params;
    }

    private static void bind(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof Integer) {
                ps.setInt(i + 1, (Integer) p);
            } else {
                ps.setString(i + 1, (String) p);
            }
        }
    }

    private static Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("product_id"));
        p.setName(rs.getString("product_name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setImage(rs.getString("image"));
        p.setCreatedDate(rs.getTimestamp("created_date"));
        p.setCategoryId(rs.getInt("cate_id"));
        p.setCategoryName(rs.getString("cate_name"));
        return p;
    }
}
