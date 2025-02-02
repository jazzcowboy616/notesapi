package org.speer.assessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteFilter {
    private String search;
    private User author;

    public Specification<Note> toPredicate() {
        return NoteSpecifications.search(search)
                .and(NoteSpecifications.author(author));
    }
}
