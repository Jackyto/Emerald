package com.emerald;

import java.io.IOException;
import java.net.Socket;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class RemoteActivity extends Activity {

	private Socket socket;

	private static String SERVER_IP = "";
	private TCPClient	client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote);
		ImageButton bplay = (ImageButton) findViewById(R.id.play);
		ImageButton bnext = (ImageButton) findViewById(R.id.next);
		ImageButton bprev = (ImageButton) findViewById(R.id.prev);
		ImageButton bstop = (ImageButton) findViewById(R.id.stop);

		Bundle b = getIntent().getExtras();
		SERVER_IP = b.getString("IP");

		new connectTask().execute("");

		bplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();				
			}
		});


		bnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				next();				
			}
		});


		bprev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prev();				
			}
		});

		bstop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop();				
			}
		});
	}

	@Override
	public void onBackPressed() {
		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
			if (client != null) {
				client.stopClient();
				client = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	public class connectTask extends AsyncTask<String,String,TCPClient> {

		@Override
		protected TCPClient doInBackground(String... message) {

			//we create a TCPClient object and
			client = new TCPClient(SERVER_IP);
			client.run();

			return null;
		}

	}

	public void play() {
		String str = "PLAY";
		client.sendMessage(str);
	}

	public void next() {
		String str = "NEXT";
		client.sendMessage(str);


	}
	public void prev() {
		String str = "PREV";
		client.sendMessage(str);


	}
	public void stop() {
		String str = "STOP";
		client.sendMessage(str);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
			if (client != null) {
				client.stopClient();
				client = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
