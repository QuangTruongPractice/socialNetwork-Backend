Mạng Xã Hội Cựu Sinh Viên - Backend API
Mô tả dự án
Backend API cho hệ thống mạng xã hội dành cho cựu sinh viên, giảng viên và quản trị viên trường đại học. Hệ thống được xây dựng với Spring Boot, hỗ trợ chia sẻ thông tin, tương tác xã hội, khảo sát và thống kê với kiến trúc MVC.

Tính năng chính

1.  Xác thực & Phân quyền

- Sử dụng **Spring Security** để quản lý đăng nhập và phân quyền.
- Tích hợp **JWT**:
  - Khi đăng nhập thành công, hệ thống sinh **Access Token**.
  - Token được dùng để truy cập API bảo mật.
- Phân quyền:
  - **ADMIN**: Quản trị toàn bộ hệ thống (truy cập trang quản trị).
  - **LECTURER**: Giảng viên (được cấp tài khoản từ admin, đổi mật khẩu trong 24h).
  - **ALUMNI**: Cựu sinh viên.
  - Người dùng thông thường **không thể truy cập trang quản trị**.

2.  Quản lý người dùng

- Đăng ký cựu sinh viên:
  - Upload avatar (lưu trữ trên **Cloudinary**).
  - Nhập mã số sinh viên để xác minh.
  - Dữ liệu được **validation** kỹ trước khi lưu.
- Giảng viên:
  - Được admin tạo tài khoản.
  - Nhận email thông tin tài khoản (qua **Spring Mail**).
  - Bắt buộc đổi mật khẩu trong **24h**.

3.  Bài viết & Tương tác

- CRUD bài viết.
- Like, Thả tim, Haha.
- Bình luận:
  - CRUD comment.
  - Chủ bài viết có thể xóa bất kỳ comment.
  - Người viết comment chỉ được sửa/xóa của mình.
- Khóa bình luận cho bài viết.

4.  Khảo sát & Thông báo

- Tạo khảo sát (admin).
- Gửi thông báo sự kiện qua email (Cloud Mail).
- Thống kê kết quả khảo sát.

5.  Thống kê dữ liệu

- API trả về dữ liệu thống kê theo:
- Có thể tích hợp Chart.js / Google Charts ở trang quản trị.
- Thống kê user, post theo năm, tháng, quý.

Công nghệ sử dụng

- Ngôn ngữ: Java 21+
- Framework: Spring MVC, Spring Boot
- Bảo mật: Spring Security, JWT
- View Engine: Thymeleaf
- ORM: Hibernate
- CSDL: MySQL
- Lưu trữ ảnh: Cloudinary
- Email: Spring Mail (JavaMailSender)
- Kiểm tra dữ liệu: Bean Validation (Hibernate Validator)

Kiến trúc dự án
src/main/java/com/tqt

- conponents/ Xử lý tự động hóa bằng Scheduler
- config/ Cấu hình Spring MVC, Security, JWT, Cloudinary, Mail
- controller/ Xử lý request từ quản trị & API
- dto/ Dữ liệu trả về
- pojo/ Các hằng số liệt kê
- repository/ Tầng truy vấn dữ liệu (Repositories)
- service/ Xử lý nghiệp vụ (Services / MicroServices)
- filters/ Kiểm tra và xác thực JWT token trong mỗi request
- utils/ Xử lý generate token và claims token

Cài đặt & Chạy backend

1. Clone dự án

git clone https://github.com/QuangTruongPractice/socialNetwork-Backend.git

2. Cấu hình Database trong databases.properties

hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.showSql=true
hibernate.connection.driverClass=com.mysql.cj.jdbc.Driver
hibernate.connection.url=jdbc:mysql://localhost:3306/socialnetworkdb <- Sửa lại theo database của bạn
hibernate.connection.username=root <- Sửa lại theo database của bạn  
hibernate.connection.password=Admin@123 <- Sửa lại theo database của bạn

Cấu hình Cloudinary ở SpringSecurityConfig và Mail ở MailConfig

File database mẫu: socialnetworkdb

3. Chạy ứng dụng
   Mở IDE NetBeans
   Thêm Server tomcat (khuyên dùng bản 11.0.4)
   Build để tải các thư viện cần thiết ở pom.xml
   Run để chạy
4. Truy cập
   API: http://localhost:8080/api/...
   Trang quản trị (Thymeleaf): http://localhost:8080/admin/...

API Documentation

-> API Endpoints Chính

Authentication

- POST `/api/auth/login` - Đăng nhập
- POST `/api/auth/register` - Đăng ký cựu sinh viên

Posts

- GET `/api/secure/posts` - Lấy danh sách bài viết
- POST `/api/secure/posts` - Tạo bài viết mới
- PUT `/api/secure/posts/{id}` - Cập nhật bài viết
- DELETE `/api/secure/posts/{id}` - Xóa bài viết
- POST `/api/secure/posts/survey` - Tạo khảo sát
- POST `/api/secure/posts/survey/vote` -Vote khảo sát
- POST `/api/secure/posts/invitation` -Tạo thư mời

Admin

- GET `/admin/dashboard` - Trang quản trị
- POST `/admin/users/{id}/approve` - Duyệt cựu sinh viên

Nhóm thực hiện

1.  Trần Quang Trường

Contact

- Email: tranquangtruong25@gmail.com
- GitHub: https://github.com/QuangTruongPractice
