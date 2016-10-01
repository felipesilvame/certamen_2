package cl.usm.telematica.certamen2;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Pipos on 30-09-2016.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder> {

    private List<Repo> repoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, updated_at;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            updated_at = (TextView) view.findViewById(R.id.updated_at);
        }
    }


    public RepoAdapter(List<Repo> moviesList) {
        this.repoList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_single_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Repo repo = repoList.get(position);
        holder.name.setText(repo.getName());
        holder.description.setText(repo.getDescription());
        holder.updated_at.setText(repo.getUpdated_at());
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }
}