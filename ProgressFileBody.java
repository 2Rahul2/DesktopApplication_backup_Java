package org.openjfx.hellofx;


import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class ProgressFileBody extends FileBody{
	 	private long bytesWritten;
	    private long totalSize;
	    public volatile ProgressOutputStream progressOutputStream;
	    private SendFilesToServer sendFileObject;
	    public ProgressFileBody(File file, ContentType contentType ,SendFilesToServer sendFileObject) {
	        super(file, contentType);
	        this.sendFileObject = sendFileObject;

	    }
	    public void resetProgress() {
	        progressOutputStream = null;  // Reset the ProgressOutputStream
	        bytesWritten = 0;  // Reset bytesWritten
	        totalSize = 0;  // Reset totalSize
	    }
	    public void pause() {    	
	    	progressOutputStream.pause = true;
	    }
	    public void resume() {
	    	progressOutputStream.pause = false;
	    	System.out.println(progressOutputStream.pause);
	    }

	    @Override
	    public void writeTo(OutputStream out) throws IOException {
	    		progressOutputStream = new ProgressOutputStream(out, this.getContentLength() ,sendFileObject);
	    		super.writeTo(progressOutputStream);
	    		// Retrieve progress information
	    		bytesWritten = progressOutputStream.getBytesWritten();
	    		totalSize = progressOutputStream.getTotalSize();
	    		System.out.println("Bytes Written :"+bytesWritten);	    		
	    }
	    public long getBytes() {
	    	if(progressOutputStream!=null) {
	    		return progressOutputStream.bytesWritten;
	    	}
	    	return 0;
	    }
	    public long getTotal() {
	    	if(progressOutputStream!=null) {
	    		return progressOutputStream.totalSize;
	    	}
	    	return 0;
	    }
	    

	    public long getBytesWritten() {
	        return bytesWritten;
	    }

	    public long getTotalSize() {
	        return totalSize;
	    }
	    
	    class ProgressOutputStream extends FilterOutputStream {
	        private long totalSize;
	        private long bytesWritten;
	        private SendFilesToServer sendFileObject;
	        public volatile boolean pause;
	        public ProgressOutputStream(OutputStream out, long totalSize ,SendFilesToServer sendFileObject) {
	            super(out);
	            this.sendFileObject = sendFileObject;
	            this.totalSize = totalSize;
	            this.bytesWritten = 0;
	            this.pause=false;
	        }
	        public synchronized void resume() {
	            pause = false;
	            notify(); // Notify the waiting thread to resume
	        }
	        public synchronized void pause() {
	        	pause = true;
	        	notify();
	        }

	        @Override
	        public void write(byte[] b, int off, int len) throws IOException {
//	        		if(pause) {
//	        			return;
//	        		}
	        	synchronized (this) {
	                while (pause) {
	                    try {
	                        wait(); // Wait until resume() is called
	                    } catch (InterruptedException e) {
	                        Thread.currentThread().interrupt();
	                    }
	                }
	            }
	        	 out.write(b, off, len);
	        	 bytesWritten += len;	        		
	        		System.out.println("writting");
	        	
//	            double percentage = ((double) bytesWritten / totalSize) * 100;	           
	        }

	        @Override
	        public void write(int b) throws IOException {
	        	 synchronized (this) {
	        	        while (pause) {
	        	            try {
	        	                wait(); // Wait until the upload is resumed
	        	            } catch (InterruptedException e) {
	        	                e.printStackTrace();
	        	            }
	        	        }

	        	    }
	        	 out.write(b);
	        	 bytesWritten++;
//	            out.write(b);
//	            bytesWritten++;
	            // Print or use bytesWritten and totalSize to track progress
//	            System.out.println("Transferred: " + bytesWritten + " bytes out of " + totalSize + " bytes");
	        }
	        
	        public long getBytesWritten() {
	            return bytesWritten;
	        }

	        public long getTotalSize() {
	            return totalSize;
	        }
	    }
}
