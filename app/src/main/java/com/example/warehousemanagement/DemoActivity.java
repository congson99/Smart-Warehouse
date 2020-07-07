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

public class DemoActivity extends AppCompatActivity {

    private MQTTHelper mqttHelper;

    TextView show_temphumi;
    TextView show_speaker;
    TextView show_nd;
    TextView show_da;
    TextView show_oo;
    TextView show_dl;

    EditText get_nd;
    EditText get_da;

    Button bt_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //Anh xa
        show_temphumi = (TextView) findViewById(R.id.demo_show_temphumi);
        show_speaker = (TextView) findViewById(R.id.demo_show_speaker);
        show_nd = (TextView) findViewById(R.id.demo_show_nd);
        show_da = (TextView) findViewById(R.id.demo_show_da);
        show_oo = (TextView) findViewById(R.id.demo_show_oo);
        show_dl = (TextView) findViewById(R.id.demo_show_dl);

        get_nd = (EditText) findViewById(R.id.demo_in_nd);
        get_da = (EditText) findViewById(R.id.demo_in_da);

        bt_change = (Button) findViewById(R.id.demo_bt_change);

        //Get value and set
        startMQTTTempHumi("TempHumi", "Topic/TempHumi", show_temphumi, show_nd,show_da);
        startMQTTSpeaker("Speaker", "Topic/Speaker",show_speaker, show_oo, show_dl);

        //Set temphumi
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(get_nd.getText().toString().equals("") && !show_nd.getText().toString().equals("loading...")) get_nd.setText(show_nd.getText().toString());
                if(get_nd.getText().toString().equals("") && show_nd.getText().toString().equals("loading...")) get_nd.setText("0");
                if(get_da.getText().toString().equals("") && !show_da.getText().toString().equals("loading...")) get_da.setText(show_da.getText().toString());
                if(get_da.getText().toString().equals("") && show_da.getText().toString().equals("loading...")) get_da.setText("0");
                try {
                    sendDataToMQTT("TempHumi", get_nd.getText().toString(), get_da.getText().toString());
                    get_nd.setText("");
                    get_da.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void startMQTTTempHumi(String ID, String topic, final TextView tv, final  TextView a,final TextView b){
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
                    a.setText(arr_value.getString(0));
                    b.setText(arr_value.getString(1));
                    float longitude = Float.parseFloat(show_nd.getText().toString());
                    if (!a.getText().toString().equals("")){
                        if(longitude > 70){
                            sendDataToMQTT("Speaker", "1", "5000");
                        }
                        else {
                            if(longitude <= 20){
                                sendDataToMQTT("Speaker", "0", "1");
                            }
                            else {
                                String temp = String.valueOf((Integer.parseInt(a.getText().toString())-20)*2*50);
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