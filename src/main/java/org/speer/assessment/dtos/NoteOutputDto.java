package org.speer.assessment.dtos;

import lombok.Data;
import org.speer.assessment.entities.Note;

@Data
public class NoteOutputDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public NoteOutputDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.author = note.getAuthor().getName();
    }
}
