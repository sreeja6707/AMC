// Main AMC Application JavaScript
let rowCounter = 1;
let isFormSubmitting = false;

$(document).ready(function() {
    initializeApplication();
});

function initializeApplication() {
    // Initialize datepickers
    initializeDatepickers();
    
    // Generate control number on page load
    generateControlNumber();
    
    // Load address list based on user's directorate
    loadAddressList();
    
    // Initialize form validation
    initializeFormValidation();
    
    // Initialize event handlers
    initializeEventHandlers();
    
    // Character count for subject field
    initializeCharacterCount();
    
    // Add first item row
    if ($('#itemsTable tbody tr').length === 0) {
        addItemRow();
    }
}

function initializeDatepickers() {
    $('.datepicker').datepicker({
        dateFormat: 'dd-mm-yy',
        changeMonth: true,
        changeYear: true,
        yearRange: "-10:+10",
        showButtonPanel: true,
        beforeShow: function(input, inst) {
            setTimeout(function() {
                inst.dpDiv.css({
                    top: $(input).offset().top + $(input).outerHeight() + 5,
                    left: $(input).offset().left
                });
            }, 0);
        }
    });
}

function generateControlNumber() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    
    $.ajax({
        url: 'GetControlNumberServlet',
        type: 'GET',
        data: { 
            year: year, 
            month: month 
        },
        dataType: 'json',
        success: function(response) {
            if (response.sequenceNumber) {
                const controlNumber = year + '-' + month + '-' + response.sequenceNumber;
                $('#controlNo').val(controlNumber);
                displayControlNumber(controlNumber);
            } else {
                console.error('Failed to generate control number');
                showAlert('Error generating control number', 'danger');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error generating control number:', error);
            showAlert('Error generating control number: ' + error, 'danger');
        }
    });
}

function displayControlNumber(controlNo) {
    $('#controlNoValue').text(controlNo);
    $('#controlNoDisplay').removeClass('d-none').addClass('fade-in');
}

function loadAddressList() {
    $.ajax({
        url: 'GetAddressListServlet',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const select = $('#amcUserId');
            select.empty().append('<option value="">Select Address List as per Directorate</option>');
            
            $.each(data, function(index, item) {
                const optionText = item.userName + ' - ' + item.designation + 
                                 (item.department ? ' (' + item.department + ')' : '');
                select.append(`<option value="${item.addressId}">${optionText}</option>`);
            });
            
            console.log('Address list loaded successfully');
        },
        error: function(xhr, status, error) {
            console.error('Error loading address list:', error);
            showAlert('Error loading address list: ' + error, 'danger');
        }
    });
}

function initializeFormValidation() {
    // Custom validation for alphanumeric AMC Letter No
    $('#amcLetterNo').on('input', function() {
        const value = $(this).val();
        const alphanumericRegex = /^[a-zA-Z0-9]*$/;
        
        if (value && !alphanumericRegex.test(value)) {
            $(this).addClass('is-invalid');
            $(this).next('.invalid-feedback').text('Only alphanumeric characters are allowed');
        } else {
            $(this).removeClass('is-invalid');
        }
    });
    
    // Date range validation
    $('#amcEffectFrom, #validUpto').on('change', function() {
        validateDateRange();
    });
    
    // Numeric validation for order value
    $('#amcOrderValue').on('input', function() {
        const value = parseFloat($(this).val());
        if (value < 0) {
            $(this).addClass('is-invalid');
        } else {
            $(this).removeClass('is-invalid');
        }
    });
}

function initializeEventHandlers() {
    // Radio button change event
    $('input[name="tracking_type"]').change(function() {
        const selectedType = $(this).val();
        updateFormBasedOnType(selectedType);
    });
    
    // Add row button
    $('#addRowBtn').click(function() {
        addItemRow();
    });
    
    // Form submission
    $('#amcForm').on('submit', function(e) {
        e.preventDefault();
        if (!isFormSubmitting) {
            submitForm();
        }
    });
    
    // Prevent double submission
    $('#amcForm input, #amcForm select, #amcForm textarea').on('input change', function() {
        if (isFormSubmitting) {
            isFormSubmitting = false;
            hideLoadingModal();
        }
    });
}

