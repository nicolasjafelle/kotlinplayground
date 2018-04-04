package test.nicolasjafelle.kithub.mvp

/**
 * Created by nicolas on 11/9/17.
 */
interface RestHttpView : MvpView {

    fun onHostUnreachable()

    fun onHttpErrorCode(errorCode: Int, message: String?)
}