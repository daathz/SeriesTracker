package hu.bme.aut.seriestracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Viktor on 2016. 12. 14..
 */

public class NewSeriesRecordDialogFragment extends AppCompatDialogFragment {

    public static final String RECORD_ID = "record_id";


    private EditText titleEditText;
    private EditText seasonEditText;
    private EditText episodeEditText;
    private EditText scoreEditText;
    private Spinner stateSpinner;

    private SeriesRecord record;

    public static final String TAG = "NewSeriesRecordDialogFragment";

    public interface  INewSeriesRecordDialogListener {
        void onSeriesRecordCreated(SeriesRecord newRecord);
    }

    public interface IRecordSelectedListener {
        void recordSelected(SeriesRecord record);
    }

    private INewSeriesRecordDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof INewSeriesRecordDialogListener) {
            listener = (INewSeriesRecordDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the INewSeriesRecordListener interface!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(record != null) {
            record = (SeriesRecord) getArguments().getSerializable(RECORD_ID);
        }

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_series_record)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onSeriesRecordCreated(getSeriesRecord());
                        }
                    }

                    private boolean isValid() {
                        return titleEditText.getText().length() > 0;
                    }

                    private SeriesRecord getSeriesRecord() {
                        SeriesRecord seriesRecord;
                        if (record == null)
                            seriesRecord = new SeriesRecord();
                        else
                            seriesRecord = record;
                        seriesRecord.title = titleEditText.getText().toString();
                        try {
                            seriesRecord.season = Integer.parseInt(seasonEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            seriesRecord.season = 1;
                        }
                        try {
                            seriesRecord.episode = Integer.parseInt(episodeEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            seriesRecord.episode = 1;
                        }
                        try {
                            seriesRecord.score = Integer.parseInt(scoreEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            seriesRecord.score = 0;
                        }
                        seriesRecord.state = SeriesRecord.State.getByOrdinal(stateSpinner.getSelectedItemPosition());
                        seriesRecord.save();
                        return seriesRecord;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_series_record, null);
        titleEditText = (EditText)contentView.findViewById(R.id.SeriesRecordTitleEditText);
        seasonEditText = (EditText)contentView.findViewById(R.id.SeriesRecordSeasonEditText);
        episodeEditText = (EditText)contentView.findViewById(R.id.SeriesRecordEpisodeEditText);
        scoreEditText = (EditText)contentView.findViewById(R.id.SeriesRecordScoreEditView);
        stateSpinner = (Spinner)contentView.findViewById(R.id.SeriesRecordStateSpinner);
        stateSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.state_items)));

        if (record != null) {
            titleEditText.setText(record.title);
            seasonEditText.setText(record.season);
            episodeEditText.setText(record.episode);
            scoreEditText.setText(record.score);
        }

        return contentView;

    }

    public static NewSeriesRecordDialogFragment newIstance(SeriesRecord record) {
        NewSeriesRecordDialogFragment dialogFragment = new NewSeriesRecordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RECORD_ID, record);
        dialogFragment.setArguments(bundle);
        return  dialogFragment;
    }

}
