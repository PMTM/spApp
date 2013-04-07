package cz.xlinux.spApp;

import cz.xlinux.spApp.R;

import android.app.Activity;
import android.os.Bundle;

public class SPActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
