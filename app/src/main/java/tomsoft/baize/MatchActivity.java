package tomsoft.baize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity {

    TextView[] names;
    TextView[] scoreBoxes;
    TextView[] frameBox;
    TextView[] breakBoxLeft;
    TextView[] breakBoxRight;
    TextView[] statusBox;
    View[] indicators;
    Button[] btn;

    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = new Match("Mike David", "David Mike",19);
        match.newFrame();

        initUI();
        initButtons();
        draw();
    }

    // UI METHODS
    public void initUI() {
        names = new TextView[2];
        names[0] = findViewById(R.id.p1_name);
        names[1] = findViewById(R.id.p2_name);

        scoreBoxes = new TextView[2];
        scoreBoxes[0] = findViewById(R.id.p1_score);
        scoreBoxes[1] = findViewById(R.id.p2_score);

        breakBoxLeft = new TextView[8];
        breakBoxLeft[0] = findViewById(R.id.p1_break);
        breakBoxLeft[1] = findViewById(R.id.p1_red);
        breakBoxLeft[2] = findViewById(R.id.p1_yellow);
        breakBoxLeft[3] = findViewById(R.id.p1_green);
        breakBoxLeft[4] = findViewById(R.id.p1_brown);
        breakBoxLeft[5] = findViewById(R.id.p1_blue);
        breakBoxLeft[6] = findViewById(R.id.p1_pink);
        breakBoxLeft[7] = findViewById(R.id.p1_black);

        breakBoxRight = new TextView[8];
        breakBoxRight[0] = findViewById(R.id.p2_break);
        breakBoxRight[1] = findViewById(R.id.p2_red);
        breakBoxRight[2] = findViewById(R.id.p2_yellow);
        breakBoxRight[3] = findViewById(R.id.p2_green);
        breakBoxRight[4] = findViewById(R.id.p2_brown);
        breakBoxRight[5] = findViewById(R.id.p2_blue);
        breakBoxRight[6] = findViewById(R.id.p2_pink);
        breakBoxRight[7] = findViewById(R.id.p2_black);

        frameBox = new TextView[3];
        frameBox[0] = findViewById(R.id.p1_frames);
        frameBox[1] = findViewById(R.id.p2_frames);
        frameBox[2] = findViewById(R.id.best_of);

        indicators = new View[2];
        indicators[0] = findViewById(R.id.p1_indicator);
        indicators[1] = findViewById(R.id.p2_indicator);

        statusBox = new TextView[2];
        statusBox[0] = findViewById(R.id.difference);
        statusBox[1] = findViewById(R.id.remaining);
    }

    public void initButtons() {
        btn = new Button[8];
        btn[0] = findViewById(R.id.b_miss);
        btn[1] = findViewById(R.id.b_red);
        btn[2] = findViewById(R.id.b_yellow);
        btn[3] = findViewById(R.id.b_green);
        btn[4] = findViewById(R.id.b_brown);
        btn[5] = findViewById(R.id.b_blue);
        btn[6] = findViewById(R.id.b_pink);
        btn[7] = findViewById(R.id.b_black);

        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.miss();
                draw();
            }
        });
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.red));
                draw();
            }
        });
        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.yel));
                draw();
            }
        });
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.grn));
                draw();
            }
        });
        btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.brn));
                draw();
            }
        });
        btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.blu));
                draw();
            }
        });
        btn[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.pnk));
                draw();
            }
        });
        btn[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(match.blk));
                draw();
            }
        });
    }

    public void draw() {
        drawNames();
        drawPlayerBoxes();
        drawBreakBoxes();
        drawFrameBox();
        drawScores();
        drawButtons();
        drawStatus();
    }
    public void drawScores() {
        scoreBoxes[0].setText("" + match.player(0).getScore());
        scoreBoxes[1].setText("" + match.player(1).getScore());
    }
    public void drawNames() {
        names[0].setText(""+match.player(0).getName());
        names[1].setText(""+match.player(1).getName());
    }
    public void drawPlayerBoxes() {
        int inactive = getResources().getColor(R.color.scoreInactive);
        int active = getResources().getColor(R.color.scoreActive);
        int textActive = getResources().getColor(R.color.textActive);
        int textInactive = getResources().getColor(R.color.textInactive);
        scoreBoxes[0].setBackgroundColor( (match.atTable() == 0) ? active : inactive );
        scoreBoxes[1].setBackgroundColor( (match.atTable() == 1) ? active : inactive );
        indicators[0].setVisibility( (match.atTable() == 0 ) ? View.VISIBLE : View.INVISIBLE );
        indicators[1].setVisibility( (match.atTable() == 1) ? View.VISIBLE : View.INVISIBLE );
        frameBox[0].setTextColor( (match.atTable() == 0) ? textActive : textInactive );
        frameBox[1].setTextColor( (match.atTable() == 1) ? textActive : textInactive );
    }
    public void drawFrameBox() {
        frameBox[0].setText("" + match.player(0).getFrames());
        frameBox[1].setText("" + match.player(1).getFrames());
        int bOf = (match.goal()*2)-1;
        frameBox[2].setText("(" + bOf + ")");
    }
    public void drawBreakBoxes() {
        breakBoxLeft[0].setText("" + match.player(0).currentBreak().getBreak());
        breakBoxLeft[0].setVisibility( ( match.atTable() == 0 ) ? View.VISIBLE : View.INVISIBLE );
        breakBoxRight[0].setText("" + match.player(1).currentBreak().getBreak());
        breakBoxRight[0].setVisibility( ( match.atTable() == 1 ) ? View.VISIBLE : View.INVISIBLE );

        for( int i=1; i<8; i++ ) {
            breakBoxLeft[i].setText(""+match.player(0).currentBreak().getPottedQuantity(match.ball(i)));
            breakBoxLeft[i].setVisibility( (match.player(0).currentBreak().getPottedQuantity(match.ball(i)) > 0) ? View.VISIBLE : View.GONE );
            breakBoxRight[i].setText(""+match.player(1).currentBreak().getPottedQuantity(match.ball(i)));
            breakBoxRight[i].setVisibility( (match.player(1).currentBreak().getPottedQuantity(match.ball(i)) > 0) ? View.VISIBLE : View.GONE );
        }
    }
    public void drawButtons() {
        float active = 1f, inactive = .4f;

        btn[1].setText(""+match.ball(Match.red).getQuantity());

        switch(match.state()) {
            case RED :
                btn[1].setAlpha(active);
                for( int i=2; i<8; i++ ) btn[i].setAlpha(inactive);
                break;
            case COLOUR :
                btn[1].setAlpha(inactive);
                for( int i=2; i<8; i++ ) btn[i].setAlpha(active);
                break;
            case CLEARANCE:
                int lowest = match.lowestAvailableColour();
                if( lowest == -1 )
                    break;
                btn[1].setAlpha(inactive);
                for( int i=2; i<8; i++ )
                    btn[i].setAlpha( (i == lowest) ? active : inactive );
                break;
        }
    }
    public void drawStatus() {
        statusBox[0].setText(""+match.difference());
        statusBox[1].setText(""+match.remaining());
    }


}
