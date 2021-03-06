package org.mifos.selfserviceapp.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.Group;
import org.mifos.selfserviceapp.presenters.UserDetailsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.UserDetailsView;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 10/7/17.
 */

public class UserProfileFragment extends BaseFragment implements UserDetailsView {


    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_activation_date)
    TextView tvActivationDate;

    @BindView(R.id.tv_office_name)
    TextView tvOfficeName;

    @BindView(R.id.tv_groups)
    TextView tvGroups;

    @BindView(R.id.tv_client_type)
    TextView tvCLientType;

    @BindView(R.id.tv_client_classification)
    TextView tvClientClassification;

    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.tv_dob)
    TextView tvDOB;

    @BindView(R.id.tv_gender)
    TextView tvGender;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @Inject
    UserDetailsPresenter presenter;

    private View rootView;

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(),
                R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(),
                R.color.white));

        presenter.getUserDetails();
        presenter.getUserImage();

        return rootView;
    }

    @Override
    public void showUserDetails(Client client) {
        collapsingToolbarLayout.setTitle(client.getDisplayName());
        tvAccountNumber.setText(client.getAccountNo());
        tvActivationDate.setText(DateHelper.getDateAsString(client.getActivationDate()));
        tvOfficeName.setText(client.getOfficeName());
        tvCLientType.setText(client.getClientType().getName());
        tvGroups.setText(getGroups(client.getGroups()));
        tvClientClassification.setText(client.getClientClassification().getName());
        tvPhoneNumber.setText(client.getMobileNo());
        tvDOB.setText(DateHelper.getDateAsString(client.getDobDate()));
        tvGender.setText(client.getGender().getName());
    }

    private String getGroups(List<Group> groups) {
        StringBuilder builder = new StringBuilder();
        for (Group group : groups) {
            builder.append(getString(R.string.string_and_string, group.getName(), " | "));
        }
        return builder.toString().substring(0, builder.toString().length() - 2);
    }

    @Override
    public void showUserImage(final Bitmap bitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivProfile.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
        appBarLayout.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }
}
