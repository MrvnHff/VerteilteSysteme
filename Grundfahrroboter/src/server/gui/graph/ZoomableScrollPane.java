package server.gui.graph;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

/**
 * Erweitertes ScrollPane welches man Zoomen und in beide Richtungen Scrollen kann
 * @author Mathias Wittling
 *
 */
public class ZoomableScrollPane extends ScrollPane {
    Group zoomGroup;
    Scale scaleTransform;
    Node content;
    double scaleValue = 1.0;
    double delta = 0.1;

    public ZoomableScrollPane(Node content) {
        this.content = content;
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        zoomGroup.setOnScroll(new ZoomHandler());
    }

    /**
     * Gibt den Zoomfactor zurück
     * @return
     */
    public double getScaleValue() {
        return scaleValue;
    }

    /**
     * Zommt auf den factor 1
     */
    public void zoomToActual() {
        zoomTo(1.0);
    }

    /**
     * Zoomt zu übergebenen factor
     * @param scaleValue
     */
    public void zoomTo(double scaleValue) {

        this.scaleValue = scaleValue;

        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);

    }

    /**
     * Zommt auf den factor 1
     */
    public void zoomActual() {
        scaleValue = 1;
        zoomTo(scaleValue);

    }

    /**
     * Zoomt um ein definiertes Delta raus
     */
    public void zoomOut() {
        scaleValue -= delta;

        if (Double.compare(scaleValue, 0.1) < 0) {
            scaleValue = 0.1;
        }

        zoomTo(scaleValue);
    }

    /**
     * Zoomt um ein definiertes Delta rein
     */
    public void zoomIn() {

        scaleValue += delta;

        if (Double.compare(scaleValue, 10) > 0) {
            scaleValue = 10;
        }

        zoomTo(scaleValue);

    }

    /**
     * Zoomt zu einem Faktor das der Inhalt komplett dargestellt werden kann
     * @param minimizeOnly Wenn der Inhalt schon in die view pass, wird nicht gezoomt falls der Parameter true ist.
     */
    public void zoomToFit(boolean minimizeOnly) {

        double scaleX = getViewportBounds().getWidth() / getContent().getBoundsInLocal().getWidth();
        double scaleY = getViewportBounds().getHeight() / getContent().getBoundsInLocal().getHeight();

        // Beachte Momentanen Zoomfaktor
        scaleX *= scaleValue;
        scaleY *= scaleValue;

        
        double scale = Math.min(scaleX, scaleY);

        // check vorbedingung
        if (minimizeOnly) {
        	
        	//Überprüfe ob zoomfaktor zu einer vergrößerung führen würde, setzte ihn zu 1
            if (Double.compare(scale, 1) > 0) {
                scale = 1;
            }
        }
        
        zoomTo(scale);
    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent scrollEvent) {
        	if (scrollEvent.getDeltaY() < 0) {
        		scaleValue -= delta;
            } else {
                scaleValue += delta;
            }
        	
        	zoomTo(scaleValue);

            scrollEvent.consume();
            
        }
    }
}
