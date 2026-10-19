package vn.iotstar.service.impl;

import java.util.List;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.dao.impl.ProductDaoImpl;
import vn.iotstar.model.Product;
import vn.iotstar.service.ProductService;
import vn.iotstar.util.UploadUtil;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void edit(Product newProduct) {
        Product old = productDao.get(newProduct.getId());
        if (old == null) {
            return;
        }
        old.setName(newProduct.getName());
        old.setDescription(newProduct.getDescription());
        old.setPrice(newProduct.getPrice());
        old.setCategoryId(newProduct.getCategoryId());
        if (newProduct.getImage() != null) {
            UploadUtil.deleteQuietly(old.getImage());
            old.setImage(newProduct.getImage());
        }
        productDao.edit(old);
    }

    @Override
    public void delete(int id) {
        Product old = productDao.get(id);
        if (old != null) {
            productDao.delete(id);
            UploadUtil.deleteQuietly(old.getImage());
        }
    }

    @Override
    public Product get(int id) {
        return productDao.get(id);
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public List<Product> search(String keyword, Integer cateId) {
        return productDao.search(keyword, cateId);
    }

    @Override
    public List<Product> getLatest(int limit) {
        return productDao.getLatest(limit);
    }

    @Override
    public List<Product> getPage(Integer cateId, String keyword, int page, int pageSize) {
        return productDao.getPage(cateId, keyword, page, pageSize);
    }

    @Override
    public int count(Integer cateId, String keyword) {
        return productDao.count(cateId, keyword);
    }

    @Override
    public int countByCategory(int cateId) {
        return productDao.countByCategory(cateId);
    }
}
