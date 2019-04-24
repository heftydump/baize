package tomsoft.baize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    Button bMatch;
    Button[] bFrames;
    TextView[] plyrName;
    TextView bestOf;

    int best = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();
    }

    void initMenu() {
        plyrName = new TextView[2];
        plyrName[0] = findViewById(R.id.p1_name_menu);
        plyrName[1] = findViewById(R.id.p2_name_menu);

        bestOf = findViewById(R.id.best_of);
        bestOf.setText(""+best);

        bFrames = new Button[2];
        bFrames[0] = findViewById(R.id.b_plus);
        bFrames[1] = findViewById(R.id.b_minus);
        bFrames[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBest(2);
            }
        });
        bFrames[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBest(-2);
            }
        });

        bMatch = findViewById(R.id.b_match);
        bMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMatch();
            }
        });
    }

    void changeBest(int offset) {
        best += offset;
        if( best < 1) best = 1;
        bestOf.setText(""+best);
    }

    void startMatch() {
        Intent i = new Intent(this, MatchActivity.class);
        i.putExtra("P1_NAME", ( !plyrName[0].getText().toString().isEmpty() ) ? plyrName[0].getText().toString() : "Player 1" );
        i.putExtra("P2_NAME", ( !plyrName[1].getText().toString().isEmpty() ) ? plyrName[1].getText().toString() : "Player 2" );
        i.putExtra("BEST_OF", best);
        startActivity(i);
    }
}
