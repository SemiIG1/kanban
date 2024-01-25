package id.ac.pnj.kanban.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class EditProjectMemberDTO {
    private int id;
    @NotNull(message = "is required")
    private String name;
    @NotNull(message = "is required")
    @Email
    private String email;
    @NotNull(message = "is required")
    private String phone;

    public EditProjectMemberDTO() {
    }

    public EditProjectMemberDTO(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
