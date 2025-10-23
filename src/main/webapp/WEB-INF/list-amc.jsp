<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AMC List - AMC/Warranty/Status Tracking</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
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
                    <h1><i class="fas fa-list me-3"></i>AMC Records List</h1>
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
                                    <li><a class="dropdown-item" href="index.jsp">
                                        <i class="fas fa-plus me-2"></i>New AMC Entry</a></li>
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
        
        <!-- Action Buttons -->
        <div class="mb-4">
            <a href="index.jsp" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>New AMC Entry
            </a>
            <button class="btn btn-success" onclick="exportToExcel()">
                <i class="fas fa-file-excel me-2"></i>Export to Excel
            </button>
        </div>
        
        <!-- AMC List Table -->
        <div class="form-section">
            <div class="table-responsive">
                <table id="amcTable" class="table table-striped table-hover">
                    <thead class="table-primary">
                        <tr>
                            <th>Control No</th>
                            <th>AMC Letter No</th>
                            <th>Subject</th>
                            <th>AMC Effect From</th>
                            <th>Valid Upto</th>
                            <th>Payment Mode</th>
                            <th>Order Value</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="amc" items="${amcList}">
                            <tr>
                                <td>
                                    <strong>${amc.controlNo}</strong>
                                </td>
                                <td>${amc.amcLetterNo}</td>
                                <td>
                                    <div class="text-truncate" style="max-width: 200px;" 
                                         title="${amc.subject}">
                                        ${amc.subject}
                                    </div>
                                </td>
                                <td>
                                    <fmt:formatDate value="${amc.amcEffectFrom}" pattern="dd-MM-yyyy" />
                                </td>
                                <td>
                                    <fmt:formatDate value="${amc.validUpto}" pattern="dd-MM-yyyy" />
                                    <c:set var="today" value="<%= new java.util.Date() %>" />
                                    <c:if test="${amc.validUpto lt today}">
                                        <span class="badge bg-danger ms-1">Expired</span>
                                    </c:if>
                                </td>
                                <td>
                                    <span class="badge bg-info">${amc.paymentMode}</span>
                                </td>
                                <td class="text-end">
                                    ₹<fmt:formatNumber value="${amc.amcOrderValue}" pattern="#,##0.00" />
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${amc.status == 'Active'}">
                                            <span class="badge bg-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">${amc.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <button type="button" class="btn btn-sm btn-outline-primary" 
                                                onclick="viewDetails('${amc.controlNo}')" 
                                                title="View Details">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-outline-secondary" 
                                                onclick="editAMC('${amc.controlNo}')" 
                                                title="Edit">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <!-- View Details Modal -->
    <div class="modal fade" id="detailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">AMC Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="modalBody">
                    <!-- Details will be loaded here -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
    <!-- Custom JS -->
    <script src="js/amc-list.js"></script>
</body>
</html>
