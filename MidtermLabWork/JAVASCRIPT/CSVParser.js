/**
 * CSVParser.js
 * Handles CSV file reading and parsing
 */

const fs = require('fs');

class CSVParser {
    /**
     * Parse CSV file and return array of objects
     */
    static parseCSV(filePath) {
        try {
            const content = fs.readFileSync(filePath, 'utf8');
            const lines = content.trim().split('\n');
            
            if (lines.length === 0) {
                throw new Error('CSV file is empty');
            }

            // Parse header
            const header = lines[0].split(',').map(h => h.trim());
            const records = [];

            // Parse data rows
            for (let i = 1; i < lines.length; i++) {
                const line = lines[i].trim();
                if (line === '') continue;

                // Simple CSV parsing (handles basic cases)
                const values = this.parseCSVLine(line);
                
                if (values.length !== header.length) {
                    continue; // Skip malformed lines
                }

                const record = {};
                for (let j = 0; j < header.length; j++) {
                    record[header[j]] = values[j];
                }
                records.push(record);
            }

            return records;
        } catch (error) {
            throw new Error(`Error parsing CSV: ${error.message}`);
        }
    }

    /**
     * Parse a single CSV line handling quoted fields
     */
    static parseCSVLine(line) {
        const result = [];
        let current = '';
        let insideQuotes = false;

        for (let i = 0; i < line.length; i++) {
            const char = line[i];
            const nextChar = line[i + 1];

            if (char === '"') {
                if (insideQuotes && nextChar === '"') {
                    current += '"';
                    i++; // Skip next quote
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (char === ',' && !insideQuotes) {
                result.push(current.trim());
                current = '';
            } else {
                current += char;
            }
        }
        result.push(current.trim());
        return result;
    }
}

module.exports = CSVParser;
