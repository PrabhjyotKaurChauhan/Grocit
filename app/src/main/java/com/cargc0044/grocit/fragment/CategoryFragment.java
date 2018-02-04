package com.cargc0044.grocit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.activity.MainActivity;
import com.cargc0044.grocit.adapters.CategoryAdapter;
import com.cargc0044.grocit.model.Category;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.cargc0044.grocit.util.constants.CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.CATEGORY_IMAGE;
import static com.cargc0044.grocit.util.constants.CATEGORY_NAME;
import static com.cargc0044.grocit.util.constants.URL_GET_CATEGORY;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements InternetConnectionListener, ApiHandler.ApiHandlerListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int CATEGORY_ACTION = 1;
    private CategorySelectionCallbacks mCallbacks;
    private ArrayList<Category> categoryList;
    private ListView categoryListView;
    private InternetConnectionListener internetConnectionListener;

    public CategoryFragment() {

    }

    public static CategoryFragment newInstance(int sectionNumber) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        ((MainActivity) context).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        try {
            mCallbacks = (CategorySelectionCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CategorySelectionCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        categoryListView = (ListView) rootView.findViewById(R.id.categoryListView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (utilMethods.isConnectedToInternet(getActivity())) {
            initCategoryList();
        } else {
            internetConnectionListener = CategoryFragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), CATEGORY_ACTION);
        }

    }

    //! function for populate category list
    private void initCategoryList() {

        /**
         * json is populating from text file. To make api call use ApiHandler class
         */
        ApiHandler apiHandler = new ApiHandler(this, URL_GET_CATEGORY);
        apiHandler.doApiRequest(ApiHandler.REQUEST_GET);

         /* You will get the response in onSuccessResponse(String tag, String jsonString) method
         * if successful api call has done. Do the parsing as the following.
         */

    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CATEGORY_ACTION) {
            initCategoryList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CATEGORY_ACTION) {
            getActivity().finish();
        }
    }

    //! catch json response from here
    @Override
    public void onSuccessResponse(String tag, String jsonString) {
        //! do same parsing as done in initCategoryList()
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            categoryList = new ArrayList<Category>();

            for (int i = 0; i < jsonArray.length(); i++) {
                Category category = new Category();
                category.setId(jsonArray.getJSONObject(i).getString(CATEGORY_ID));
                category.setTitle(jsonArray.getJSONObject(i).getString(CATEGORY_NAME));

                if (!TextUtils.isEmpty(jsonArray.getJSONObject(i).getString(CATEGORY_IMAGE))) {
                    category.setImageUrl(jsonArray.getJSONObject(i).getString(CATEGORY_IMAGE));
                }
                categoryList.add(category);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    categoryListView.setAdapter(new CategoryAdapter(getActivity(), mCallbacks, categoryList));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //! detect response error here
    @Override
    public void onFailureResponse(String tag) {
    }

    //! callback interface listen by CategoryActivity to detect user click on category
    public static interface CategorySelectionCallbacks {
        public abstract void onCategorySelected(String catID, String title);
    }

}
