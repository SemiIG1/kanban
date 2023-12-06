package id.ac.pnj.kanban.dto;

import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Status;
import id.ac.pnj.kanban.validation.DatesMatch;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DatesMatch.List({
        @DatesMatch(
                startField = "startDatetime",
                endField = "deadline",
                message = "The end date must be after the start date."
        )
})
public class TaskDTO {
    private int id;

    @NotNull
    private String title;


    private LocalDateTime startDatetime;


    private LocalDateTime deadline;

    public TaskDTO() {
    }

    public TaskDTO(String title, LocalDateTime startDatetime, LocalDateTime deadline) {
        this.title = title;
        this.startDatetime = startDatetime;
        this.deadline = deadline;
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

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
