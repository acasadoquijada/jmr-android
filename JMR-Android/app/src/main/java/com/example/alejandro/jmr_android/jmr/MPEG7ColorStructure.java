package com.example.alejandro.jmr_android.jmr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.alejandro.jmr_android.HMMDImage;

/**
 * Created by alejandro on 06/09/2017.
 */

public class MPEG7ColorStructure {
    /**
     * The source media of this descriptor
     */
  //  protected transient BufferedImage source = null;
    protected transient Bitmap source = null;
    /**

     * Quantization Level of the HMMD ColorSpace
     */
    protected int qLevels = 256;
    /**
     * Histogram representing this descriptor
     */
    protected int[] histo = null;
    /**
     * Index for the 'hue' component in the HMMD color space
     */
    private static final int HUE = 0;
    /**
     * Index for the 'max' component in the HMMD color space
     */
    private static final int MAX = 1;
    /**
     * Index for the 'min' component in the HMMD color space
     */
    private static final int MIN = 2;
    /**
     * Index for the 'diff' component in the HMMD color space
     */
    private static final int DIFF = 3;
    /**
     * Quantization table for 256, 128, 64 and 32 quantization bins
     */
    private static final int[][][] QUANTIZATION_TABLE = {
            // Hue, Sum - subspace 0,1,2,3,4
            {{1, 8}, {4, 4}, {4, 4}, {4, 1}, {4, 1}}, // 32 levels
            {{1, 8}, {4, 4}, {4, 4}, {8, 2}, {8, 1}}, // 64 levels
            {{1, 16}, {4, 4}, {8, 4}, {8, 4}, {8, 4}}, // 128 level
            {{1, 32}, {4, 8}, {16, 4}, {16, 4}, {16, 4}}};  // 256 levels
    /**
     * Offset
     */
    private int offset = 0;
    /**
     * Default number of levels.
     */
    protected static final int DEFAULT_NUM_LEVELS = 256;

    /**
     * Constructs a new color structure descriptor and initializes it from the
     * image given by parameter
     *
     * @param image the source image
     * @param qLevels the number of levels associated to this descriptor
     */
    public MPEG7ColorStructure(Bitmap image, int qLevels) {
        this.source = image;
        this.init(image, qLevels);
    }

    public MPEG7ColorStructure(int[] h){
        this.histo = h;
        qLevels = DEFAULT_NUM_LEVELS;
    }

    public MPEG7ColorStructure(Bitmap image) {
        this(image, DEFAULT_NUM_LEVELS);
    }

    static {
        System.loadLibrary("descriptor");
    }

    public native int quantFuncC(double x);

    public int[] getHisto(){
        return histo;
    }

    private void setLevels(int qLevels) {
        if (qLevels <= 32) {
            this.qLevels = 32;
        } else if (qLevels <= 64) {
            this.qLevels = 64;
        } else if (qLevels <= 128) {
            this.qLevels = 128;
        } else {
            this.qLevels = 256;
        }
        this.offset = (int) log2(qLevels) - 5; // 2^5=32 => log2(32)-5 = 0
    }

    public void init(Bitmap image, int qLevels) {
        // The MPEG7ColorStructure need a JMRExtendedBufferedImage to be calculated

        /*
            Convertir la imagen a HMMD
         */
        HMMDImage hmmdImage = new HMMDImage(image);

        //hmmdImage.toString();
        this.setLevels(qLevels);
        byte[][] imQ = quantHMMDImage(hmmdImage);
        float[] histo = structuredHisto(imQ, hmmdImage.getWidth(), hmmdImage.getHeight());
        this.histo = reQuantization(histo);
    }

