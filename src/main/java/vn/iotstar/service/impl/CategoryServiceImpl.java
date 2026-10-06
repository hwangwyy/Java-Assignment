package vn.iotstar.service.impl;

import java.util.List;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.UploadUtil;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void edit(Category newCategory) {
        Category old = categoryDao.get(newCategory.getId());
        if (old == null) {
            return;
        }
        old.setName(newCategory.getName());
        if (newCategory.getIcon() != null) {
            UploadUtil.deleteQuietly(old.getIcon()); // remove the old image file
            old.setIcon(newCategory.getIcon());
        }
        categoryDao.edit(old);
    }

    @Override
    public void delete(int id) {
        Category old = categoryDao.get(id);
        if (old != null) {
            categoryDao.delete(id);
            UploadUtil.deleteQuietly(old.getIcon());
        }
    }

    @Override
    public Category get(int id) {
        return categoryDao.get(id);
    }

    @Override
    public Category get(String name) {
        return categoryDao.get(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDao.search(keyword);
    }
}
