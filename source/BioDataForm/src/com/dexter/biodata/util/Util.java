package com.dexter.biodata.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import veridis.biometric.BiometricException;
import veridis.biometric.BiometricImage;
import veridis.biometric.BiometricSDK;
import veridis.biometric.BiometricScanner;
import veridis.biometric.CaptureEventListener;
import veridis.biometric.samples.util.LicenseHelper;

import com.dexter.biodata.BioDataForm;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Util implements CaptureEventListener
{
	public boolean isCaptureOn = false;
	String arch;
	private static BiometricImage image = null;
	private CaptureEventListener listener = this;
	private BioDataForm mainForm;
	
	public Util(BioDataForm mainForm)
	{
		this.mainForm = mainForm;
		
		initVeridisFreeSDK();
	}
	
	private void initVeridisFreeSDK()
	{
		UUID id = UUID.randomUUID();
    	String tempFolderName = System.getProperty("java.io.tmpdir")+"/"+id;
    	copyToTempFolder(tempFolderName);
    	
    	/*
		 * You have to set the directory where the .dll (windows) or .so (linux)
		 * are found 
		 * Don't forget that they must be on the system_path, or on
		 * the java library path or in temporary path
		 */
		LicenseHelper.setLibrariesDirectory(tempFolderName);
		
		/*Asks for license key before starting
		 * you MUST install a license before using this SDK*/
	    String key = "0000-019A-C4CF-DBCD-405B";
	    BiometricSDK.InstallLicense(key);
	}
	
	public void destroy()
	{
		//destroyFingerprintSDK();
	}
	
	public void saveToFile(File paramFile, ImageWriterSpi paramImageWriterSpi)
	{
		try
		{
			ImageWriter localImageWriter = paramImageWriterSpi.createWriterInstance();
			ImageOutputStream localImageOutputStream = ImageIO.createImageOutputStream(paramFile);
			localImageWriter.setOutput(localImageOutputStream);
			
			localImageWriter.write(image.getSubimage(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight()));
			
			localImageOutputStream.close();
			localImageWriter.dispose();
		}
		catch(IOException localIOException)
		{
			mainForm.writeLog(localIOException.toString());
		}
	}
	
	public void loadFingerprintFromFile(File paramFile, int paramInt, ImageReaderSpi paramImageReaderSpi)
	{
		try
		{
			ImageReader localImageReader = paramImageReaderSpi.createReaderInstance();
			ImageInputStream localImageInputStream = ImageIO.createImageInputStream(paramFile);
			localImageReader.setInput(localImageInputStream);
			
			BufferedImage localBufferedImage = localImageReader.read(0);
			
			localImageReader.dispose();
			localImageInputStream.close();
			
			image = new BiometricImage(localBufferedImage, 500);
		}
		catch(Exception localException)
		{
			mainForm.writeLog(localException.toString());
		}
	}
	
	public void loadPhotoFile(File paramFile, ImageReaderSpi paramImageReaderSpi)
	{
		try
		{
			ImageReader localImageReader = paramImageReaderSpi.createReaderInstance();
			ImageInputStream localImageInputStream = ImageIO.createImageInputStream(paramFile);
			localImageReader.setInput(localImageInputStream);
			
			BufferedImage localBufferedImage = localImageReader.read(0);
			
			localImageReader.dispose();
			localImageInputStream.close();
			
			mainForm.writeLog("Photo loaded from: " + paramFile.getAbsolutePath());
			mainForm.showPhotoImage(localBufferedImage);
		}
		catch(Exception localException)
		{
			mainForm.writeLog(localException.toString());
		}
	}
	
	/*copy files to temporary folder*/
	public void copyToTempFolder(String tempFolderName)
	{
		/*finds out if you are running on 32 or 64bits*/
		if( System.getProperty("sun.arch.data.model").equals("64"))
		{
			arch = "x64";
		}
		else arch = "x86";
		
    	ArrayList <String> dllNames = new ArrayList<String> ();

    	/*dlls to be loaded*/
    	dllNames.add("pthreadVC2.dll");
    	dllNames.add("VrBio.dll");
    	dllNames.add("VrModuleFutronic.dll");
    	dllNames.add("VrModuleDigitalPersona.dll");
    	dllNames.add("VrModuleNitgen.dll");
    	dllNames.add("VrModuleSuprema.dll");
		dllNames.add("libusb0.dll");
		dllNames.add("ftrScanAPI.dll");
		dllNames.add("NBioBSP.dll");
		dllNames.add("UFScanner.dll");
		dllNames.add("UFLicense.dat");
		
		String dllName;
		/*for each name in list*/
    	for(int i = 0; i < dllNames.size(); i++)
    	{
    		dllName = dllNames.get(i);
    		/*makes new folder*/
    		new File(tempFolderName).mkdirs();
            File tmpDir = new File(tempFolderName);
            File tmpFile = new File(tmpDir, dllName);

            if(!new File(tmpDir+"/"+dllName).isFile())
            {
	            try
	            {
	                InputStream in = getClass().getResourceAsStream("/dlls/"+arch+"/"+dllName);
	                OutputStream out = new FileOutputStream(tmpFile);
	
	                byte[] buf = new byte[8192];
	                int len;
	                while ((len = in.read(buf)) != -1)
	                {
	                    out.write(buf, 0, len);
	                }
	
	                in.close();
	                out.close();
	
	            }
	            catch (UnsatisfiedLinkError e)
	            {
	            	e.printStackTrace();
	                // deal with exception
	            }
	            catch(Exception e){
	            	e.printStackTrace();
	            }
	    	}
    	}
	}
	
	/* Add listener(this) to the capture event listener to start capture */
	public void startCapture()
	{
		if (!isCaptureOn)
			BiometricSDK.StartSDK(this);
		isCaptureOn = true;
	}

	/* Remove listener(this) to the capture event listener to stop capture */
	public void stopCapture()
	{
		if (isCaptureOn) {
			BiometricSDK.StopSDK(this);
		}
		isCaptureOn = false;
	}

	public static BiometricImage getImage()
	{
		return image;
	}
	
    public static byte[] bufferedImageToByteArray(BufferedImage img) throws ImageFormatException, IOException
    {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
    	encoder.encode(img);
    	return os.toByteArray();
    }
    
    public static BufferedImage byteArrayToBufferedImage(byte[] data) throws ImageFormatException, IOException
    {
    	ByteArrayInputStream is = new ByteArrayInputStream(data);
    	JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(is);
    	return decoder.decodeAsBufferedImage();
    }
	
	/*
	 * Must be implemented in order to implement CaptureEventListener; handles
	 * the capture events
	 */
	public void onCaptureEvent(CaptureEventType eventType,
			BiometricScanner reader, BiometricImage image)
	{
		// Prints the event, including the image size and resolution, if a image
		// is available
		if (image != null)
			mainForm.writeLog(reader + ": " + eventType + "  => " + image);
		else
			mainForm.writeLog(reader + ": " + eventType);

		switch (eventType)
		{
			// Device plugged. I want to receive images from it.
			case PLUG:
			{
				try
				{
					reader.addCaptureEventListener(listener);
					
					mainForm.setSensorName(reader.getVendor() + ": " + reader.getName());
				}
				catch (BiometricException e)
				{
					mainForm.writeLog("Cannot start device " + reader + ": " + e);
				}
				break;
			}
			// Device unplugged.
			// It might be nice to display it on your UI, but no action is required.
			case UNPLUG:
			{
				mainForm.setSensorName("No sensor detected!");
				stopCapture();
				break;
			}
			// The biometric feature has been placed on the scanner.
			// It might be nice to display it on your UI, but no action is required.
			case PLACED:
				break;
			// The biometric feature has been removed from the scanner.
			// It might be nice to display it on your UI, but no action is required.
			case REMOVED:
				break;
	
			// A image frame has been received.
			// It might be nice to display it on your UI, but no action is required.
			case IMAGE_FRAME:
				mainForm.showFingerprintImage(image);
				// pack(); //Refresh layout
				break;
			// A 'final' image has been captured. THIS is the image that we must
			// handle!
			case IMAGE_CAPTURED:
			{
				mainForm.showFingerprintImage(image);
			
				Util.image = image;
				stopCapture();
	
				break;
			}
			default:
			{
				
				break;
			}
		}
	}
}
