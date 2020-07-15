# Warehouse Management

## Giới thiệu đề tài

Bảo quản nông sản tươi sau thu hoạch ở nước ta nói riêng và trên thế giới nói chung ngày càng là vấn đề được quan tâm hàng đầu. Đây được xem là công đoạn thiết yếu nhằm bảo đảm chất lượng đầu ra của sản phẩm cùng những vấn đề liên quan. Đa số các loại nông sản như rau quả là loại nông sản khó bảo quản vì lượng nước trong rau quả cao, có thể chiếm 85%-95% khối lượng. Do đó loại thực phẩm này rất dễ bị hỏng trong quá trình bảo quản. Theo thống kê từ VCCI, mức độ tổn thất với rau quả và trái cây có thể lên tới 45%.

Vì nhu cầu nông sản cần được bảo quản tốt hơn và ít tốt chi phí cũng như thời gian nhất. Nên nhóm chúng em quyết định lên ý tưởng cho một ứng dụng IoT điều khiển kho nông sản. Đây sẽ là một kho nông sản thông minh được giám sát thông qua các cảm biến khác nhau được kết nối với mạng internet. Dữ liệu từ các cảm biến truyền về sẽ là dữ liệu thời gian thực. Ngoài việc quan sát ra, ta còn có thể điều khiển từ xa các thiết bị đầu ra tùy thuộc vào điều kiện hiện tại. Không những thế ứng dụng còn có nhiều tính năng mở rộng giúp tối ưu hiệu suất sử dụng.

Cụ thể, trong dự án này nhóm chúng em mô phỏng một kho nông sản thực tế.

Thiết bị input của kho gồm 2 cảm biến là cảm biến nhiệt độ không khí và cảm biến độ ẩm không khí (Thiết bị demo là TempHumi). Thiết bị output là một động cơ quạt (Thiết bị demo là Speaker).

Người dùng sẽ tương tác với một ứng dụng di động đã kết nối với các thiết bị trong kho thông qua internet. Ứng dụng giúp người dùng xem thông số các thiết bị input theo thời gian thực và chủ động điều khiển thiết bị output theo cơ chế định sẵn hoặc theo cách thủ công.

Cơ chế tổng quát được trình bày trong hình dưới đây:

![](https://scontent.fvca1-2.fna.fbcdn.net/v/t1.15752-9/108943767_1361991273971618_5810066279860407823_n.png?_nc_cat=107&_nc_sid=ae9488&_nc_ohc=sY38WHvrKBIAX_NbPUG&_nc_ht=scontent.fvca1-2.fna&oh=445346c3bcc20bb7f4b48ac89c8975c8&oe=5F3307FC)

## Thành viên nhóm

Giáo viên hướng dẫn: Bùi Xuân Giang

Sinh viên thực hiện:
- Hồ Công Sơn     1712964
- Tô Phú Quý      1712892
- Phan Tấn Quốc   1712855
- Hoàng Văn Sang  1712928
- Nguyễn Văn Sỹ   1712996

## Thiết kế hệ thống

Hệ thống dự án gồm có 5 thành phần sau:
- Người dùng
- Ứng dụng
- Server
- Kho nông sản
- Database (Firebase)

Các thành phần sẽ kết hợp với nhau như hình dưới đây để tạo thành một hệ thống hoàn chỉnh.

![](https://scontent.fvca1-2.fna.fbcdn.net/v/t1.15752-9/109051684_734800797271173_8556610505957094046_n.png?_nc_cat=104&_nc_sid=ae9488&_nc_ohc=-GUEsBig_VsAX_SPFaq&_nc_ht=scontent.fvca1-2.fna&oh=c497b76455da61f75e0d7bb216da4fbf&oe=5F35DDC2)

## Mô tả chi tiết
### Đăng nhập và đăng ký

Ngay khi mở ứng dụng, ta sẽ được đưa tới màn hình đăng nhập. Nếu đã có tài khoản, ta sẽ nhập tài khoản và nhấn đăng nhập.

Ứng dụng sẽ kiểm tra xem ta đã nhập đúng yêu cầu chưa. Nếu đúng ứng dụng sẽ truy cập vào Database và kiểm tra xem tài khoản và mật khẩu có đúng hay không. Nếu đúng, đăng nhập thành công. Nếu sai, ứng dụng sẽ báo lỗi cụ thể để người dùng kiểm tra.

![](https://scontent.fvca1-2.fna.fbcdn.net/v/t1.15752-9/109014116_2481252698834665_1348439733629559756_n.png?_nc_cat=101&_nc_sid=ae9488&_nc_ohc=WMHYrpuqS0UAX_xuJfq&_nc_ht=scontent.fvca1-2.fna&oh=cb7744480f568173a94f209bd6862df6&oe=5F33CD8C)

Nếu chưa có tài khoản, ta sẽ bấm nút đăng kí và ứng dụng sẽ chuyển đến màn hình đăng kí.

![](https://scontent.fvca1-1.fna.fbcdn.net/v/t1.15752-9/107734806_308129543570024_6502924941859991031_n.png?_nc_cat=106&_nc_sid=ae9488&_nc_ohc=2r3WII6-sdkAX9bGn4I&_nc_ht=scontent.fvca1-1.fna&oh=8844d0fbc8cb9254d7966f523c0d52a0&oe=5F33A405)

Sau khi được đưa đến màn hình đăng kí, ta thực hiện nhập các thông tin và nhấn đăng kí.

### Trang chủ

