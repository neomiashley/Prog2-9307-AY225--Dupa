/**
 * DataRecord.js
 * Represents a single game record from the CSV dataset
 */

class DataRecord {
    constructor(data) {
        this.img = data.img || '';
        this.title = data.title || '';
        this.console = data.console || '';
        this.genre = data.genre || '';
        this.publisher = data.publisher || '';
        this.developer = data.developer || '';
        this.criticScore = parseFloat(data.critic_score) || 0;
        this.totalSales = parseFloat(data.total_sales) || 0;
        this.naSales = parseFloat(data.na_sales) || 0;
        this.jpSales = parseFloat(data.jp_sales) || 0;
        this.palSales = parseFloat(data.pal_sales) || 0;
        this.otherSales = parseFloat(data.other_sales) || 0;
        this.releaseDate = data.release_date || '';
        this.lastUpdate = data.last_update || '';
    }

    /**
     * Validate if record has required data
     */
    isValid() {
        return this.genre && this.genre.trim() !== '' && this.totalSales > 0;
    }

    /**
     * Get sales value for this record
     */
    getSalesValue() {
        return this.totalSales;
    }

    /**
     * Convert record to string representation
     */
    toString() {
        return `${this.title} (${this.genre}) - Sales: ${this.totalSales}M`;
    }
}

module.exports = DataRecord;
