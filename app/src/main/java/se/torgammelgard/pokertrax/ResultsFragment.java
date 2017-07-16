package se.torgammelgard.pokertrax;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultsFragment extends android.support.v4.app.Fragment implements
        AdapterView.OnItemLongClickListener {
    private static final String LOG = "ResultsFragment";
    protected Object mActionMode;
    private int selectedItemPos = -1;
    private ListView mResultListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.results, container, false);
        mResultListView = view.findViewById(R.id.list_result);

        updateListView();

        return view;
    }

    /** updates the result list  in an AsyncTask*/
    public void updateListView() {
        new AsyncTask<Void, Void, ResultAdapter>() {

            @Override
            protected ResultAdapter doInBackground(Void... params) {
                ArrayList<Map<String, String>> dataList = new ArrayList<>();
                ArrayList<Session> sessions = ((MainApp)getActivity().getApplication())
                        .mDataSource.getLastSessions(20);
                ArrayList<Game_Structure> game_structures = (((MainApp) getActivity().
                        getApplication()).mDataSource.getAllGameStructures());
                ArrayList<String> gameStructureStringList = new ArrayList<>();
                for (Game_Structure game_structure : game_structures) {
                    gameStructureStringList.add(game_structure.toString());
                }
                for (Session session : sessions) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(session.getId()));
                    map.put("gameTypeRef", String.valueOf(session.getGame_type_ref()));
                    map.put("gameStructure", gameStructureStringList.get(session.getGame_structure_ref()-1));
                    map.put("minutes", String.valueOf(session.getDuration()));
                    map.put("result", String.valueOf(session.getResult()));
                    dataList.add(map);
                }

                ArrayList<String> allGameTypes = ((MainApp)getActivity().getApplication())
                        .mDataSource.getAllGameTypes();

                String[] from = {"gameStructure", "gameTypeRef", "minutes", "result"};
                int[] to = {R.id.text0, R.id.text1, R.id.text2 , R.id.text3};

                return new ResultAdapter(getActivity(), dataList, allGameTypes,
                        R.layout.result_list_item, from, to);


            }

            @Override
            protected void onPostExecute(ResultAdapter adapter) {
                mResultListView.setAdapter(adapter);
            }
        }.execute();

        if (mResultListView != null)
            mResultListView.setOnItemLongClickListener(this);
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
                try {
                    @SuppressWarnings("unchecked cast")
                    HashMap<String, String> mapOfSession= (HashMap<String, String>)mResultListView.
                            getAdapter().getItem(selectedItemPos);
                    long sessionID = Long.valueOf(mapOfSession.get("id"));
                    ((MainApp)getActivity().getApplication()).mDataSource.deleteSession(sessionID);
                } catch (NumberFormatException e) {
                    Log.d(LOG, "Couldn't delete", e);
                } catch (ClassCastException e) {
                    Log.d(LOG, "Could't delete", e);
                }
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // when the action mode is closed
            mActionMode = null;
            selectedItemPos = -1;
            updateListView();
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectedItemPos != -1)
            return true;
        selectedItemPos = position;
        view.setBackgroundColor(Color.RED);
        if (mActionMode != null)
            return false;
        View v = ResultsFragment.this.getView();
        if (v != null) {
            mActionMode = ResultsFragment.this.getView().startActionMode(mActionModeCallback);
        }
        view.setSelected(true);
        return true;
    }
}
