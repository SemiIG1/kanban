package id.ac.pnj.kanban.response;

public class ProjectResponse {
    private int percentage;

    public ProjectResponse() {
    }

    public ProjectResponse(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