  /*  private native byte[][] quantHMMDImageC(
            float[][][] imSrc,
            float[][][] quantizationTable ,
            int offset,
            int height,
            int width,
            int hue,
            int max,
            int min,
            int diff);
*/
    private byte[][] quantHMMDImage(HMMDImage imSrc) {
        //Source image variable
        int wImg = imSrc.getWidth();
        int hImg = imSrc.getHeight();
        //Raster imRst = imSrc.getRaster();
        float[] pix = new float[4];
        //Destination image array
        byte[][] imDst = new byte[hImg][wImg];
        int subspace, hue_bin, sum_bin, v;
        int[] startSubSpacePos = getStartSubspacePos();
        for (int y = 0; y < hImg; y++) {
            for (int x = 0; x < wImg; x++) {
                pix = imSrc.getPixel(x,y);
                //imRst.getPixel(x, y, pix);
                //Define the subspace along the Diff axis
                subspace = getSubspace(pix[DIFF]);
                //Obtain the value of the hue in this quantization space
                hue_bin = (int) ((pix[HUE] / 361.0f) * QUANTIZATION_TABLE[offset][subspace][0]);
                //Obtain the value of the sum and multiply it by the hue value
                float tmp = ((pix[MIN] + pix[MAX]) / 2 - 1 / 255);
                sum_bin = (int) (tmp * QUANTIZATION_TABLE[offset][subspace][1]);
                if (sum_bin != 0) {
                    tmp = 0;
                    //Shift until the start position for this subspace in the histogram
                }
                v = startSubSpacePos[subspace] + sum_bin * QUANTIZATION_TABLE[offset][subspace][0] + hue_bin;
                //Check if value is not bigger than qLevels
                if (v >= qLevels) {
                    // Value computed is bigger than qLevels.
                    throw new RuntimeException("Error in  HMMD color space conversion");
                }
                //Set the value of the float
                imDst[y][x] = (byte) (v);
            }
        }
        return imDst;
    }


    public void init(Bitmap image) {
        init(image,DEFAULT_NUM_LEVELS);
    }

    private int getSubspace(float diff) {
        if (diff < 7f / 255f) {
            return 0;
        }
        else if (diff < 21f / 255f) {
            return 1;
        }
        else if (diff < 61f / 255f) {
            return 2;
        }
        else if (diff < 111f / 255f) {
            return 3;
        }
        else {
            return 4;
        }
    }

    private native float[] structureHistoC(byte[][] imQ, int wImg, int hImg, int qLevels);

    private float[] structuredHisto(byte[][] imQ, int wImg, int hImg) {


         int m = 0;
        double hw = Math.sqrt(hImg * wImg);
        double p = Math.floor(Math.log(hw) / Math.log(2) - 7.5); //Formula by Manjunath2002
        if (p < 0) {
            p = 0; //Minimum size of the division factor to have K=1
        }
        double K = Math.pow(2, p); //Determine the space between each structuring element
        double E = 8 * K; //Determine the size of the moving windows
        // Setting the local temporary and the CDS histograms
        float histo[] = new float[qLevels]; // CSD histograms
        int winHisto[] = new int[qLevels]; // local histo for a specific windows
        for (int i = 0; i < qLevels; i++) {
            histo[i] = 0.0f;
        }
        for (int y = 0; y < hImg - E; y += K) {
            for (int x = 0; x < wImg - E; x += K) {
                // Reinitialize the local windows histogram t[m]
                for (m = 0; m < qLevels; m++) {
                    winHisto[m] = 0;
                }
                for (int yy = y; yy < y + E; yy += K) {
                    for (int xx = x; xx < x + E; xx += K) {
                        //Obtain the pixel values of the HMMD quantized image
                        m = (int) (imQ[yy][xx] & 0x000000FF); //WARNING imQ is signed byte
                        //pixel value correspond to the bin value in qLevels CSD Histo
                        winHisto[m]++;
                    }
                } //End of local histogram for a local windows
                // Increment the color structure histogram for each color present in the structuring element
                for (m = 0; m < qLevels; m++) {
                    if (winHisto[m] > 0) {
                        histo[m]++;
                    }
                }
            }
        }
        //Normalize the histograms by the number of times the windows was shift
        int winShift_X = ((wImg - 1) - (int) E + (int) K);
        int winShift_Y = ((hImg - 1) - (int) E + (int) K);
        int S = (winShift_X / (int) K) * (winShift_Y / (int) K);
        for (m = 0; m < qLevels; m++) {
            histo[m] = histo[m] / S;
        }
        return histo;
    }

