package id.ac.pnj.kanban.dto;

import id.ac.pnj.kanban.validation.DatesMatch;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DatesMatch.List({
        @DatesMatch(
                startField = "start",
                endField = "end",
                message = "The end date must be after the start date."
        )
})
public class EventDTO {
    private int id;

    @NotNull(message = "is required")
    private String title;
    @NotNull(message = "is required")
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    public EventDTO() {

    }

    public EventDTO(String title, String description, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
