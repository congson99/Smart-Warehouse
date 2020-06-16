package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    TextView light_show;
    EditText light_state;
    EditText light_value;
    Button bt_light;

    TextView speak_show;
    EditText speak_state;
    EditText speak_value;
    Button bt_speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        light_show = (TextView) findViewById(R.id.light_show);
        light_state = (EditText) findViewById(R.id.light_state);
        light_value = (EditText) findViewById(R.id.light_value);
        bt_light = (Button) findViewById(R.id.button_light);

        speak_show = (TextView) findViewById(R.id.speak_show);
        speak_state = (EditText) findViewById(R.id.speak_state);
        speak_value = (EditText) findViewById(R.id.speak_value);
        bt_speak = (Button) findViewById(R.id.button_speak);

        startMQTT("LightD", "Topic/LightD", light_show);
        startMQTT("Speaker", "Topic/Speaker", speak_show);

        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!light_state.getText().toString().equals("1")) light_state.setText("0");
                if (light_value.getText().toString().equals("")) light_value.setText("0");

                try {
                    sendDataToMQTT("LightD", light_state.getText().toString(), light_value.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                light_state.setText("");
                light_value.setText("");
            }
        });

        bt_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!speak_state.getText().toString().equals("1")) speak_state.setText("0");
                if (speak_value.getText().toString().equals("")) speak_value.setText("0");

                try {
                    sendDataToMQTT("Speaker", speak_state.getText().toString(), speak_value.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speak_state.setText("");
                speak_value.setText("");
            }
        });

    }

    private void startMQTT(String ID, String topic, final TextView tv){
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
                tv.setText(message.toString());
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

        String x = "\"".substring(0,1);
        JSONObject payload = new JSONObject();
        payload.put("device_id", ID);
        payload.put("values", "");

        String list = "["+x+value1+x+","+x+value2+x+"]";
        String a = payload.toString().substring(0,payload.toString().length()-3) + list + "}";
        byte[] b = a.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish("Topic/" + ID, msg);
        }catch (MqttException ignored){
        }
    }
}