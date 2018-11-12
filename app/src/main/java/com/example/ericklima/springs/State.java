package com.example.ericklima.springs;

import java.util.ArrayList;
import java.util.List;

public class State {
    List<Double> value;

    public State() {
        value = new ArrayList<>();
        value.add(0.);
        value.add(0.);
        value.add(0.);
        value.add(0.);
    }

    public State(State s) {
        this();
        for (int i = 0; i<4; i++) {
            value.set(i, s.getState(i));
        }
    }

    public State(List<Double> value)
    {
        this.value = value;
    }

    public void changeState(int pos, Double val) {
        value.set(pos, val);
    }

    public Double getState(int pos) {
        return value.get(pos);
    }

    public State F() {
        State output = new State(this);

        Double a1, a2;

        //Constant values:
        double c1 = 3; double c2 = 1;
        double k1 = 2; double k2 = 3;
        double M1 = 30; double M2 = 30;

        a1 = (1/M1)*(k2*(getState(1) - getState(0)) + c2*(getState(3) - getState(2)) - k1*getState(0) - c1*getState(3) - 24);
        a2 = (1/M2)*(-k2*(getState(1) - getState(0)) - c2*(getState(3) - getState(2)) + 32);

        output.changeState(0, getState(2));
        output.changeState(1, getState(3));
        output.changeState(2, a1);
        output.changeState(3, a2);

        return output;
    }

    public State sum(State s) {
        State output = new State();
        for (int i=0; i<4; i++) {
            output.changeState(i, getState(i) + s.getState(i));
        }

        return output;
    }

    public static State sum(State... states) {
        State output = new State();
        for (State s : states) {
            for (int i=0; i<4; i++) {
                output.changeState(i, output.getState(i) + s.getState(i));
            }
        }

        return output;
    }

    public State multiply(Double val) {
        State output = new State();

        for (int i=0; i<4; i++) {
            output.changeState(i, getState(i) * val);
        }

        return output;
    }

    public Double distanceTo(State s) {
        Double distance = 0.;
        for (int i = 0; i<4; i++) {
            distance = Math.max(distance, Math.abs(getState(i)-s.getState(i)));
        }
        return distance;
    }
}
