package cl.usm.telematica.certamen2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cl.usm.telematica.certamen2.model.ClickListener;
import cl.usm.telematica.certamen2.model.DividerItemDecoration;
import cl.usm.telematica.certamen2.model.RecyclerTouchListener;
import cl.usm.telematica.certamen2.model.Repository;
import cl.usm.telematica.certamen2.model.RepositoryAdapter;
import cl.usm.telematica.certamen2.presenters.GitInfoPresenterImpl;

public class DisplayResultsActivity extends AppCompatActivity implements DisplayResultsView {
    private GitInfoPresenterImpl mPresenter;
    private RepositoryAdapter repoAdapter;
    private List<Repository> repoList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);
        Bundle b= getIntent().getExtras();
        mPresenter = new GitInfoPresenterImpl(this, this);
        if (b != null){
            mPresenter.setData(b.getString("info"));
            mPresenter.setUsername(b.getString("username"));
            mPresenter.showInfo();
        }
        else{
            Toast.makeText(this, "Error trayendo los datos", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDataReady(List<Repository> repositoryList) {
        repoList = repositoryList;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        repoAdapter = new RepositoryAdapter(repoList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager repoLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(repoLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(repoAdapter);
        repoAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Repository repo = repoList.get(position);
                String url = repo.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void setError(String username) {
        TextView texto = (TextView) findViewById(R.id.info_username);
        texto.setGravity(View.TEXT_ALIGNMENT_CENTER);
        texto.setText("Usuario "+username+" no existe.");
    }

    @Override
    public void setTextUser(String username) {
        TextView texto = (TextView) findViewById(R.id.info_username);
        texto.setGravity(View.TEXT_ALIGNMENT_CENTER);
        texto.setText("Repositorios de "+username);
    }
}
