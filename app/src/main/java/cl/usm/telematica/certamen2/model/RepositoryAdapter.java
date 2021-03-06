package cl.usm.telematica.certamen2.model;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cl.usm.telematica.certamen2.R;

/**
 * Created by Pipos on 30-09-2016.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.MyViewHolder> {

    private List<Repository> repoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, updated_at;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            updated_at = (TextView) view.findViewById(R.id.updated_at);
        }
    }


    public RepositoryAdapter(List<Repository> rpoList) {
        this.repoList = rpoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_single_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Repository repo = repoList.get(position);
        holder.name.setText(repo.getName());
        holder.description.setText(repo.getDescription());
        holder.updated_at.setText(repo.getUpdated_at());
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }
}