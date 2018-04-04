package test.nicolasjafelle.kithub.mvp

/**
 * Created by nicolas on 11/9/17.
 */
interface Presenter<MvpView> {

    fun attachMvpView(mvpView: MvpView)

    fun detachMvpView()

}