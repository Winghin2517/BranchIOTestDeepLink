package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                        .setCanonicalIdentifier("item/12345")
                                // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                        .setTitle("Suits")
                        .setContentDescription("Great suits here")
                        .setContentImageUrl("http://steezo.com/wp-content/uploads/2012/12/man-in-suit.jpg")
                                // You use this to specify whether this content can be discovered publicly - default is public
                        .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                                // Here is where you can add custom keys/values to the deep link data
                        .addContentMetadata("picurl", "http://steezo.com/wp-content/uploads/2012/12/man-in-suit.jpg");

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("$desktop_url", "http://www.yahoo.com")
                        .addControlParameter("$ios_url", "http://www.microsoft.com");

                branchUniversalObject.generateShortUrl(MainActivity.this, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            Log.i("MyApp", "got my Branch link to share: " + url);
                        }
                        else {
                            Log.e("error", error.toString());
                        }
                    }
                });
                ShareSheetStyle shareSheetStyle = new ShareSheetStyle(MainActivity.this, "Check this out!", "This stuff is awesome: ")
                        .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL);

                branchUniversalObject.showShareSheet(MainActivity.this,
                        linkProperties,
                        shareSheetStyle,
                        new Branch.BranchLinkShareListener() {
                            @Override
                            public void onShareLinkDialogLaunched() {
                            }

                            @Override
                            public void onShareLinkDialogDismissed() {
                            }

                            @Override
                            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                                Log.e("LinkShared", "success");
                            }

                            @Override
                            public void onChannelSelected(String channelName) {
                            }
                        });


            }
        });
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
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

// ONLY use the line below IF you ARE NOT using automatic session management.
// Branch branch = Branch.getInstance(getApplicationContext());

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                } else {
                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
        // Lifecycle callback method
    }
    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
