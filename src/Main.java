public class Main {
    public static void main(String[] args) {
        String outputFilePath = "README.md"; // Output file path
        // Input: Job id and Processing time
        Job[] jobs = JobScheduler.readJobsFromFile("src\\task1-input.txt");
        JobScheduler.scheduleJobs(jobs, outputFilePath);

        Job[] priorityJobs = PriorityJobScheduler.readJobsFromFile("src\\task2-input.txt");
        PriorityJobScheduler.scheduleJobs(priorityJobs, outputFilePath);

        Job[] dynamicJobs = DynamicJobScheduler.readJobsFromFile("src\\task3-input.txt");
        DynamicJobScheduler.scheduleJobs(dynamicJobs, outputFilePath);
    }
}
