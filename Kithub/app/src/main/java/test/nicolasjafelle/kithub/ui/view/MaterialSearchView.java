package test.nicolasjafelle.kithub.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import test.nicolasjafelle.kithub.R;
import test.nicolasjafelle.kithub.utils.AnimationUtils;
import test.nicolasjafelle.kithub.utils.UIUtils;


/**
 * Created by nicolas on 5/5/16.
 */
public class MaterialSearchView extends FrameLayout implements Filter.FilterListener {
    public static final int REQUEST_VOICE = 9999;

    // Avoid using magic numbers.
    private static final int MAX_RESULTS = 1;
    public EditText searchSrcTextView;
    private MenuItem menuItem;
    private boolean isSearchOpen = false;
    private boolean clearingFocus;
    //Views
    private View searchLayout;
    private View tintView;
    private ListView suggestionsListView;
    private ImageButton backBtn;
    private ImageButton voiceBtn;
    private ImageButton emptyBtn;
    private RelativeLayout searchTopBar;

    private CharSequence oldQueryText;
    private CharSequence userQuery;

    private OnQueryTextListener onQueryChangeListener;
    private SearchViewListener searchViewListener;

    private ListAdapter adapter;
    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == backBtn) {
                closeSearch();
            } else if (v == voiceBtn) {
                onVoiceClicked();
            } else if (v == emptyBtn) {
                searchSrcTextView.setText(null);
            } else if (v == searchSrcTextView) {
                showSuggestions();
            } else if (v == tintView) {
                closeSearch();
            }
        }
    };
    private SavedState savedState;
    private boolean shouldAnimate;

    private int initialWidth, initialHeight;

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        shouldAnimate = true;
        initiateView();
    }

    private void initiateView() {
        inflate(getContext(), R.layout.material_search_view, this);
        searchLayout = findViewById(R.id.search_layout);

        searchTopBar = searchLayout.findViewById(R.id.search_top_bar);
        suggestionsListView = searchLayout.findViewById(R.id.suggestion_list);
        searchSrcTextView = searchLayout.findViewById(R.id.material_search_completion_view);
        backBtn = searchLayout.findViewById(R.id.action_up_btn);
        voiceBtn = searchLayout.findViewById(R.id.action_voice_btn);
        emptyBtn = searchLayout.findViewById(R.id.action_empty_btn);
        tintView = searchLayout.findViewById(R.id.transparent_view);

        searchSrcTextView.setOnClickListener(mOnClickListener);
        backBtn.setOnClickListener(mOnClickListener);
        voiceBtn.setOnClickListener(mOnClickListener);
        emptyBtn.setOnClickListener(mOnClickListener);
        tintView.setOnClickListener(mOnClickListener);

        showVoice(true);

        initSearchView();

        suggestionsListView.setVisibility(GONE);

        searchTopBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.elevation));


    }

    private void initSearchView() {
        searchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        searchSrcTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userQuery != null) {
                    userQuery = s;
                    MaterialSearchView.this.onTextChanged(s);
                } else {
                    userQuery = "";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    UIUtils.showSoftKeyboard(searchSrcTextView);
                    showSuggestions();
                }
            }
        });

    }

    public void toolbarPosition(int width, int height) {
        this.initialWidth = width;
        this.initialHeight = height;
    }


    private void startFilter(CharSequence s) {
        if (adapter != null && adapter instanceof Filterable) {
            ((Filterable) adapter).getFilter().filter(s, MaterialSearchView.this);
        }
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getContext().getString(R.string.search_for_repo));    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_RESULTS);    // quantity of results we want to receive
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = searchSrcTextView.getText();
        userQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            emptyBtn.setVisibility(VISIBLE);
            showVoice(false);
        } else {
            emptyBtn.setVisibility(GONE);
            showVoice(true);
        }

        if (onQueryChangeListener != null && oldQueryText != null && !TextUtils.equals(newText, oldQueryText)) {
            onQueryChangeListener.onQueryTextChange(newText.toString());
        }
        oldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        String keywords = searchSrcTextView.getText().toString();

        if (TextUtils.getTrimmedLength(keywords) > 0) {
            if (onQueryChangeListener == null || !onQueryChangeListener.onQueryTextSubmit(keywords)) {
                closeSearch();
                searchSrcTextView.setText(null);
            }
        }
    }

    private boolean isVoiceAvailable() {
        if (isInEditMode())
            return false;
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        } else {
            return true;
        }
    }


    //Public Attributes

