package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button r, g, b, x;
    DrawingCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = (Button)findViewById(R.id.red);
        g = (Button)findViewById(R.id.green);
        b = (Button)findViewById(R.id.blue);
        x = (Button)findViewById(R.id.clear);
        canvas = (DrawingCanvas)findViewById(R.id.drawingCanvas);
        View.OnClickListener checkColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setColor(((ColorDrawable)v.getBackground()).getColor());
            }
        };
        r.setOnClickListener(checkColor);
        g.setOnClickListener(checkColor);
        b.setOnClickListener(checkColor);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clearCanvas();
            }
        });
    }
}
