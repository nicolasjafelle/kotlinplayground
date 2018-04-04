package test.nicolasjafelle.kithub.repository

import test.nicolasjafelle.kithub.api.response.RepoListResponse
import retrofit2.Call


/**
 * Created by nicolas on 11/9/17.
 */
interface Repository {
    fun populationResponse(): Call<RepoListResponse>
}


class RepositoryImpl: Repository {

    override fun populationResponse() = ApiClient.instance.getRepoList()
}