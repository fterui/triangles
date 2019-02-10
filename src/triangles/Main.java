package triangles;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static final double WIDTH = 500;
    private static final double HEIGHT = 500;
    private static final int MAGNITUDE = 10;
    
    private static final double CENTER_X = WIDTH/2;
    private static final double CENTER_Y = HEIGHT/2;
    
    private double x;
    private double y;
    
    private ScheduledExecutorService threads;
    
    @FXML
    private Canvas canvas;
    
    @FXML
    private Button draw;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/triangles/main.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void initialize() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.strokeLine(CENTER_X, 0, CENTER_X, HEIGHT);
        gc.strokeLine(0, CENTER_Y, WIDTH, CENTER_Y);
        
        gc.setStroke(Color.BLACK);
        this.x = 0;
        this.y = 1;
        drawTriangle();
    }

    @FXML
    public void draw(ActionEvent ev) {
        if (this.threads == null) {
            // start
            this.threads = Executors.newScheduledThreadPool(1, (r) -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
            this.threads.scheduleAtFixedRate(() -> drawTriangle(), 0, 1, TimeUnit.SECONDS);
            this.draw.setText("Stop");
        } else {
            this.threads.shutdown();
            this.threads = null;
            this.draw.setText("Draw");
        }
    }
    
    private void drawTriangle() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.strokeLine(CENTER_X, CENTER_Y, CENTER_X+this.x*MAGNITUDE, CENTER_Y+this.y*MAGNITUDE);
        double angle = Math.atan2(this.y, this.x);
        angle -= Math.PI/2;
        double nx = this.x + Math.cos(angle);
        double ny = this.y + Math.sin(angle);
        gc.strokeLine(CENTER_X+this.x*MAGNITUDE, CENTER_Y+this.y*MAGNITUDE, CENTER_X+nx*MAGNITUDE, CENTER_Y+ny*MAGNITUDE);
        gc.strokeLine(CENTER_X, CENTER_Y, CENTER_X+nx*MAGNITUDE, CENTER_Y+ny*MAGNITUDE);
        this.x = nx;
        this.y = ny;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
