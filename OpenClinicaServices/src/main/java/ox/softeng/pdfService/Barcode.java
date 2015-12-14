package ox.softeng.pdfService;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.Code128Writer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Barcode {
	
	
	public void generateDataMatrix(String input,Map<EncodeHintType,Object> hintTypes,String fileName){
		 
		BitMatrix bitMatrix;
		try {
			bitMatrix = new DataMatrixWriter().encode(input, BarcodeFormat.DATA_MATRIX, 48, 48,hintTypes);
			BufferedImage newBufferedImage = scale(bitMatrix,300,300);
			ImageIO.write(newBufferedImage, "PNG", new File(fileName));			
		} catch (Exception e) {
			System.out.println("Exception Found." + e.getMessage());
		}		
	}
	
	
	public void generateCode128(String barcodeValue,String fileName){
			
			BitMatrix bitMatrix;
			FileOutputStream fileStream;
			try {
  				//the barcode size is 600x90, which is a large file BUT later in the PDF XSL, 
				//we scale it to fit to 7cmx1cm with scaling="uniform" so it will keep the quality
				Map<EncodeHintType,Object> hintType = new HashMap<EncodeHintType,Object>();
				hintType.put(EncodeHintType.MARGIN, 0);
				
				bitMatrix = new Code128Writer().encode(barcodeValue, BarcodeFormat.CODE_128, 1200,180, hintType);
				
				fileStream = new FileOutputStream(new File(fileName));
				
				BitMatrix croppedBitMatrix = cropImage(bitMatrix);				
				MatrixToImageWriter.writeToStream(croppedBitMatrix, "png",fileStream);			
			
				
			} catch (Exception e) {
				System.out.println("Exception Found." + e.getMessage());
			}
	}
	
	public void generateGS1DataMatrix(String input,String fileName) throws Exception{
		try {			
 			Map<EncodeHintType,Object> hintType =new HashMap<EncodeHintType,Object>();
			hintType.put(EncodeHintType.DATA_MATRIX_SHAPE, com.google.zxing.datamatrix.encoder.SymbolShapeHint.FORCE_SQUARE);
			
			hintType.put(EncodeHintType.GS1_ENCODE, true);
			generateDataMatrix(input,hintType,fileName);
			
		} catch (Exception e) {
			System.out.println("Exception Found." + e.getMessage());
		}
	}
	
	public String readDataMatrix(String fileName) throws FileNotFoundException, IOException, NotFoundException{
		 BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
		            ImageIO.read(new FileInputStream(fileName)))));
			
		 Map<DecodeHintType,Object> hintTypes =new HashMap<DecodeHintType,Object>();
		 
		 ArrayList<BarcodeFormat> possibleFormats = new ArrayList<BarcodeFormat>();
		 possibleFormats.add(BarcodeFormat.DATA_MATRIX);
		 hintTypes.put(DecodeHintType.POSSIBLE_FORMATS,possibleFormats);
		 hintTypes.put(DecodeHintType.TRY_HARDER,true);
		 hintTypes.put(DecodeHintType.ASSUME_GS1,true);
		 hintTypes.put(DecodeHintType.PURE_BARCODE,true);		 
		 
		 Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintTypes);
		 return  qrCodeResult.getText();	
	}
	
	
	
	private BitMatrix cropImage(BitMatrix bitMatrix) throws IOException{
	
		int width  = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		
		int cropEdge = 0 ;
		for (int y = 0; y < height; y++) {
		    for (int x = 0; x < width; x++) {
		      if(bitMatrix.get(x, y)){
		    	  cropEdge = x;
		    	  break;
		      }
		    }
		}
		
		if(cropEdge-5 >= 0)
			cropEdge = cropEdge - 5;
		
		
		int newWidth = width- 2*cropEdge;
		BitMatrix pixels = new BitMatrix(newWidth,height);

		
		for (int y = 0; y < height; y++) {
			
		    for (int x = 0; x < newWidth; x++) {
		    	if(bitMatrix.get(x + cropEdge, y) )
		    		pixels.set(x,y);
		      }
		    }
		 return pixels;      
	}


	//Source from
	//http://stackoverflow.com/questions/22766017/datamatrix-encoding-with-zxing-only-generates-14px-bitmap
	private BufferedImage scale(BitMatrix bitMatrix, int requestedWidth,
			int requestedHeight) {

		int BLACK = 0xFF000000;
		int WHITE = 0xFFFFFFFF;

		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();

		// calculating the scaling factor
		int pixelsize = requestedWidth / width;
		if (pixelsize > requestedHeight / height) {
			pixelsize = requestedHeight / height;
		}

		int[] pixels = new int[requestedWidth * requestedHeight];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * requestedWidth * pixelsize;

			// scaling pixel height
			for (int pixelsizeHeight = 0; pixelsizeHeight < pixelsize; pixelsizeHeight++, offset += requestedWidth) {
				for (int x = 0; x < width; x++) {
					int color = bitMatrix.get(x, y) ? BLACK : WHITE;

					// scaling pixel width
					for (int pixelsizeWidth = 0; pixelsizeWidth < pixelsize; pixelsizeWidth++) {
						pixels[offset + x * pixelsize + pixelsizeWidth] = color;
					}
				}
			}
		}

		BufferedImage bitmap = new BufferedImage(requestedWidth,
				requestedHeight, BufferedImage.TYPE_INT_ARGB);
		bitmap.getRaster().setDataElements(0, 0, requestedWidth,
				requestedHeight, pixels);
		return bitmap;

	}

	public String readCode128(String fileName) throws FileNotFoundException, IOException, NotFoundException{
		
		 BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
		            ImageIO.read(new FileInputStream(fileName)))));
			
		 Map<DecodeHintType,Object> hintTypes =new HashMap<DecodeHintType,Object>();		 
		 ArrayList<BarcodeFormat> possibleFormats = new ArrayList<BarcodeFormat>();
		 possibleFormats.add(BarcodeFormat.CODE_128);
		 hintTypes.put(DecodeHintType.POSSIBLE_FORMATS,possibleFormats);
		 hintTypes.put(DecodeHintType.TRY_HARDER,true);
		 hintTypes.put(DecodeHintType.PURE_BARCODE,true);		 
		 
		 Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintTypes);
		 return  qrCodeResult.getText();	
	}
	
}
