package cl.usm.telematica.certamen2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    public String searchbox = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onSearch(View v){
        try{
            EditText git_user = (EditText) findViewById(R.id.editText);
            searchbox = git_user.getText().toString();
            new AsyncSearch(searchbox).execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private class AsyncSearch extends AsyncTask<Void,Void,Void>{
        ProgressDialog dialog = null;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition tryagain = lock.newCondition();
        private volatile boolean finished = false;
        private String username;
        private String server_response;

        private String getUsername(){
            return this.username;
        }
        private AsyncSearch(String username){
            this.username = username;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Conectando...", "Buscando usuario...", true, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                lock.lockInterruptibly();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            do{
                try{
                    URL url = new URL("https://api.github.com/users/"+username+"/repos");
                    //test
                    //URL url = new URL("http://www.mocky.io/v2/57eee3822600009324111202");

                    //test 2
                    //URL url = new URL("http://www.mocky.io/v2/57eee5352600000d25111203");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);

                    urlConnection.connect();
                    int response = urlConnection.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK){
                        server_response = readStream(urlConnection.getInputStream());
                        Log.v("CatalogClient", server_response);
                        terminateTask();
                    }

                }catch (MalformedURLException e){
                    Toast.makeText(MainActivity.this, "URL inválida", Toast.LENGTH_SHORT).show();
                }catch (FileNotFoundException e){
                    Toast.makeText(MainActivity.this, "Error! :(", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }while(!finished);
            return null;
        }
        public void runAgain() {
            // Call this to request data from the server again
            tryagain.signal();
        }

        public void terminateTask() {
            // The task will only finish when we call this method
            finished = true;
            lock.unlock();
        }

        @Override
        protected void onCancelled() {
            // Make sure we clean up if the task is killed
            dialog.dismiss();
            terminateTask();
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Intent i = new Intent(MainActivity.this, ResultsActivity.class);
            Bundle b = new Bundle();
            b.putString("info", server_response);
            b.putString("username", username);
            i.putExtras(b);
            startActivity(i);
            finish();
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }
}