function initializeCharacterCount() {
    $('#subject').on('input', function() {
        const currentLength = $(this).val().length;
        const maxLength = 200;
        const remaining = maxLength - currentLength;
        
        $('#charCount').text(currentLength);
        
        // Change color based on remaining characters
        if (remaining < 20) {
            $('#charCount').addClass('text-danger').removeClass('text-warning text-primary');
        } else if (remaining < 50) {
            $('#charCount').addClass('text-warning').removeClass('text-danger text-primary');
        } else {
            $('#charCount').addClass('text-primary').removeClass('text-danger text-warning');
        }
    });
}

function addItemRow() {
    rowCounter++;
    const newRow = `
        <tr id="row_${rowCounter}" class="slide-up">
            <td class="text-center">${rowCounter}</td>
            <td><input type="text" name="itemName_${rowCounter}" class="form-control form-control-sm" required></td>
            <td><input type="text" name="make_${rowCounter}" class="form-control form-control-sm" required></td>
            <td><input type="text" name="model_${rowCounter}" class="form-control form-control-sm" required></td>
            <td><input type="number" name="quantity_${rowCounter}" class="form-control form-control-sm" min="1" required></td>
            <td class="text-center">
                <button type="button" class="btn btn-sm btn-danger" onclick="removeRow(${rowCounter})" title="Remove Row">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `;
    
    $('#itemsTable tbody').append(newRow);
    $(`#row_${rowCounter} input:first`).focus();
}

function removeRow(rowId) {
    if ($('#itemsTable tbody tr').length > 1) {
        $(`#row_${rowId}`).fadeOut(300, function() {
            $(this).remove();
            updateSerialNumbers();
        });
    } else {
        showAlert('At least one item is required', 'warning');
    }
}

function updateSerialNumbers() {
    let serialNumber = 1;
    $('#itemsTable tbody tr').each(function(index) {
        // Update serial number display
        $(this).find('td:first').text(serialNumber);
        
        // Update row ID
        $(this).attr('id', 'row_' + serialNumber);
        
        // Update input names
        $(this).find('input').each(function() {
            const currentName = $(this).attr('name');
            if (currentName) {
                const baseName = currentName.split('_');
                $(this).attr('name', baseName + '_' + serialNumber);
            }
        });
        
        // Update remove button onclick
        $(this).find('button[onclick]').attr('onclick', 'removeRow(' + serialNumber + ')');
        
        serialNumber++;
    });
    
    rowCounter = serialNumber - 1;
}

function validateDateRange() {
    const effectFromStr = $('#amcEffectFrom').val();
    const validUptoStr = $('#validUpto').val();
    
    if (effectFromStr && validUptoStr) {
        const effectFromParts = effectFromStr.split('-');
        const validUptoParts = validUptoStr.split('-');
        
        const effectFromDate = new Date(effectFromParts, effectFromParts - 1, effectFromParts);
        const validUptoDate = new Date(validUptoParts, validUptoParts - 1, validUptoParts);
        
        if (effectFromDate >= validUptoDate) {
            $('#validUpto').addClass('is-invalid');
            showAlert('Valid Upto date must be after AMC Effect From date', 'warning');
            return false;
        } else {
            $('#validUpto').removeClass('is-invalid');
            return true;
        }
    }
    
    return true;
}

