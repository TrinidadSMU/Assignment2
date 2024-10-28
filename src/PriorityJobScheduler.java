import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PriorityJobScheduler {
    public static void scheduleJobs(Job[] jobs, String outputFilePath) {
        // Step 1: Sort the jobs using Min-Heap
        minHeapSort(jobs);

        // Step 2: Schedule the jobs
        int totalCompletionTime = 0;
        int currentTime = 0;
        StringBuilder executionOrder = new StringBuilder();

        for (Job job : jobs) {
            // Update the current time and total completion time
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;

            // Store the order of execution
            if (executionOrder.length() > 0) {
                executionOrder.append(", ");
            }
            executionOrder.append(job.id);
        }

        // Step 3: Calculate average completion time
        double averageCompletionTime = (double) totalCompletionTime / jobs.length;

        // Output results to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            writer.write("Priority Job Scheduler Output:\n<br>");
            writer.write("Execution order: [" + executionOrder + "]\n<br>");
            writer.write(String.format("Average completion time: %.1f%n", averageCompletionTime));
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to perform Min-Heap Sort
    public static void minHeapSort(Job[] jobs) {
        int n = jobs.length;

        // Build min heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            minHeapify(jobs, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Job temp = jobs[0];
            jobs[0] = jobs[i];
            jobs[i] = temp;

            // Call min heapify on the reduced heap
            minHeapify(jobs, i, 0);
        }
    }

    // To heapify a subtree rooted with node i
    public static void minHeapify(Job[] jobs, int n, int i) {
        int smallest = i; // Initialize smallest as root
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2

        // Compare by priority first, then processing time
        if (left < n && (jobs[left].priority > jobs[smallest].priority ||
                (jobs[left].priority == jobs[smallest].priority &&
                        jobs[left].processingTime > jobs[smallest].processingTime))) {
            smallest = left;
        }

        if (right < n && (jobs[right].priority > jobs[smallest].priority ||
                (jobs[right].priority == jobs[smallest].priority &&
                        jobs[right].processingTime > jobs[smallest].processingTime))) {
            smallest = right;
        }

        // If smallest is not root
        if (smallest != i) {
            Job swap = jobs[i];
            jobs[i] = jobs[smallest];
            jobs[smallest] = swap;

            // Recursively heapify the affected subtree
            minHeapify(jobs, n, smallest);
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
