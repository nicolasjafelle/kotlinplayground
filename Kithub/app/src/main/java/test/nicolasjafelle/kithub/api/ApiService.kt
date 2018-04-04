package test.nicolasjafelle.kithub.api

import test.nicolasjafelle.kithub.api.response.RepoListResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by nicolas on 11/9/17.
 */
interface ApiService {

    @GET(Endpoints.SEARCH_ANDROID)
    fun getRepoList(): Call<RepoListResponse>
}