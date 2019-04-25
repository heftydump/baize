package tomsoft.baize;
import tomsoft.baize.action.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MatchActivity extends AppCompatActivity {

    SlidingUpPanelLayout slidingMenu;

    TextView[] names;
    TextView[] scoreBoxes;
    TextView[] frameBox;
    TextView[] breakBoxLeft;
    TextView[] breakBoxRight;
    TextView[] statusBox;
    TextView[] statisticsBox;
    TextView rQuant;
    View[] indicators;
    Button[] btn;
    Button undo;
    Button[] menuBtn;

    Vibrator vib;
    int vibTime = 60;

    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = new Match( getIntent().getStringExtra("P1_NAME"),
                            getIntent().getStringExtra("P2_NAME"),
                            getIntent().getIntExtra("BEST_OF", 1));
        match.newFrame();

        initUI();
        initButtons();
        draw();
    }

    // UI METHODS
    public void initUI() {
        initMenu();
        initSlidingPanel();

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

        indicators = new View[3];
        indicators[0] = findViewById(R.id.p1_indicator);
        indicators[1] = findViewById(R.id.p2_indicator);
        indicators[2] = findViewById(R.id.remain_indicator);

        statusBox = new TextView[3];
        statusBox[0] = findViewById(R.id.difference);
        statusBox[1] = findViewById(R.id.remaining);
        statusBox[2] = findViewById(R.id.label_remaining);
    }

    public void initSlidingPanel() {
        initMenu();
        slidingMenu = findViewById(R.id.sliding_menu);
        slidingMenu.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                drawMenu();
                drawStatistics();
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
    }

    public void initMenu() {
        rQuant = findViewById(R.id.r_quant);
        menuBtn = new Button[5];
        menuBtn[0] = findViewById(R.id.b_rminus);
        menuBtn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( match.ball(1).getQuantity() < 1 || match.state() == Match.State.FREE_BALL) return;
                match.changeReds(-1);
                drawMenu();
                drawButtons();
                drawStatus();
            }
        });
        menuBtn[1] = findViewById(R.id.b_rplus);
        menuBtn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( match.state() == Match.State.FREE_BALL ) return;
                match.changeReds(1);
                drawMenu();
                drawButtons();
                drawStatus();
            }
        });
        menuBtn[3] = findViewById(R.id.b_freeball);
        menuBtn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !( match.lastAction() instanceof Foul) ) return;
                match.freeBall();
                slidingMenu.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                drawButtons();
                drawStatus();
            }
        });
        menuBtn[4] = findViewById(R.id.b_concede);
        menuBtn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                match.concede();
                                slidingMenu.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                draw();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getContext());
                builder.setMessage("Concede the frame?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        statisticsBox = new TextView[13];
        statisticsBox[0] = findViewById(R.id.p1_name_menu);
        statisticsBox[1] = findViewById(R.id.p2_name_main);
        statisticsBox[2] = findViewById(R.id.p1_score_menu);
        statisticsBox[3] = findViewById(R.id.p2_score_menu);
        statisticsBox[4] = findViewById(R.id.frames_menu);
        statisticsBox[5] = findViewById(R.id.p1_total);
        statisticsBox[6] = findViewById(R.id.p2_total);
        statisticsBox[7] = findViewById(R.id.p1_pots);
        statisticsBox[8] = findViewById(R.id.p2_pots);
        statisticsBox[9] = findViewById(R.id.p1_highest);
        statisticsBox[10] = findViewById(R.id.p2_highest);
        statisticsBox[11] = findViewById(R.id.p1_success);
        statisticsBox[12] = findViewById(R.id.p2_success);
    }

    public void initButtons() {
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btn = new Button[8];
        btn[0] = findViewById(R.id.b_miss);
        btn[1] = findViewById(R.id.b_red);
        btn[2] = findViewById(R.id.b_yellow);
        btn[3] = findViewById(R.id.b_green);
        btn[4] = findViewById(R.id.b_brown);
        btn[5] = findViewById(R.id.b_blue);
        btn[6] = findViewById(R.id.b_pink);
        btn[7] = findViewById(R.id.b_black);
        undo = findViewById(R.id.b_undo);

        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // miss button
                match.miss();
                draw();
            }
        });
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // red button (short press to pot)
                match.score(match.ball(Match.red));
                draw();
            }
        });
        btn[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    // red button (long press to foul)
                match.foul(match.ball(Match.red));
                draw();
                return true;
            }
        });
        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // yellow button (short press to pot)
                match.score(match.ball(Match.yel));
                draw();
            }
        });
        btn[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    // yellow button (long press to foul)
                match.foul(match.ball(Match.yel));
                draw();
                return true;
            }
        });
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       // you get the idea
                match.score(match.ball(Match.grn));
                draw();
            }
        });
        btn[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                match.foul(match.ball(Match.grn));
                draw();
                return true;
            }
        });
        btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(Match.brn));
                draw();
            }
        });
        btn[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                match.foul(match.ball(Match.brn));
                draw();
                return true;
            }
        });
        btn[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(Match.blu));
                draw();
            }
        });
        btn[5].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                match.foul(match.ball(Match.blu));
                draw();
                return true;
            }
        });
        btn[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(Match.pnk));
                draw();
            }
        });
        btn[6].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                match.foul(match.ball(Match.pnk));
                draw();
                return true;
            }
        });
        btn[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match.score(match.ball(Match.blk));
                draw();
            }
        });
        btn[7].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                match.foul(match.ball(Match.blk));
                draw();
                return true;
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(vibTime);
                match.undo();
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
                for( int i=2; i<8; i++ ) { btn[i].setAlpha(inactive); btn[i].setText(""); }
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
                for( int i=2; i<8; i++ ) {
                    btn[i].setAlpha((i == lowest) ? active : inactive);
                    btn[i].setText("");
                }
                break;
            case FREE_BALL:
                int on = ( match.ball(1).getQuantity() > 0 ) ? 1 : match.lowestAvailableColour();
                for( int i=1; i<8; i++ ) {
                    btn[i].setAlpha( (i == on) ? active : inactive );
                }
                btn[on].setText("F");
                break;
        }
    }
    public void drawStatus() {
        statusBox[0].setText(""+match.difference());
        statusBox[1].setText(""+match.remaining());
        String snookz = ( match.snookers() == 1 ) ? match.snookers() + " snooker" : match.snookers() + " snookers";
        statusBox[2].setText( (match.snookers() > 0) ? "Remaining (" + snookz + ")" : "Remaining");

        int enough = getResources().getColor(R.color.scoreInactive);
        int notEnough = getResources().getColor(R.color.remainingRed);
        statusBox[1].setBackgroundColor( ( match.difference() < match.remaining() ) ? enough : notEnough );
        indicators[2].setBackgroundColor( ( match.difference() < match.remaining() ) ? enough : notEnough );
    }
    public void drawMenu() {
        rQuant.setText(""+match.ball(Match.red).getQuantity());
    }
    public void drawStatistics() {
        int bOf = (match.goal()*2)-1;
        statisticsBox[0].setText(""+match.player(0).getName());
        statisticsBox[1].setText(""+match.player(1).getName());
        statisticsBox[2].setText(""+match.player(0).getScore());
        statisticsBox[3].setText(""+match.player(1).getScore());
        statisticsBox[4].setText(""+match.player(0).getFrames()+"   ("+bOf+")   "+match.player(1).getFrames());
        statisticsBox[5].setText(""+match.player(0).getTotalPoints());
        statisticsBox[6].setText(""+match.player(1).getTotalPoints());
        statisticsBox[7].setText(""+match.player(0).getPottedBalls());
        statisticsBox[8].setText(""+match.player(1).getPottedBalls());
        statisticsBox[9].setText(""+match.player(0).getHighest().getBreak());
        statisticsBox[10].setText(""+match.player(1).getHighest().getBreak());
        statisticsBox[11].setText(""+(int)(match.player(0).getPotSuccess()*100)+"%");
        statisticsBox[12].setText(""+(int)(match.player(1).getPotSuccess()*100)+"%");
    }
}