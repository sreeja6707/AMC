// AMC List Page JavaScript
$(document).ready(function() {
    initializeDataTable();
    initializeEventHandlers();
});

function initializeDataTable() {
    $('#amcTable').DataTable({
        responsive: true,
        pageLength: 25,
        lengthMenu: [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
        order: [[0, 'desc']],
        columnDefs: [
            {
                targets: [6], // Order Value column
                className: 'text-end'
            },
            {
                targets: [7, 8], // Status and Actions columns
                className: 'text-center'
            },
            {
                targets: [8], // Actions column
                orderable: false,
                searchable: false
            }
        ],
        language: {
            search: "Search AMC Records:",
            lengthMenu: "Show _MENU_ records per page",
            info: "Showing _START_ to _END_ of _TOTAL_ records",
            infoEmpty: "Showing 0 to 0 of 0 records",
            infoFiltered: "(filtered from _MAX_ total records)",
            zeroRecords: "No matching records found",
            emptyTable: "No AMC records available"
        },
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'excel',
                text: '<i class="fas fa-file-excel"></i> Export Excel',
                className: 'btn btn-success btn-sm',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4, 5, 6, 7] // Exclude Actions column
                }
            },
            {
                extend: 'pdf',
                text: '<i class="fas fa-file-pdf"></i> Export PDF',
                className: 'btn btn-danger btn-sm',
                orientation: 'landscape',
                pageSize: 'A4',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4, 5, 6, 7] // Exclude Actions column
                }
            },
            {
                extend: 'print',
                text: '<i class="fas fa-print"></i> Print',
                className: 'btn btn-info btn-sm',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4, 5, 6, 7] // Exclude Actions column
                }
            }
        ],
        initComplete: function() {
            console.log('AMC DataTable initialized successfully');
            
            // Add custom search inputs for specific columns if needed
            this.api().columns([1, 2, 5]).every(function() {
                var column = this;
                var title = $(column.header()).text();
                // This can be expanded for advanced filtering
            });
        }
    });
}

function initializeEventHandlers() {
    // Handle row click for quick view
    $('#amcTable tbody').on('click', 'tr', function(e) {
        // Don't trigger on button clicks
        if (!$(e.target).closest('button').length) {
            const table = $('#amcTable').DataTable();
            const data = table.row(this).data();
            if (data) {
                const controlNo = $(this).find('td:first strong').text();
                if (controlNo) {
                    viewDetails(controlNo);
                }
            }
        }
    });
    
    // Handle keyboard navigation
    $(document).on('keydown', function(e) {
        // Ctrl + N for new AMC
        if (e.ctrlKey && e.key === 'n') {
            e.preventDefault();
            window.location.href = 'index.jsp';
        }
        
        // Ctrl + E for export
        if (e.ctrlKey && e.key === 'e') {
            e.preventDefault();
            exportToExcel();
        }
        
        // ESC to close modal
        if (e.key === 'Escape') {
            $('#detailsModal').modal('hide');
        }
    });
}

function viewDetails(controlNo) {
    if (!controlNo) {
        showAlert('Invalid Control Number', 'danger');
        return;
    }
    
    // Show loading in modal
    $('#detailsModal .modal-title').text('Loading...');
    $('#modalBody').html(`
        <div class="text-center">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2">Loading AMC details...</p>
        </div>
    `);
    
    $('#detailsModal').modal('show');
    
    // Make AJAX call to get AMC details
    $.ajax({
        url: 'AMCServlet',
        type: 'GET',
        data: {
            action: 'getDetails',
            controlNo: controlNo
        },
        dataType: 'json',
        success: function(response) {
            if (response.status === 'success' && response.data) {
                displayAMCDetails(response.data);
            } else {
                showModalError('Failed to load AMC details: ' + (response.message || 'Unknown error'));
            }
        },
        error: function(xhr, status, error) {
            console.error('Error loading AMC details:', error);
            showModalError('Error loading AMC details: ' + error);
        }
    });
}

