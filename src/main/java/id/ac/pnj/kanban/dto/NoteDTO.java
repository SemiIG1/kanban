package id.ac.pnj.kanban.dto;

import jakarta.validation.constraints.NotNull;

public class NoteDTO {
    private int id;
    @NotNull(message = "is required")
    private String message;

    public NoteDTO() {
    }

    public NoteDTO(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
