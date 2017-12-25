package state.identity.blockchain.iosl.de.blockidclientqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import state.identity.blockchain.iosl.de.blockidclientqrscanner.async.GetUserId;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.async.PostUserCredentials;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.ApiRequest;

public class MainActivity extends AppCompatActivity {

    private final Activity activity = this;
    private Logic logic;

    public transient String latestUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.logic = new Logic();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            new IntentIntegrator(activity).initiateScan(); // `this` is the current Activity
        });

        Button search = findViewById(R.id.search);
        search.setOnClickListener(view -> {
            EditText givenName = findViewById(R.id.givenName);
            EditText familyName = findViewById(R.id.familyName);

            if(givenName.getText() == null || givenName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide a given name", Toast.LENGTH_SHORT).show();
                return;
            } else if (familyName.getText() == null || familyName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please provide a family name", Toast.LENGTH_SHORT).show();
                return;
            }

            new GetUserId(this).execute(logic, givenName.getText().toString(), familyName.getText().toString());
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                processQRCode(result.getContents(), latestUserID);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processQRCode(String contents, String userId) {
        if(userId != null) {
            ApiRequest apiRequest = logic.approveUser(contents);
            new PostUserCredentials(this).execute(logic, userId, apiRequest);
        } else {
            Log.i("Help", "user id was null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTextField(String textField) {
        TextView view = findViewById(R.id.result);
        view.setText(textField);
    }
}
