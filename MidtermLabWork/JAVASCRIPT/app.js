/**
 * PROGRAMMING 2 – MACHINE PROBLEM
 * Product Category Profitability Analysis
 * 
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Computer Science – Data Science
 * 
 * Requirements:
 * 1. User inputs full file path to CSV dataset
 * 2. Validate file existence and format
 * 3. Load dataset and perform analysis
 * 4. Group by product category and analyze profitability
 * 5. Display formatted results
 */

const fs = require('fs');
const readline = require('readline');
const path = require('path');

// Import custom modules
const DataRecord = require('./DataRecord');
const CSVParser = require('./CSVParser');
const ProfitabilityAnalysis = require('./ProfitabilityAnalysis');

/**
 * Create readline interface for user input
 */
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

/**
 * Ask user for CSV file path with validation
 */
function askFilePath(callback) {
    rl.question('\n📂 Enter dataset file path: ', function(userPath) {
        // Trim whitespace
        userPath = userPath.trim();

        // Check if path is empty
        if (!userPath) {
            console.log('❌ Error: File path cannot be empty. Please try again.');
            askFilePath(callback);
            return;
        }

        // Check if file exists
        if (!fs.existsSync(userPath)) {
            console.log(`❌ Error: File not found at path: ${userPath}`);
            console.log('   Please check the path and try again.');
            askFilePath(callback);
            return;
        }

        // Check if it's a file (not a directory)
        const stats = fs.statSync(userPath);
        if (stats.isDirectory()) {
            console.log(`❌ Error: Path is a directory, not a file: ${userPath}`);
            askFilePath(callback);
            return;
        }

        // Check if file is readable
        try {
            fs.accessSync(userPath, fs.constants.R_OK);
        } catch (error) {
            console.log(`❌ Error: File is not readable: ${userPath}`);
            askFilePath(callback);
            return;
        }

        // Check if file extension is CSV
        const extension = path.extname(userPath).toLowerCase();
        if (extension !== '.csv') {
            console.log(`❌ Error: Invalid file format. Expected .csv file, got ${extension}`);
            askFilePath(callback);
            return;
        }

        console.log(`✓ File validated successfully!`);
        callback(userPath);
    });
}

/**
 * Load and parse CSV file
 */
function loadDataset(filePath) {
    try {
        console.log('\n⏳ Processing dataset...');
        const rawRecords = CSVParser.parseCSV(filePath);
        
        if (rawRecords.length === 0) {
            throw new Error('CSV file contains no data rows');
        }

        // Convert to DataRecord objects and filter valid records
        const records = rawRecords
            .map(rawData => new DataRecord(rawData))
            .filter(record => record.isValid());

        if (records.length === 0) {
            throw new Error('No valid records found in dataset (records without genre or sales)');
        }

        console.log(`✓ Loaded ${records.length} valid records from dataset`);
        return records;
    } catch (error) {
        console.log(`\n❌ Error loading dataset: ${error.message}`);
        askFilePath(processAnalysis);
    }
}

/**
 * Main analysis function
 */
function processAnalysis(filePath) {
    try {
        // Load dataset
        const records = loadDataset(filePath);
        
        if (!records) return;

        // Perform profitability analysis
        console.log('📊 Analyzing profitability by category...');
        const analysisResults = ProfitabilityAnalysis.analyzeByCatego(records);
        const statistics = ProfitabilityAnalysis.getStatistics(analysisResults);

        // Display results
        ProfitabilityAnalysis.displayResults(statistics);

        // Offer to analyze another file
        rl.question('Would you like to analyze another dataset? (yes/no): ', function(answer) {
            if (answer.toLowerCase() === 'yes' || answer.toLowerCase() === 'y') {
                askFilePath(processAnalysis);
            } else {
                console.log('\n👋 Thank you for using the Profitability Analysis Tool!');
                rl.close();
            }
        });
    } catch (error) {
        console.log(`\n❌ Unexpected error: ${error.message}`);
        rl.close();
    }
}

/**
 * Program entry point
 */
function main() {
    console.log('\n' + '='.repeat(100));
    console.log('PRODUCT CATEGORY PROFITABILITY ANALYSIS SYSTEM'.padEnd(100));
    console.log('University of Perpetual Help System DALTA – Molino Campus'.padEnd(100));
    console.log('BS Computer Science – Data Science'.padEnd(100));
    console.log('='.repeat(100));
    
    console.log('\nThis program analyzes product sales data by category.');
    console.log('It calculates total sales and average sales per category.');
    askFilePath(processAnalysis);
}

// Start the program
main();
