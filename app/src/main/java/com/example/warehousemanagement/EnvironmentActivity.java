package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentActivity extends AppCompatActivity {

    private MQTTHelper mqttHelper;
    TextView tv_temp;
    Button bt_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        tv_temp = (TextView) findViewById(R.id.E_temp);
        bt_temp = (Button) findViewById(R.id.E_temp2);

        startMQTT("Speaker", "Topic/Speaker");

        bt_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sendDataToMQTT("Speaker", "0" , "30");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startMQTT("Speaker", "Topic/Speaker");
            }
        });

    }

    private void startMQTT(String ID, String topic){
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
                tv_temp.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void sendDataToMQTT(String ID, String value1, String value2) throws JSONException {

        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        List<String> list = new ArrayList<String>();
        list.add(value1);
        list.add(value2);
        JSONObject payload = new JSONObject();
        payload.put("device id", ID);
        payload.put("values", list);

        byte[] b = payload.toString().getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish("Topic/" + ID, msg);
        }catch (MqttException ignored){
        }
    }
}