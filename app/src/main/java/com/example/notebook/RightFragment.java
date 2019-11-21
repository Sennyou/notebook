package com.example.notebook;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RightFragment extends Fragment {
    private TextView wordEn,wordZh;
    private EditText exmaple;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    public static word word_st=new word();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view=inflater.inflate(R.layout.right_fragment,container,false);
        wordEn=(TextView)view.findViewById(R.id.wordEn);
        wordZh=view.findViewById(R.id.wordZh);
        exmaple=view.findViewById(R.id.example);
        exmaple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("tiaoshi","changed"+wordEn.getText().toString());
                word word=new word();
                word.setExample(editable.toString());
                word.updateAll("en = ? ",wordEn.getText().toString());
            }
        });
        return view;
    }

    public void refresh(String wordEn){
        List<word> wordList= LitePal.where("en=?",wordEn).find(word.class);
        this.wordEn.setText(wordList.get(0).getEn());
        wordZh.setText(wordList.get(0).getZh());
        exmaple.setText(wordList.get(0).getExample());
        try {
            Log.i("tiasho",mediaPlayer.toString());
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(getContext(),wordList.get(0).getUri());
            mediaPlayer.prepare();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void refresh(String meiyong,String wordEn){
        Log.i("tiaoshi","refresh");
        send(wordEn);
    }
    public void save(){
        Log.i("tiaoshi","save");
        wordEn=getView().findViewById(R.id.wordEn);
        wordZh=getView().findViewById(R.id.wordZh);
        Log.i("tiaoshi",word_st.getUri().getPath());
        word word =word_st;
       // word.setEn(wordEn.getText().toString());
        //word.setZh(wordZh.getText().toString());
        word.save();
        LeftFragment leftFragment=(LeftFragment)getFragmentManager().findFragmentById(R.id.left);
        leftFragment.refresh();
    }
    public void delete(){
        Log.i("tiaoshi","delete");
        wordEn=getView().findViewById(R.id.wordEn);
        String string=wordEn.getText().toString();
        Log.i("tiaoshi",string);
        LitePal.deleteAll(word.class,"en = ?",string);
        LeftFragment leftFragment=(LeftFragment)getFragmentManager().findFragmentById(R.id.left);
        leftFragment.refresh();
    }

    public Handler handler=new Handler(){
        public void handleMessage(Message message){
            word word=(word)message.obj;
            wordEn.setText(word.getEn());
            wordZh.setText(word.getZh());
            exmaple.setText(word.getExample());
            try {
                Log.i("tiasho",mediaPlayer.toString());
                mediaPlayer=new MediaPlayer();
                mediaPlayer.setDataSource(getContext(),word.getUri());
                mediaPlayer.prepare();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    public void play(){
        mediaPlayer.start();
    }
   public void Destroy() {
        if(mediaPlayer!=null) {
            Log.i("tiaoshi", "close");
            mediaPlayer.stop();
            mediaPlayer.release();
        }


    }
    public void send(final String En){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Log.i("tiaoshi","ok");
                    String string, appid, miss, salt, q, string1;
                    q = En;
                    appid = "20191107000354016";
                    miss = "9fq6KXPUbQFLEvwyDY8o";
                    salt = String.valueOf(System.currentTimeMillis());
                    string = "http://api.fanyi.baidu.com/api/trans/vip/translate?";
                    string = string + "q="+q+"&from=en&to=zh&appid=20191107000354016&salt="+salt+"&sign=";
                    string1 = appid + q + salt + miss;
                    string = string + getMD5String(string1);
                    Request request = new Request.Builder()
                            .url(string)
                            .build();
                    Log.i("tiaoshi","ok2");
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("tiaoshi",responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String a=jsonObject.getString("trans_result");
                    JSONArray into=new JSONArray(a);
                    String yuanwen=into.getJSONObject(0).getString("src");
                    String yiwen=into.getJSONObject(0).getString("dst");
                    String uri=into.getJSONObject(0).getString("src_tts");
                    String b=into.getJSONObject(0).getString("dict");
                    JSONObject into_dict=new JSONObject(b);
                    String c=into_dict.getString("word_result");
                    JSONObject into_word_result=new JSONObject(c);
                    String d=into_word_result.getString("edict");
                    JSONObject into_edict=new JSONObject(d);
                    JSONArray into_item=into_edict.getJSONArray("item");
                    JSONArray into_tr_group=into_item.getJSONObject(0).getJSONArray("tr_group");
                    Log.i("tiaoshi",into_tr_group.toString());
                    String examle=into_tr_group.getJSONObject(0).getString("example");
                    Log.i("tiaoshi",examle);
                    word word=new word();
                    word_st.setEn(yuanwen);
                    word_st.setZh(yiwen);
                    Uri u=Uri.parse(uri);
                    word_st.setUri(u);
                    word_st.setExample(examle);
                    Message message=new Message();
                    word=word_st;
                    message.obj=word;
                    Log.i("tiaoshi",word.getEn());
                    Log.i("tiaoshi",word.getUri().getPath());
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("tiaoshi", e.toString());
                }
            }
        }).start();
    }

    public static String getMD5String(String str) {

        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
