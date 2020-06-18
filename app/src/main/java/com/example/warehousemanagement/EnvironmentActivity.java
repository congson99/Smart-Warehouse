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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentActivity extends AppCompatActivity {

    private MQTTHelper mqttHelper;

    TextView speak_show;
    TextView t1;
    TextView t2;
    EditText speak_state;
    EditText speak_value;
    Button bt_speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        speak_show = (TextView) findViewById(R.id.speak_show);
        t1 = (TextView) findViewById(R.id.temp1);
        t2 = (TextView) findViewById(R.id.temp2);
        speak_state = (EditText) findViewById(R.id.speak_state);
        speak_value = (EditText) findViewById(R.id.speak_value);
        bt_speak = (Button) findViewById(R.id.button_speak);

        startMQTTSpeaker("Speaker", "Topic/Speaker", speak_show, t1, t2);

        bt_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!speak_state.getText().toString().equals("1")) speak_state.setText("0");
                if (speak_value.getText().toString().equals("")) speak_value.setText("0");
                if (Integer.parseInt(speak_value.getText().toString()) > 5000) speak_value.setText("5000");

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

    private void startMQTTSpeaker(String ID, String topic, final TextView tv, final  TextView a,final TextView b){
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
                JSONArray jsonArray = new JSONArray(message.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String device_id = jsonObject.getString("device_id");
                    String location = jsonObject.getString("values");

                    JSONArray arr_value = new JSONArray(location);
                    if (arr_value.getString(0).equals("0")){
                        a.setText("Off");
                    }
                    else {
                        a.setText("On");
                    }
                    b.setText(arr_value.getString(1));
                }

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
        String a = "[" + payload.toString().substring(0,payload.toString().length()-3) + list + "}]";
        byte[] b = a.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish("Topic/" + ID, msg);
        }catch (MqttException ignored){
        }
    }
}