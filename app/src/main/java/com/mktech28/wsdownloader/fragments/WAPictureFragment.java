package com.mktech28.wsdownloader.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mktech28.wsdownloader.R;
import com.mktech28.wsdownloader.adapters.WAPictureAdapter;
import com.mktech28.wsdownloader.models.ModelStatuses;
import com.mktech28.wsdownloader.utils.Config;

import java.io.File;
import java.util.ArrayList;

public class WAPictureFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<ModelStatuses> data;
    RecyclerView rv;
    TextView textView;
    SwipeRefreshLayout mSwipRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_status_pic, container, false);

        rv = view.findViewById(R.id.rv_status);
        mSwipRefreshLayout = view.findViewById(R.id.contentView);
        mSwipRefreshLayout.setOnRefreshListener(this);
        textView = view.findViewById(R.id.textView);
        rv.setHasFixedSize(true);

        loadData();

        return view;
    }

    private void loadData() {

        data = new ArrayList<>();
        final String path = Config.WhatsAppDirectoryPath;
        File directory = new File(path);
        if(directory.exists()){
            final File [] files = directory.listFiles();
            Log.d("Files", "Size: "+files.length);
            final String[] paths = {""};
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    for(int i=0; i< files.length; i++){
                        Log.d("Files", "FileName:" + files[i].getName());
                        Log.d("Files", "FileName:" + files[i].getName().substring(0, files[i].getName().length() - 4));
                        if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith("gif")) {
                            paths[0] = path + "" + files[i].getName();
                            ModelStatuses modelStatus = new ModelStatuses(paths[0], files[i].getName().substring(0, files[i].getName().length() - 4), 2);
                            data.add(modelStatus);
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (!(data.toArray().length > 0)) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("No Status Available \n Check Out some Status & come back again...");
                    }
                    WAPictureAdapter adapter = new WAPictureAdapter(getActivity(), data);
                    rv.setAdapter(adapter);

                    LinearLayoutManager llm = new GridLayoutManager(getActivity(), 2);
                    rv.setLayoutManager(llm);

                }
            }.execute();
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Status Available \n Check Out some Status & come back again...");

            Snackbar.make(getActivity().findViewById(android.R.id.content), "WhatsApp Not Installed",
                    Snackbar.LENGTH_SHORT).show();
        }
        refreshItems();

    }

    private void refreshItems() {
        onItemsLoadComplete();


    }

    private void onItemsLoadComplete() {
    mSwipRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}