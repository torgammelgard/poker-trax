package se.torgammelgard.pokertrax.fragments

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import se.torgammelgard.pokertrax.MainApp
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.Adapters.ResultAdapter

import java.util.ArrayList
import java.util.HashMap

class ResultsFragment : android.support.v4.app.Fragment(), AdapterView.OnItemLongClickListener {
    private var mActionMode: Any? = null
    private var mSelectedItemPos = -1
    private var mResultListView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.results, container, false)
        mResultListView = view.findViewById<ListView>(R.id.list_result)

        updateListView()

        return view
    }

    /** updates the result list  in an AsyncTask */
    fun updateListView() {
        object : AsyncTask<Void, Void, ResultAdapter>() {

            override fun doInBackground(vararg params: Void): ResultAdapter {
                val dataList = ArrayList<Map<String, String>>()
                val sessions = (activity.application as MainApp).mDataSource!!.getLastSessions(20)
                val game_structures = (activity.application as MainApp).mDataSource!!.allGameStructures
                val gameStructureStringList = ArrayList<String>()
                for (game_structure in game_structures!!) {
                    gameStructureStringList.add(game_structure.toString())
                }
                for (session in sessions!!) {
                    val map = HashMap<String, String>()
                    map.put("id", session.id.toString())
                    map.put("gameTypeRef", session.game_type_ref.toString())
                    map.put("gameStructure", gameStructureStringList[session.game_structure_ref - 1])
                    map.put("minutes", session.duration.toString())
                    map.put("result", session.result.toString())
                    dataList.add(map)
                }

                val allGameTypes = (activity.application as MainApp).mDataSource!!.allGameTypes

                val from = arrayOf("gameStructure", "gameTypeRef", "minutes", "result")
                val to = intArrayOf(R.id.text0, R.id.text1, R.id.text2, R.id.text3)

                return ResultAdapter(activity, dataList, allGameTypes!!,
                        R.layout.result_list_item, from, to)
            }

            override fun onPostExecute(adapter: ResultAdapter) {
                mResultListView!!.adapter = adapter
            }
        }.execute()

        if (mResultListView != null)
            mResultListView!!.onItemLongClickListener = this
    }

    private val mActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // called once on initial creation
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.session_selection, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            // after creation and any time the ActionMode is invalidated
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            if (item.itemId == R.id.menuitem_discard) {
                try {
                    val mapOfSession = mResultListView!!.adapter.getItem(mSelectedItemPos) as HashMap<String, String>
                    val sessionID = java.lang.Long.valueOf(mapOfSession["id"])!!
                    (activity.application as MainApp).mDataSource!!.deleteSession(sessionID)
                } catch (e: NumberFormatException) {
                    Log.d(LOG, "Couldn't delete", e)
                } catch (e: ClassCastException) {
                    Log.d(LOG, "Could't delete", e)
                }

            }
            mode.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            // when the action mode is closed
            mActionMode = null
            mSelectedItemPos = -1
            updateListView()
        }
    }

    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        if (mSelectedItemPos != -1)
            return true
        mSelectedItemPos = position
        view.setBackgroundColor(Color.RED)
        if (mActionMode != null)
            return false
        val v = this@ResultsFragment.view
        if (v != null) {
            mActionMode = this@ResultsFragment.view!!.startActionMode(mActionModeCallback)
        }
        view.isSelected = true
        return true
    }

    companion object {
        private val LOG = "ResultsFragment"
    }
}
