package com.example.alejandro.jmr_android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.example.alejandro.jmr_android.jmr.ImageViewJMR;
import com.example.alejandro.jmr_android.jmr.ResultList;
import com.example.alejandro.jmr_android.jmr.ResultMetadata;
import com.example.alejandro.jmr_android.jmr.SingleColorDescription;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class MainActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, btnGaleria;
    private ImageButton botonAñadirImagenConsulta, botonCalcular;
    private ImageView imageViewConsulta;
    private String userChoosenTask;
    private LinearLayout consultLayout;
    private LinearLayout imagenesConsultaScrollLayout;
    private HorizontalScrollView horizontalScrollView;
    private GridLayout gridLayoutResultado;
    private Galeria galeria;
    private Bitmap imagenConsulta;
    private Color colorImagenConsulta;
    private ArrayList<Resultado> resultados;
    private ResultList <ResultMetadata> resultMetadatas;
    private Semaphore semaforo;
    private Lock l;
    private TouchImageView tImg;
    private Animator mCurrentAnimator;
    private boolean pressed;
    private Animation zoomin;
    private Animation zoomout;
    private ImageViewJMR imageViewJMR;
    private ArrayList<ImageViewJMR> imagesViewJMR;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.

    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pressed = false;
        galeria = new Galeria(this);

        imagesViewJMR = new ArrayList<>();
        resultados = new ArrayList<>();

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);

        resultMetadatas = new ResultList<>();

        botonAñadirImagenConsulta = (ImageButton) findViewById
                (R.id.botonAñadirImagenConsulta);

        botonCalcular = (ImageButton) findViewById
                (R.id.botonCacular);

        botonCalcular.setVisibility(View.INVISIBLE);

        consultLayout = (LinearLayout) findViewById
                (R.id.ConsultLayout);

        horizontalScrollView = (HorizontalScrollView) findViewById
                (R.id.ScrollHorizontalConsulta);

        imagenesConsultaScrollLayout = (LinearLayout) findViewById
                (R.id.ImagenesConsultaScrollLayout);

        gridLayoutResultado = (GridLayout) findViewById
                (R.id.gridLayoutResultado);

        botonAñadirImagenConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirImagenConsulta();
            }
        });

        botonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagenConsulta != null){
                    calcularDescriptor();
                }
                else{
                    Log.e("Error:", "Imagen no cargada, capullo");
                }
                //añadirImagenConsulta();
            }
        });
    }

    public void añadirImagenConsulta(){

        // Obtenemos la imagen y la añadimos.
        Log.d("Estoy en: ", "aniadir imagen");

        obtenerImagen();

        botonCalcular.setVisibility(View.VISIBLE);


        // Recorremos la galeria y calculamos.
        // calcularDescriptor();

        // Colocamos las imagenes acorde a dicho calculo.
        //colocarImagenesResultado();

    }

    public void calcularDescriptor(){

        int tamanioGaleria = galeria.getTamanioGaleria();

        /* Cojo la imagen consulta y la meto en el array
         de resultados, para poder compararla con las de
         la galeria
        */

        double distancia = 0.00;

        ResultMetadata<Double, Bitmap> resultMetada =
                new ResultMetadata(0.0,imagenConsulta);

        SingleColorDescription descriptorImagenConsulta =
                new SingleColorDescription(imagenConsulta);

        resultMetadatas.add(resultMetada);

        Log.d("Estoy en: ", "principio calcular galeria");
        for(int i = 1; i < 11; i++){
            Log.d("Estoy en: ", "Cojo imagen galeria");
            Bitmap img = galeria.getImagen(i);

            Log.d("Estoy en: ", "Calculo descriptor");
            SingleColorDescription descriptor = new SingleColorDescription(img);

            Log.d("Estoy en: ", "Calculo distancia");

            distancia = SingleColorDescription.DefaultComparator
                    (descriptorImagenConsulta, descriptor);

            ResultMetadata<Double, Bitmap> resultMetadaGaleria
                    = new ResultMetadata(distancia, img);

            resultMetadatas.add(resultMetadaGaleria);
       }

        for(int i = 0; i < 11; i++){
            double aux = (Double)resultMetadatas.get(i).getResult();
            Log.d("Imagen " + i," " + Double.toString(aux));
        }

        normalize();

        for(int i = 0; i < 11; i++){
            double aux = (Double)resultMetadatas.get(i).getResult();
            Log.d("Imagen " + i," " + Double.toString(aux));        }

        colocarImagenesResultado();
    }

    public void colocarImagenesResultado(){

        for(int i = 1; i < 10; i++) {

            imageViewConsulta = new ImageView(this);

            imageViewJMR = new ImageViewJMR(this);

            final Bitmap imagenGaleria = (Bitmap)resultMetadatas.get(i).getMetadata();

            final String nombre = Double.toString((Double)resultMetadatas.get(i).getResult());

            imageViewJMR.setImageBitmap(imagenGaleria);

            imageViewJMR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLICK","HOLAAA");
                    if(!imageViewJMR.isPressed()) {
                        v.startAnimation(zoomin);
                        imageViewJMR.setPressed(!imageViewJMR.isPressed());
                        Log.d("CLICK","ZOOMIN");
                    } else {
                        v.startAnimation(zoomout);
                        imageViewJMR.setPressed(!imageViewJMR.isPressed());
                        Log.d("CLICK","ZOOMOUT");
                    }
                }
            });
            gridLayoutResultado.addView(imageViewJMR);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void obtenerImagen() {
        final CharSequence[] items = {
                "Obtener desde la cámara",
                "Obtener desde la galería",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Obtener imagen consulta");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Obtener desde la cámara")) {
                    userChoosenTask ="Obtener desde la cámara";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Obtener desde la galería")) {
                    userChoosenTask = "Obtener desde la galería";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap imagenConsulta = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagenConsulta.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addImagenScrollConsulta(imagenConsulta);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        imagenConsulta=null;
        if (data != null) {
            try {
                imagenConsulta = MediaStore.Images.Media.
                        getBitmap(getApplicationContext()
                                .getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImagenScrollConsulta(imagenConsulta);
    }

    public void addImagenScrollConsulta(Bitmap b){

        imageViewConsulta = new ImageView(this);

        RelativeLayout rl = new RelativeLayout(this);
        Bitmap bm = Bitmap.createScaledBitmap(b, 300,300, true);
        //  parms.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                444,
                444);
        parms.setMargins(20, 20, 20, 20);
        imageViewConsulta.setLayoutParams(parms);
        imageViewConsulta.getLayoutParams().height = 500;
        imageViewConsulta.getLayoutParams().width = 500;
        imageViewConsulta.setImageBitmap(bm);
        rl.addView(imageViewConsulta);
        imagenesConsultaScrollLayout.addView(rl);

    }

    public void normalize(){

        Collections.sort(resultMetadatas, new Comparator<ResultMetadata>(){
            public int compare(ResultMetadata r1, ResultMetadata r2) {
                return r1.compareTo(r2);
            }
        });

        double min = (Double)resultMetadatas.get(0).getResult();
        double max = (Double)resultMetadatas.getLast().getResult();

        for(int i = 1; i < 11; i++){
            double newResult;
            double xi = (Double)resultMetadatas.get(i).getResult();

            newResult  = (xi - min)/(max-min);

            resultMetadatas.get(i).setResult(newResult);

        }
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap image) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) thumbView;
       /* final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);*/

        expandedImageView.setImageBitmap(image);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.MainLayout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}