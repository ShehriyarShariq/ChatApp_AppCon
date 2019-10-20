package com.appcon.appconchatapp.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ActivityPhoneVerificationBinding;
import com.appcon.appconchatapp.utils.LoaderDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

    private static final int CODE_LENGTH = 6;

    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String verificationId;

    private String phoneNum, token;

    private FirebaseAuth firebaseAuth;

    ActivityPhoneVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification);

        firebaseAuth = FirebaseAuth.getInstance();

        // UI
        numberInput();

        Intent loginActIntent = getIntent();
        phoneNum = loginActIntent.getStringExtra("phoneNum");

        sendVerificationCode(phoneNum);

        binding.resendCodeBtnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNum, resendToken);
            }
        });
    }

    private void numberInput() {
        // For input views
        final EditText[] codeDigInputs = {
                binding.firstDigitInp,
                binding.secondDigitInp,
                binding.thirdDigitInp,
                binding.fourthDigitInp,
                binding.fifthDigitInp,
                binding.sixthDigitInp
        };

        final HashMap<EditText, CardView> inpToCardMap = new HashMap<>();
        inpToCardMap.put(binding.firstDigitInp, binding.codeInpFirstLayout);
        inpToCardMap.put(binding.secondDigitInp, binding.codeInpSecondLayout);
        inpToCardMap.put(binding.thirdDigitInp, binding.codeInpThirdLayout);
        inpToCardMap.put(binding.fourthDigitInp, binding.codeInpFourthLayout);
        inpToCardMap.put(binding.fifthDigitInp, binding.codeInpFifthLayout);
        inpToCardMap.put(binding.sixthDigitInp, binding.codeInpSixthLayout);

        // Original color of input box
        final ColorStateList cardOriginalColor = binding.codeInpFirstLayout.getCardBackgroundColor();

        // Change color of selected input box
        for (final EditText digInp : codeDigInputs) {
            if (digInp.getOnFocusChangeListener() == null) {
                digInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            inpToCardMap.get(digInp).setCardBackgroundColor(Color.parseColor("#9C5AFF91"));
                        } else {
                            inpToCardMap.get(digInp).setCardBackgroundColor(cardOriginalColor);
                        }
                    }
                });
            }
        }

        // Automatically change focus of input box when filled
        for (int i = 0; i < codeDigInputs.length - 1; i++) {
            final int finalI = i;

            codeDigInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    codeDigInputs[finalI].clearFocus();
                    codeDigInputs[finalI + 1].requestFocus();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String code = "";
                    for (EditText digInp : codeDigInputs) {
                        if (!digInp.getText().toString().isEmpty()) {
                            code += digInp.getText().toString().trim();
                        }
                    }

                    if (code.length() == CODE_LENGTH) {
                        processCode(code);
                    }

                }
            });

        }

        // Last box
        codeDigInputs[codeDigInputs.length - 1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Remove all focus
                codeDigInputs[codeDigInputs.length - 1].clearFocus();
                binding.focusDeflectionView.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Verify entered code
                String code = "";
                for (EditText digInp : codeDigInputs) {
                    if (!digInp.getText().toString().isEmpty()) {
                        code += digInp.getText().toString().trim();
                    }
                }

                if (code.length() == CODE_LENGTH) {
                    processCode(code);
                }
            }
        });
    }

    private void sendVerificationCode(String phoneNum) {
        // Send OTP at provided phone number
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private void resendVerificationCode(String phoneNum, PhoneAuthProvider.ForceResendingToken resendToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback,
                resendToken);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String verifId, PhoneAuthProvider.ForceResendingToken token) {
            Toast.makeText(PhoneVerificationActivity.this, "Code Sent...", Toast.LENGTH_SHORT).show();

            verificationId = verifId;
            resendToken = token;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            binding.codeErrorLayout.setVisibility(View.GONE);
            verifyCode(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            binding.codeErrorLayout.setVisibility(View.VISIBLE);
        }
    };

    private boolean isCodeValid(String code) {
        char[] codeDigits = code.toCharArray();

        for (char digit : codeDigits) {
            if ((Integer.parseInt(String.valueOf(digit)) > 9) || (Integer.parseInt(String.valueOf(digit)) < 0)) {
                return false;
            }
        }

        return true;
    }

    private void processCode(String code) {
        boolean isValidCode = isCodeValid(code);

        if (isValidCode) {
            binding.codeErrorLayout.setVisibility(View.GONE);

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            verifyCode(credential);
        } else {
            binding.codeErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void verifyCode(PhoneAuthCredential credential) {
        final LoaderDialog processDialog = new LoaderDialog(PhoneVerificationActivity.this, "Process");
        processDialog.showDialog();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                processDialog.hideDialog();
                if (task.isSuccessful()) {
                    // Valid Code
                    numberVerificationSuccess();
                } else {
                    // Invalid Code
                    binding.codeErrorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void numberVerificationSuccess() {
        binding.codeErrorLayout.setVisibility(View.GONE);

        Intent returnSuccessIntent = new Intent();
        returnSuccessIntent.putExtra("phoneNum", phoneNum);

        setResult(Activity.RESULT_OK, returnSuccessIntent);
        finish();
    }
}