    private int[] reQuantization(float[] colorHistogramTemp) {
        int[] uniformCSD = new int[colorHistogramTemp.length];

        for (int i = 0; i < colorHistogramTemp.length; i++) {
            uniformCSD[i] = quantFuncC((double) colorHistogramTemp[i]);
        }
        return uniformCSD;
    }

    public int quantFunc(double x) {
        return quantFuncC(x);
    }

    private static int[] resizeCSD(MPEG7ColorStructure c, int qSizeDst) {
        int qSizeSrc = c.getQuantLevels();
        int[] dstHisto = new int[qSizeDst];
        int[] srcHisto = c.histo;
        if (qSizeSrc > qSizeDst) {
            int offsetSrc = (int) log2(qSizeSrc);
            int offsetDst = (int) log2(qSizeDst) - 5;
            int[] subStartPosSrc = getStartSubspacePos(offsetSrc);
            int[] subStartPosDst = getStartSubspacePos(offsetDst);
            int tmp = 0, sVal;
            double sumBinSrc, hueBinSrc, hueBinDst, sumBinDst;
            //We resize this descriptors
            for (int i = 0; i < qSizeSrc; i++) {
                tmp = 0;
                //Obtain the subspace Value or DiffBin looking at starting position
                for (sVal = 1; sVal < 5; sVal++) {
                    if (i < subStartPosSrc[sVal]) {
                        break;
                    }
                }
                sVal--;
                //Obtain the sum value
                tmp = i - subStartPosSrc[sVal];
                hueBinSrc = tmp % QUANTIZATION_TABLE[offsetSrc][sVal][0];
                sumBinSrc = Math.floor(tmp / QUANTIZATION_TABLE[offsetSrc][sVal][0]);
                //Compute their analog value in destination histograms
                hueBinDst = QUANTIZATION_TABLE[offsetDst][sVal][0] * (hueBinSrc / QUANTIZATION_TABLE[offsetSrc][sVal][0]);
                sumBinDst = QUANTIZATION_TABLE[offsetDst][sVal][1] * (sumBinSrc / QUANTIZATION_TABLE[offsetSrc][sVal][1]);
                //Then compute find the exact position in the destination histogram and increment
                tmp = subStartPosDst[sVal]  + QUANTIZATION_TABLE[offsetDst][sVal][0] * (int) sumBinDst + (int) hueBinDst;
                dstHisto[tmp] += srcHisto[i];
            }
        }
        return dstHisto;
    }

    public Double compare(MPEG7ColorStructure desc) {
        int[] f1, f2;
        if (desc.histo == null || this.histo == null) {
            return (null);
        }
        if (this.qLevels == desc.qLevels) {
            f1 = this.histo;
            f2 = desc.histo;
        } else if (this.qLevels < desc.qLevels) {
            f1 = this.histo;
            f2 = resizeCSD(desc, this.qLevels);
        } else {
            f1 = resizeCSD(this, desc.qLevels);
            f2 = desc.histo;
        }
        Log.d("size f1 f2",Integer.toString(f1.length) +  " " + Integer.toString(f2.length));
        Double distance = 0.0;
        for (int i = 0; i < f1.length; i++) {
            distance += Math.abs(f1[i] - f2[i]);
        }
        distance /= (256 * f1.length); //Normalization

        return distance;
    }

    public int getQuantLevels() {
        return qLevels;
    }

    private static native int[] getStartSubspacePosC(
            int offset,
            int[] quantArray);

    private int[] getStartSubspacePos() {
        return getStartSubspacePos(this.offset);
    }

    private static int[] getStartSubspacePos(int offset) {
        int[] startP = new int[5];
        startP[0] = 0;
        for (int i = 1; i < startP.length; i++) {
            startP[i] = startP[i - 1]; //Set the position of the previous subspace start
            startP[i] += QUANTIZATION_TABLE[offset][i - 1][0] * QUANTIZATION_TABLE[offset][i - 1][1]; //Add the length of the previous subspace
        }
        return startP;
    }

    private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }


}
