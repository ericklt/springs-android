package com.example.ericklima.springs;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    Float carWidth;
    Integer num = 4000;
    Double deltaT = 0.1;
    Button mStartAnimationsBtn;
    ImageView mCarF1, mCarF2, mCarB1, mCarB2;
    ImageView mCarM1, mCarM2, mCarRK1, mCarRK2;
    float aux, t;
    ImageView mSpringF1, mSpringF2, mSpringB1, mSpringB2;
    ImageView mSpringM1, mSpringM2, mSpringRK1, mSpringRK2;
    Boolean isAnimating;
    State initialState;
    ValueAnimator mValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartAnimationsBtn = (Button) findViewById(R.id.start_animation);
        mCarF1 = (ImageView) findViewById(R.id.car_f_1);
        mCarF2 = (ImageView) findViewById(R.id.car_f_2);
        mCarB1 = (ImageView) findViewById(R.id.car_b_1);
        mCarB2 = (ImageView) findViewById(R.id.car_b_2);
        mCarM1 = (ImageView) findViewById(R.id.car_m_1);
        mCarM2 = (ImageView) findViewById(R.id.car_m_2);
        mCarRK1 = (ImageView) findViewById(R.id.car_rk_1);
        mCarRK2 = (ImageView) findViewById(R.id.car_rk_2);
        mSpringF1 = (ImageView) findViewById(R.id.spring_f_1);
        mSpringF2 = (ImageView) findViewById(R.id.spring_f_2);
        mSpringB1 = (ImageView) findViewById(R.id.spring_b_1);
        mSpringB2 = (ImageView) findViewById(R.id.spring_b_2);
        mSpringM1 = (ImageView) findViewById(R.id.spring_m_1);
        mSpringM2 = (ImageView) findViewById(R.id.spring_m_2);
        mSpringRK1 = (ImageView) findViewById(R.id.spring_rk_1);
        mSpringRK2 = (ImageView) findViewById(R.id.spring_rk_2);

        List<Double> initial = new ArrayList<>();
        initial.add(6.);
        initial.add(18.);
        initial.add(0.);
        initial.add(0.);
        initialState = new State(initial);

        mCarF1.setOnTouchListener(this); mCarF2.setOnTouchListener(this);
        mCarB1.setOnTouchListener(this); mCarB2.setOnTouchListener(this);
        mCarM1.setOnTouchListener(this); mCarM2.setOnTouchListener(this);
        mCarRK1.setOnTouchListener(this); mCarRK2.setOnTouchListener(this);

        Resources r = getResources();
        carWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());

        resetPositions();

        mValueAnimator = ValueAnimator.ofInt(0, num-1);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(40000);
    }

    public void onClick(View v) {
        if (isAnimating) {
            mStartAnimationsBtn.setText("Start Animation");
            mValueAnimator.cancel();
            resetPositions();
        }
        else {
            mStartAnimationsBtn.setEnabled(false);
            new StartProcessing().execute((Void)null);
        }
    }

    public void setUpSprings() {
        mSpringF1.requestLayout();mSpringF2.requestLayout();
        mSpringB1.requestLayout();mSpringB2.requestLayout();
        mSpringM1.requestLayout();mSpringM2.requestLayout();
        mSpringRK1.requestLayout();mSpringRK2.requestLayout();

        mSpringF1.setTranslationX(Math.min(0, mCarF1.getTranslationX()));
        mSpringF1.getLayoutParams().width = ((int) Math.abs(mCarF1.getTranslationX()));
        mSpringF2.setTranslationX(Math.min(mCarF1.getTranslationX() + carWidth, mCarF2.getTranslationX()));
        mSpringF2.getLayoutParams().width = ((int) (Math.abs(mCarF2.getTranslationX() - mCarF1.getTranslationX() - carWidth)));

        mSpringB1.setTranslationX(Math.min(0, mCarB1.getTranslationX()));
        mSpringB1.getLayoutParams().width = ((int) Math.abs(mCarB1.getTranslationX()));
        mSpringB2.setTranslationX(Math.min(mCarB1.getTranslationX() + carWidth, mCarB2.getTranslationX()));
        mSpringB2.getLayoutParams().width = ((int) (Math.abs(mCarB2.getTranslationX() - mCarB1.getTranslationX() - carWidth)));

        mSpringM1.setTranslationX(Math.min(0, mCarM1.getTranslationX()));
        mSpringM1.getLayoutParams().width = ((int) Math.abs(mCarM1.getTranslationX()));
        mSpringM2.setTranslationX(Math.min(mCarM1.getTranslationX() + carWidth, mCarM2.getTranslationX()));
        mSpringM2.getLayoutParams().width = ((int) (Math.abs(mCarM2.getTranslationX() - mCarM1.getTranslationX() - carWidth)));

        mSpringRK1.setTranslationX(Math.min(0, mCarRK1.getTranslationX()));
        mSpringRK1.getLayoutParams().width = ((int) Math.abs(mCarRK1.getTranslationX()));
        mSpringRK2.setTranslationX(Math.min(mCarRK1.getTranslationX() + carWidth, mCarRK2.getTranslationX()));
        mSpringRK2.getLayoutParams().width = ((int) (Math.abs(mCarRK2.getTranslationX() - mCarRK1.getTranslationX() - carWidth)));
    }

    public void resetPositions() {
        isAnimating = false;
        mCarF1.setTranslationX(initialState.getState(0).floatValue()*50);
        mCarF2.setTranslationX(initialState.getState(1).floatValue()*50);
        mCarB1.setTranslationX(initialState.getState(0).floatValue()*50);
        mCarB2.setTranslationX(initialState.getState(1).floatValue()*50);
        mCarM1.setTranslationX(initialState.getState(0).floatValue()*50);
        mCarM2.setTranslationX(initialState.getState(1).floatValue()*50);
        mCarRK1.setTranslationX(initialState.getState(0).floatValue()*50);
        mCarRK2.setTranslationX(initialState.getState(1).floatValue()*50);
        setUpSprings();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!isAnimating && (view == mCarF1 || view == mCarF2 || view == mCarB1 || view == mCarB2
                            || view == mCarM1 || view == mCarM2 || view == mCarRK1 || view == mCarRK2)) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    t = view.getTranslationX();
                    aux = motionEvent.getRawX();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = motionEvent.getRawX() - aux;
                    if (view == mCarF1 || view == mCarB1 || view == mCarM1 || view == mCarRK1) {
                        mCarF1.setTranslationX(t+deltaX);
                        mCarB1.setTranslationX(t+deltaX);
                        mCarM1.setTranslationX(t+deltaX);
                        mCarRK1.setTranslationX(t+deltaX);
                        initialState.changeState(0, view.getTranslationX() / 50.);
                    }
                    else {
                        mCarF2.setTranslationX(t+deltaX);
                        mCarB2.setTranslationX(t+deltaX);
                        mCarM2.setTranslationX(t+deltaX);
                        mCarRK2.setTranslationX(t+deltaX);
                        initialState.changeState(1, view.getTranslationX() / 50.);
                    }
                    setUpSprings();
                    return true;
            }
        }
        return false;
    }

    private class StartProcessing extends AsyncTask<Void, Void, Void> {

        private List<State> forward, backward, modified, rungeKutta;

        @Override
        protected Void doInBackground(Void... voids) {
            forward = InitialValueProblem.solveIVP(initialState, deltaT, num);
            backward = InitialValueProblem.solveBackwardIVP(initialState, deltaT, num, 0.1);
            modified = InitialValueProblem.solveModifiedIVP(initialState, deltaT, num);
            rungeKutta = InitialValueProblem.solveRungeKuttaIVP(initialState, deltaT, num);
            return null;
        }

        @Override
        protected void onPostExecute(Void lists) {
            mStartAnimationsBtn.setEnabled(true);
            mStartAnimationsBtn.setText("Cancel Animation");
            isAnimating = true;

            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer animation = (Integer) valueAnimator.getAnimatedValue();
                    mCarF1.setTranslationX(forward.get(animation).getState(0).floatValue() * 50);
                    mCarF2.setTranslationX(forward.get(animation).getState(1).floatValue() * 50);
                    mCarB1.setTranslationX(backward.get(animation).getState(0).floatValue() * 50);
                    mCarB2.setTranslationX(backward.get(animation).getState(1).floatValue() * 50);
                    mCarM1.setTranslationX(modified.get(animation).getState(0).floatValue() * 50);
                    mCarM2.setTranslationX(modified.get(animation).getState(1).floatValue() * 50);
                    mCarRK1.setTranslationX(rungeKutta.get(animation).getState(0).floatValue() * 50);
                    mCarRK2.setTranslationX(rungeKutta.get(animation).getState(1).floatValue() * 50);
                    setUpSprings();
                }
            });

            mValueAnimator.start();
        }
    }
}
