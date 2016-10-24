package cl.usm.telematica.certamen2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cl.usm.telematica.certamen2.presenters.GitSearchPresenterImpl;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private EditText mUser;
    private GitSearchPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mUser = (EditText) findViewById(R.id.user_search_edittext);
        mPresenter = new GitSearchPresenterImpl(this, this);
    }

    @Override
    public void onUserObtained(View v) {
        String username = mUser.getText().toString();
        mPresenter.searchGit(username);
    }
}
