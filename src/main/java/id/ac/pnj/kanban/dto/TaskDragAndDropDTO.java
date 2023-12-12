package id.ac.pnj.kanban.dto;

import jakarta.validation.constraints.NotNull;

public class TaskDragAndDropDTO {
    @NotNull
    private int id;
    @NotNull
    private String status;



    public TaskDragAndDropDTO() {
    }

    public TaskDragAndDropDTO(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
