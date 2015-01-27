package org.ipfs.android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals("ipfs")) {
            String hashPath = uri.getPath();
            hashPath = hashPath.substring(1);
            hashPath += "#" + uri.getFragment();
            ((TextView) findViewById(R.id.hash)).setText(hashPath);

            launchBrowser(hashPath);
        }
    }

    public void hashButtonOnClick(View view) {
        launchBrowser(((TextView) findViewById(R.id.hash)).getText().toString());
    }

    private void launchBrowser(String hashPath) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_VIEW);
        String url = "http://gateway.ipfs.io/ipfs/" + hashPath;
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }
}
