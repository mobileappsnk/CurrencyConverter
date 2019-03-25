package nk.mobileapps.currencyconverter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrencyListAdapter extends BaseAdapter {

	private Activity activity;
	public static String[][] data;

	private static LayoutInflater inflater = null;
	private Animation animation;

	public CurrencyListAdapter(Activity activity, String[][] data) {
		this.activity = activity;
		this.data = data;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public CurrencyListAdapter(Activity activity, ArrayList<String> code,
			ArrayList<String> name) {

		this.activity = activity;

		data = new String[code.size()][2];
		for (int i = 0; i < code.size(); i++) {
			data[i][0] = code.get(i);
			data[i][1] = name.get(i);
		}

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.search_currency_listview, null);
		TextView TEXT_CURRENCY_CODE = (TextView) vi
				.findViewById(R.id.TEXT_CURRENCY_CODE);
		TEXT_CURRENCY_CODE.setText(data[position][0]);
		animation = AnimationUtils.loadAnimation(
				activity.getApplicationContext(), R.anim.zoom_out);

		TEXT_CURRENCY_CODE.setAnimation(animation);
		TextView SEARCH_PAGE_CURRENCY_NAME_TEXT = (TextView) vi
				.findViewById(R.id.SEARCH_PAGE_CURRENCY_NAME_TEXT);
		SEARCH_PAGE_CURRENCY_NAME_TEXT.setText(data[position][1]);

		return vi;
	}
}
