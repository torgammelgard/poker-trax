package se.torgammelgard.pokertrax.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.activities.SessionAdapter
import se.torgammelgard.pokertrax.model.entities.Session
import java.util.*

class SessionsFragment : Fragment() {

    private val mDummySessions = arrayOf(
            Session().apply {
                location = "Home game"
                result = 28
                date = Date(350_000_000)
            },
            Session().apply {
                location = "Commerce Casino"
                result = 2300
                date = Date(360_000_000)
            },
            Session().apply {
                location = "The Bike"
                result = -1100
                date = Date(370_000_000)
            })
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sessions_frag, container, false)
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = SessionAdapter(mDummySessions)
        recyclerView = view.findViewById<RecyclerView>(R.id.sessions_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.notifyDataSetChanged()
    }
}
