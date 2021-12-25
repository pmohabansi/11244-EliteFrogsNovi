package org.firstinspires.ftc.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.enums.FREIGHT_OBJECT;
import org.firstinspires.ftc.teamcode.models.BarCodeObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TeleOp(name = "Vuforia Marker Detection", group = "Concept")
@Disabled
public class VuforiaMarkerDetection extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
    };

    private static final String VUFORIA_KEY =
            "AYnwvy//////AAABmb7CbfH0f03Nrl90EkgFFxgQsKYg5wdzlq6MU2uXenx7dK7DLZ5swuvzjKpg6mFH5UzKbHF93+2abr54O4XGMTcis75+VPt/xxGL2uG/z++P9JswAn6dkdCGpSjCDGoIVwn/zBGvodhSu6zBLgDRntTnMFVcEDeylhnzRQsafWJz8JG+XXgQNNIqO6je6ED9HfwNVzpXq5axiVC60bR//chpWSOC3NDV7gdcpGfZo55Hie4dL6EkNLEXKEDKiCTaFQ0SMvXr+NgYoaBvCfoYwh4oUFnoV1ky3p5r4oLLPrHcxfBhLa18qyTz7ZXPEZUf+fJ1Q9cKWI54oNKT1WmEA0uqf5bTlntFjFzYrmlhK4QX";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        while (opModeIsActive()) {
            if (tfod != null) {
                identifyDuckMarkerPosition();
            }
        }
    }

    protected int identifyDuckMarkerPosition() {
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());

            List<BarCodeObject> barcodeObjects = new ArrayList<>();

            for (Recognition recognition : updatedRecognitions) {
                telemetry.addLine("# Object Detected" + recognition.getLabel() + " " + recognition.getLeft() + " " + recognition.getRight());

                if(recognition.getLabel().equals(FREIGHT_OBJECT.MARKER.getLabel())) {
                    barcodeObjects.add(new BarCodeObject(recognition.getLeft(), recognition.getRight(), false));
                } else if(recognition.getLabel().equals(FREIGHT_OBJECT.DUCK.getLabel())) {
                    barcodeObjects.add(new BarCodeObject(recognition.getLeft(), recognition.getRight(), true));
                } else {
                    telemetry.addData("Some other object", "who");
                }
            }
            Collections.sort(barcodeObjects);

            int pos = 1;
            for (BarCodeObject obj : barcodeObjects) {
                if(obj.isDuck()) {
                    break;
                }
                pos++;
            }

            telemetry.addData("# Duck Marker Position", pos);
            telemetry.update();
            return pos;
        }
        return -1;
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minResultConfidence = 0.8f;
       tfodParameters.isModelTensorFlow2 = true;
       tfodParameters.inputSize = 320;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
