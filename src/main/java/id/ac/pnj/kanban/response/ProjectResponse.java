package id.ac.pnj.kanban.response;

public class ProjectResponse {
    private Double percentage;

    public ProjectResponse() {
    }

    public ProjectResponse(Double percentage) {
        this.percentage = percentage;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
