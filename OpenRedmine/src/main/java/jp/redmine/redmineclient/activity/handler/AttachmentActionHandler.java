package jp.redmine.redmineclient.activity.handler;

import jp.redmine.redmineclient.R;
import jp.redmine.redmineclient.fragment.FileDownload;
import jp.redmine.redmineclient.param.AttachmentArgument;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class AttachmentActionHandler extends Core implements AttachmentActionInterface {

	public AttachmentActionHandler(ActivityRegistry manager) {
		super(manager);
	}

	@Override
	public void onAttachmentSelected(int connectionid, int attachmentid) {

		final AttachmentArgument arg = new AttachmentArgument();
		arg.setArgument();
		arg.setConnectionId(connectionid);
		arg.setAttachmentId(attachmentid);

		runTransaction(new TransitFragment() {
			@Override
			public void action(FragmentTransaction tran) {
				tran.replace(R.id.fragmentOne, FileDownload.newInstance(arg));
				Fragment frag = manager.getFragment().findFragmentById(R.id.fragmentOneFooter);
				if(frag != null)
					tran.remove(frag);
			}
		}, null);
	}

}