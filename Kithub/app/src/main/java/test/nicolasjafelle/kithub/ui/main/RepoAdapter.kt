package test.nicolasjafelle.kithub.ui.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.ui.view.RepoItemView

/**
 * Created by nicolas on 11/10/17.
 */
class RepoAdapter(val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val repoList: MutableList<Repo> = ArrayList()

    fun addList(repoList: List<Repo>) {
        clearList()
        this.repoList.addAll(repoList)
        notifyDataSetChanged()
    }

    fun clearList() {
        repoList.clear()
    }


    fun getItemAt(position: Int): Repo? {
        return if (!repoList.isEmpty()) {
            repoList[position]
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.populateData(getItemAt(position))
        }
    }

    override fun getItemCount() = repoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(RepoItemView(parent.context))
    }

    /**
     * Inner class
     */
    private inner class ItemViewHolder(itemView: RepoItemView) : RecyclerView.ViewHolder(itemView) {

        fun populateData(repo: Repo?) {
            repo?.let {
                (itemView as RepoItemView).loadData(it) // it means the class using it...
                itemView.setOnClickListener { onItemClick(adapterPosition) }
            }
        }
    }
}