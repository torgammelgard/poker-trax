package se.torgammelgard.pokertrax.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.activities.SessionAdapter
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import javax.inject.Inject

class SessionsFragment : Fragment() {

    @Inject lateinit var sessionRepository: SessionRepository

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sessions_frag, container, false)

        doAsync {
            val sessions = sessionRepository.getAllSessions()
            uiThread {
                recyclerView = view.findViewById<RecyclerView>(R.id.sessions_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = SessionAdapter(sessions)
                }
            }
        }
        return view
    }

    fun update() {
        doAsync {
            val sessions = sessionRepository.getAllSessions()
            uiThread {
                recyclerView.adapter = SessionAdapter(sessions)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

    }

}
