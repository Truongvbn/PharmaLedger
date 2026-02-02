# PharmaLedger Auto Alert System

Hệ thống quản lý tồn kho dược phẩm và cảnh báo tự động, xây dựng trên nền tảng Java Web MVC2.

## 🏗️ Kiến trúc Hệ thống

Hệ thống tuân thủ nghiêm ngặt mô hình **MVC2 (Model-View-Controller type 2)**:

### 1. View Layer (JSP)
- **Vị trí**: `web/`
- **Vai trò**: Hiển thị giao diện người dùng, không chứa logic nghiệp vụ.
- **Công nghệ**: JSP, JSTL, EL (Expression Language).
- **Styling**: Vanilla CSS (`web/assets/css/style.css`) với thiết kế hiện đại, responsive.

### 2. Controller Layer (Servlet)
- **Vị trí**: `src/java/com/pharmaledger/controllers/`
- **Front Controller**: `MainController` - Điểm tiếp nhận duy nhất cho mọi request.
  - Đọc tham số `action`.
  - Điều hướng (dispatch) đến Controller chức năng tương ứng.
- **Action Controllers**: `DrugController`, `InboundController`, v.v.
  - Tương tác với DAO để lấy/lưu dữ liệu.
  - Đặt dữ liệu vào `request` attribute.
  - Forward đến JSP tiếp theo.

### 3. Model Layer (DAO + DTO)
- **DTO (Data Transfer Object)**: `src/java/com/pharmaledger/dto/`
  - Mang dữ liệu giữa các lớp, tương ứng 1-1 với bảng DB.
  - Implement `Serializable`.
- **DAO (Data Access Object)**: `src/java/com/pharmaledger/dao/`
  - Chứa logic truy xuất SQL Server.
  - Sử dụng `DBUtils` để quản lý kết nối.

### 4. Database Layer
- **Hệ quản trị**: SQL Server.
- **Schema**: `database/pharmaledger_schema.sql` (17 bảng).
- **Kết nối**: `DBUtils.java` sử dụng `sqljdbc42.jar` (hoặc tương đương).

---

## 🔄 Luồng dữ liệu (Request Flow)

Ví dụ quy trình hiển thị danh sách thuốc:

1. **User Request**: Browser gửi `GET /MainController?action=DrugList`
2. **MainController**: 
   - Nhận request.
   - Xác định `action = "DrugList"`.
   - Forward sang `DrugController`.
3. **DrugController**:
   - Gọi `DrugDAO.findAll()`.
4. **DrugDAO**:
   - Mở kết nối SQL.
   - Execute `SELECT * FROM Drugs`.
   - Map `ResultSet` sang `List<DrugDTO>`.
   - Trả về List.
5. **DrugController**:
   - `request.setAttribute("LIST_DATA", list)`.
   - Forward sang `drugList.jsp`.
6. **drugList.jsp**:
   - Dùng `<c:forEach items="${LIST_DATA}">` để render bảng HTML.

---

## 🚀 Hướng dẫn Cài đặt & Triển khai

### Yêu cầu
- JDK 8+
- Apache Tomcat 8.5/9.0
- SQL Server 2017+
- NetBeans / Eclipse / IntelliJ IDEA

### Các bước cài đặt
1. **Cơ sở dữ liệu**:
   - Mở SQL Server Management Studio (SSMS).
   - Chạy script `database/pharmaledger_schema.sql` để tạo DB và dữ liệu mẫu.
   
2. **Cấu hình Code**:
   - Mở `src/java/com/pharmaledger/utils/DBUtils.java`.
   - Cập nhật thông tin `USER_NAME` và `PASSWORD` của SQL Server local.

3. **Chạy ứng dụng**:
   - Clean & Build Project.
   - Deploy lên Tomcat.
   - Truy cập: `http://localhost:8080/PharmaLedger/`.
   - **Tài khoản mặc định**: `admin` / `admin`.

---

## 🛠️ Hướng dẫn Mở rộng Tính năng

### Thêm một Module mới (Ví dụ: Promotion)

1. **Database**:
   - Tạo bảng `Promotions` trong SQL Server.
   
2. **Model**:
   - Tạo `PromotionDTO.java` trong gói `dto`.
   - Tạo `PromotionDAO.java` trong gói `dao` với các phương thức CRUD.

3. **Controller**:
   - Tạo `PromotionController.java` trong gói `controllers`.
   - Xử lý các action: `List`, `Create`, `Save`.
   - Đăng ký action mới vào `MainController` (hoặc cấu hình nếu dùng dynamic mapping).

4. **View**:
   - Tạo `promotionList.jsp` và `promotionForm.jsp`.
   - Thêm link vào `header.jsp`.

### Tích hợp AI
- Logic AI hiện tại đang để placeholder trong `ForecastController`.
- Để tích hợp thật:
  1. Viết Python Flask/FastAPI service bọc mô hình AI.
  2. Dùng Java `HttpClient` trong `ForecastDAO` để gọi API Python.
  3. Lưu kết quả về DB (`ForecastPoints`).

---

## 📂 Cấu trúc thư mục

```
PharmaLedger/
├── database/               # SQL Scripts
├── src/java/com/pharmaledger/
│   ├── controllers/        # MainController + 10 Functional Controllers
│   ├── dao/                # 13 DAOs
│   ├── dto/                # 17 DTOs
│   └── utils/              # DBUtils, Constants
└── web/                    # Web Root
    ├── assets/css/         # Styles (style.css)
    ├── common/             # Header/Footer
    ├── WEB-INF/            # web.xml
    └── *.jsp               # Pages
```
