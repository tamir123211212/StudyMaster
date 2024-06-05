package com.example.project.Summaries;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.Summaries.ProfessionsActivity;

public class SummariesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summaries, container, false);

        ImageButton engImage = view.findViewById(R.id.engimg);
        ImageButton mathImage = view.findViewById(R.id.mathimg);
        ImageButton citizenImage = view.findViewById(R.id.citizenimg);

        engImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfessionsActivity.class);
                intent.putExtra("subject", "English");
                startActivity(intent);
            }
        });

        mathImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfessionsActivity.class);
                intent.putExtra("subject", "Math");
                startActivity(intent);
            }
        });

        citizenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfessionsActivity.class);
                intent.putExtra("subject", "Citizen");
                startActivity(intent);
            }
        });

        return view;
    }
}
