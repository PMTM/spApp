
package cz.xlinux.spApp;

import android.app.Activity;
import android.content.ComponentName;
import android.widget.Toast;
import cz.xlinux.libAPI.libFce.APIConnection;

public class MyAPIConnection extends APIConnection {

    Activity act;

    public MyAPIConnection(SPActivity spActivity) {
        super(spActivity);
        this.act = spActivity;
        Toast.makeText(act, "MyAPIConnection.MyAPIConnection(SPActivity spActivity)", Toast.LENGTH_LONG).show();
    }

    public void onServiceDisconnected(ComponentName name) {
        super.onServiceDisconnected(name);
        Toast.makeText(act, "MyAPIConnection.onServiceDisconnected(ComponentName name)", Toast.LENGTH_LONG).show();
        act.finish();
    }

}
