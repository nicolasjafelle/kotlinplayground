package com.brastlewar.kotlin.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by nicolas on 11/10/17.
 */
class SpacesItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
            outRect.top = spacing
        }
        outRect.bottom = spacing // item bottom
    }
}