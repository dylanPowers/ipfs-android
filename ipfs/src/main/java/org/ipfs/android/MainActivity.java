package org.ipfs.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        handleIntent(getIntent());
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void ipfsHashButtonOnClick(View view) {
        TextView ipfsTextView = (TextView) findViewById(R.id.ipfs_hash);
        launchBrowser(ipfsTextView.getText().toString(), Scheme.IPFS);
    }

    public void ipnsHashButtonClick(View view) {
        TextView ipnsTextView = (TextView) findViewById(R.id.ipns_hash);
        launchBrowser(ipnsTextView.getText().toString(), Scheme.IPNS);
    }

    private void handleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String hashPath = parseHashPath(uri);
            if (uri.getScheme().equals("ipfs")) {
                ((TextView) findViewById(R.id.ipfs_hash)).setText(hashPath);
                launchBrowser(hashPath, Scheme.IPFS);
            } else if (uri.getScheme().equals("ipns")) {
                ((TextView) findViewById(R.id.ipns_hash)).setText(hashPath);
                launchBrowser(hashPath, Scheme.IPNS);
            }
        }
    }

    private void launchBrowser(String hashPath, Scheme scheme) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_VIEW);
        String hostnamePrefKey = getString(R.string.pref_serverHostname_key);

        String hostname = PreferenceManager.getDefaultSharedPreferences(this).getString(hostnamePrefKey, null /* It exists */);
        if (hostname == null) {
            throw new RuntimeException("The world is ending!!!!");
        }

        String url = "http://" + hostname;
        if (scheme == Scheme.IPFS) {
            url += "/ipfs/";
        } else {
            url += "/ipns/";
        }
        url += hashPath;
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }

    private String parseHashPath(Uri uri) {
        String hashPath = uri.getSchemeSpecificPart();
        hashPath = hashPath.substring(2); // Remove leading '//'

        if (uri.getFragment() != null) {
            hashPath += "#" + uri.getFragment();
        }
        return hashPath;
    }

    private enum Scheme {
        IPFS, IPNS
    }
}
