package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.io.File;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;


public class TestDownloadFile {
	
	
	static String accountname = "fileforff1";
	
	static String accountkey = "XSbx9Cu6R7PZc8Auf7btW6OV+qH8M25FCMkfNsihJ7eCP0IDg0LIy5w9DzTIeeWeDocCCeXVlbLthVnMDYcXIg==";

	public static void main(String[] args) throws java.lang.Exception{
		
		String storageConnectionString =
				"DefaultEndpointsProtocol=https;" +
				"AccountName=fileforff1;" +
				"AccountKey=XSbx9Cu6R7PZc8Auf7btW6OV+qH8M25FCMkfNsihJ7eCP0IDg0LIy5w9DzTIeeWeDocCCeXVlbLthVnMDYcXIg==";
		
		System.out.println("Azure Blob storage quick start sample");

		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);;
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		
		CloudBlobContainer container = blobClient.getContainerReference("fileforff1");

		// Create the container if it does not exist with public access.
		System.out.println("Creating container: " + container.getName());
		container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());	
		
		blobClient = storageAccount.createCloudBlobClient();

		
		
		CloudBlockBlob blob = container.getBlockBlobReference("firmware.txt");
		
		//File downloadedFile = new File("D:\\", "firmware.txt");
		//blob.downloadToFile(downloadedFile.getAbsolutePath());
		String d = blob.downloadText();
		System.out.println(d);

	}
	
	

}
