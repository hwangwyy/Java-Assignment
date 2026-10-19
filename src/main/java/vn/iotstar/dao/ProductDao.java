package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.model.Product;

public interface ProductDao {
    void insert(Product product);

    void edit(Product product);

    void delete(int id);

    Product get(int id);

    List<Product> getAll();

    List<Product> search(String keyword, Integer cateId);

    /** Returns up to {@code limit} newest products ordered by created_date DESC, product_id DESC. */
    List<Product> getLatest(int limit);

    /** Returns a 1-based page of products filtered optionally by category and keyword. */
    List<Product> getPage(Integer cateId, String keyword, int page, int pageSize);

    int count(Integer cateId, String keyword);

    int countByCategory(int cateId);
}
