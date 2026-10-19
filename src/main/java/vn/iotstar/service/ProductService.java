package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.Product;

public interface ProductService {
    void insert(Product product);

    void edit(Product product);

    void delete(int id);

    Product get(int id);

    List<Product> getAll();

    List<Product> search(String keyword, Integer cateId);

    List<Product> getLatest(int limit);

    List<Product> getPage(Integer cateId, String keyword, int page, int pageSize);

    int count(Integer cateId, String keyword);

    int countByCategory(int cateId);
}
