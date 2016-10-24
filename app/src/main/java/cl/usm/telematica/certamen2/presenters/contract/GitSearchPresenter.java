package cl.usm.telematica.certamen2.presenters.contract;

/**
 * Created by Pipos on 23-10-2016.
 */

public interface GitSearchPresenter {
    void onGitFound(String username, String data);
    void searchGit(String username);
}
