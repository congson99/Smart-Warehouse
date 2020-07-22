**Trường Đại Học Bách Khoa Tp.Hồ Chí Minh**

**Khoa Khoa Học và Kỹ Thuật Máy Tính**

___

# Warehouse Management
## Môn Thực tập đồ án đa ngành

Giáo viên hướng dẫn: Bùi Xuân Giang

Sinh viên thực hiện:
- Hồ Công Sơn     1712964
- Tô Phú Quý      1712892
- Phan Tấn Quốc   1712855
- Hoàng Văn Sang  1712928
- Nguyễn Văn Sỹ   1712996

## Giới thiệu đề tài

Bảo quản nông sản tươi sau thu hoạch ở nước ta nói riêng và trên thế giới nói chung ngày càng là vấn đề được quan tâm hàng đầu. Đây được xem là công đoạn thiết yếu nhằm bảo đảm chất lượng đầu ra của sản phẩm cùng những vấn đề liên quan. Đa số các loại nông sản như rau quả là loại nông sản khó bảo quản vì lượng nước trong rau quả cao, có thể chiếm 85%-95% khối lượng. Do đó loại thực phẩm này rất dễ bị hỏng trong quá trình bảo quản. Theo thống kê từ VCCI, mức độ tổn thất với rau quả và trái cây có thể lên tới 45%.

Vì nhu cầu nông sản cần được bảo quản tốt hơn và ít tốt chi phí cũng như thời gian nhất. Nên nhóm chúng em quyết định lên ý tưởng cho một ứng dụng IoT điều khiển kho nông sản. Đây sẽ là một kho nông sản thông minh được giám sát thông qua các cảm biến khác nhau được kết nối với mạng internet. Dữ liệu từ các cảm biến truyền về sẽ là dữ liệu thời gian thực. Ngoài việc quan sát ra, ta còn có thể điều khiển từ xa các thiết bị đầu ra tùy thuộc vào điều kiện hiện tại. Không những thế ứng dụng còn có nhiều tính năng mở rộng giúp tối ưu hiệu suất sử dụng.

Cụ thể, trong dự án này nhóm chúng em mô phỏng một kho nông sản thực tế.

Thiết bị input của kho gồm 2 cảm biến là cảm biến nhiệt độ không khí và cảm biến độ ẩm không khí (Thiết bị demo là TempHumi). Thiết bị output là một động cơ quạt (Thiết bị demo là Speaker).

Người dùng sẽ tương tác với một ứng dụng di động đã kết nối với các thiết bị trong kho thông qua internet. Ứng dụng giúp người dùng xem thông số các thiết bị input theo thời gian thực và chủ động điều khiển thiết bị output theo cơ chế định sẵn hoặc theo cách thủ công.

Cơ chế tổng quát được trình bày trong hình dưới đây:

![](https://github.com/congson99/Warehouse-Management/blob/master/Report/IntroDiagram.png?raw=true)

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

Ngoài ra, trước khi mật khẩu được đưa lên Database, nó sẽ được mã hoá theo chuẩn Advanced Encryption Standard với key được đặt sẵn.

Việc mã hoá này sẽ đảm bảo phần nào việc tài khoản người dùng không bị đánh cắp khi có kẻ xâm nhập vào Database hoặc tấn công từ mạng internet.
Hàm mã hoá sẽ như sau:

    private String AESEncryptionMethod(String string){
      byte[] encryptionKey = {21, 35, 44, 69, 11, 55, 19, 99, 18, 20, 15, 44, 77, 23, 76, 12};
      secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
      byte[] stringByte = string.getBytes();
      byte[] encryptedByte = new byte[stringByte.length];
      try {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        encryptedByte = cipher.doFinal(stringByte);
      } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
        e.printStackTrace(); 
      }
      String returnString = null;
      try {
        returnString = new String(encryptedByte, "ISO-8859-1"); 
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace(); 
      }
      return returnString;
    }

Tương tự khi lấy dữ liệu về để so sánh khi đăng nhập hoặc khi đổi mật khẩu, ứng dụng sẽ lấy đoạn text đã được mã hoá về và giải mã bằng key tương ứng.
Hàm giải mã sẽ như sau:

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
      byte[] encryptionKey = {21, 35, 44, 69, 11, 55, 19, 99, 18, 20, 15, 44, 77, 23, 76, 12};
      secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
      byte[] EncryptedByte = string.getBytes("ISO-8859-1");
      String decryptedString = string;
      byte[] decryption;
      try {
        decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        decryption = decipher.doFinal(EncryptedByte);
        decryptedString = new String(decryption);
      } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
        e.printStackTrace();
      }
      return decryptedString;
    }

Ví dụ khi mật khẩu là "son123" sau khi được mã hoá và được lưu vào Database như sau:

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/pass.png?raw=true" width="450" title="hover text">
</p>

### Trang chủ

