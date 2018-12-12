package ua.readabook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.readabook.entity.BookEntity;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> ,
        JpaSpecificationExecutor<BookEntity> {

    @Query("SELECT p FROM BookEntity p WHERE p.name = :name")
    BookEntity findBookByName(@Param("name") String name);

    BookEntity findByName(String name);

    List<BookEntity> findByNameLike(String name);
    
    Boolean existsByName(String name);
}
