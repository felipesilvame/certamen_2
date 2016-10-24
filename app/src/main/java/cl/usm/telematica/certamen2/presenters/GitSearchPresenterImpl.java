package cl.usm.telematica.certamen2.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

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

import cl.usm.telematica.certamen2.DisplayResultsActivity;

import cl.usm.telematica.certamen2.SearchActivity;
import cl.usm.telematica.certamen2.SearchView;
import cl.usm.telematica.certamen2.presenters.contract.GitSearchPresenter;

/**
 * Created by Pipos on 23-10-2016.
 */

public class GitSearchPresenterImpl implements GitSearchPresenter {
    private SearchActivity mActivity;
    private SearchView mView;

    public GitSearchPresenterImpl(SearchActivity activity, SearchView mainView){
        mActivity = activity;
        mView = mainView;
    }

    @Override
    public void onGitFound(String data, String username) {
        Intent i = new Intent(mActivity, DisplayResultsActivity.class);
        Bundle b = new Bundle();
        b.putString("info", data);
        b.putString("username", username);
        i.putExtras(b);
        mActivity.startActivity(i);
    }

    @Override
    public void searchGit(String username) {
        new AsyncSearch(username).execute();
    }

    @Override
    public void onGitNotFound(String username) {
        mActivity.userNotFound(username);
    }

    private class AsyncSearch extends AsyncTask<Void,Void,Boolean> {
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
            dialog = ProgressDialog.show(mActivity, "Conectando...", "Buscando usuario...", true, true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
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
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                    terminateTask();


                }catch (MalformedURLException e){
                    //Toast.makeText(mActivity, "URL inválida", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return false;
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    return false;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }while(!finished);
            return true;
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
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result){
            onGitFound(server_response,username);
            } else{
                onGitNotFound(username);
            }

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
