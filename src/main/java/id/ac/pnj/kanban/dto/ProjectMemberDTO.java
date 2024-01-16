package id.ac.pnj.kanban.dto;

public class ProjectMemberDTO {
    private int id;
    private String member;

    public ProjectMemberDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
