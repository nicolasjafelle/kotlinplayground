package test.nicolasjafelle.kithub.mvp

sealed class ViewState {

    object LOADING : ViewState()
    object ERROR: ViewState()
    object IDLE: ViewState()
    object FINISH: ViewState()

}