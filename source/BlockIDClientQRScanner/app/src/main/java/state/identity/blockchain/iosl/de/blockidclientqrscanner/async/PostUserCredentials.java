package state.identity.blockchain.iosl.de.blockidclientqrscanner.async;

import android.os.AsyncTask;

import state.identity.blockchain.iosl.de.blockidclientqrscanner.Logic;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.MainActivity;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.ApiRequest;

public class PostUserCredentials extends AsyncTask<Object, Void, Void> {

    private final ThreadLocal<MainActivity> callback;

    public PostUserCredentials(MainActivity callback) {
        this.callback = new ThreadLocal<MainActivity>() {
            @Override
            protected MainActivity initialValue() {
                return callback;
            }
        };
    }

    @Override
    protected Void doInBackground(Object... objects) {
        Logic logic = (Logic) objects[0];
        String userId = (String) objects[1];
        ApiRequest apiRequest = (ApiRequest) objects[2];

        logic.doPost(userId, apiRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        callback.get().setTextField("Successful updated user.");
    }
}
