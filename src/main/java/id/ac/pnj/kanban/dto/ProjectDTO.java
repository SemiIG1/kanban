package id.ac.pnj.kanban.dto;

import id.ac.pnj.kanban.validation.DatesMatch;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DatesMatch.List({
        @DatesMatch(
                startField = "startUpdate",
                endField = "deadline",
                message = "The end date must be after the start date."
        )
})
public class ProjectDTO {
    private int id;
    @NotNull(message = "is required")
    private String project;
    @NotNull(message = "is required")
    private String customer;

    private LocalDateTime startUpdate;

    private LocalDateTime deadline;

    @NotNull(message = "is required")
    private String status;



    public ProjectDTO() {
    }

    public ProjectDTO(String project, String customer, LocalDateTime startUpdate, LocalDateTime deadline, String status) {
        this.project = project;
        this.customer = customer;
        this.startUpdate = startUpdate;
        this.deadline = deadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LocalDateTime getStartUpdate() {
        return startUpdate;
    }

    public void setStartUpdate(LocalDateTime startUpdate) {
        this.startUpdate = startUpdate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
