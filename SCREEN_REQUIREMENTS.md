# PharmaLedger Auto Alert - Danh sách Màn hình (JSP)

Tài liệu này liệt kê toàn bộ 41 màn hình cần thiết cho hệ thống theo yêu cầu "FINAL PROJECT".
Hiện tại trạng thái là **MISSING (Thiếu)** do đã được xóa sạch để tập trung vào Backend.

## 📊 Tổng quan
- **Tổng số màn hình**: 41
- **Tổng số module**: 7
- **Trạng thái**: 41/41 Chưa có (Pending Implementation)

## 1. Module Xác thực & Quản trị (4 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `login.jsp` | Đăng nhập hệ thống | ❌ Thiếu |
| `userList.jsp` | Danh sách người dùng (CRUD) | ❌ Thiếu |
| `roleList.jsp` | Quản lý phân quyền (Role, Permission) | ❌ Thiếu |
| `auditLog.jsp` | Nhật ký truy vết hệ thống | ❌ Thiếu |

## 2. Module Danh mục & NCC (5 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `drugList.jsp` | Danh sách thuốc (Search, Filter) | ❌ Thiếu |
| `drugForm.jsp` | Thêm/Sửa thuốc | ❌ Thiếu |
| `drugDetail.jsp` | Chi tiết thuốc & Lịch sử | ❌ Thiếu |
| `supplierList.jsp` | Danh sách nhà cung cấp | ❌ Thiếu |
| `supplierForm.jsp` | Thêm/Sửa nhà cung cấp | ❌ Thiếu |

## 3. Module Kho & Tồn (8 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `batchList.jsp` | Quản lý lô thuốc | ❌ Thiếu |
| `batchForm.jsp` | Thêm/Sửa lô | ❌ Thiếu |
| `locationList.jsp` | Danh sách vị trí lưu trữ | ❌ Thiếu |
| `locationForm.jsp` | Sơ đồ kho | ❌ Thiếu |
| `inventoryDashboard.jsp` | Tổng quan tồn kho (Main Dashboard) | ❌ Thiếu |
| `stockSearch.jsp` | Tra cứu tồn kho nâng cao (Multi-criteria) | ❌ Thiếu |
| `nearExpirySearch.jsp` | Tìm thuốc cận hạn & Đề xuất luân chuyển | ❌ Thiếu |
| `lowStockSearch.jsp` | Tìm thuốc dưới định mức tồn | ❌ Thiếu |

## 4. Module Nhập - Xuất (6 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `inboundList.jsp` | Danh sách phiếu nhập | ❌ Thiếu |
| `inboundForm.jsp` | Tạo phiếu nhập mới | ❌ Thiếu |
| `outboundList.jsp` | Danh sách phiếu xuất | ❌ Thiếu |
| `outboundForm.jsp` | Tạo phiếu xuất mới | ❌ Thiếu |
| `ledgerList.jsp` | Sổ cái tồn kho (Audit Trail) | ❌ Thiếu |
| `ledgerDetail.jsp` | Chi tiết giao dịch sổ cái | ❌ Thiếu |

## 5. Module Cảnh báo & Case (9 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `ruleList.jsp` | Cấu hình quy tắc cảnh báo | ❌ Thiếu |
| `ruleForm.jsp` | Thêm quy tắc mới | ❌ Thiếu |
| `alertsList.jsp` | Danh sách sự kiện cảnh báo (Inbox) | ❌ Thiếu |
| `alertDetail.jsp` | Chi tiết cảnh báo | ❌ Thiếu |
| `alertExplain.jsp` | Giải thích lý do cảnh báo (AI/Rule) | ❌ Thiếu |
| `caseList.jsp` | Danh sách Case xử lý | ❌ Thiếu |
| `caseDetail.jsp` | Chi tiết Case & Bằng chứng | ❌ Thiếu |
| `caseAssign.jsp` | Phân công xử lý | ❌ Thiếu |
| `caseClose.jsp` | Đóng Case & Ghi nhận kết quả | ❌ Thiếu |

## 6. Module AI & Dự báo (4 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `forecastDashboard.jsp` | Biểu đồ dự báo nhu cầu | ❌ Thiếu |
| `forecastDetail.jsp` | Chi tiết dự báo từng mặt hàng | ❌ Thiếu |
| `reorderSuggest.jsp` | Gợi ý nhập hàng tự động | ❌ Thiếu |
| `reorderExplain.jsp` | Giải thích đề xuất nhập hàng | ❌ Thiếu |

## 7. Module Báo cáo (5 màn hình)
| Tên File | Chức năng | Trạng thái |
|----------|-----------|------------|
| `reportStockout.jsp` | Tỷ lệ hết hàng (Stockout Rate) | ❌ Thiếu |
| `reportExpiryWaste.jsp` | Chi phí hủy thuốc hết hạn | ❌ Thiếu |
| `reportForecastQuality.jsp` | Độ chính xác dự báo (MAE/RMSE) | ❌ Thiếu |
| `reportAlertQuality.jsp` | Độ chính xác cảnh báo | ❌ Thiếu |
| `reportOpsLatency.jsp` | Thời gian phản hồi vận hành | ❌ Thiếu |

---

## 📝 Ghi chú Phát triển Frontend

Do toàn bộ Front-end hiện đang thiếu, lộ trình phát triển tiếp theo (Next Phase) nên là:

1. **Phase 1: Core Layout & Auth**
   - Re-implement `login.jsp`
   - Setup `header.jsp`, `footer.jsp` (đã có trong common, cần kiểm tra lại)

2. **Phase 2: Master Data (CRUD đơn giản)**
   - Implement nhóm Danh mục & NCC (`drugList`, `supplierList`...)

3. **Phase 3: Transaction (Nghiệp vụ chính)**
   - Implement Nhập/Xuất kho (`inbound`, `outbound`)

4. **Phase 4: Advanced Features**
   - Implement Dashboard, Search nâng cao, Reports.
