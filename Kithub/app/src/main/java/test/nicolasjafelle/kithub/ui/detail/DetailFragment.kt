package test.nicolasjafelle.kithub.ui.detail

import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import test.nicolasjafelle.kithub.R
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.ui.AbstractFragment

/**
 * Created by nicolas on 11/14/17.
 */
class DetailFragment : AbstractFragment<Unit>() {

    private lateinit var rootLinear: LinearLayout
    private lateinit var secondLinear: LinearLayout

    private lateinit var nameView: TextView
    private lateinit var fullnameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var ownerView: TextView
    private lateinit var idView: TextView


    companion object {
        const val SELECTED_REPO = "selected_repo"

        fun newInstance(args: Bundle): Fragment {
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!

        rootLinear = view.findViewById(R.id.fragment_detail_linear)
        secondLinear = view.findViewById(R.id.fragment_detail_second_linear)
        nameView = view.findViewById(R.id.fragment_detail_name_label)
        fullnameView = view.findViewById(R.id.fragment_detail_fullname_label)
        descriptionView = view.findViewById(R.id.fragment_detail_description_label)
        ownerView = view.findViewById(R.id.fragment_detail_owner_label)
        idView = view.findViewById(R.id.fragment_detail_id_label)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootLinear.isTransitionGroup = false
            secondLinear.isTransitionGroup = true
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = arguments?.getParcelable<Repo>(SELECTED_REPO)

        repository?.let {
            nameView.text = it.name
            fullnameView.text = it.fullName
            descriptionView.text = it.description
            idView.text = it.owner.id.toString()
            ownerView.text = it.owner.login
        }

    }


    override fun getMainLayoutResId() = R.layout.fragment_detail
}