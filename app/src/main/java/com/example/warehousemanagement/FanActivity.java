package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class FanActivity extends AppCompatActivity {

    private MQTTHelper mqttHelper;

    TextView show_speaker;
    TextView show_oo;
    TextView show_cd;

    EditText get_oo;
    EditText get_cd;

    Button bt_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);

        //Anh xa
        show_speaker = (TextView) findViewById(R.id.fan_show_speaker);
        show_oo = (TextView) findViewById(R.id.fan_show_oo);
        show_cd = (TextView) findViewById(R.id.fan_show_cd);

        get_oo = (EditText) findViewById(R.id.fan_in_oo);
        get_cd = (EditText) findViewById(R.id.fan_in_cd);

        bt_change = (Button) findViewById(R.id.fan_bt_change);

        //Get value and set
        startMQTTSpeaker("Speaker", "Topic/Speaker",show_speaker, show_oo, show_cd);

        //Set temphumi
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(get_oo.getText().toString().equals("") || get_cd.getText().toString().equals("") || Integer.parseInt(get_oo.getText().toString()) > 1){
                    get_oo.setText("0");
                    get_cd.setText("1");
                }
                try {
                    sendDataToMQTT("Speaker", get_oo.getText().toString(), get_cd.getText().toString());
                    get_oo.setText("");
                    get_cd.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void startMQTTSpeaker(String ID, String topic, final TextView tv, final  TextView a, final TextView b){
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