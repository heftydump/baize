package tomsoft.baize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button bMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
    }

    void initButtons() {

        bMatch = findViewById(R.id.b_match);
        bMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMatch();
            }
        });
    }

    void startMatch() {
        Intent i = new Intent(this, MatchActivity.class);
        startActivity(i);
    }
}
