package id.ac.pnj.kanban.dto;

import jakarta.validation.constraints.NotNull;

public class AnnouncementDTO {
    private int id;
    @NotNull(message = "is required")
    private String message;

    public AnnouncementDTO() {
    }

    public AnnouncementDTO(String message) {
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
