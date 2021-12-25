package org.firstinspires.ftc.teamcode.capability;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.teamcode.models.BarCodeObject;
import org.firstinspires.ftc.teamcode.models.NavTargetPosition;
import org.firstinspires.ftc.teamcode.enums.FREIGHT_OBJECT;
import org.firstinspires.ftc.teamcode.enums.NAVTARGET;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class VisionLayer extends BaseOpMode {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";

    private static final String[] LABELS = {
            FREIGHT_OBJECT.BALL.getLabel(),
            FREIGHT_OBJECT.CUBE.getLabel(),
            FREIGHT_OBJECT.DUCK.getLabel(),
            FREIGHT_OBJECT.MARKER.getLabel()
    };

    private static final String VUFORIA_KEY =
            "AYnwvy//////AAABmb7CbfH0f03Nrl90EkgFFxgQsKYg5wdzlq6MU2uXenx7dK7DLZ5swuvzjKpg6mFH5UzKbHF93+2abr54O4XGMTcis75+VPt/xxGL2uG/z++P9JswAn6dkdCGpSjCDGoIVwn/zBGvodhSu6zBLgDRntTnMFVcEDeylhnzRQsafWJz8JG+XXgQNNIqO6je6ED9HfwNVzpXq5axiVC60bR//chpWSOC3NDV7gdcpGfZo55Hie4dL6EkNLEXKEDKiCTaFQ0SMvXr+NgYoaBvCfoYwh4oUFnoV1ky3p5r4oLLPrHcxfBhLa18qyTz7ZXPEZUf+fJ1Q9cKWI54oNKT1WmEA0uqf5bTlntFjFzYrmlhK4QX";

    private VuforiaLocalizer vuforia;

    private VuforiaTrackables targetsFreightFrenzy;

    /** The queue into which all frames from the camera are placed as they become available.
     * Frames which are not processed by the OpMode are automatically discarded. */
    private EvictingBlockingQueue<Bitmap> frameQueue;


    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    protected void initialize(boolean auto) {
        super.initialize(auto);
        if (robot.webcamName == null) {
            return;
        }

        initializeVuforia();
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
            tfod.setZoom(2.5, 16.0 / 9.0);
        }

//        VuforiaTrackables targetsFreightFrenzy = getVuforiaTrackables();
        if (targetsFreightFrenzy != null) {
            targetsFreightFrenzy.activate();
        }

    }

    @Override
    protected void releaseResources() {
        if (tfod != null) {
            tfod.deactivate();
        }
        if (targetsFreightFrenzy != null) {
            targetsFreightFrenzy.deactivate();
        }
    }

    @NonNull
    protected VuforiaTrackables getVuforiaTrackables() {
        if (targetsFreightFrenzy == null) {
            // Load the trackable objects from the Assets file, and give them meaningful names
            targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
            targetsFreightFrenzy.get(0).setName(NAVTARGET.BlueStorage.getName());
            targetsFreightFrenzy.get(1).setName(NAVTARGET.BlueAllianceWall.getName());
            targetsFreightFrenzy.get(2).setName(NAVTARGET.RedStorage.getName());
            targetsFreightFrenzy.get(3).setName(NAVTARGET.RedAllianceWall.getName());
        }
        return targetsFreightFrenzy;
    }

    protected void initializeVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(robot.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;
        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        parameters.cameraName = robot.webcamName;
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
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

    protected NavTargetPosition identifyNavigationTarget() {
        if (targetsFreightFrenzy == null) {
            return null;
        }

        OpenGLMatrix targetPose = null;
        NavTargetPosition targetPos = null;
        for (VuforiaTrackable trackable : getVuforiaTrackables()) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                targetPose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getVuforiaCameraFromTarget();

                // if we have a target, process the "pose" to determine the position of the target relative to the robot.
                if (targetPose != null) {
                    String targetName = trackable.getName();
                    VectorF trans = targetPose.getTranslation();

                    // Extract the X & Y components of the offset of the target relative to the robot
                    double targetX = trans.get(0) / MM_PER_INCH; // Image X axis
                    double targetY = trans.get(2) / MM_PER_INCH; // Image Z axis

                    // target range is based on distance from robot position to origin (right triangle).
                    double targetRange = Math.hypot(targetX, targetY);

                    // target bearing is based on angle formed between the X axis and the target range line
                    double targetBearing = Math.toDegrees(Math.asin(targetX / targetRange));

                    targetPos = new NavTargetPosition(targetName, targetRange, targetBearing);

                    break;  // jump out of target tracking loop if we find a target.

                }
            }
        }

        return targetPos;
    }

    protected void findAllFreightObjectsInView() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                i++;
            }
            telemetry.update();
        }
    }


    /*
       Returns the coordinates info of the first object of that type identified.
       It does not check which is closer etc at this point.
       But it will be better to add eventually to improve performance.
     */
    protected Recognition checkIfObjectOfThisTypeInView(FREIGHT_OBJECT objectType) {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;

            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(objectType.getLabel())) {
                    return recognition;
                }
            }
        }
        return null;
    }

    protected int identifyDuckMarkerPosition() {
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());

            List<BarCodeObject> barcodeObjects = new ArrayList<>();

            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(FREIGHT_OBJECT.MARKER) || recognition.getLabel().equals(FREIGHT_OBJECT.DUCK)) {
                    boolean isDuck = recognition.getLabel().equals(FREIGHT_OBJECT.DUCK);
                    barcodeObjects.add(new BarCodeObject(recognition.getLeft(), recognition.getRight(), isDuck));
                }
            }
            Collections.sort(barcodeObjects);

            int pos = 1;
            for (BarCodeObject obj : barcodeObjects) {
                if (obj.isDuck()) {
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

    protected int identifyLameDuck() {
        tfod.setZoom(2, 1);
        int pos = -1;

        List<Recognition> updatedRecognitions = tfod.getRecognitions();

        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());

            List<BarCodeObject> barcodeObjects = new ArrayList<>();

            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equalsIgnoreCase(FREIGHT_OBJECT.DUCK.toString())) {
                    float left = recognition.getLeft();
                    float right = recognition.getRight();

                    telemetry.addLine("# Found Duck ");
                    telemetry.addData("left", left);
                    telemetry.addData("right", right);

                    if (left < 180) {
                        pos = 1;
                    } else if (left > 180 && left <= 300) {
                        pos = 2;
                    } else if (left > 300) {
                        pos = 3;
                    }
                    break;
                } else {
                    telemetry.addLine("Object Type : " + recognition.getLabel());
                }
            }
        } else {
            telemetry.addLine("Recognitions is null");
        }

        telemetry.addData("# Duck Marker Position", pos);
        telemetry.update();

        return pos;
    }


