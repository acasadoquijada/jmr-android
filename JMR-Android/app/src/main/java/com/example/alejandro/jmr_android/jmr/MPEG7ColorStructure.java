package com.example.alejandro.jmr_android.jmr;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by alejandro on 06/09/2017.
 */

public class MPEG7ColorStructure {
    /**
     * The source media of this descriptor
     */
    protected transient Bitmap source = null;
    /**
     * The color space used in this descriptor
     */
    //public static final int COLOR_SPACE = ColorSpaceJMR.CS_HMMD;
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

    public MPEG7ColorStructure(Bitmap image) {
        this(image, DEFAULT_NUM_LEVELS);
    }

    /**
     * Initializes the quantization level taking into account that the valid
     * values are 32, 64, 128 or 256.
     */
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
        /*// The MPEG7ColorStructure need a JMRExtendedBufferedImage to be calculated
        JMRExtendedBufferedImage JMRimage = null;
        try {
            JMRimage = (JMRExtendedBufferedImage) image;
        } catch (ClassCastException ex) {
            JMRimage = new JMRExtendedBufferedImage(image);
        }
        // The color space and the image model must been the suitable ones.
        if (!checkImage(JMRimage)) {
            JMRimage = convertImg(JMRimage);
        }*/
        this.setLevels(qLevels);
        byte[][] imQ = quantHMMDImage(image);
        float[] histo = structuredHisto(imQ, image.getWidth(), image.getHeight());
        this.histo = reQuantization(histo);
    }

    /**
     * Re-quantize the histogram (following Caliph & Emir code).
     *
     * @param	colorHistogramTemp a {@link #qLevels} non uniform CSD histograms
     * containing values between [0-1]
     * @return	a {@link #qLevels} uniform histograms.
     */
    private int[] reQuantization(float[] colorHistogramTemp) {
        int[] uniformCSD = new int[colorHistogramTemp.length];
        for (int i = 0; i < colorHistogramTemp.length; i++) {
            uniformCSD[i] = quantFunc((double) colorHistogramTemp[i]);
        }
        return uniformCSD;
    }

    /**
     * Quantize the given value
     *
     * @param x the value to be quantized
     * @return the quantized value
     */
    static public int quantFunc(double x) {
        double[] stepIn = {0.000000001, 0.037, 0.08, 0.195, 0.32, 1};
        int[] stepOut = {-1, 0, 25, 45, 80, 115};
        int y = 0;
        if (x <= 0) {
            y = 0;
        } else if (x >= 1) {
            y = 255;
        } else {
            y = (int) Math.round(((x - 0.32) / (1 - 0.32)) * 140.0);
            for (int i = 0; i < stepIn.length; i++) {
                if (x < stepIn[i]) {
                    y += stepOut[i];
                    break;
                }
            }
            // Since there is a bug in Caliph & emir version the data
            // are between -66 and 255
            y = (int) (255.0 * ((double) y + 66) / (255.0 + 66.0));
        }
        return y;
    }

    private byte[][] quantHMMDImage(Bitmap imSrc) {
        //Source image variable
        int wImg = imSrc.getWidth();
        int hImg = imSrc.getHeight();
        float[] pix = new float[4];
        //Destination image array
        byte[][] imDst = new byte[hImg][wImg];
        int subspace, hue_bin, sum_bin, v;
        int[] startSubSpacePos = getStartSubspacePos();
        for (int y = 0; y < hImg; y++) {
            for (int x = 0; x < wImg; x++) {
                // Cojo el pixel
                int c = imSrc.getPixel(x, y);
                pix[0] += Color.red(c);
                pix[1] += Color.green(c);
                pix[2] += Color.blue(c);
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

    /**
     * Returns the type of subspace used in this descriptor.
     *
     * @param diff the 'diff' component in the HMMD color space
     * @return the type of subspace
     */
    private int getSubspace(float diff) {
        if (diff < 7f / 255f) {
            return 0;
        } else if (diff < 21f / 255f) {
            return 1;
        } else if (diff < 61f / 255f) {
            return 2;
        } else if (diff < 111f / 255f) {
            return 3;
        } else {
            return 4;
        }
    }
    /**
     * Calculates log(x)/log(2)
     * @param x the value
     * @return log(x)/log(2)
     */
    private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Calculates the subspace start positions (depending on the qLevels)
     *
     * @return an array with the 5 start position
     */
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

    /**
     * Returns the CSD histograms with value between 0 and 1.
     *
     * It creates a structuring block elements according to the size of the
     * quantified HMMD image. Then it computes a local histogram with the 8x8
     * structuring elements in the "sliding windows" block element. If one color
     * is present at least once on the local histogram of the sliding windows,
     * fill the CSD histogram with this color.
     *
     * @param imQ a byte matrix representing the quantifized values between
     * [0,qLevels] (heigh x width)
     * @param wImg width of the image
     * @param hImg height of the image
     * @return	a {@link #qLevels} histograms
     */
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

}
