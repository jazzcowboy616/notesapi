package org.speer.assessment.entities;

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "notes")
public class Note implements Serializable {
    private static final long serialVersionUID = 7419229779731522702L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "content_search_vector", columnDefinition = "tsvector")
    private String contentSearchVector;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "note_share",
            joinColumns = {@JoinColumn(name = "note_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> shareList;

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
