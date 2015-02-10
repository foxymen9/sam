package com.jiang.src.sam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jiang.src.common.BaseActivity;

public class ContactActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		initControls();
	}

	private void initControls() {
		Button btnEmail = (Button) findViewById(R.id.contact_email_btn);

		btnEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] recipients = {"info@learnaboutsam.org"};

				Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri
						.parse("mailto:"));
				// prompts email clients only
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
				
				try {
					startActivity(Intent.createChooser(emailIntent,
							"Choose an email client from..."));

				} catch (android.content.ActivityNotFoundException ex) {

					Toast.makeText(ContactActivity.this,
							"No email client installed.", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}
}
