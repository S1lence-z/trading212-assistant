package utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/**
 * Utility class for creating a countdown timer and updating a label with the remaining time.
 */
public class CountDownTimer {
    private static final String LABEL_TEXT = "The list refreshes in: ";

    /**
     * Starts a countdown timer for the specified number of seconds.
     *
     * @param seconds     The number of seconds for the countdown.
     * @param labelToUse  The label to update with the remaining time.
     * @param onTimerEnd  A runnable that is executed when the timer ends.
     */
    public static void startTimer(int seconds, Label labelToUse, Runnable onTimerEnd) {
        Task<Void> timerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = seconds; i >= 0; i--) {
                    int timeRemaining = i;
                    String secondsLabel = timeRemaining == 1 ? "second" : "seconds";
                    Platform.runLater(() -> labelToUse.setText(LABEL_TEXT + timeRemaining + " " + secondsLabel));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> labelToUse.setText(""));
                onTimerEnd.run();
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> labelToUse.setText("Timer failed"));
            }
        };

        new Thread(timerTask).start();
    }
}
