package id.ac.pnj.kanban.dto;

import jakarta.validation.constraints.NotNull;

public class TeamMemberDTO {
    private int id;
    @NotNull
    private String email;
}
