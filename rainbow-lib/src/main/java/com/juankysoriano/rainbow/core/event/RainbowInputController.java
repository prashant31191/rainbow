package com.juankysoriano.rainbow.core.event;

import android.os.AsyncTask;
import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.listeners.RainbowInteractionListener;

public class RainbowInputController {
    private final EventDispatcher eventDispatcher;
    private RainbowInteractionListener rainbowInteractionListener;
    private float x, y;
    private float px, py;
    private boolean screenTouched;
    private boolean handlingEvent;

    public RainbowInputController() {
        eventDispatcher = new EventDispatcher();
        x = y = px = py = -1;
    }

    public synchronized void postEvent(final RainbowEvent motionEvent, final RainbowDrawer rainbowDrawer, final boolean looping) {
        if (!handlingEvent) {
            eventDispatcher.setEvent(motionEvent);
            if (!looping) {
                dequeueEvents(rainbowDrawer);
            }
        }
    }

    public synchronized void dequeueEvents(final RainbowDrawer rainbowDrawer) {
        if (eventDispatcher.hasEvent()) {
            final RainbowEvent motionEvent = eventDispatcher.getEvent();
            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    preHandleEvent(motionEvent);
                }

                @Override
                protected Void doInBackground(Void[] params) {
                    handleSketchEvent(motionEvent, rainbowDrawer);
                    return null;
                }

                @Override
                protected void onPostExecute(Void param) {
                    postHandleEvent(motionEvent);
                }
            };
            asyncTask.execute();
        }
    }

    private void handleSketchEvent(final RainbowEvent event, final RainbowDrawer rainbowDrawer) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                screenTouched = true;
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onSketchTouched(event, rainbowDrawer);
                }
                break;
            case MotionEvent.ACTION_UP:
                screenTouched = false;
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onSketchReleased(event, rainbowDrawer);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                screenTouched = true;
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onFingerDragged(event, rainbowDrawer);
                }
                break;
        }

        if (hasInteractionListener()) {
            rainbowInteractionListener.onMotionEvent(event, rainbowDrawer);
        }
    }

    private synchronized void preHandleEvent(RainbowEvent event) {
        handlingEvent = true;
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                || (event.getAction() == MotionEvent.ACTION_UP)
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            px = x;
            py = y;
            x = event.getX();
            y = event.getY();
        }
    }

    private synchronized void postHandleEvent(RainbowEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                || (event.getAction() == MotionEvent.ACTION_UP)
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            px = x;
            py = y;
        }
        handlingEvent = false;
    }

    /**
     * Used to set a RainbowInteractionListener which will listen for different interaction events
     * @param rainbowInteractionListener
     */
    public void setRainbowInteractionListener(RainbowInteractionListener rainbowInteractionListener) {
        this.rainbowInteractionListener = rainbowInteractionListener;
    }

    /**
     * Used to remove the attached RainbowInteractionListener
     */
    public void removeSketchInteractionListener() {
        this.rainbowInteractionListener = null;
    }

    private boolean hasInteractionListener() {
        return rainbowInteractionListener != null;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getPreviousX() {
        if (px == -1) {
            return x;
        } else {
            return px;
        }
    }

    public float getPreviousY() {
        if (py == -1) {
            return y;
        } else {
            return py;
        }
    }

    public boolean isScreenTouched() {
        return screenTouched;
    }
}