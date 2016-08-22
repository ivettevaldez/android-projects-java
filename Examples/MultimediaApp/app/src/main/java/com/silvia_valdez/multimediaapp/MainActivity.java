package com.silvia_valdez.multimediaapp;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String GOOGLE_DRIVE_URL
            = "https://drive.google.com/file/d/0B_yxRIPFWs-KLU5KWlk0YkxUU0E/view?usp=sharing";
    private static final String POSITION = "POSITION";

    private int mPosition = 0;

    private VideoView mVideoView;
    private ImageView mImageReplay;
    private FrameLayout mFrame;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.setNavigationItemSelectedListener(this);
        }

        /**
         * Non generated methods
         **/
        initViews();
        addListenersToViews();
        downloadUrl(GOOGLE_DRIVE_URL);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            // Handle the slideshow action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Use onSaveInstanceState to save the play position of the video.
        savedInstanceState.putInt(POSITION, mPosition);
        mVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Play the video from the saved position.
        mPosition = savedInstanceState.getInt(POSITION);
        mVideoView.seekTo(mPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Use onSaveInstanceState to save the play position of the video.
        mPosition = mVideoView.getCurrentPosition();
    }


    /**
     * METHODS.
     */
    private void initViews() {
        mImageReplay = (ImageView) findViewById(R.id.main_image_replay);
        if (mImageReplay != null) {
            mImageReplay.setVisibility(View.INVISIBLE);
        }

        // This frame is used to hide the VideoView until it's prepared to show the video.
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        if (mFrame != null) {
            mFrame.setVisibility(View.INVISIBLE);
        }

        // Create multimedia controls.
        MediaController mediaController = new MediaController(this);

        mVideoView = (VideoView) findViewById(R.id.main_video_view);
        if (mVideoView != null) {
            mVideoView.setMediaController(mediaController);
            mediaController.setAnchorView(mVideoView);
        }
    }

    private void addListenersToViews() {
        mImageReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadUrl(GOOGLE_DRIVE_URL);
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // When video is prepared to show, dismiss the ProgressDialog and show the frame.
                if (mDialog.isShowing()) mDialog.dismiss();
                if (mFrame != null) {
                    mFrame.setVisibility(View.VISIBLE);
                }

                // Play the video on a loop (on repeat).
                mediaPlayer.setLooping(true);

                if (mPosition == 0) {
                    // If had a position saved on savedInstanceState, the video should start there.
                    mVideoView.start();
                } else {
                    // If we had the activity "resumed", the video would be put on pause.
                    mVideoView.pause();
                }
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mDialog.dismiss();
                mImageReplay.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void initVideoView(String downloadedUrl) {
        mDialog.setMessage(getString(R.string.action_start_video));
        Uri uri = Uri.parse(downloadedUrl);

        if (mVideoView != null) {
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }
    }

    public void downloadUrl(final String strUrl) {
        // Set a ProgressDialog to show the app status
        mDialog = ProgressDialog.show(this, "", getString(R.string.action_downloading_url), true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    final String downloadedUrl = readInputStream(inputStream);
                    Log.d(TAG, String.format("New URL: %s", downloadedUrl));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initVideoView(downloadedUrl);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    R.string.error_failed_connecting,
                                    Toast.LENGTH_LONG).show();

                            mImageReplay.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    public String readInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("fmt_stream_map")) {
                stringBuilder.append(line).append("\n");
                break;
            }
        }
        reader.close();
        String result = decode(stringBuilder.toString());
        String[] url = result.split("\\|");
        return url[1];
    }

    public String decode(String url) {
        String working = url;
        int index;
        index = working.indexOf("\\u");
        while (index > -1) {
            int length = working.length();
            if (index > (length - 6)) break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring, 16);
            String stringStart = working.substring(0, index);
            String stringEnd = working.substring(numFinish);
            working = stringStart + ((char) number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

}
