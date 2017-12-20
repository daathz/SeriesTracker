package hu.bme.aut.seriestracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NewSeriesRecordDialogFragment.INewSeriesRecordDialogListener {

    private RecyclerView recyclerView;
    private SeriesAdapter adapter;

    private SeriesRecord record;


    private void initRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.MainRecyclerView);
        adapter = new SeriesAdapter();
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<SeriesRecord>>() {

            @Override
            protected List<SeriesRecord> doInBackground(Void... voids) {
                return SeriesRecord.listAll(SeriesRecord.class);
            }

            @Override
            protected void onPostExecute(List<SeriesRecord> seriesRecords) {
                super.onPostExecute(seriesRecords);
                adapter.update(seriesRecords);
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewSeriesRecordDialogFragment().show(getSupportFragmentManager(), NewSeriesRecordDialogFragment.TAG);
            }
        });
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.MenuRemoveAll) {
            adapter.removeAllItems();
            return true;
        }

        if(item.getItemId() == R.id.MenuAbout) {
            new AboutDialogFragment().show(getSupportFragmentManager(), null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSeriesRecordCreated(SeriesRecord newRecord) {
        adapter.addRecord(newRecord);
    }
}
