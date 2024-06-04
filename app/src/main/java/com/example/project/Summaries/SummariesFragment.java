package com.example.project.Summaries;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class SummariesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summaries, container, false);

        // מציאת האלמנטים לפי ה-ID שלהם
        ImageButton engImage = view.findViewById(R.id.engimg);
        ImageButton mathImage = view.findViewById(R.id.mathimg);
        ImageButton citizenImage = view.findViewById(R.id.citizenimg);

        // הוספת מאזיני לחיצה לכל אלמנט עם פרמטרים שונים
        setOnClickListener(engImage, "English");
        setOnClickListener(mathImage, "Math");
        setOnClickListener(citizenImage, "Citizenship");

        return view;
    }

    private void setOnClickListener(ImageButton imageButton, final String subject) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfessionsActivity.class);
                intent.putExtra("subject", subject); // מעביר פרמטר שונה בהתאם ללחיצה
                startActivity(intent);
            }
        });
    }
}
