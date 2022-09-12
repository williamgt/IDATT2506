package no.ntnu.idatt2506.stud.williagt.descriptions

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.lang.ClassCastException

class GameTitles : Fragment() {
    private var titles: Array<String> = arrayOf()
    private var memberListener: OnFragmentInteractionListener? = null //For sharing info with the other fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titles = resources.getStringArray(R.array.titles)
    }

    private fun onClickTitle(parent: AdapterView<*>, view: View, position: Int, rowId: Long) {
        memberListener!!.onFragmentInteractionListener(position)
    }

    private fun initList(view: View) {
        val titleList = view.findViewById<ListView>(R.id.titles)
        titleList.onItemClickListener = AdapterView.OnItemClickListener {parent, view, position, rowId ->
            onClickTitle(parent, view, position, rowId)
        }
        val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_activated_1, titles)

        titleList.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_titles, container, false)

        initList(view)

        return view
    }

    //Interface to be implemented by the other fragment
    interface OnFragmentInteractionListener {
        fun onFragmentInteractionListener(indexClicked: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Setting the memberListener to the activity, aka the other fragment, that implements the defined interface
        memberListener = try {
            activity as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw e
        }
    }

    override fun onDetach() {
        super.onDetach()
        memberListener = null
    }
}