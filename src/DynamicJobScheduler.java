import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicJobScheduler {
    public static void scheduleJobs(Job[] jobs, String outputFilePath) {
        int currentTime = 0;
        int totalCompletionTime = 0;
        StringBuilder executionOrder = new StringBuilder();

        boolean[] jobExecuted = new boolean[jobs.length]; // Track executed jobs

        while (true) {
            // Check if all jobs are executed
            boolean allExecuted = true;
            for (boolean executed : jobExecuted) {
                if (!executed) {
                    allExecuted = false;
                    break;
                }
            }
            if (allExecuted) break;

            // Select the next job to execute based on SPT rule
            Job nextJob = null;
            int jobIndex = -1;

            for (int i = 0; i < jobs.length; i++) {
                // Only consider jobs that have arrived and not yet executed
                if (!jobExecuted[i] && jobs[i].priority <= currentTime) {
                    if (nextJob == null || jobs[i].processingTime < nextJob.processingTime) {
                        nextJob = jobs[i];
                        jobIndex = i;
                    }
                }
            }

            // If there's a job to execute
            if (nextJob != null) {
                // Execute the job
                currentTime += nextJob.processingTime;
                totalCompletionTime += currentTime;

                // Mark job as executed
                jobExecuted[jobIndex] = true;

                // Record the execution order
                if (executionOrder.length() > 0) {
                    executionOrder.append(", ");
                }
                executionOrder.append(nextJob.id);
            } else {
                // If no jobs are available, advance to the next job's arrival time
                int nextArrivalTime = Integer.MAX_VALUE;
                for (Job job : jobs) {
                    if (!jobExecuted[job.id - 1] && job.priority < nextArrivalTime) {
                        nextArrivalTime = job.priority;
                    }
                }
                currentTime = nextArrivalTime; // Jump to the next arrival time
            }
        }

        // Calculate average completion time
        double averageCompletionTime = (double) totalCompletionTime / jobs.length;

        // Output results to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            writer.write("Dynamic Job Scheduler Output:\n<br>");
            writer.write("Execution order: [" + executionOrder + "]\n<br>");
            writer.write(String.format("Average completion time: %.1f%n", averageCompletionTime));
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Job[] readJobsFromFile(String inputFilePath) {
        Job[] jobs = new Job[100]; // Assuming a maximum of 100 jobs
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                int id = Integer.parseInt(parts[0]);
                int processingTime = Integer.parseInt(parts[1]);
                int arrivalTime = Integer.parseInt(parts[2]);
                jobs[count++] = new Job(id, processingTime, arrivalTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Trim the jobs array to the actual number of jobs
        Job[] result = new Job[count];
        System.arraycopy(jobs, 0, result, 0, count);
        return result;
    }
}
