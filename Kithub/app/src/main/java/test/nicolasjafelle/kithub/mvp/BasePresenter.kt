package test.nicolasjafelle.kithub.mvp

/**
 * Created by nicolas on 11/9/17.
 */
open class BasePresenter<T : MvpView> : Presenter<T> {

    var mvpView: T? = null

    protected lateinit var viewState: ViewState

    override fun attachMvpView(mvpView: T) {
        this.mvpView = mvpView
        viewState = ViewState.IDLE
    }

    override fun detachMvpView() {
        mvpView = null
    }


    fun isViewAttached() = mvpView != null


    fun setCurrentState(newState: ViewState) {
        viewState = newState
    }

    fun isLoading(): Boolean {
        return when (viewState) {
            is ViewState.IDLE -> false
            is ViewState.LOADING -> true
            is ViewState.ERROR -> false
            is ViewState.FINISH -> false
        }
    }


    fun isIdle(): Boolean {
        return when (viewState) {
            is ViewState.IDLE -> true
            is ViewState.LOADING -> false
            is ViewState.ERROR -> false
            is ViewState.FINISH -> false
        }
    }


    fun isFinished(): Boolean {
        return when (viewState) {
            is ViewState.IDLE -> false
            is ViewState.LOADING -> false
            is ViewState.ERROR -> false
            is ViewState.FINISH -> true
        }
    }


    fun hasFailed() : Boolean {
        return when (viewState) {
            is ViewState.IDLE -> false
            is ViewState.LOADING -> false
            is ViewState.ERROR -> true
            is ViewState.FINISH -> false
        }
    }


}