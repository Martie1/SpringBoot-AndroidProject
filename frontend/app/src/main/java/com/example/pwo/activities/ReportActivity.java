package com.example.pwo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pwo.R;
import com.example.pwo.network.models.ReportRequest;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.ApiService;
import com.example.pwo.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {


    private EditText reasonEditText;
    private Button reportButton;
    private ApiService apiService;
    private TokenManager tokenManager;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reasonEditText = findViewById(R.id.reasonEditText);
        reportButton = findViewById(R.id.reportButton);
        apiService = ApiClient.getInstance(getApplicationContext()).getApiService();

        tokenManager = new TokenManager(this);

        // pobieranie  idposta z intentu
        postId = getIntent().getIntExtra("postId", -1);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }

    private void submitReport() {
        String reason = reasonEditText.getText().toString().trim();
        if (reason.isEmpty()) {
            Toast.makeText(this, "Reason cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        tokenManager = new TokenManager(getApplicationContext());

        if (tokenManager == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ReportRequest reportRequest = new ReportRequest(reason);

        apiService.reportPost(postId, reportRequest, "Bearer " + tokenManager)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ReportActivity.this, "Report submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ReportActivity.this, "Failed to report post", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}