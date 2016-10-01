package cl.usm.telematica.certamen2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private List<Repo> repoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_results);
        Bundle b= getIntent().getExtras();
        String server_response = null;
        String username = null;
        if (b != null){
            try {
                server_response = b.getString("info");
                username = b.getString("username");
                Object jsonObject = new JSONTokener(server_response).nextValue();
                if (jsonObject instanceof JSONObject){
                    JSONObject err = new JSONObject(server_response);
                    if (err.has("message")){
                        TextView error_user = (TextView) findViewById(R.id.textView);
                        error_user.setText("Error: "+username+" "+err.getString("message"));

                    }
                }
                else if (jsonObject instanceof JSONArray){
                    TextView user = (TextView) findViewById(R.id.textView);
                    user.setText(username+" Repositories");
                    user.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    repoAdapter = new RepoAdapter(repoList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager repoLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(repoLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(repoAdapter);

                    JSONArray jsonArray = new JSONArray(server_response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject repo = jsonArray.getJSONObject(i);
                        String name = repo.getString("name");
                        String description = repo.getString("description");
                        String updated_at = repo.getString("updated_at");
                        String html_url = repo.getString("html_url");
                        Repo repoObject = new Repo(name, description,updated_at,html_url);
                        repoList.add(repoObject);
                    }
                    repoAdapter.notifyDataSetChanged();
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Repo repo = repoList.get(position);
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
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            TextView error_url = (TextView) findViewById(R.id.textView);
            error_url.setText("Hubo un error recibiendo los datos");
            error_url.setGravity(View.TEXT_ALIGNMENT_CENTER);

        }



    }
}
