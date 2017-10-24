package com.robalascott.todolist.todolist;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by robscott on 2017-10-22.
 */

//edit update get all and delete one class
public class AllTask extends AsyncTask<String[],Void,Object> {
    private Exception exception;
    private boolean running = true;
    private final OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    private String fail = "404";
    private WeakReference<TaskInfomer> mCallBack;
    private MediaType JSON = MediaType.parse("application/json,charset=utf-8");

    //Interface to MainActivity
    public AllTask(TaskInfomer callback){
        this.mCallBack = new WeakReference<TaskInfomer>(callback);
    }

    @Override
    protected void onCancelled() {
        running = false;
    }
    @Override
    protected Object doInBackground(String[]... params) {
        while(running) {
            try {
                String[] task = params[0];
                switch (task[0]) {
                    case "ADD":
                        return add(task[1], task[3], task[4]);
                    case "ALL":
                        return all(task[1]);
                    case "DELETE":
                        return delete(task[1] + "/" + task[2]);
                    case "EDIT":
                        return edit(task[1] + "/" + task[2], task[3], task[4]);
                    default:
                        return null;
                }
            } catch (NullPointerException e) {
                this.exception = e;
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object getResponse) {
        super.onPostExecute(getResponse);
        final TaskInfomer callBack = mCallBack.get();
        if(callBack!=null){callBack. onTaskAll(getResponse);}
    }


    public String delete(String url)throws IOException{
            String result = null;
            Request request = new Request.Builder().url(url).delete().build();
            try (Response response = client.newCall(request).execute()) {
                //Lazy programing must get code
                result = String.valueOf(response.code());
            }catch (Exception e){
                Log.i("Exep",e.toString());
                return fail;
            }
        return result;
    }

    public TaskObject edit(String url,String title, String description)throws IOException{
        TaskObject placeholder = null;
        RequestBody body = RequestBody.create(JSON,JSonBuilder(title, description));
        Request request = new Request.Builder().url(url).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonObject = response.body().string();
            Type collect = new TypeToken<TaskObject>() {}.getType();
            placeholder = gson.fromJson(jsonObject, collect);
        }catch (Exception e){
           return null;
        }
        return placeholder;
    }


    public ArrayList<TaskObject> all(String url)throws IOException{
        Response response = null;
        ArrayList<TaskObject> finalObject = new ArrayList<>();
        try{
            Request request = new Request.Builder().url(url).build();
            response = client.newCall(request).execute();
            String jsonObject = response.body().string();
            Type collect = new TypeToken<Collection<TaskObject>>() {}.getType();
            Collection<TaskObject> placeholder = gson.fromJson(jsonObject, collect);
            TaskObject[] temp = placeholder.toArray(new TaskObject[placeholder.size()]);
            finalObject = new ArrayList<>(Arrays.asList(temp));
        }catch (Exception e){
            Log.i("Error", e.toString() );
        }
        if(finalObject==null){
            //posible placeholder
            finalObject.add(new TaskObject(1,"Wow","Such Empty"));
        }
        return finalObject;
    }
    private TaskObject add(String url, String title, String description) {
        TaskObject placeholder = null;
        RequestBody body = RequestBody.create(JSON,JSonBuilder(title, description));
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonObject = response.body().string();
            Type collect = new TypeToken<TaskObject>() {}.getType();
            placeholder = gson.fromJson(jsonObject, collect);
        }catch (Exception e){
        }
        return placeholder;
    }

    public String JSonBuilder(String title, String description){
       return "{\"title\": \"" + title + "\"," + "\"description\":\"" + description +"\"}";
    }

}
