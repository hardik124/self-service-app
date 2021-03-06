package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithdraw;
import org.mifos.selfserviceapp.presenters.LoanAccountWithdrawPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanAccountWithdrawView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 7/6/17.
 */

public class LoanAccountWithdrawFragment extends BaseFragment implements LoanAccountWithdrawView {

    @BindView(R.id.tv_client_name)
    TextView tvClientName;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.et_withdraw_reason)
    EditText etWithdrawReason;

    @Inject
    LoanAccountWithdrawPresenter loanAccountWithdrawPresenter;

    View rootView;
    private LoanAccount loanAccount;


    public static LoanAccountWithdrawFragment newInstance(LoanAccount loanAccount) {
        LoanAccountWithdrawFragment fragment = new LoanAccountWithdrawFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.LOAN_ACCOUNT, loanAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanAccount = getArguments().getParcelable(Constants.LOAN_ACCOUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_withdraw, container, false);
        setToolbarTitle(getString(R.string.withdraw_loan));

        ButterKnife.bind(this, rootView);
        showUserInterface();

        loanAccountWithdrawPresenter.attachView(this);

        return rootView;
    }

    private void showUserInterface() {
        tvClientName.setText(loanAccount.getClientName());
        tvAccountNumber.setText(loanAccount.getAccountNo());
    }

    @OnClick(R.id.btn_withdraw_loan)
    public void onLoanWithdraw() {
        LoanWithdraw loanWithdraw = new LoanWithdraw();
        loanWithdraw.setNote(etWithdrawReason.getText().toString());
        loanWithdraw.setWithdrawnOnDate(DateHelper
                .getDateAsStringFromLong(System.currentTimeMillis()));
        loanAccountWithdrawPresenter.withdrawLoanAccount(loanAccount.getId(), loanWithdraw);
    }

    @Override
    public void showLoanAccountWithdrawSuccess() {
        Toaster.show(rootView, R.string.loan_application_withdrawn_successfully);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showLoanAccountWithdrawError(String message) {
        Toaster.show(rootView, message);
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
