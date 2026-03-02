import java.io.*;
import java.util.*;

public class ProfitabilityAnalysis {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        File file;
        String path;

        // Step 1: Prompt and Validate File Path
        while (true) {
            System.out.print("Enter dataset file path (e.g., C:\\Downloads\\vgchartz-2024.csv): ");
            path = input.nextLine();
            file = new File(path);

            if (file.exists() && file.isFile() && file.canRead()) {
                if (path.toLowerCase().endsWith(".csv")) {
                    break;
                } else {
                    System.out.println("Error: File is not in CSV format.");
                }
            } else {
                System.out.println("Error: Invalid file path or file is not readable. Please try again.");
            }
        }

        // Step 2: Load and Process Data
        List<DataRecord> records = loadDataset(path);

        if (records.isEmpty()) {
            System.out.println("No data found or error reading the file.");
            input.close();
            return;
        }

        // Step 3: Perform Analytics
        performAnalytics(records);
        
        input.close();
    }

    private static List<DataRecord> loadDataset(String filePath) {
        List<DataRecord> records = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            br.readLine();
            
            while ((line = br.readLine()) != null) {
                // Using regex split to handle potential commas inside quotes (CSV standard)
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Columns: 1=title, 3=genre (category), 7=total_sales
                if (values.length > 7) {
                    try {
                        String title = values[1];
                        String category = values[3];
                        String salesStr = values[7].trim();
                        
                        if (!salesStr.isEmpty() && !salesStr.equalsIgnoreCase("N/A")) {
                            double sales = Double.parseDouble(salesStr);
                            records.add(new DataRecord(title, category, sales));
                        }
                    } catch (NumberFormatException e) {
                        // Skip rows with invalid numeric sales values
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Critical Error reading file: " + e.getMessage());
        }
        return records;
    }

    private static void performAnalytics(List<DataRecord> records) {
        Map<String, Double> totalSalesMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // Grouping data
        for (DataRecord record : records) {
            String cat = record.getCategory();
            double sales = record.getSales();

            totalSalesMap.put(cat, totalSalesMap.getOrDefault(cat, 0.0) + sales);
            countMap.put(cat, countMap.getOrDefault(cat, 0) + 1);
        }

        // Analytics variables
        String mostProfitableCat = "";
        double maxSales = -1;
        String leastProfitableCat = "";
        double minSales = Double.MAX_VALUE;

        // Formatted Output Header
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("%-20s | %-15s | %-15s\n", "Category", "Total Sales", "Avg Sale");
        System.out.println("-".repeat(60));

        for (String category : totalSalesMap.keySet()) {
            double totalSales = totalSalesMap.get(category);
            int count = countMap.get(category);
            double avgSale = totalSales / count;

            System.out.printf("%-20s | $%-14.2f | $%-14.2f\n", category, totalSales, avgSale);

            // Check for most/least profitable
            if (totalSales > maxSales) {
                maxSales = totalSales;
                mostProfitableCat = category;
            }
            if (totalSales < minSales) {
                minSales = totalSales;
                leastProfitableCat = category;
            }
        }

        // Display results
        System.out.println("=".repeat(60));
        System.out.println("ANALYTICS SUMMARY:");
        System.out.printf("Most Profitable Category:  %s ($%.2f)\n", mostProfitableCat, maxSales);
        System.out.printf("Least Profitable Category: %s ($%.2f)\n", leastProfitableCat, minSales);
        System.out.println("=".repeat(60));
    }
}