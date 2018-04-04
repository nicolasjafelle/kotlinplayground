package test.nicolasjafelle.kithub.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by nicolas on 11/8/17.
 */
abstract class AbstractFragment<T> : Fragment() {


    protected var callback: T? = null

    protected abstract fun getMainLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true

//        BrastlewarkApplication.injectMembers(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            with(callback, {
                callback = context as T
            })
//            callback = context as T

        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement Callback interface")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getMainLayoutResId(), container, false)
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}