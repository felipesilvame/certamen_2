package cl.usm.telematica.certamen2;

import java.util.List;

import cl.usm.telematica.certamen2.model.Repository;

/**
 * Created by Pipos on 23-10-2016.
 */

public interface DisplayResultsView {
    void onDataReady(List<Repository> repositoryList);
    void setError(String username);
    void setTextUser(String username);
}
