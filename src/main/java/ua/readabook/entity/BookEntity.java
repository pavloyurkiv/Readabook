package ua.readabook.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @Column(nullable = false, length = 200, unique = false)
    private String author;

    @Column(nullable = true)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String annotation;

    @Column(nullable = false, unique = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "lang_id")
    private LangEntity lang;

}
