package ua.readabook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.readabook.entity.CategoryEntity;

@Repository
public interface CategoryRepository
        extends JpaRepository<CategoryEntity, Long> {
}
