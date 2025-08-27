/**
 * Table Search Functionality
 * Enhanced search and filtering for data tables
 */

const TableSearch = {
    
    // Initialize search functionality for a specific table
    init: function(tableId, searchInputId, options = {}) {
        const table = document.getElementById(tableId);
        const searchInput = document.getElementById(searchInputId);
        
        if (!table || !searchInput) {
            console.warn(`Table search: Could not find table (${tableId}) or search input (${searchInputId})`);
            return;
        }

        const config = {
            debounceDelay: 300,
            highlightMatches: true,
            caseSensitive: false,
            searchColumns: 'all', // 'all' or array of column indices
            emptyMessage: 'No results found',
            ...options
        };

        // Store original rows for reset
        const originalRows = Array.from(table.querySelectorAll('tbody tr'));
        
        // Create debounced search function
        const debouncedSearch = CourseManagement.debounce((searchTerm) => {
            this.performSearch(table, originalRows, searchTerm, config);
        }, config.debounceDelay);

        // Bind search event
        searchInput.addEventListener('input', function() {
            debouncedSearch(this.value.trim());
        });

        // Add clear functionality
        this.addClearButton(searchInput, () => {
            this.clearSearch(table, originalRows);
        });

        // Store config for later use
        table.setAttribute('data-search-config', JSON.stringify(config));
    },

    // Perform the actual search
    performSearch: function(table, rows, searchTerm, config) {
        let visibleCount = 0;
        const tbody = table.querySelector('tbody');
        
        if (!searchTerm) {
            // Show all rows
            rows.forEach(row => {
                row.style.display = '';
                this.removeHighlight(row);
            });
            visibleCount = rows.length;
        } else {
            const searchLower = config.caseSensitive ? searchTerm : searchTerm.toLowerCase();
            
            rows.forEach(row => {
                const shouldShow = this.rowMatchesSearch(row, searchLower, config);
                
                if (shouldShow) {
                    row.style.display = '';
                    if (config.highlightMatches) {
                        this.highlightMatches(row, searchTerm, config.caseSensitive);
                    }
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                    this.removeHighlight(row);
                }
            });
        }

        // Update results counter
        this.updateResultsCounter(table, visibleCount, rows.length);

        // Show empty message if no results
        this.toggleEmptyMessage(table, visibleCount === 0, config.emptyMessage);
    },

    // Check if a row matches the search term
    rowMatchesSearch: function(row, searchTerm, config) {
        const cells = row.querySelectorAll('td');
        const searchColumns = config.searchColumns === 'all' ? 
            Array.from({length: cells.length}, (_, i) => i) : 
            config.searchColumns;

        return searchColumns.some(columnIndex => {
            const cell = cells[columnIndex];
            if (!cell) return false;
            
            const text = config.caseSensitive ? 
                cell.textContent.trim() : 
                cell.textContent.trim().toLowerCase();
                
            return text.includes(searchTerm);
        });
    },

    // Highlight matching text
    highlightMatches: function(row, searchTerm, caseSensitive) {
        const cells = row.querySelectorAll('td');
        const regex = new RegExp(`(${this.escapeRegex(searchTerm)})`, caseSensitive ? 'g' : 'gi');
        
        cells.forEach(cell => {
            // Skip cells with buttons or complex HTML
            if (cell.querySelector('button, a, .btn')) return;
            
            const originalHTML = cell.getAttribute('data-original-html') || cell.innerHTML;
            if (!cell.hasAttribute('data-original-html')) {
                cell.setAttribute('data-original-html', originalHTML);
            }
            
            const highlightedHTML = originalHTML.replace(regex, '<mark class="search-highlight">$1</mark>');
            cell.innerHTML = highlightedHTML;
        });
    },

    // Remove highlighting
    removeHighlight: function(row) {
        const cells = row.querySelectorAll('td[data-original-html]');
        cells.forEach(cell => {
            const originalHTML = cell.getAttribute('data-original-html');
            if (originalHTML) {
                cell.innerHTML = originalHTML;
            }
        });
    },

    // Clear search
    clearSearch: function(table, originalRows) {
        originalRows.forEach(row => {
            row.style.display = '';
            this.removeHighlight(row);
        });
        
        this.updateResultsCounter(table, originalRows.length, originalRows.length);
        this.toggleEmptyMessage(table, false);
    },

    // Add clear button to search input
    addClearButton: function(searchInput, clearCallback) {
        const inputGroup = searchInput.closest('.input-group');
        if (!inputGroup || inputGroup.querySelector('.search-clear-btn')) return;

        const clearButton = document.createElement('button');
        clearButton.className = 'btn btn-outline-secondary search-clear-btn';
        clearButton.type = 'button';
        clearButton.innerHTML = '<i class="fas fa-times"></i>';
        clearButton.title = 'Clear search';
        clearButton.style.display = 'none';

        clearButton.addEventListener('click', function() {
            searchInput.value = '';
            clearCallback();
            this.style.display = 'none';
            searchInput.focus();
        });

        // Show/hide clear button based on input
        searchInput.addEventListener('input', function() {
            clearButton.style.display = this.value ? 'block' : 'none';
        });

        // Insert clear button after the input
        searchInput.parentNode.insertBefore(clearButton, searchInput.nextSibling);
    },

    // Update results counter
    updateResultsCounter: function(table, visibleCount, totalCount) {
        const container = table.closest('.table-container');
        if (!container) return;

        let counter = container.querySelector('.search-results-counter');
        if (!counter) {
            // Create counter if it doesn't exist
            const header = container.querySelector('.table-header h5');
            if (header) {
                counter = document.createElement('small');
                counter.className = 'search-results-counter text-muted ms-2';
                header.appendChild(counter);
            }
        }

        if (counter) {
            if (visibleCount === totalCount) {
                counter.textContent = '';
            } else {
                counter.textContent = `(${visibleCount} of ${totalCount})`;
            }
        }
    },

    // Toggle empty message
    toggleEmptyMessage: function(table, show, message = 'No results found') {
        const tbody = table.querySelector('tbody');
        let emptyRow = tbody.querySelector('.empty-search-message');

        if (show) {
            if (!emptyRow) {
                emptyRow = document.createElement('tr');
                emptyRow.className = 'empty-search-message';
                const colspan = table.querySelectorAll('thead th').length;
                emptyRow.innerHTML = `
                    <td colspan="${colspan}" class="text-center py-4 text-muted">
                        <i class="fas fa-search mb-2" style="font-size: 2rem; opacity: 0.5;"></i>
                        <div>${message}</div>
                    </td>
                `;
                tbody.appendChild(emptyRow);
            }
            emptyRow.style.display = '';
        } else if (emptyRow) {
            emptyRow.style.display = 'none';
        }
    },

    // Escape regex special characters
    escapeRegex: function(string) {
        return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    },

    // Advanced search with multiple filters
    initAdvancedSearch: function(tableId, filters) {
        const table = document.getElementById(tableId);
        if (!table) return;

        const originalRows = Array.from(table.querySelectorAll('tbody tr'));
        
        // Create filter function
        const applyFilters = () => {
            let visibleCount = 0;
            
            originalRows.forEach(row => {
                const shouldShow = filters.every(filter => 
                    this.applyFilter(row, filter)
                );
                
                if (shouldShow) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });
            
            this.updateResultsCounter(table, visibleCount, originalRows.length);
        };

        // Bind filter events
        filters.forEach(filter => {
            const element = document.getElementById(filter.elementId);
            if (element) {
                element.addEventListener('change', applyFilters);
                element.addEventListener('input', 
                    CourseManagement.debounce(applyFilters, 300)
                );
            }
        });
    },

    // Apply individual filter
    applyFilter: function(row, filter) {
        const element = document.getElementById(filter.elementId);
        if (!element) return true;

        const value = element.value.trim();
        if (!value) return true;

        const cell = row.cells[filter.columnIndex];
        if (!cell) return true;

        const cellText = cell.textContent.trim().toLowerCase();
        const searchValue = value.toLowerCase();

        switch (filter.type) {
            case 'contains':
                return cellText.includes(searchValue);
            case 'equals':
                return cellText === searchValue;
            case 'startsWith':
                return cellText.startsWith(searchValue);
            case 'endsWith':
                return cellText.endsWith(searchValue);
            default:
                return cellText.includes(searchValue);
        }
    }
};

// Auto-initialize common table searches
document.addEventListener('DOMContentLoaded', function() {
    // Initialize search for common table patterns
    const searchConfigs = [
        { tableId: 'schoolsTable', searchInputId: 'searchInput' },
        { tableId: 'coursesTable', searchInputId: 'searchInput' },
        { tableId: 'studentsTable', searchInputId: 'searchInput' },
        { tableId: 'groupsTable', searchInputId: 'searchInput' }
    ];

    searchConfigs.forEach(config => {
        if (document.getElementById(config.tableId) && document.getElementById(config.searchInputId)) {
            TableSearch.init(config.tableId, config.searchInputId);
        }
    });
});

// Make TableSearch globally available
window.TableSearch = TableSearch;

