package org.speer.assessment.entities;

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notes")
public class Note implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "content_search_vector", columnDefinition = "tsvector")
    private String contentSearchVector;

    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author.getName() +
                '}';
    }

}