//    public int getPos() throws Exception {
//        Image img = getImage();
//        ByteBuffer pixels = img.getPixels();
//
//        File file1 = new File("images/image1.jpg");
//        File file2 = new File("images/image2.jpg");
//        File file3 = new File("images/image3.jpg");
//
//
//
//        return 3;
//    }
//
//    public Image getImage() throws Exception {
//        vuforia.enableConvertFrameToBitmap();
//        vuforia.setFrameQueueCapacity(1); //tells VuforiaLocalizer to only store one frame at a time
//
//        /*To access the image: you need to iterate through the images of the frame object:*/
//
//        VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take(); //takes the frame at the head of the queue
//        Image rgb = null;
//
//        long numImages = frame.getNumImages();
//
//        for (int i = 0; i < numImages; i++) {
//            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
//                rgb = frame.getImage(i);
//                break;
//            }//if
//        }//for
//
//        return rgb;
//    }

//    public int getBarCode() {
//
//        try {
//            File fileA = new File("images/image1.jpg");
//            File fileB = new File("/home / pratik /" + " Desktop / image2.jpg");
//
//            imgA = ImageIO.read(fileA);
//            imgB = ImageIO.read(fileB);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//
//        {
//            long difference = 0;
//            for (int y = 0; y < height1; y++) {
//                for (int x = 0; x < width1; x++) {
//                    int rgbA = imgA.getRGB(x, y);
//                    int rgbB = imgB.getRGB(x, y);
//                    int redA = (rgbA >> 16) & 0xff;
//                    int greenA = (rgbA >> 8) & 0xff;
//                    int blueA = (rgbA) & 0xff;
//                    int redB = (rgbB >> 16) & 0xff;
//                    int greenB = (rgbB >> 8) & 0xff;
//                    int blueB = (rgbB) & 0xff;
//                    difference += Math.abs(redA - redB);
//                    difference += Math.abs(greenA - greenB);
//                    difference += Math.abs(blueA - blueB);
//                }
//            }
//
//            // Total number of red pixels = width * height
//            // Total number of blue pixels = width * height
//            // Total number of green pixels = width * height
//            // So total number of pixels = width * height * 3
//            double total_pixels = width1 * height1 * 3;
//
//            // Normalizing the value of different pixels
//            // for accuracy(average pixels per color
//            // component)
//            double avg_different_pixels = difference / total_pixels;
//
//            // There are 255 values of pixels in total
//            double percentage = (avg_different_pixels /255) * 100;
//        }
//
//        return percentage;
//    }



}