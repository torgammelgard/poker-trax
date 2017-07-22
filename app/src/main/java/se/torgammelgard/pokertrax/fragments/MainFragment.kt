package se.torgammelgard.pokertrax.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.activities.TabMain_FragmentActivity

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as TabMain_FragmentActivity).updateUI()
    }

    companion object {

        private val mLOGMESSAGE = "LOGMESSAGE"
    }
}
