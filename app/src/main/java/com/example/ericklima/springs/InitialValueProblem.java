package com.example.ericklima.springs;

import java.util.ArrayList;
import java.util.List;

public class InitialValueProblem {

    public static State getNextStateForward(State last, Double delta) {
        return State.sum(last, last.F().multiply(delta));
    }

    public static List<State> solveIVP(State initial, Double delta, int n) {
        List<State> output = new ArrayList<>();
        State s = new State(initial);

        for (int i=0; i<n; i++) {
            output.add(s);
            s = getNextStateForward(s, delta);
        }

        return output;
    }

    public static List<State> solveBackwardIVP(State initial, double delta, int n, double epsilon) {
        List<State> output = new ArrayList<>();
        State s = new State(initial);

        for (int i=0; i<n; i++) {
            output.add(s);
            s = State.sum(s, calculateNextState(s, delta, epsilon).F().multiply(delta));
        }

        return output;
    }

    public static State calculateNextState(State initial, double delta, double epsilon) {
        State nextS = new State(initial);
        State lastS;

        do {
            lastS = nextS;
            nextS = State.sum(initial, lastS.F().multiply(delta));
        } while (nextS.distanceTo(lastS)>epsilon);

        return nextS;
    }

    public static List<State> solveModifiedIVP(State initial, double h, int n) {
        List<State> output = new ArrayList<>();
        State s = new State(initial);

        for (int i=0; i<n; i++) {
            output.add(s);
            s = State.sum(s, s.F().multiply(h/2), getNextStateForward(s, h).F().multiply(h/2));
        }

        return output;
    }

    public static List<State> solveRungeKuttaIVP(State initial, double h, int n) {
        List<State> output = new ArrayList<>();
        State s = new State(initial);
        State k1, k2, k3, k4, k5, k6;

        for (int i=0; i<n; i++) {
            output.add(s);

            k1 = s.F();
            k2 = (State.sum(s, k1.multiply(h/4))).F();
            k3 = (State.sum(s, k1.multiply(h/8), k2.multiply(h/8))).F();
            k4 = (State.sum(s, k2.multiply(-h/2), k3.multiply(h))).F();
            k5 = (State.sum(s, k1.multiply(3*h/16), k4.multiply(9*h/16))).F();
            k6 = (State.sum(s, k1.multiply(-3*h/7), k2.multiply(3*h/7), k3.multiply(12*h/7), k4.multiply(-12*h/7), k5.multiply(8*h/7))).F();

            s = s.sum(State.sum(
                    k1.multiply(7.),
                    k3.multiply(32.),
                    k4.multiply(12.),
                    k5.multiply(32.),
                    k6.multiply(7.)
            ).multiply(h/90));
        }

        return output;
    }
}
