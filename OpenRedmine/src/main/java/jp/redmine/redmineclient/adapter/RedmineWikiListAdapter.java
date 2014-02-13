package jp.redmine.redmineclient.adapter;

import android.content.Context;
import android.view.View;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import jp.redmine.redmineclient.db.cache.DatabaseCacheHelper;
import jp.redmine.redmineclient.entity.RedmineWiki;
import jp.redmine.redmineclient.form.RedmineWikiListItemForm;

public class RedmineWikiListAdapter extends RedmineDaoAdapter<RedmineWiki, Long, DatabaseCacheHelper> {
	protected Integer connection_id;
	protected Long project_id;
	public RedmineWikiListAdapter(DatabaseCacheHelper helper, Context context) {
		super(helper, context, RedmineWiki.class);
	}

	public void setupParameter(int connection, long project){
		connection_id = connection;
		project_id = project;
	}

    @Override
	public boolean isValidParameter(){
		if(connection_id == null || project_id == null)
			return false;
		else
			return true;
	}

	@Override
	protected int getItemViewId() {
		return android.R.layout.simple_list_item_1;
	}

	@Override
	protected void setupView(View view, RedmineWiki data) {
		RedmineWikiListItemForm form = new RedmineWikiListItemForm(view);
		form.setValue(data);
	}

	@Override
	protected QueryBuilder<RedmineWiki, Long> getQueryBuilder() throws SQLException {
		QueryBuilder<RedmineWiki, Long> builder = dao.queryBuilder();
		builder
				.orderBy(RedmineWiki.TITLE, true)
				.where()
				.eq(RedmineWiki.CONNECTION, connection_id)
				.and()
				.eq(RedmineWiki.PROJECT_ID, project_id)
		;
		return builder;
	}

	@Override
	protected long getDbItemId(RedmineWiki item) {
		if(item == null){
			return -1;
		} else {
			return item.getId();
		}
	}

}
