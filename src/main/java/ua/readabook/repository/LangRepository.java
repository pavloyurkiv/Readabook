package ua.readabook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.readabook.entity.LangEntity;

@Repository
public interface LangRepository
        extends JpaRepository<LangEntity, Long> {
}
