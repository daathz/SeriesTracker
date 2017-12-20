package hu.bme.aut.seriestracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor on 2016. 12. 14..
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private final List<SeriesRecord> records;
    //private final IRecordSelectedListener listener;

    public SeriesAdapter() {
        //listener = selectedListener;
        records = new ArrayList<>();
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recordView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_series_list, parent , false);
        SeriesViewHolder viewHolder = new SeriesViewHolder(recordView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SeriesViewHolder holder, int position) {
        final SeriesRecord record = records.get(position);
        holder.titleTextView.setText(record.title);
        holder.episodeTextView.setText(record.season + "x" + record.episode);
        holder.stateTextView.setText(record.state.name());
        holder.scoreTextView.setText(record.score + "/10");


        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRecord(holder.getAdapterPosition());
            }
        });

        holder.episodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //NewSeriesRecordDialogFragment.newIstance(record);
                increaseEpisode(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.episodeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                decreaseEpisode(holder.getAdapterPosition());
                return false;
            }
        });

        holder.scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseScore(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.scoreButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                decreaseScore(holder.getAdapterPosition());
                return false;
            }
        });
/*        holder.sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upState(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });*/
        holder.stateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

/*    private void upState(int position) {
        SeriesRecord record = records.get(position);
        if(record.state.ordinal() > 0) {
            record.state = SeriesRecord.State.getByOrdinal(record.state.ordinal() - 1);
            notifyDataSetChanged();
            record.save();
        }
    }*/

    private void decreaseEpisode(int position) {
        SeriesRecord record = records.get(position);
        record.episode--;
        notifyDataSetChanged();
        record.save();
    }

    public void removeRecord(int position) {
        SeriesRecord removed = records.remove(position);
        removed.delete();
        notifyItemRemoved(position);
        if (position < records.size()) {
            notifyItemRangeChanged(position, records.size() - position);
        }
    }

    public void increaseEpisode(int position) {
        SeriesRecord record = records.get(position);
        record.episode++;
        notifyDataSetChanged();
        record.save();
    }

    public void increaseScore(int position) {
        SeriesRecord record = records.get(position);
        record.score++;
        notifyDataSetChanged();
        record.save();
    }

    public void decreaseScore(int position) {
        SeriesRecord record = records.get(position);
        record.score--;
        notifyDataSetChanged();
        record.save();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void addRecord(SeriesRecord record) {
        records.add(record);
        notifyItemInserted(records.size() - 1);
    }

    public void update(List<SeriesRecord> seriesRecords) {
        records.clear();
        records.addAll(seriesRecords);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        for (SeriesRecord record: records) {
            record.delete();
        }

        int count = records.size();
        records.clear();
        notifyItemRangeRemoved(0, count);
    }


    public class SeriesViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView episodeTextView;
        TextView stateTextView;
        TextView scoreTextView;
        ImageButton removeButton;
        ImageButton episodeButton;
        ImageButton scoreButton;
        ImageButton sButton;


        public SeriesViewHolder(View recordView) {
            super(recordView);
            titleTextView = (TextView) recordView.findViewById(R.id.SeriesRecordTitleTextView);
            episodeTextView = (TextView) recordView.findViewById(R.id.SeriesRecordEpisodeTextView);
            stateTextView = (TextView) recordView.findViewById(R.id.SeriesRecordStateTextView);
            scoreTextView = (TextView) recordView.findViewById(R.id.SeriesRecordScoreTextView);
            removeButton = (ImageButton)recordView.findViewById(R.id.SeriesRecordRemoveButton);
            episodeButton = (ImageButton)recordView.findViewById(R.id.SeriesRecordIncreaseButton);
            scoreButton = (ImageButton)recordView.findViewById(R.id.SeriesRecordDecreaseButton);
            sButton = (ImageButton)recordView.findViewById(R.id.SeriesRecordScoreButton);


        }
    }
}
