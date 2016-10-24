package cl.usm.telematica.certamen2;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cl.usm.telematica.certamen2.presenters.GitSearchPresenterImpl;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private EditText mUser;
    private GitSearchPresenterImpl mPresenter;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mUser = (EditText) findViewById(R.id.user_search_edittext);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .activity_search);
        mPresenter = new GitSearchPresenterImpl(this, this);
    }

    @Override
    public void onUserObtained(View v) {
        String username = mUser.getText().toString();
        mPresenter.searchGit(username);
    }

    @Override
    public void userNotFound(String username) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Usuario "+username+" no encontrado", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
