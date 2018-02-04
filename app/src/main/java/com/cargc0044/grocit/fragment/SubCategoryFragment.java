package com.cargc0044.grocit.fragment;

/**
 * Created by Nguza on 23/02/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.SubCategoryAdapter;
import com.cargc0044.grocit.model.Category;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_NAME;
import static com.cargc0044.grocit.util.constants.JF_DATA;
import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.KEY_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.URL_GET_SUB_CATEGORY;
import static com.cargc0044.grocit.util.constants.isHomeOpened;
import static com.cargc0044.grocit.util.constants.isResultListFragmentOpened;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;

public class SubCategoryFragment extends Fragment implements InternetConnectionListener, ApiHandler.ApiHandlerListener {

    public static String catId;
    private final int SUB_CATEGORY_ACTION = 1;
    private ListView subCategoryListView;
    private ArrayList<Category> subCategoryList;
    private SubCategorySelectionCallbacks mCallbacks;
    private InternetConnectionListener internetConnectionListener;

    public SubCategoryFragment() {

    }

    public static SubCategoryFragment newInstance(String id) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        catId = id;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (SubCategorySelectionCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SubCategorySelectionCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        subCategoryListView = (ListView) rootView.findViewById(R.id.subCategoryListView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isHomeOpened = false;
        isResultListFragmentOpened = false;
        if (utilMethods.isConnectedToInternet(getActivity()))
            initSubCategoryList();
        else {
            internetConnectionListener = SubCategoryFragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), SUB_CATEGORY_ACTION);
        }
    }

    private void initSubCategoryList() {

        /**
         * json is populating from text file. To make api call use ApiHandler class
         */
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, catId);
        ApiHandler apiHandler = new ApiHandler(this, URL_GET_SUB_CATEGORY, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);

         /* You will get the response in onSuccessResponse(String tag, String jsonString) method
         * if successful api call has done.
         */

        /*String jsonString = loadJSONFromAsset(getActivity(), "get_sub_category_list");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            subCategoryList = new ArrayList();
            JSONArray categoryArray = jsonObject.getJSONArray(JF_DATA);
            for (int i = 0; i < categoryArray.length(); i++) {
                CategoryActivity category = new CategoryActivity();
                category.setId(categoryArray.getJSONObject(i).getString(JF_ID));
                category.setTitle(categoryArray.getJSONObject(i).getString(JF_TITLE));
                subCategoryList.add(category);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    subCategoryListView.setAdapter(new SubCategoryAdapter(getActivity(), mCallbacks, subCategoryList));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == SUB_CATEGORY_ACTION) {
            initSubCategoryList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == SUB_CATEGORY_ACTION) {
            getActivity().finish();
        }
    }

    //! catch json response from here
    @Override
    public void onSuccessResponse(String tag, String jsonString) {
        //! do same parsing as done in initSubCategoryList()
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            subCategoryList = new ArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                Category category = new Category();
                category.setId(jsonArray.getJSONObject(i).getString(SUB_CATEGORY_ID));
                category.setTitle(jsonArray.getJSONObject(i).getString(SUB_CATEGORY_NAME));
                subCategoryList.add(category);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    subCategoryListView.setAdapter(new SubCategoryAdapter(getActivity(), mCallbacks, subCategoryList));
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

    //! callback interface listen by CategoryActivity to detect user click on sub-category
    public static interface SubCategorySelectionCallbacks {
        void onSubCategorySelected(String subCatID, String title);
    }

}
