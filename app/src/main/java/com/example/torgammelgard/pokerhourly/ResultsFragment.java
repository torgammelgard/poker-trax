package com.example.torgammelgard.pokerhourly;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsFragment extends android.support.v4.app.Fragment {

    protected Object mActionMode;
    private int selectedItemPos = -1;
    private ListView resultListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.results, container, false);
        resultListView = (ListView)view.findViewById(R.id.resultListView);
        updateListView(view);
        return view;
    }

    public void updateListView(View view) {

        //resultListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        final ArrayList<Session> sessionList = ((Tab_main_fragactivity)getActivity())
                .getDataSource()
                .getAllSessions();

        ArrayList<Map<String, String>> list;
        list = makeDataList(sessionList);
        String[] from = {"gameinfo", "hours", "result"};
        int[] to = {R.id.text1, R.id.text2 , R.id.text3};

        final SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.result_list_item,
                from, to);
        resultListView.setAdapter(adapter);

        resultListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                if (selectedItemPos != -1)
                    return true;
                selectedItemPos = position;
                view.setBackgroundColor(Color.RED);
                Toast.makeText(getActivity(), sessionList.get(position).getDate().toString(), Toast.LENGTH_SHORT).show(); //TODO erase
                if (mActionMode != null)
                    return false;
                mActionMode = ResultsFragment.this.getView().startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });


    }

    ArrayList<Map<String, String>> makeDataList(ArrayList<Session> sessions) {

        ArrayList<Map<String, String>> dataList = new ArrayList();

        for (Session session : sessions) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", String.valueOf(session.getId()));
            map.put("gameinfo", String.valueOf(session.getGame_type_ref()));
            map.put("minutes", String.valueOf(session.getDuration()));
            map.put("result", String.valueOf(session.getResult()));
            dataList.add(map);
        }

        return dataList;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // called once on initial creation
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.session_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // after creation and any time the ActionMode is invalidated
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menuitem_discard) {
                Map<String, String> mapOfSession = (Map<String, String>)resultListView.getAdapter()
                        .getItem(selectedItemPos);
                long sessionID = Long.valueOf(mapOfSession.get("id"));
                ((Tab_main_fragactivity)getActivity()).dataSource.deleteSession(sessionID);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // when the action mode is closed
            mActionMode = null;
            selectedItemPos = -1;
            updateListView(ResultsFragment.this.getView());
        }
    };
}
