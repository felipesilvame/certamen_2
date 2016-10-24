package cl.usm.telematica.certamen2.presenters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import cl.usm.telematica.certamen2.DisplayResultsActivity;
import cl.usm.telematica.certamen2.DisplayResultsView;
import cl.usm.telematica.certamen2.model.Repository;
import cl.usm.telematica.certamen2.presenters.contract.GitInfoPresenter;

/**
 * Created by Pipos on 23-10-2016.
 */

public class GitInfoPresenterImpl implements GitInfoPresenter{
    private String data;
    private String username;
    private DisplayResultsActivity mActivity;
    private DisplayResultsView mView;
    private List<Repository> repoList;

    public GitInfoPresenterImpl(DisplayResultsActivity activity, DisplayResultsView view){
        mActivity = activity;
        mView = view;
        repoList = new ArrayList<>();
    }

    @Override
    public void showInfo() {
        try {
            Object jsonObject = new JSONTokener(data).nextValue();
            if (jsonObject instanceof JSONObject){
                JSONObject err = new JSONObject(data);
                if (err.has("message")){
                    mActivity.setError(username);
                }
            }
            else if (jsonObject instanceof JSONArray){
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject repo = jsonArray.getJSONObject(i);
                    String name = repo.getString("name");
                    String description = repo.getString("description");
                    String updated_at = repo.getString("updated_at");
                    String html_url = repo.getString("html_url");
                    Repository repoObject = new Repository(name, description,updated_at,html_url);
                    repoList.add(repoObject);
                }
                mActivity.setTextUser(username);
                mActivity.onDataReady(repoList);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }
}
