package test.nicolasjafelle.kithub.ui.main

import test.nicolasjafelle.kithub.api.response.RepoListResponse
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.mvp.RestHttpView

/**
 * Created by nicolas on 11/9/17.
 */
interface MainView : RestHttpView {

    fun onGetData(response: RepoListResponse?)

    fun onSearchResult(filteredList: List<Repo>?)

}