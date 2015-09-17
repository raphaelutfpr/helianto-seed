package org.helianto.home.storage;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 * Amazon S3-based file reader.
 *
 * @author mauriciofernandesdecastro
 */
public class S3FileReader {

	private final AWSCredentials awsCredentials;
	private final String bucketName;
	private String baseUrl = "//s3-sa-east-1.amazonaws.com";
	
	/**
	 * Creates a S3-based file storage.
	 * 
	 * @param baseUrl
	 * @param accessKey the S3 access key
	 * @param secretAccessKey S3 the secret
	 * @param bucketName the bucket in your account where files should be stored
	 */
	public S3FileReader(String baseUrl, String accessKey, String secretAccessKey, String bucketName) {
		this.baseUrl = baseUrl;
		awsCredentials = new AWSCredentials(accessKey, secretAccessKey);
		this.bucketName = bucketName;
	}
	
	/**
	 * Creates a S3-based file storage.
	 * 
	 * @param accessKey the S3 access key
	 * @param secretAccessKey S3 the secret
	 * @param bucketName the bucket in your account where files should be stored
	 */
	public S3FileReader(String accessKey, String secretAccessKey, String bucketName) {
		awsCredentials = new AWSCredentials(accessKey, secretAccessKey);
		this.bucketName = bucketName;
	}
	
	public String absoluteUrl(String fileName) {
		return baseUrl + "/" + bucketName + "/" + fileName;
	}
	
	/**
	 * Read from S3-based file storage.
	 * 
	 * @param objectName
	 */
	public byte[] read(String objectName) {
		S3Service s3 = createS3Service();
		S3Bucket bucket;
		
		try {
			bucket = s3.getBucket(bucketName);
			System.out.println(bucket.getName());
			S3Object objectComplete = s3.getObject(bucket, objectName);
			return IOUtils.toByteArray(objectComplete.getDataInputStream());
		} catch (S3ServiceException e) {
			throw new IllegalStateException("Unable to retrieve S3 Bucket", e);
		} catch (ServiceException e) {
			throw new IllegalStateException("Unable to read data from S3 Bucket", e);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read data", e);
		}
		
	}

	// internal helpers
	
	private S3Service createS3Service() {
		try {
			return new RestS3Service(awsCredentials);
		} catch (S3ServiceException e) {
			throw new IllegalArgumentException("Unable to init REST-based S3Service with provided credentials", e);
		}
	}
	
}
