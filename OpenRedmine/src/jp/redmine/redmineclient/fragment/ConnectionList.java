package jp.redmine.redmineclient.fragment;

import java.io.File;

import jp.redmine.redmineclient.CommonPreferenceActivity;
import jp.redmine.redmineclient.R;
import jp.redmine.redmineclient.adapter.ConnectionListAdapter;
import jp.redmine.redmineclient.db.cache.DatabaseCacheHelper;
import jp.redmine.redmineclient.db.store.DatabaseHelper;
import jp.redmine.redmineclient.entity.RedmineConnection;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ConnectionList extends ListFragment {
	private DatabaseHelper helperStore;
	private ConnectionListAdapter adapter;
	private View mFooter;
	private OnArticleSelectedListener mListener;
	public interface OnArticleSelectedListener {
		public void onConnectionSelected(int connectionid);
		public void onConnectionEdit(int connectionid);
		public void onConnectionAdd();
	}

	public ConnectionList(){
		super();
	}

	static public ConnectionList newInstance(){
		return new ConnectionList();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof ActivityInterface){
			mListener = ((ActivityInterface)activity).getHandler(OnArticleSelectedListener.class);
		}
		if(mListener == null) {
			//setup empty events
			mListener = new OnArticleSelectedListener() {
				@Override
				public void onConnectionSelected(int connectionid) {
				}
				@Override
				public void onConnectionEdit(int connectionid) {
				}
				@Override
				public void onConnectionAdd() {
				}
			};
		}

	}

	@Override
	public void onDestroyView() {
		setListAdapter(null);
		super.onDestroyView();
		if(helperStore != null){
			helperStore.close();
			helperStore = null;
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().addFooterView(mFooter);
		getListView().setFastScrollEnabled(true);

		helperStore = new DatabaseHelper(getActivity());

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				ListView listView = (ListView) parent;
				RedmineConnection item = (RedmineConnection) listView.getItemAtPosition(position);
				mListener.onConnectionEdit(item.getId());
				return true;
			}
		});

		mFooter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onConnectionAdd();
			}
		});

		adapter = new ConnectionListAdapter(helperStore);
		adapter.notifyDataSetInvalidated();
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFooter = inflater.inflate(R.layout.listview_add,null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		super.onListItemClick(parent, v, position, id);
		ListView listView = (ListView) parent;
		RedmineConnection item = (RedmineConnection) listView.getItemAtPosition(position);
		mListener.onConnectionSelected(item.getId());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu( menu, inflater );
		inflater.inflate( R.menu.connection, menu );
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch ( item.getItemId() )
		{
			case R.id.menu_access_addnew:
			{
				mListener.onConnectionAdd();
				return true;
			}
			case R.id.menu_access_removecache:
			{
				String path = DatabaseCacheHelper.getDatabasePath(getActivity().getApplicationContext());
				File file = new File(path);
				file.delete();
				Log.d("Cache Deleted",path);
				getActivity().finish();
				//@todo show dialog
				return true;
			}
			case R.id.menu_settings:
			{
				Intent intent = new Intent( getActivity().getApplicationContext(), CommonPreferenceActivity.class );
				startActivity( intent );

				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
