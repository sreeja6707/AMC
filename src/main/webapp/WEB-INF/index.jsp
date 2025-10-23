<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AMC/Warranty/Status Tracking</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery UI CSS -->
    <link href="https://code.jquery.com/ui/1.13.2/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/amc-styles.css" rel="stylesheet">
</head>
<body>
    <!-- Session Check -->
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="login.jsp"/>
    </c:if>
    
    <div class="container-fluid">
        <!-- Header Section -->
        <header class="header-section">
            <div class="row">
                <div class="col-md-8">
                    <h1><i class="fas fa-clipboard-list me-3"></i>AMC/Warranty/Status Tracking</h1>
                </div>
                <div class="col-md-4">
                    <div class="user-info">
                        <div class="d-flex justify-content-end align-items-center">
                            <div class="user-details me-3">
                                <small class="d-block text-light">Welcome</small>
                                <strong>${sessionScope.user.fullName}</strong>
                                <small class="d-block">${sessionScope.user.directorate}</small>
                            </div>
                            <div class="dropdown">
                                <button class="btn btn-outline-light btn-sm dropdown-toggle" type="button" 
                                        data-bs-toggle="dropdown">
                                    <i class="fas fa-user-circle"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="AMCServlet?action=list">
                                        <i class="fas fa-list me-2"></i>View AMC List</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="LogoutServlet">
                                        <i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Navigation Section -->
        <div class="navigation-section">
            <div class="tracking-types">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="tracking_type" 
                           id="amc" value="AMC" checked>
                    <label class="form-check-label" for="amc">
                        <i class="fas fa-cogs me-2"></i>AMC
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="tracking_type" 
                           id="warranty" value="Warranty">
                    <label class="form-check-label" for="warranty">
                        <i class="fas fa-shield-alt me-2"></i>Warranty
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="tracking_type" 
                           id="status" value="Status Tracking">
                    <label class="form-check-label" for="status">
                        <i class="fas fa-chart-line me-2"></i>Status Tracking
                    </label>
                </div>
            </div>
        </div>

        <!-- Control Number Display -->
        <div id="controlNoDisplay" class="alert alert-info d-none">
            <i class="fas fa-barcode me-2"></i>
            <strong>Control No: <span id="controlNoValue"></span></strong>
        </div>

        <!-- Main Form -->
        <form id="amcForm" class="needs-validation" novalidate>
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="controlNo" id="controlNo">
            
            <!-- Entry Form Section -->
            <div class="form-section">
                <h3><i class="fas fa-edit me-2"></i>Entry Form</h3>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="amcUserId" class="form-label">
                                1. AMC User ID <span class="required">*</span>
                            </label>
                            <select id="amcUserId" name="amcUserId" class="form-select" required>
                                <option value="">Select Address List as per Directorate</option>
                            </select>
                            <div class="invalid-feedback">
                                Please select an AMC User ID.
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="amcLetterNo" class="form-label">
                                2. AMC Letter No <span class="required">*</span>
                            </label>
                            <input type="text" id="amcLetterNo" name="amcLetterNo" 
                                   class="form-control" pattern="[A-Za-z0-9]+" 
                                   title="Alphanumeric characters only" required>
                            <div class="invalid-feedback">
                                Please enter a valid AMC Letter No (alphanumeric only).
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="amcLetterDate" class="form-label">
                                3. AMC Letter Date <span class="required">*</span>
                            </label>
                            <input type="text" id="amcLetterDate" name="amcLetterDate" 
                                   class="form-control datepicker" placeholder="dd-mm-yyyy" required>
                            <div class="invalid-feedback">
                                Please select AMC Letter Date.
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="subject" class="form-label">
                                4. Subject <span class="required">*</span>
                            </label>
                            <textarea id="subject" name="subject" class="form-control" 
                                    maxlength="200" rows="3" required></textarea>
                            <div class="form-text">
                                <span id="charCount">0</span>/200 characters
                            </div>
                            <div class="invalid-feedback">
                                Please enter a subject (max 200 characters).
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="gemOrderNo" class="form-label">5. Gem Order No</label>
                            <input type="text" id="gemOrderNo" name="gemOrderNo" class="form-control">
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="gemOrderDate" class="form-label">6. Gem Order Date</label>
                            <input type="text" id="gemOrderDate" name="gemOrderDate" 
                                   class="form-control datepicker" placeholder="dd-mm-yyyy">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="amcEffectFrom" class="form-label">
                                7. AMC Effect From <span class="required">*</span>
                            </label>
                            <input type="text" id="amcEffectFrom" name="amcEffectFrom" 
                                   class="form-control datepicker" placeholder="dd-mm-yyyy" required>
                            <div class="invalid-feedback">
                                Please select AMC Effect From date.
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="validUpto" class="form-label">
                                8. Valid Upto <span class="required">*</span>
                            </label>
                            <input type="text" id="validUpto" name="validUpto" 
                                   class="form-control datepicker" placeholder="dd-mm-yyyy" required>
                            <div class="invalid-feedback">
                                Please select Valid Upto date.
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="paymentMode" class="form-label">
                                9. Payment Mode <span class="required">*</span>
                            </label>
                            <select id="paymentMode" name="paymentMode" class="form-select" required>
                                <option value="">Select Payment Mode</option>
                                <option value="Monthly">Monthly</option>
                                <option value="Quarterly">Quarterly</option>
                                <option value="Half yearly">Half yearly</option>
                                <option value="Annually">Annually</option>
                            </select>
                            <div class="invalid-feedback">
                                Please select a payment mode.
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="form-group mb-3">
                            <label for="amcOrderValue" class="form-label">
                                10. AMC Order Value <span class="required">*</span>
                            </label>
                            <div class="input-group">
                                <span class="input-group-text">₹</span>
                                <input type="number" id="amcOrderValue" name="amcOrderValue" 
                                       class="form-control" step="0.01" min="0" required>
                            </div>
                            <div class="invalid-feedback">
                                Please enter a valid order value.
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Child Table Section -->
            <div class="form-section">
                <h3><i class="fas fa-list me-2"></i>AMC Warranty Item Details</h3>
                
                <div class="table-responsive">
                    <table id="itemsTable" class="table table-bordered table-hover">
                        <thead class="table-primary">
                            <tr>
                                <th style="width: 60px;">S.No</th>
                                <th>Item Name <span class="required">*</span></th>
                                <th>Make <span class="required">*</span></th>
                                <th>Model <span class="required">*</span></th>
                                <th style="width: 100px;">Quantity <span class="required">*</span></th>
                                <th style="width: 100px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr id="row_1">
                                <td class="text-center">1</td>
                                <td><input type="text" name="itemName_1" class="form-control form-control-sm" required></td>
                                <td><input type="text" name="make_1" class="form-control form-control-sm" required></td>
                                <td><input type="text" name="model_1" class="form-control form-control-sm" required></td>
                                <td><input type="number" name="quantity_1" class="form-control form-control-sm" min="1" required></td>
                                <td class="text-center">
                                    <button type="button" class="btn btn-sm btn-danger" onclick="removeRow(1)">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <div class="text-end">
                    <button type="button" id="addRowBtn" class="btn btn-success">
                        <i class="fas fa-plus me-2"></i>Add Item
                    </button>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-lg">
                    <i class="fas fa-save me-2"></i>Save AMC Details
                </button>
                <button type="button" class="btn btn-secondary btn-lg ms-2" onclick="resetForm()">
                    <i class="fas fa-undo me-2"></i>Reset
                </button>
                <a href="AMCServlet?action=list" class="btn btn-info btn-lg ms-2">
                    <i class="fas fa-list me-2"></i>View List
                </a>
            </div>
        </form>
    </div>
    
    <!-- Loading Modal -->
    <div class="modal fade" id="loadingModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-sm modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body text-center">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2 mb-0">Processing...</p>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- jQuery UI -->
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="js/amc-scripts.js"></script>
</body>
</html>
