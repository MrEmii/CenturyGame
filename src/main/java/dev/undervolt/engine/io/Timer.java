package dev.undervolt.engine.io;

public class Timer {

    public double frame_cap;
    public double time;
    public double unprocessed;
    public double time_passed;
    public double passed;

    public double frame_time;
    public double frames;

    public static double getTime() {
        return (double) System.nanoTime() / 1000000000L;
    }

    public Timer() {
        frame_cap = 1.0 / 120.0;
        time = getTime();
        unprocessed = 0;

        frame_time = 0;
        frames = 0;
    }

    public void update() {
        time_passed = getTime();
        passed = time_passed - time;
        unprocessed += passed;
        frame_time += passed;

        time = time_passed;

        while (unprocessed >= frame_cap) {
            unprocessed -= frame_cap;
            frames++;
            if(frame_time > 1.0){
                frame_time = 0;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
}
