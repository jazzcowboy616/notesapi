package org.speer.assessment.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.speer.assessment.entities.Note;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String author;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.author = note.getAuthor().getName();
    }
}