Sau khi đăng nhập thành công, ngừoi dùng sẽ được đưa đến giao diện trang chủ, tại màn hình này người dùng sẽ lựa chọn đi tới tính năng mong muốn.

Trang chủ sẽ hiển thị Avatar, tên người dùng và đầy đủ các chức năng của ứng dụng.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/home.png?raw=true" width="350" title="hover text">
</p>

### Xem và thiết lập môi trường kho

Đây chính là phần trọng tâm của dự án.

Tại đây người dùng sẽ theo dõi thông số từ các thiết bị theo thời gian thực.

Đồng thời, màn hình cũng hiển thị thông số của ba thiết bị gồm: - Nhiệt độ: màu đỏ, đơn vị *C.
- Độ ẩm: màu xanh dương, đơn vị %
- Quạt: màu xanh lá, đơn vị % công suất tối đa.

Ngoài ra ngừoi dùng còn có thể thiết lập "nhiệt độ tiêu chuẩn" để quạt chạy với cơ chế như sau. Nếu nhiệt độ thực tế lớn hơn nhiệt độ tiêu chuẩn x độ quạt sẽ chạy với công suất (2x)% và công suất tối đa của quạt là 100%.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/env2.png?raw=true" width="350" title="hover text">
</p>

Ngoài ra,ta còn có một công tắc quạt thủ công, khi tắt công tắt dù nhiệt độ có chênh lệch bao nhiêu quạt cũng sẽ tắt.

Để thực hiện tự động bật tắt quạt, ứng dụng gọi đến hàm startMQTTTempHumi để lấy thông số cảm biến và thực hiện tính toán. Sau đó sẽ gọi hàm sendDataToMQTT (hàm này sẽ được trình bày ở phần kết nối server) để thay đổi thông số quạt (Speaker).

    private void startMQTTTempHumi (String ID, String topic, final  TextView a,final TextView b, final  TextView cel){
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
    }

### Xem lịch sử

Sau khi nhấn lưu lịch sử ở màn hình môi trường kho, ứng dụng sẽ đọc lại thông tin môi trường kho tại thời điểm đó. Nếu các thông số hợp lệ ứng dụng sẽ lưu lại vào Database kèm theo thời gian và tài khoản lưu. Đồng thời thông báo đến ngừoi dùng đã lưu thành công.

Khi ngừoi dùng vào giao diện Xem lịch sử sẽ thấy được toàn bộ lịch sử đã lược lưu dưới dạng list và sắp xếp theo thời gian.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/history.png?raw=true" width="350" title="hover text">
</p>

### Xem thông số gợi ý các loại nông sản

Tại màn hình xem thông số gợi ý các loại nông sản, ngừoi dùng có thể thấy thông số nhiệt độ và độ ẩm phù hợp cho từng loại nông sản, từ đó có thể điều chỉnh thông số hợp lí cho kho bảo quản của mình.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/para.png?raw=true" width="350" title="hover text">
</p>

### Xem thời tiết

Tại màn hình này, ngừoi dùng sẽ được xem thời tiết thực tế tại khu vực của kho bảo quản từ đó có thể đưa ra các thông số phù hợp, kịp thời thay đổi, kiểm tra hoặc ứng phó với những biến cố thời tiết.

Địa điểm của kho được thiết lập và thay đổi trong phần thay đổi thông tin sẽ được trình bày ở phần tiếp theo.

Thời tiết được lấy từ trang www.accuweather.com.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/weather.png?raw=true" width="350" title="hover text">
</p>

### Tuỳ chỉnh tài khoản

Khi vừa vào tính năng này, người dùng sẽ thấy thông tin tài khoảng của mình cùng 3 nút chức năng là thay đổi thông tin, thay đổi mật khẩu và đăng xuất.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/info.png?raw=true" width="350" title="hover text">
</p>

Nếu nhấn vào nút thay đổi thông tin, ứng dụng sẽ đưa ngừoi dùng đến màn hình thay đổi thông tin.

Tại đây ngừoi dùng có thể thay đổi ảnh đại diện bằng cách chụp ảnh hoặc chọn ảnh từ thiết bị và thay đổi các thông tin khác bằng cách nhập. Riêng ô điền tên nguời dùng dùng sẽ không được để trống, nếu không ứng dụng sẽ báo lỗi và không lưu thay đổi. Ảnh đại diện sẽ được đưa về dạng byte trước khi lưu vào Database.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/changeinfo.png?raw=true" width="350" title="hover text">
</p>

Tương tự khi người dùng nhấn vào thay đổi mật khẩu, ứng dụng sẽ đưa ngừoi dùng đến giao diện thay đổi mật khẩu. Tại đây ngừoi dùng sẽ nhập mật khẩu hiện tịa và mật khẩu mới. Ứng dụng sẽ kiểm tra xem các giá trị nhập có hợp lệ không. Nếu hợp lệ, mật khẩu sẽ được thay đổi và lưu lại vào Database.

