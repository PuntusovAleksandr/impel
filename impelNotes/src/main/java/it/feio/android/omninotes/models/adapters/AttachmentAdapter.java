package it.feio.android.omninotes.models.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.async.BitmapWorkerTask;
import it.feio.android.omninotes.models.Attachment;
import it.feio.android.omninotes.models.listeners.OnAttachingFileListener;
import it.feio.android.omninotes.models.views.ExpandableHeightGridView;
import it.feio.android.omninotes.models.views.SquareImageView;
import it.feio.android.omninotes.utils.BitmapHelper;
import it.feio.android.omninotes.utils.Constants;
import it.feio.android.omninotes.utils.Fonts;
import it.feio.android.omninotes.utils.date.DateHelper;
import roboguice.util.Ln;

import java.util.ArrayList;
import java.util.List;


public class AttachmentAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Attachment> attachmentsList = new ArrayList<Attachment>();
    private ExpandableHeightGridView mGridView;
    private LayoutInflater inflater;
    private OnAttachingFileListener mOnAttachingFileErrorListener;


    public AttachmentAdapter(Activity mActivity, List<Attachment> attachmentsList, ExpandableHeightGridView mGridView) {
        this.mActivity = mActivity;
        this.attachmentsList = attachmentsList;
        this.mGridView = mGridView;
        this.inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return attachmentsList.size();
    }


    public Attachment getItem(int position) {
        return attachmentsList.get(position);
    }


    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Ln.v("GridView called for position " + position);

        Attachment mAttachment = attachmentsList.get(position);

        AttachmentHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, mActivity.getSharedPreferences(Constants.PREFS_NAME, 
                    Context.MODE_MULTI_PROCESS), convertView);

            holder = new AttachmentHolder();
            holder.image = (SquareImageView) convertView.findViewById(R.id.gridview_item_picture);
            holder.text = (TextView) convertView.findViewById(R.id.gridview_item_text);
            convertView.setTag(holder);
        } else {
            holder = (AttachmentHolder) convertView.getTag();
        }

        // Draw name in case the type is an audio recording
        if (mAttachment.getMime_type() != null && mAttachment.getMime_type().equals(Constants.MIME_TYPE_AUDIO)) {
            String text = "";

            if (mAttachment.getLength() > 0) {
                // Recording duration
                text = DateHelper.formatShortTime(mActivity, mAttachment.getLength());
            } else {
                // Recording date otherwise
                text = DateHelper.getLocalizedDateTime(mActivity, mAttachment
                                .getUri().getLastPathSegment().split("\\.")[0],
                        Constants.DATE_FORMAT_SORTABLE);
            }

            if (text == null) {
                text = mActivity.getString(R.string.attachment);
            }
            holder.text.setText(text);
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.text.setVisibility(View.GONE);
        }

        // Draw name in case the type is an audio recording (or file in the future)
        if (mAttachment.getMime_type() != null && mAttachment.getMime_type().equals(Constants.MIME_TYPE_FILES)) {
            holder.text.setText(mAttachment.getName());
            holder.text.setVisibility(View.VISIBLE);
        }

        // Starts the AsyncTask to draw bitmap into ImageView
//		loadThumbnail(holder, mAttachment);
        Uri thumbnailUri = BitmapHelper.getThumbnailUri(mActivity, mAttachment);
        Glide.with(mActivity)
                .load(thumbnailUri)
                .centerCrop()
                .crossFade()
                .into(holder.image);

        return convertView;
    }


    @SuppressLint("NewApi")
    private void loadThumbnail(AttachmentHolder holder, Attachment mAttachment) {
        if (cancelPotentialWork(mAttachment.getUri(), holder.image)) {
            BitmapWorkerTask task = new BitmapWorkerTask(mActivity, holder.image, Constants.THUMBNAIL_SIZE,
                    Constants.THUMBNAIL_SIZE);
            holder.image.setAsyncTask(task);
            if (mOnAttachingFileErrorListener != null)
                task.setOnErrorListener(mOnAttachingFileErrorListener);
            if (Build.VERSION.SDK_INT >= 11) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mAttachment);
            } else {
                task.execute(mAttachment);
            }
        }
    }


    public static boolean cancelPotentialWork(Uri uri, SquareImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = (BitmapWorkerTask) imageView.getAsyncTask();

        if (bitmapWorkerTask != null && bitmapWorkerTask.getAttachment() != null) {
            final Uri bitmapData = bitmapWorkerTask.getAttachment().getUri();
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || bitmapData != uri) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was
        // cancelled
        return true;
    }


    public class AttachmentHolder {

        TextView text;
        SquareImageView image;
    }


    public void setOnErrorListener(OnAttachingFileListener listener) {
        this.mOnAttachingFileErrorListener = listener;
    }

}
