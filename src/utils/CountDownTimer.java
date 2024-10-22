package utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class CountDownTimer {
    private static final String LABEL_TEXT = "The list refreshes in: ";

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