<p align="center">
  <img src="https://github.com/congson99/Warehouse-Management/blob/master/Report/changepass.png?raw=true" width="350" title="hover text">
</p>

### Kết nối server

Đầu tiên, chúng ta sử dụng file hỗ trợ MQTTHelper để kết nối với server giúp thực hiện các chức năng Publisher và Subscriber.


    public class MQTTHelper {
        final String serverUri = "tcp://xxx.xxx.xxx.xx";
        /* Đường dẫn tới server, với giao thức tcp, và phải có thêm thông tin về cổng (Port).*/

        final String username = "abc";
        final String password = "***";
        private String clientId, subscriptionTopic;

        public MqttAndroidClient mqttAndroidClient;

        public MQTTHelper(Context context, String Id, String Topic){
            clientId = Id;
            subscriptionTopic = Topic;
            mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.w("Mqtt", serverURI);
                }

                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Log.w("Mqtt", mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            connect();
        }

        public void setCallBack(MqttCallbackExtended callBack){
            mqttAndroidClient.setCallback(callBack);
        }

        private void connect(){
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(password.toCharArray());

            try{
                mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        subscribeToTopic();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                    }
                });
            }catch (MqttException ex){
                Log.w("Exception", "Exception");
                ex.printStackTrace();
            }
        }

        private void subscribeToTopic(){
            try {
                mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Subscribed!!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Subscribed fail!");
                    }
                });
            } catch (MqttException ex) {
                System.err.println("Exception subscribing");
                ex.printStackTrace();
            }
        }
    }
    
Sau đó gọi và thực hiện Subscriber bằng hàm startMQTT. Ví dụ như hàm Subscriber vào Top- ic/Speaker dưới đây:

    private void startMQTTSpeaker(String ID, String topic, final  TextView a){
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
                    if (arr_value.getString(0).equals("0") || arr_value.getString(1).equals("0")){
                        a.setText("0");
                    }
                    else {
                        a.setText(String.valueOf(Integer.parseInt(arr_value.getString(1))/50));
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    
Thực hiện Publish bằng hàm sendDataToMQTT.

    private void sendDataToMQTT(String ID, String value1, String value2) throws JSONException {

        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        String x = "\"".substring(0,1);
        JSONObject payload = new JSONObject();
        payload.put("device_id", ID);
        payload.put("values", "");

        String list = "["+x+value1+x+","+x+value2+x+"]";
        String a = "[" + payload.toString().substring(0,payload.toString().length()-3) + list + "}]";
        byte[] b = a.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish("Topic/" + ID, msg);
        }catch (MqttException ignored){
        }
    }
    
### Input

thông tin input nhận được sẽ có dạng JSON như sau.

    [
      {   "device_id": "TempHumi ",
        "values": ["60","72"]
      }
    ]

Trong đó, 60 là nhiệt độ và 72 là độ ẩm ("values": ["Nhiệt độ","Độ ẩm không khí"]), TempHumi là tên thiết bị.

### Output

thông tin output nhận được sẽ có dạng JSON như sau.

    [
      {   "device_id": "Speaker",
        "values": ["1", "100"]
      }
    ]

Trong đó, thiết bị output sẽ có 2 trạng thái là “OFF ứng với giá trị 0” và “ON ứng với giá trị 1” kèm theo đó là giá trị về cường độ.

Miền giá trị cường độ âm thanh: [0, 5000]

Khi thực hiện Publish ta gửi lên server file JSON có định dạng tương tự đã được chuyển về String.

## Hướng phát triển

Sau khi hoàn thành dự án, nhóm nhận thấy đây là dự án rất có tiềm năng, mang nhiều lợi ích thực tế và thị trường rất rộng lớn.

Vì vậy, nhóm đã đề ra rất nhiều hướng phát triển và hoàn thiện dự án. Cụ thể như sau:

- Trước hết phát triển ứng dụng trên đa nền tảng chứ không chỉ android để mọi ngừoi dùng đều có thể sử dụng (IOS, PC, Web).
- Thiết kế xây dựng lại cấu trúc quản lí đa kho, đa người dùng để phục vụ cho nhiều người dùng với kho riêng của mình.
- Xây dựng server để quản lý giúp việc đa kho, đa người dùng trở nên dễ dàng hơn.
- Tăng cường thêm nhiều tính năng, nhiều tuỳ chọn tự động hoá.
- Liên kết với công ty cung cấm thiết bị để tối ưu hoá hệ thống IOT cũng như dễ dàng lắp đặt cho người dùng đầu cuối.
- Tích hợp các công nghệ mới như AI để phân tích số liệu, định hướng phát triển, phát triển tính năng dựa trên thói quen người dùng hoặc dựa trên hướng phát triển chung của ngành.
- Mở rộng dự án sang các lĩnh vực liên quan có thể tích hợp như camera giám sát an ninh cho kho bảo quản, quản lí nông sản và gợi ý đầu tư, quản lý nông trại,...

## Liên hệ

congson99vn@gmail.com
