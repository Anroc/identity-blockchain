package state.identity.blockchain.iosl.de.blockidclientqrscanner.async;

import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import state.identity.blockchain.iosl.de.blockidclientqrscanner.Logic;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.MainActivity;

public class GetUserId extends AsyncTask<Object, Void, List<String>> {

    private final ThreadLocal<MainActivity> callback;

    public GetUserId(MainActivity callback) {
        this.callback = new ThreadLocal<MainActivity>() {
            @Override
            protected MainActivity initialValue() {
                return callback;
            }
        };
    }

    @Override
    protected List<String> doInBackground(Object... objects) {
        Logic logic = (Logic) objects[0];
        String givenName = (String) objects[1];
        String familyName = (String) objects[2];
        return logic.doGet(givenName, familyName);
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        callback.get().setTextField("Found user ids:" + Arrays.toString(strings.toArray()));
        if(! strings.isEmpty()) {
            callback.get().latestUserID = strings.get(0);
        }
    }
}