function displayAMCDetails(amcData) {
    $('#detailsModal .modal-title').text(`AMC Details - ${amcData.controlNo}`);
    
    let itemsHtml = '';
    if (amcData.itemDetailsList && amcData.itemDetailsList.length > 0) {
        itemsHtml = `
            <h6 class="mt-4 mb-3">Item Details:</h6>
            <div class="table-responsive">
                <table class="table table-sm table-bordered">
                    <thead class="table-light">
                        <tr>
                            <th>S.No</th>
                            <th>Item Name</th>
                            <th>Make</th>
                            <th>Model</th>
                            <th>Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
        `;
        
        amcData.itemDetailsList.forEach(function(item) {
            itemsHtml += `
                <tr>
                    <td>${item.sno}</td>
                    <td>${item.itemName}</td>
                    <td>${item.make}</td>
                    <td>${item.model}</td>
                    <td>${item.quantity}</td>
                </tr>
            `;
        });
        
        itemsHtml += '</tbody></table></div>';
    }
    
    const modalContent = `
        <div class="row">
            <div class="col-md-6">
                <p><strong>Control No:</strong> ${amcData.controlNo}</p>
                <p><strong>AMC Letter No:</strong> ${amcData.amcLetterNo || 'N/A'}</p>
                <p><strong>AMC Letter Date:</strong> ${formatDate(amcData.amcLetterDate)}</p>
                <p><strong>Subject:</strong> ${amcData.subject || 'N/A'}</p>
                <p><strong>Gem Order No:</strong> ${amcData.gemOrderNo || 'N/A'}</p>
            </div>
            <div class="col-md-6">
                <p><strong>Gem Order Date:</strong> ${formatDate(amcData.gemOrderDate)}</p>
                <p><strong>AMC Effect From:</strong> ${formatDate(amcData.amcEffectFrom)}</p>
                <p><strong>Valid Upto:</strong> ${formatDate(amcData.validUpto)}</p>
                <p><strong>Payment Mode:</strong> ${amcData.paymentMode || 'N/A'}</p>
                <p><strong>Order Value:</strong> ₹${formatNumber(amcData.amcOrderValue)}</p>
            </div>
        </div>
        ${itemsHtml}
        <div class="mt-3">
            <small class="text-muted">
                <strong>Created:</strong> ${formatDateTime(amcData.createdDate)} by ${amcData.createdBy}
            </small>
        </div>
    `;
    
    $('#modalBody').html(modalContent);
}

function showModalError(message) {
    $('#detailsModal .modal-title').text('Error');
    $('#modalBody').html(`
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-triangle me-2"></i>
            ${message}
        </div>
    `);
}

function editAMC(controlNo) {
    if (!controlNo) {
        showAlert('Invalid Control Number', 'danger');
        return;
    }
    
    // Redirect to edit page (if implemented) or show message
    showAlert('Edit functionality will be implemented in next version', 'info');
    
    // For future implementation:
    // window.location.href = 'index.jsp?action=edit&controlNo=' + controlNo;
}

function exportToExcel() {
    const table = $('#amcTable').DataTable();
    const button = table.button('.buttons-excel');
    if (button) {
        button.trigger();
    } else {
        showAlert('Export functionality not available', 'warning');
    }
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    
    try {
        const date = new Date(dateStr);
        return date.toLocaleDateString('en-GB'); // dd/mm/yyyy format
    } catch (e) {
        return dateStr;
    }
}

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    
    try {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('en-GB'); // dd/mm/yyyy, hh:mm:ss
    } catch (e) {
        return dateTimeStr;
    }
}

function formatNumber(num) {
    if (!num) return '0.00';
    
    try {
        return parseFloat(num).toLocaleString('en-IN', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    } catch (e) {
        return num;
    }
}

function showAlert(message, type = 'info') {
    // Remove existing alerts
    $('.alert').not('.alert-permanent').remove();
    
    // Create new alert
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            <i class="fas fa-${getIconForType(type)} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    // Insert after header
    $('header').after(alertHtml);
    
    // Auto-dismiss after 5 seconds for info and success alerts
    if (type === 'info' || type === 'success') {
        setTimeout(function() {
            $('.alert-' + type).fadeOut(500);
        }, 5000);
    }
    
    // Scroll to alert
    $('html, body').animate({
        scrollTop: $('.alert').offset().top - 100
    }, 300);
}

function getIconForType(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-circle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// Initialize tooltips
$(function () {
    $('[data-bs-toggle="tooltip"]').tooltip();
});
