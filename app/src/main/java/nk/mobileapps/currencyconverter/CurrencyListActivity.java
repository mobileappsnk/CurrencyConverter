package nk.mobileapps.currencyconverter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;

import nk.mobileapps.currencyconverter.utils.HelperClass;

public class CurrencyListActivity extends Activity {

	CurrencyListAdapter adapter;
	ImageView IMG_CANCEL;
	EditText EDIT_SEARCH_BOX;
	ScrollView SCROLLVIEW;
	TableLayout TABLEL_RECENTTABLE;
	ListView ALL_CURRENCY_LISTVIEW;
	LinearLayout LL_ERROR_MESSAGE, POPULARLIST_HEADER;

	ArrayList<String> currencycode_sort = new ArrayList<String>();
	ArrayList<String> currencyname_sort = new ArrayList<String>();
	int textlength = 0;
	String reqcode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currencysearch);
		final Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.zoom_in);
		reqcode = getIntent().getExtras().getString("requestcode");
		findViews();
		adapter = new CurrencyListAdapter(this, HelperClass.currency_data);
		ALL_CURRENCY_LISTVIEW.setAdapter(adapter);

		ALL_CURRENCY_LISTVIEW.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

					}
				});

				view.startAnimation(anim);

				Intent data = new Intent();
				data.putExtra("CCODE", CurrencyListAdapter.data[position][0]);
				data.putExtra("CNAME", CurrencyListAdapter.data[position][1]);
				if (reqcode.equals("11"))
					setResult(RESULT_OK, data);
				else if (reqcode.equals("22"))
					setResult(RESULT_OK, data);

				finish();

			}
		});

		// Edit Text
		EDIT_SEARCH_BOX.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

				textlength = EDIT_SEARCH_BOX.getText().length();
				currencycode_sort.clear();
				currencyname_sort.clear();
				int len = HelperClass.currency_data.length;
				String text[][] = HelperClass.currency_data;
				for (int i = 0; i < len; i++) {

					System.out.println("textlength: " + textlength);
					System.out.println("text[i][1].length(): "
							+ text[i][1].length() + " :" + text[i][1]);

					if (textlength <= text[i][1].length()) {
						if (EDIT_SEARCH_BOX
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) text[i][1].subSequence(0,
												textlength))) {
							currencycode_sort.add(text[i][0]);
							currencyname_sort.add(text[i][1]);
						}
					}

				}

				if (currencycode_sort.size() > 0) {
					LL_ERROR_MESSAGE.setVisibility(View.GONE);
					POPULARLIST_HEADER.setVisibility(View.VISIBLE);
					ALL_CURRENCY_LISTVIEW.setAdapter(new CurrencyListAdapter(
							CurrencyListActivity.this, currencycode_sort,
							currencyname_sort));
				} else {
					LL_ERROR_MESSAGE.setVisibility(View.VISIBLE);
					POPULARLIST_HEADER.setVisibility(View.GONE);
					ALL_CURRENCY_LISTVIEW.setAdapter(new CurrencyListAdapter(
							CurrencyListActivity.this, currencycode_sort,
							currencyname_sort));

				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		// IMG_CANCEL
		IMG_CANCEL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}

	private void findViews() {
		IMG_CANCEL = (ImageView) findViewById(R.id.IMG_CANCEL);
		EDIT_SEARCH_BOX = (EditText) findViewById(R.id.EDIT_SEARCH_BOX);
		SCROLLVIEW = (ScrollView) findViewById(R.id.SCROLLVIEW);
		TABLEL_RECENTTABLE = (TableLayout) findViewById(R.id.TABLEL_RECENTTABLE);
		ALL_CURRENCY_LISTVIEW = (ListView) findViewById(R.id.ALL_CURRENCY_LISTVIEW);
		LL_ERROR_MESSAGE = (LinearLayout) findViewById(R.id.LL_ERROR_MESSAGE);
		POPULARLIST_HEADER = (LinearLayout) findViewById(R.id.POPULARLIST_HEADER);
	}
}
