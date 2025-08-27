/**
 * Course Management System - Main JavaScript
 * Common functionality and utilities
 */

// Main application object
const CourseManagement = {
    
    // Initialize the application
    init: function() {
        this.initFormValidation();
        this.initSearchFunctionality();
        this.initTooltips();
        this.initConfirmDialogs();
    },

    // Form validation functionality
    initFormValidation: function() {
        const forms = document.querySelectorAll('form[novalidate]');
        
        forms.forEach(form => {
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            });
        });

        // Real-time validation feedback
        const inputs = document.querySelectorAll('.form-control-modern');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                if (this.checkValidity()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                    this.classList.add('is-invalid');
                }
            });

            // Clear validation on input
            input.addEventListener('input', function() {
                if (this.classList.contains('is-invalid') || this.classList.contains('is-valid')) {
                    this.classList.remove('is-invalid', 'is-valid');
                }
            });
        });
    },

    // Search functionality for tables
    initSearchFunctionality: function() {
        const searchInputs = document.querySelectorAll('input[id$="searchInput"]');
        
        searchInputs.forEach(searchInput => {
            const tableId = this.getTableIdFromSearchInput(searchInput);
            const table = document.getElementById(tableId);
            
            if (table) {
                searchInput.addEventListener('keyup', function() {
                    CourseManagement.filterTable(table, this.value.toLowerCase());
                });

                // Add clear button functionality
                this.addClearButton(searchInput);
            }
        });
    },

    // Helper function to get table ID from search input
    getTableIdFromSearchInput: function(searchInput) {
        const inputId = searchInput.id;
        if (inputId === 'searchInput') {
            // Try to find table by common IDs
            const tables = ['schoolsTable', 'coursesTable', 'studentsTable', 'groupsTable'];
            for (let tableId of tables) {
                if (document.getElementById(tableId)) {
                    return tableId;
                }
            }
        }
        return null;
    },

    // Filter table rows based on search term
    filterTable: function(table, searchTerm) {
        const rows = table.getElementsByTagName('tr');
        let visibleRows = 0;

        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const text = row.textContent.toLowerCase();
            
            if (text.includes(searchTerm)) {
                row.style.display = '';
                visibleRows++;
            } else {
                row.style.display = 'none';
            }
        }

        // Update results counter if exists
        this.updateResultsCounter(table, visibleRows);
    },

    // Update results counter
    updateResultsCounter: function(table, count) {
        const container = table.closest('.table-container');
        if (container) {
            const counter = container.querySelector('.results-counter');
            if (counter) {
                counter.textContent = `${count} results found`;
            }
        }
    },

    // Add clear button to search inputs
    addClearButton: function(searchInput) {
        const inputGroup = searchInput.closest('.input-group');
        if (inputGroup && !inputGroup.querySelector('.btn-clear')) {
            const clearBtn = document.createElement('button');
            clearBtn.className = 'btn btn-outline-secondary btn-clear';
            clearBtn.type = 'button';
            clearBtn.innerHTML = '<i class="fas fa-times"></i>';
            clearBtn.title = 'Clear search';
            
            clearBtn.addEventListener('click', function() {
                searchInput.value = '';
                searchInput.dispatchEvent(new Event('keyup'));
                searchInput.focus();
            });

            const appendDiv = document.createElement('div');
            appendDiv.className = 'input-group-append';
            appendDiv.appendChild(clearBtn);
            inputGroup.appendChild(appendDiv);
        }
    },

    // Initialize Bootstrap tooltips
    initTooltips: function() {
        // Initialize tooltips if Bootstrap is available
        if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"], [title]'));
            tooltipTriggerList.map(function(tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        }
    },

    // Initialize confirmation dialogs
    initConfirmDialogs: function() {
        const deleteLinks = document.querySelectorAll('a[href*="/delete/"]');
        deleteLinks.forEach(link => {
            if (!link.hasAttribute('onclick')) {
                link.addEventListener('click', function(e) {
                    const entityName = this.getAttribute('data-entity') || 'item';
                    if (!confirm(`Are you sure you want to delete this ${entityName}?`)) {
                        e.preventDefault();
                    }
                });
            }
        });
    },

    // Utility function to show toast notifications
    showToast: function(message, type = 'info') {
        // Create toast container if it doesn't exist
        let toastContainer = document.getElementById('toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toast-container';
            toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
            toastContainer.style.zIndex = '9999';
            document.body.appendChild(toastContainer);
        }

        // Create toast element
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;

        toastContainer.appendChild(toast);

        // Show toast if Bootstrap is available
        if (typeof bootstrap !== 'undefined' && bootstrap.Toast) {
            const bsToast = new bootstrap.Toast(toast);
            bsToast.show();
            
            // Remove toast element after it's hidden
            toast.addEventListener('hidden.bs.toast', function() {
                toast.remove();
            });
        } else {
            // Fallback: show for 3 seconds then remove
            setTimeout(() => {
                toast.remove();
            }, 3000);
        }
    },

    // Utility function to format dates
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },

    // Utility function to debounce function calls
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    // Handle loading states
    setLoading: function(element, isLoading) {
        if (isLoading) {
            element.classList.add('loading');
            element.disabled = true;
            const originalText = element.textContent;
            element.setAttribute('data-original-text', originalText);
            element.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Loading...';
        } else {
            element.classList.remove('loading');
            element.disabled = false;
            const originalText = element.getAttribute('data-original-text');
            if (originalText) {
                element.textContent = originalText;
                element.removeAttribute('data-original-text');
            }
        }
    }
};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    CourseManagement.init();
});

// Make CourseManagement globally available
window.CourseManagement = CourseManagement;

