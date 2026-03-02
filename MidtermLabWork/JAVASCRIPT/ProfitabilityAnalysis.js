/**
 * ProfitabilityAnalysis.js
 * Performs product category profitability analysis
 */

class ProfitabilityAnalysis {
    /**
     * Analyze profitability by category
     */
    static analyzeByCatego(records) {
        const categoryData = {};

        // Group records by category (genre)
        records.forEach(record => {
            const category = record.genre || 'Unknown';
            
            if (!categoryData[category]) {
                categoryData[category] = {
                    category: category,
                    records: [],
                    totalSales: 0,
                    count: 0
                };
            }

            const sales = parseFloat(record.totalSales) || 0;
            categoryData[category].records.push(record);
            categoryData[category].totalSales += sales;
            categoryData[category].count += 1;
        });

        // Calculate averages and format results
        const results = Object.values(categoryData).map(cat => ({
            category: cat.category,
            totalSales: parseFloat(cat.totalSales.toFixed(2)),
            averageSales: parseFloat((cat.totalSales / cat.count).toFixed(2)),
            productCount: cat.count
        }));

        // Sort by total sales descending
        results.sort((a, b) => b.totalSales - a.totalSales);

        return results;
    }

    /**
     * Get profitability statistics
     */
    static getStatistics(results) {
        if (results.length === 0) {
            return null;
        }

        return {
            mostProfitable: results[0],
            leastProfitable: results[results.length - 1],
            categories: results
        };
    }

    /**
     * Display analysis results in formatted table
     */
    static displayResults(statistics) {
        if (!statistics) {
            console.log('No data available for analysis.');
            return;
        }

        console.log('\n' + '='.repeat(100));
        console.log('PRODUCT CATEGORY PROFITABILITY ANALYSIS'.padEnd(100));
        console.log('='.repeat(100));

        console.log('\n┌─ CATEGORY SUMMARY ─────────────────────────────────────────────────────────────────────┐');
        console.log('│ Rank │ Category              │ Total Sales (M) │ Avg Sales (M) │ # Products │');
        console.log('├──────┼───────────────────────┼─────────────────┼───────────────┼────────────┤');

        statistics.categories.forEach((cat, index) => {
            const rank = String(index + 1).padEnd(5);
            const category = cat.category.substring(0, 21).padEnd(23);
            const totalSales = String(cat.totalSales).padStart(15);
            const avgSales = String(cat.averageSales).padStart(13);
            const count = String(cat.productCount).padStart(10);
            
            console.log(`│ ${rank}│ ${category}│ ${totalSales} │ ${avgSales} │ ${count} │`);
        });
        console.log('└──────┴───────────────────────┴─────────────────┴───────────────┴────────────┘');

        console.log('\n┌─ KEY INSIGHTS ─────────────────────────────────────────────────────────────────────────┐');
        console.log(`│ Most Profitable Category:   ${statistics.mostProfitable.category.padEnd(70)} │`);
        console.log(`│   • Total Sales: ${String(statistics.mostProfitable.totalSales + 'M').padEnd(70)} │`);
        console.log(`│   • Average per Product: ${String(statistics.mostProfitable.averageSales + 'M').padEnd(70)} │`);
        console.log(`│   • Product Count: ${String(statistics.mostProfitable.productCount).padEnd(70)} │`);
        
        console.log(`│                                                                                      │`);
        console.log(`│ Least Profitable Category:  ${statistics.leastProfitable.category.padEnd(70)} │`);
        console.log(`│   • Total Sales: ${String(statistics.leastProfitable.totalSales + 'M').padEnd(70)} │`);
        console.log(`│   • Average per Product: ${String(statistics.leastProfitable.averageSales + 'M').padEnd(70)} │`);
        console.log(`│   • Product Count: ${String(statistics.leastProfitable.productCount).padEnd(70)} │`);
        console.log('└────────────────────────────────────────────────────────────────────────────────────────┘');

        console.log('\n' + '='.repeat(100) + '\n');
    }
}

module.exports = ProfitabilityAnalysis;
