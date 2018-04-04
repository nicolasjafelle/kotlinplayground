package test.nicolasjafelle.kithub.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import test.nicolasjafelle.kithub.R

/**
 * Created by nicolas on 11/10/17.
 */
class LoadingView : FrameLayout {

    private var progressBar: ProgressBar

    private var linearLayout: LinearLayout

    private var textView: TextView

    private var retryButton: Button


    var mainContentView: ViewGroup? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        View.inflate(context, R.layout.loading_view, this)

        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams = params

        isClickable = true

        progressBar = findViewById(R.id.loading_view_progress_bar)
        linearLayout = findViewById(R.id.loading_view_linear_layout)
        textView = findViewById(R.id.loading_view_text)
        retryButton = findViewById(R.id.loading_view_retry_button)

        progressBar.indeterminateDrawable.setColorFilter(fetchThemeColor(), android.graphics.PorterDuff.Mode.SRC_ATOP)
        setBackgroundResource(android.R.color.transparent)

    }


    private fun fetchThemeColor(): Int {
        val typedValue = TypedValue()

        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        return color
    }

    private fun fetchThemeBackgroundColor(): Int {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(android.R.attr.windowBackground))
        val color = a.getColor(0, 0)

        a.recycle()
        return color
    }

    fun setThemeBackgroundColor() {
        setBackgroundColor(fetchThemeBackgroundColor())
    }


    /**
     * Computes all the width and height of the component.
     *
     * @param rootView        - the ViewGroup where this component will be added
     * @param mainContentView - the mainContent to display when the {@link LoadingView#dismiss()} is called.
     * @param fullHeight      - Compute Full Screen Height or just the rootView's height
     */
    fun attach(rootView: ViewGroup,
               mainContentView: ViewGroup? = null,
               show: Boolean = true,
               fullHeight: Boolean = true,
               onRetryAction: () -> Unit) {

        if (mainContentView != null) {
            this.mainContentView = mainContentView
        }

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        if (rootView is RelativeLayout || rootView is FrameLayout) {
            rootView.addView(this, layoutParams)

        } else if (rootView is LinearLayout) {
            rootView.addView(this, 0, layoutParams)
        }

        if (fullHeight) {
            computeFullHeight()
        }

        if (show) {
            show()
        } else {
            dismiss()
        }

        retryButton.setOnClickListener {
            show()
            onRetryAction()
        }
    }

    private fun computeFullHeight() {

        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                var statusBarHeight = 0
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    statusBarHeight = resources.getDimensionPixelSize(resourceId)
                }

                var actionBarHeight = 0
                val tv = TypedValue()
                if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
                }

                //Previously we need to compute the softButton Height now it seems is not necessary.
//                int softButtonHeight = 0;
//                resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//                if (resourceId > 0) {
//                    softButtonHeight = resources.getDimensionPixelSize(resourceId);
//                }

                layoutParams.height = screenHeight - statusBarHeight - actionBarHeight
                viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    fun show() {
        alpha = 1f
        visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        linearLayout.visibility = View.GONE
        mainContentView?.visibility = View.GONE
    }


    fun showErrorView(errorMessage: String) {
        textView.text = errorMessage
        retryButton.setText(R.string.retry)
        visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
        mainContentView?.visibility = View.GONE
    }

    fun showErrorView(@StringRes errorMessage: Int = R.string.connection_error_try_again) {
        showErrorView(resources.getString(errorMessage))
    }

    fun showNoContentView(@StringRes stringResId: Int = R.string.no_content) {
        visibility = View.VISIBLE
        textView.setText(stringResId)
        retryButton.setText(R.string.refresh)
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
        mainContentView?.visibility = View.GONE
    }

    fun showLabel(@StringRes stringResId: Int) {
        visibility = View.VISIBLE
        textView.setText(stringResId)
        retryButton.visibility = View.GONE
        progressBar.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
        mainContentView?.visibility = View.GONE
    }

    fun isShowing() = visibility == View.VISIBLE

    fun dismiss(animated: Boolean) {

        if (animated) {
            animate().alpha(0f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    visibility = View.GONE
                    mainContentView?.visibility = View.VISIBLE
                }
            })
        } else {
            visibility = View.GONE
        }
    }

    fun dismiss() {
        dismiss(mainContentView != null)
    }

}

