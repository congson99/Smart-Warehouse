# Warehouse Management

## Giới thiệu đề tài

Bảo quản nông sản tươi sau thu hoạch ở nước ta nói riêng và trên thế giới nói chung ngày càng là vấn đề được quan tâm hàng đầu. Đây được xem là công đoạn thiết yếu nhằm bảo đảm chất lượng đầu ra của sản phẩm cùng những vấn đề liên quan. Đa số các loại nông sản như rau quả là loại nông sản khó bảo quản vì lượng nước trong rau quả cao, có thể chiếm 85%-95% khối lượng. Do đó loại thực phẩm này rất dễ bị hỏng trong quá trình bảo quản. Theo thống kê từ VCCI, mức độ tổn thất với rau quả và trái cây có thể lên tới 45%.

Vì nhu cầu nông sản cần được bảo quản tốt hơn và ít tốt chi phí cũng như thời gian nhất. Nên nhóm chúng em quyết định lên ý tưởng cho một ứng dụng IoT điều khiển kho nông sản. Đây sẽ là một kho nông sản thông minh được giám sát thông qua các cảm biến khác nhau được kết nối với mạng internet. Dữ liệu từ các cảm biến truyền về sẽ là dữ liệu thời gian thực. Ngoài việc quan sát ra, ta còn có thể điều khiển từ xa các thiết bị đầu ra tùy thuộc vào điều kiện hiện tại. Không những thế ứng dụng còn có nhiều tính năng mở rộng giúp tối ưu hiệu suất sử dụng.

Cụ thể, trong dự án này nhóm chúng em mô phỏng một kho nông sản thực tế.

Thiết bị input của kho gồm 2 cảm biến là cảm biến nhiệt độ không khí và cảm biến độ ẩm không khí (Thiết bị demo là TempHumi). Thiết bị output là một động cơ quạt (Thiết bị demo là Speaker).

Người dùng sẽ tương tác với một ứng dụng di động đã kết nối với các thiết bị trong kho thông qua internet. Ứng dụng giúp người dùng xem thông số các thiết bị input theo thời gian thực và chủ động điều khiển thiết bị output theo cơ chế định sẵn hoặc theo cách thủ công.

Cơ chế tổng quát được trình bày trong hình dưới đây:

![](https://github.com/congson99/Warehouse-Management/blob/master/Report/IntroDiagram.png?raw=true)

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

![](https://github.com/congson99/Warehouse-Management/blob/master/Report/SystemDiagram.png?raw=true)

## Mô tả chi tiết
### Đăng nhập và đăng ký

Ngay khi mở ứng dụng, ta sẽ được đưa tới màn hình đăng nhập. Nếu đã có tài khoản, ta sẽ nhập tài khoản và nhấn đăng nhập.

Ứng dụng sẽ kiểm tra xem ta đã nhập đúng yêu cầu chưa. Nếu đúng ứng dụng sẽ truy cập vào Database và kiểm tra xem tài khoản và mật khẩu có đúng hay không. Nếu đúng, đăng nhập thành công. Nếu sai, ứng dụng sẽ báo lỗi cụ thể để người dùng kiểm tra.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/login2.png?raw=true" width="350" title="hover text">
</p>

Nếu chưa có tài khoản, ta sẽ bấm nút đăng kí và ứng dụng sẽ chuyển đến màn hình đăng kí.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/register2.png?raw=true" width="350" title="hover text">
</p>

Sau khi được đưa đến màn hình đăng kí, ta thực hiện nhập các thông tin và nhấn đăng kí.

### Trang chủ

Sau khi đăng nhập thành công, ngừoi dùng sẽ được đưa đến giao diện trang chủ, tại màn hình này người dùng sẽ lựa chọn đi tới tính năng mong muốn.

Trang chủ sẽ hiển thị Avatar, tên người dùng và đầy đủ các chức năng của ứng dụng.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/home.png?raw=true" width="350" title="hover text">
</p>

### Xem và thiết lập môi trường kho

Đây chính là phần trọng tâm của dự án.

Tại đây người dùng sẽ theo dõi thông số từ các thiết bị theo thời gian thực.

Ngoài ra ngừoi dùng còn có thể thiết lập "nhiệt độ tiêu chuẩn" để quạt chạy với cơ chế như sau. Nếu nhiệt độ thực tế lớn hơn nhiệt độ tiêu chuẩn x độ quạt sẽ chạy với công suất (2x)% và công suất tối đa của quạt là 100%.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/env2.png?raw=true" width="350" title="hover text">
</p>

Để thực hiện tự động bật tắt quạt, ứng dụng gọi đến hàm startMQTTTempHumi để lấy thông số cảm biến và thực hiện tính toán. Sau đó sẽ gọi hàm sendDataToMQTT (hàm này sẽ được trình bày ở phần kết nối server) để thay đổi thông số quạt (Speaker).

`private void startMQTTTempHumi (String ID, String topic, final  TextView a,final TextView b, final  TextView cel) {

        mqttHelper = new MQTTHelper(getApplicationContext(), ID, topic);
        mqttHelper.setCallBack(new MqttCallbackExtended() {
        
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(message.toString());
                JSONArray jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String device_id = jsonObject.getString("device_id");
                    String location = jsonObject.getString("values");

                    JSONArray arr_value = new JSONArray(location);
                    a.setText(arr_value.getString(0));
                    b.setText(arr_value.getString(1));
                    float longitude = Float.parseFloat(a.getText().toString());
                    if (!a.getText().toString().equals("") && !cel.getText().toString().equals("")){
                        if(longitude > Integer.parseInt(cel.getText().toString()) + 50){
                            sendDataToMQTT("Speaker", "1", "5000");
                        }
                        else {
                            if(longitude <= Integer.parseInt(cel.getText().toString())){
                                sendDataToMQTT("Speaker", "0", "1");
                            }
                            else {
                                String temp = String.valueOf((Integer.parseInt(a.getText().toString())-Integer.parseInt(cel.getText().toString()))*2*50);
                                sendDataToMQTT("Speaker", "1", temp);
                            }
                        }
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }`
