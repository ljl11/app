package com.example.util.newDialog;

import com.example.newenergyvehicle.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowInterDialog extends Dialog {

	public ShowInterDialog(Context context) {
		super(context);
	}

	public ShowInterDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private View contentView;
		private Context context;
		private LayoutInflater inflater;

		public Builder(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		public Builder setContent(View contentView) {
			this.contentView = contentView;
			return this;
		}

		public ShowInterDialog onCreate() {
			ShowInterDialog dialog;
			View v = inflater.inflate(R.layout.showinterdialog, null);
			dialog = new ShowInterDialog(context, R.style.Dialog);

			LinearLayout ll = (LinearLayout) v
					.findViewById(R.id.showinter_content);
			TextView titleV = (TextView) v.findViewById(R.id.inter_title);

			if (contentView.getParent() != null) {
				((LinearLayout) contentView.getParent())
						.removeView(contentView);
			}

			ll.addView(contentView);
			dialog.setContentView(v);
			return dialog;
		}
	}
}
