
package cz.xlinux.spApp;

import android.app.Activity;
import android.content.ComponentName;
import cz.xlinux.libAPI.libFce.APIConnection;

public class MyAPIConnection extends APIConnection {

    Activity act;

    public MyAPIConnection(SPActivity spActivity) {
        super(spActivity);
        this.act = spActivity;
    }

    public void onServiceDisconnected(ComponentName name) {
        super.onServiceDisconnected(name);
        act.finish();
    }

}
