public class DataRecord {
    private String title;
    private String category;
    private double sales;

    public DataRecord(String title, String category, double sales) {
        this.title = title;
        this.category = category;
        this.sales = sales;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getSales() {
        return sales;
    }
}