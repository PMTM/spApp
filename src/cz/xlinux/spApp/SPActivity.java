package cz.xlinux.spApp;

import aidl.core.API.EntryPoint;
import aidl.core.API.OnCouponChange;
import aidl.core.API.OnNewHistoryItem;
import aidl.core.API.OnNewReceipt;
import aidl.core.API.OnTicketChange;
import aidl.core.API.SecurityWatchdog;
import aidl.sp.API.Coupon;
import aidl.sp.API.HistoryItem;
import aidl.sp.API.Receipt;
import aidl.sp.API.Ticket;
import cz.xlinux.libAPI.libFce.APIConnection;
import cz.xlinux.libAPI.libFce.CBOnSvcChange;
import cz.xlinux.spApp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SPActivity extends Activity implements OnClickListener,
		CBOnSvcChange {

	private static final String LOG_TAG = "SPActivity";
	private APIConnection conn;
	private TextView mTvLog;
	private boolean isBound;
	private EntryPoint apiService;
	private SecurityWatchdog wd;
	private OnCouponChange couponCB;
	private OnNewHistoryItem histCB;
	private Coupon exCoupon;
	private HistoryItem exHistoryItem;
	private Ticket exTicket;
	private Receipt exReceipt;
	private OnTicketChange ticketCB;
	private OnNewReceipt receiptCB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ---
		mTvLog = (TextView) findViewById(R.id.tvLog);
		// mTvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

		Button clr;
		clr = (Button) findViewById(R.id.btGetWatchDog);
		clr.setOnClickListener(this);

		clr = (Button) findViewById(R.id.btPushHist);
		clr.setOnClickListener(this);
		clr = (Button) findViewById(R.id.btPushCoupon);
		clr.setOnClickListener(this);
		clr = (Button) findViewById(R.id.btPullCoupon);
		clr.setOnClickListener(this);

		clr = (Button) findViewById(R.id.btPushReceipt);
		clr.setOnClickListener(this);
		clr = (Button) findViewById(R.id.btPushTicket);
		clr.setOnClickListener(this);
		clr = (Button) findViewById(R.id.btPullTicket);
		clr.setOnClickListener(this);
		// ---

		bindRemoteService();
	}

	private void bindRemoteService() {
		conn = new APIConnection(this);

		Intent intent = new Intent("core.API.BindRemote");
		intent.putExtra("version", "1.0");
		Log.d(LOG_TAG, "intent = " + intent);

		isBound = bindService(intent, conn, Context.BIND_AUTO_CREATE);
		Log.d(LOG_TAG, "bindService = " + isBound);
	}

	@Override
	public void onClick(View v) {
		mTvLog.setText("...");
		Log.d(LOG_TAG, "onClick,v=" + v);
		switch (v.getId()) {
		case R.id.btGetWatchDog:
			getWatchDog();
			break;
		case R.id.btPushHist:
			pushHistory();
			break;
		case R.id.btPushCoupon:
			pushCoupon();
			break;
		case R.id.btPullCoupon:
			pullCoupon();
			break;
		case R.id.btPushReceipt:
			pushReceipt();
			break;
		case R.id.btPushTicket:
			pushTicket();
			break;
		case R.id.btPullTicket:
			pullTicket();
			break;
		}
	}

	private void initCoupon() {
		if (exCoupon == null) {
			exCoupon = new Coupon("Coupon", "DM Drogerie");
		}
	}

	private void initTicket() {
		if (exTicket == null) {
			exTicket = new Ticket("Ticket", "U2 Strahov 22.5.2000");
		}
	}

	private void initHistoryItem() {
		if (exHistoryItem == null) {
			exHistoryItem = new HistoryItem("Paid", "2.346USD");
		}
	}

	private void initReceipt() {
		if (exReceipt == null) {
			exReceipt = new Receipt("Paid", "2.346USD");
		}
	}

	private void pullCoupon() {
		initCoupon();
		if (couponCB != null) {
			try {
				couponCB.removeCoupon(exCoupon);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no couponCB:-(");
		}
	}

	private void pushCoupon() {
		initCoupon();
		if (couponCB != null) {
			try {
				couponCB.addCoupon(exCoupon);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no couponCB:-(");
		}
	}

	private void pullTicket() {
		initTicket();
		if (ticketCB != null) {
			try {
				Log.d(LOG_TAG, "exTicket = " + exTicket);
				ticketCB.removeTicket(exTicket);
			} catch (DeadObjectException e) {
				Log.d(LOG_TAG, "dead object aka other side dead");
				bindRemoteService();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no TicketCB:-(");
		}
	}

	private void pushTicket() {
		initTicket();
		if (ticketCB != null) {
			try {
				ticketCB.addTicket(exTicket);
			} catch (DeadObjectException e) {
				Log.d(LOG_TAG, "dead object aka other side dead");
				bindRemoteService();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no TicketCB:-(");
		}
	}

	private void pushHistory() {
		initHistoryItem();
		if (histCB != null) {
			try {
				histCB.addHistoryItem(exHistoryItem);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no histCB:-(");
		}
	}

	private void pushReceipt() {
		initReceipt();
		if (receiptCB != null) {
			try {
				receiptCB.addReceipt(exReceipt);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(LOG_TAG, "no receiptCB:-(");
		}
	}

	private void getWatchDog() {
		if (apiService != null) {
			try {
				if (wd != null) {
					wd.renewTimer();
					wd.expireTimerNow();
				} else {
					Log.d(LOG_TAG, "Watchdog was not delivered");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			if (isBound) {
				unbindService(conn);
				Log.e(LOG_TAG, "Activity destroy was called");
			}
		} catch (Throwable t) {
		}
	}

	@Override
	public void setService(EntryPoint apiService) {
		Log.d(LOG_TAG, "setService apiService = " + apiService);
		this.apiService = apiService;
		if (apiService != null) {
			try {
				wd = apiService.getSecurityWatchdog();
				couponCB = apiService.getCouponCB();
				histCB = apiService.getHistoryCB();
				ticketCB = apiService.getTicketCB();
				receiptCB = apiService.getReceiptCB();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
