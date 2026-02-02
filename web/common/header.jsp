<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <header class="main-header">
            <div class="header-content">
                <div class="logo">
                    <a href="MainController?action=InventoryDashboard">
                        <h1>PharmaLedger</h1>
                    </a>
                </div>

                <nav class="main-nav">
                    <ul>
                        <li><a href="MainController?action=InventoryDashboard">Tồn kho</a></li>
                        <li><a href="MainController?action=DrugList">Thuốc</a></li>
                        <li><a href="MainController?action=InboundList">Nhập kho</a></li>
                        <li><a href="MainController?action=OutboundList">Xuất kho</a></li>
                        <li><a href="MainController?action=AlertsOpen">Cảnh báo</a></li>
                        <li><a href="MainController?action=CaseList">Case</a></li>
                        <li><a href="MainController?action=ReportDashboard">Báo cáo</a></li>
                    </ul>
                </nav>

                <div class="user-info">
                    <c:if test="${not empty sessionScope.LOGIN_USER}">
                        <span>Xin chào, ${sessionScope.LOGIN_USER.fullname}</span>
                        <a href="MainController?action=Logout" class="btn btn-sm">Đăng xuất</a>
                    </c:if>
                </div>
            </div>
        </header>