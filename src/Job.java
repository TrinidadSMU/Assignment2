public class Job {
    int id;
    int processingTime;
    int priority;
    public Job(int id, int processingTime) {
        this.id = id;
        this.processingTime = processingTime;
    }
    public Job(int id, int processingTime, int priority) {
        this.id = id;
        this.processingTime = processingTime;
        this.priority = priority;
    }
}