//    @Override
//    public void setBackground(Drawable background) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            searchTopBar.setBackground(background);
//        } else {
//            searchTopBar.setBackgroundDrawable(background);
//        }
//    }

    @Override
    public void setBackgroundColor(int color) {
        searchTopBar.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        searchSrcTextView.setTextColor(color);
    }

    public void setHintTextColor(int color) {
        searchSrcTextView.setHintTextColor(color);
    }

    public void setHint(CharSequence hint) {
        searchSrcTextView.setHint(hint);
    }

    public void setVoiceIcon(Drawable drawable) {
        voiceBtn.setImageDrawable(drawable);
    }

    public void setCloseIcon(Drawable drawable) {
        emptyBtn.setImageDrawable(drawable);
    }

    public void setBackIcon(Drawable drawable) {
        backBtn.setImageDrawable(drawable);
    }

    public void setSuggestionBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            suggestionsListView.setBackground(background);
        } else {
            suggestionsListView.setBackgroundDrawable(background);
        }
    }

    public void setShouldAnimate(boolean animate) {
        shouldAnimate = animate;
    }

    //Public Methods

    /**
     * Call this method to show suggestions list. This shows up when adapter is set. Call {@link #setAdapter(ListAdapter)} before calling this.
     */
    public void showSuggestions() {
        if (adapter != null && adapter.getCount() > 0 && suggestionsListView.getVisibility() == GONE) {
            suggestionsListView.setVisibility(VISIBLE);
        }
    }

    /**
     * Set Suggest List OnItemClickListener
     *
     * @param listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        suggestionsListView.setOnItemClickListener(listener);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        searchSrcTextView.setOnEditorActionListener(listener);
    }

    /**
     * Set Adapter for suggestions list. Should implement Filterable.
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        suggestionsListView.setAdapter(adapter);
        startFilter(searchSrcTextView.getText());
    }

    /**
     * Dissmiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (suggestionsListView.getVisibility() == VISIBLE) {
            suggestionsListView.setVisibility(GONE);
        }
    }


    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */
    public void setQuery(CharSequence query, boolean submit) {
        searchSrcTextView.setText(query);
        if (query != null) {
            searchSrcTextView.setSelection(searchSrcTextView.length());
            userQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * if show is true, this will enable voice search. If voice is not available on the device, this method call has not effect.
     *
     * @param show
     */
    public void showVoice(boolean show) {
        if (show && isVoiceAvailable()) {
            voiceBtn.setVisibility(VISIBLE);
        } else {
            voiceBtn.setVisibility(GONE);
        }
    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }

    /**
     * Return true if search is open
     *
     * @return
     */
    public boolean isSearchOpen() {
        return isSearchOpen;
    }

    /**
     * Open Search View. This will animate the showing of the view.
     */
    public void showSearch() {
        if (shouldAnimate) {
            showSearch(true);
        } else {
            showSearch(false);
        }
    }


    /**
     * Open Search View. if animate is true, Animate the showing of the view.
     *
     * @param animate
     */
    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }

        //Request Focus
        searchSrcTextView.setText(null);
        searchSrcTextView.requestFocus();

        if (animate) {
            performInAnimation();

        } else {
            searchLayout.setVisibility(VISIBLE);
            if (searchViewListener != null) {
                searchViewListener.onSearchViewShown();
            }
        }
        isSearchOpen = true;
    }

    private void performInAnimation() {
        AnimationUtils.AnimationListener animationListener = new AnimationUtils.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (searchViewListener != null) {
                    searchViewListener.onSearchViewShown();
                }
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtils.revealInAnimation(searchLayout, this.initialWidth, this.initialHeight, AnimationUtils.ANIMATION_DURATION_MEDIUM, animationListener);
        } else {
            AnimationUtils.fadeInView(searchLayout, AnimationUtils.ANIMATION_DURATION_SHORT);
        }
    }

    private void performOutAnimation() {
        AnimationUtils.AnimationListener animationListener = new AnimationUtils.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtils.revealOutAnimation(searchLayout, AnimationUtils.ANIMATION_DURATION_MEDIUM, animationListener);
        } else {
            AnimationUtils.fadeOutView(searchLayout, AnimationUtils.ANIMATION_DURATION_SHORT);
        }
    }

    /**
     * Close search view.
     */
    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }

        oldQueryText = null;
        searchSrcTextView.getText().clear();
        dismissSuggestions();
        clearFocus();
        performOutAnimation();

        if (searchViewListener != null) {
            searchViewListener.onSearchViewClosed();
        }
        isSearchOpen = false;
    }

    /**
     * Set this listener to listen to Query Change events.
     *
     * @param listener
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        onQueryChangeListener = listener;
    }

    /**
     * Set this listener to listen to Search View open and close events
     *
     * @param listener
     */
    public void setOnSearchViewListener(SearchViewListener listener) {
        searchViewListener = listener;
    }


    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    /**
     * @hide
     */
    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (clearingFocus) return false;
        // Check if SearchView is focusable.
        if (!isFocusable()) return false;
        return searchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }

    /**
     * @hide
     */
    @Override
    public void clearFocus() {
        clearingFocus = true;
        UIUtils.hideSoftKeyboard(this);
        super.clearFocus();
        searchSrcTextView.clearFocus();
        clearingFocus = false;
    }


    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        savedState = new SavedState(superState);
        //end
        savedState.query = userQuery != null ? userQuery.toString() : null;
        savedState.isSearchOpen = this.isSearchOpen;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        savedState = (SavedState) state;

        if (savedState.isSearchOpen) {
            showSearch(false);
            setQuery(savedState.query, false);
        }

        super.onRestoreInstanceState(savedState.getSuperState());
    }

    public interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }


    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        String query;
        boolean isSearchOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }
    }

}
