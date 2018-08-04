package com.kumar.akshay.libmag.Barcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.print.PrintHelper;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class BarCode {

    Bitmap bitmap = null;
    Context context;

    public BarCode(Context con) {
        context = con;
    }

    public String scanBarCode() {
//        Intent intent = new Intent(BarCode.this, ScanBarcodeActivity.class);
//        startActivityForResult(intent, 0);
        return "";
    }

    public void generateBarCode(String barCode, ImageView barCodeImageView) {
        try {
            bitmap = encodeAsBitmap(barCode, BarcodeFormat.PDF_417, 600, 300);
            barCodeImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

//        if (requestCode == 0){
//            if (requestCode == CommonStatusCodes.SUCCESS){
//                if (data != null){
//                    Barcode barcode = data.getParcelableExtra("barcode");
//                    textView.setText("Barcode value is " + barcode.displayValue);
//                }else
//                    textView.setText("No barcode found");
//            }
//        }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }


    public void saveBarCode(String barCode) {
        try {
            bitmap = encodeAsBitmap(barCode, BarcodeFormat.PDF_417, 600, 300);
            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/LibMag");
            dir.mkdirs();
            String fileName = String.format("%s.jpg", "BookId : " + barCode);
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Toast.makeText(context, "Barcode saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "File not found exception", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "IOException found", Toast.LENGTH_SHORT).show();
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving barcode", Toast.LENGTH_SHORT).show();
        }
    }

    public void printBarCode(String barCode) {
        if (bitmap != null) {
            PrintHelper printHelper = new PrintHelper(context);
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FILL);
            printHelper.printBitmap("Printing the barcode : LibMag", bitmap);
        } else if (barCode != null) {
            try {
                bitmap = encodeAsBitmap(barCode, BarcodeFormat.PDF_417, 600, 300);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            printBarCode(barCode);
        } else
            Toast.makeText(context, "Barcode not yet generated", Toast.LENGTH_LONG).show();
    }
}