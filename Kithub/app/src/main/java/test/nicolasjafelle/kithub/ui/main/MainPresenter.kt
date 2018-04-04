package test.nicolasjafelle.kithub.ui.main

import test.nicolasjafelle.kithub.repository.Repository
import com.brastlewar.kotlin.utils.RestHttpExceptionHandler
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import test.nicolasjafelle.kithub.api.response.RepoListResponse
import test.nicolasjafelle.kithub.domain.Repo
import test.nicolasjafelle.kithub.mvp.BasePresenter
import test.nicolasjafelle.kithub.mvp.ViewState
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by nicolas on 11/9/17.
 */
class MainPresenter(val repository: Repository) : BasePresenter<MainView>() {

    private val MIN_LENGHT = 2

    var response: RepoListResponse? = null

    var filteredList: List<Repo>? = null

    var lastQuery: String? = null

    private lateinit var job: Job


    fun getPopulationList() {
        var ref = asReference()

        job = launch(UI) {
            try {
                ref().setCurrentState(ViewState.LOADING)
                val background = bg {
                    if (isActive) {
                        repository.populationResponse().execute().body()
                    } else {
                        return@bg null
                    }
                }

                ref().let {
                    it.response = background.await()
                    it.mvpView?.onGetData(it.response)
                    it.setCurrentState(ViewState.FINISH)
                }
            } catch (e: Exception) {
                RestHttpExceptionHandler().handle(UI, e, ref())
            }
        }
    }

    override fun detachMvpView() {
        super.detachMvpView()
        job.cancel()
    }

    fun showAll() {
        this.filteredList = null
        this.lastQuery = null
        mvpView?.onGetData(response)
    }


    fun searchCitizen(textToSearch: String) {
        val ref = asReference()

        job = launch(UI) {
            try {
                setCurrentState(ViewState.LOADING)

                val background = bg {
                    val repositories: List<Repo>
                    lastQuery = textToSearch

                    if (lastQuery!!.length <= MIN_LENGHT) {
                        repositories = response?.repoList!!
//                        return@bg response?.citizenList!!
                    } else {
                        repositories = filterByRepoName(lastQuery!!)
//                        return@bg filterByCitizenName(lastQuery!!)
                    }

                    return@bg repositories //just to know how to implicit return the value
                }

                ref().let {
                    it.filteredList = background.await()
                    it.mvpView?.onSearchResult(it.filteredList)
                    it.setCurrentState(ViewState.FINISH)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ref().mvpView?.onError(e)
            }
        }
    }

    private fun filterByRepoName(name: String): List<Repo> {
        val filteredList = ArrayList<Repo>(0)

        response?.repoList?.forEach { repository ->
            if (repository.name.toLowerCase().contains(name.toLowerCase())) {
                filteredList.add(repository)
            }
        }
        return filteredList
    }

}