function validateForm() {
    let isValid = true;
    const errors = [];
    
    $('.form-control, .form-select').removeClass('is-invalid is-valid');
    
    $('#amcForm [required]').each(function() {
        const $field = $(this);
        const value = $field.val().trim();
        
        if (!value) {
            $field.addClass('is-invalid');
            const label = $field.closest('.form-group').find('label').text().replace('*', '').trim();
            errors.push(`${label} is required`);
            isValid = false;
        } else {
            $field.addClass('is-valid');
        }
    });
    
    if (!validateDateRange()) {
        isValid = false;
        errors.push('Invalid date range');
    }
    
    const orderValue = parseFloat($('#amcOrderValue').val());
    if (isNaN(orderValue) || orderValue <= 0) {
        $('#amcOrderValue').addClass('is-invalid');
        errors.push('AMC Order Value must be a positive number');
        isValid = false;
    }
    
    const hasItems = $('#itemsTable tbody tr').length > 0;
    if (!hasItems) {
        errors.push('At least one item is required');
        isValid = false;
    }
    
    $('#itemsTable tbody tr').each(function() {
        const rowNum = $(this).attr('id').split('_');
        const itemName = $(`input[name="itemName_${rowNum}"]`).val().trim();
        const make = $(`input[name="make_${rowNum}"]`).val().trim();
        const model = $(`input[name="model_${rowNum}"]`).val().trim();
        const quantity = $(`input[name="quantity_${rowNum}"]`).val().trim();
        
        if (!itemName || !make || !model || !quantity) {
            errors.push(`Row ${rowNum}: All item fields are required`);
            isValid = false;
        }
        
        if (quantity && (isNaN(quantity) || parseInt(quantity) <= 0)) {
            errors.push(`Row ${rowNum}: Quantity must be a positive number`);
            isValid = false;
        }
    });
    
    if (!isValid) {
        const errorMessage = errors.length > 1 ? 
            'Please fix the following errors:<ul><li>' + errors.join('</li><li>') + '</li></ul>' :
            errors;
        showAlert(errorMessage, 'danger');
    }
    
    return isValid;
}

function submitForm() {
    if (!validateForm()) {
        return;
    }
    
    isFormSubmitting = true;
    showLoadingModal();
    
    const formData = $('#amcForm').serialize();
    
    $.ajax({
        url: 'AMCServlet',
        type: 'POST',
        data: formData,
        dataType: 'json',
        timeout: 30000,
        success: function(response) {
            hideLoadingModal();
            isFormSubmitting = false;
            
            if (response.status === 'success') {
                showAlert('AMC details saved successfully!', 'success');
                setTimeout(function() {
                    resetForm();
                    generateControlNumber();
                }, 2000);
            } else {
                showAlert('Error: ' + (response.message || 'Unknown error occurred'), 'danger');
            }
        },
        error: function(xhr, status, error) {
            hideLoadingModal();
            isFormSubmitting = false;
            
            let errorMessage = 'Error submitting form';
            if (status === 'timeout') {
                errorMessage = 'Request timed out. Please try again.';
            } else if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            } else if (error) {
                errorMessage = 'Error: ' + error;
            }
            
            showAlert(errorMessage, 'danger');
        }
    });
}

function showLoadingModal() {
    $('#loadingModal').modal('show');
}

function hideLoadingModal() {
    $('#loadingModal').modal('hide');
}

function resetForm() {
    $('#amcForm').reset();
    $('.form-control, .form-select').removeClass('is-invalid is-valid');
    $('#charCount').text('0').removeClass('text-danger text-warning').addClass('text-primary');
    $('#itemsTable tbody').empty();
    rowCounter = 0;
    addItemRow();
    $('#controlNoDisplay').addClass('d-none');
    $('html, body').animate({ scrollTop: 0 }, 500);
    $('#amcUserId').focus();
    showAlert('Form has been reset', 'info');
}

function updateFormBasedOnType(type) {
    console.log('Selected tracking type:', type);
    
    const titleMap = {
        'AMC': 'AMC Entry Form',
        'Warranty': 'Warranty Entry Form',
        'Status Tracking': 'Status Tracking Form'
    };
    
    const newTitle = titleMap[type] || 'Entry Form';
    $('.form-section h3').first().html(`<i class="fas fa-edit me-2"></i>${newTitle}`);
}

function showAlert(message, type = 'info') {
    $('.alert:not(#controlNoDisplay)').remove();
    
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show slide-up" role="alert">
            <i class="fas fa-${getIconForType(type)} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    if ($('#controlNoDisplay').length) {
        $('#controlNoDisplay').after(alertHtml);
    } else {
        $('.navigation-section').after(alertHtml);
    }
    
    if (type === 'info' || type === 'success') {
        setTimeout(function() {
            $('.alert-' + type).fadeOut(500);
        }, 5000);
    }
    
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

// Handle page refresh warning if form has been modified
let formModified = false;

$('#amcForm input, #amcForm select, #amcForm textarea').on('input change', function() {
    formModified = true;
});

$(window).on('beforeunload', function() {
    if (formModified && !isFormSubmitting) {
        return 'You have unsaved changes. Are you sure you want to leave?';
    }
});
