package com.orangelabs.iot.azure.central.IOTAzureCentralTest.firmware;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.apache.log4j.Logger;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;

public class FirmwareDownloadUtil {

	static Logger log = Logger.getLogger(FirmwareDownloadUtil.class);

	public static CloudBlockBlob getCloudBlockBlob(GetAccessPropertiesFactory factory) {

		CloudBlockBlob blob = null;

		try {
			String accountname = factory.getStorageAccountName();
			String accountkey = factory.getStorageAccountKey();
			String containerName = factory.getStorageContainerName();
			String fileName = factory.getFirmwareFileName();

			String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=" + accountname + ";"
					+ "AccountKey=" + accountkey;

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			CloudBlobContainer container = blobClient.getContainerReference(containerName);

			// Create the container if it does not exist with public access.
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			blobClient = storageAccount.createCloudBlobClient();

			blob = container.getBlockBlobReference(fileName);
		} catch (InvalidKeyException e) {
			log.error("create CloudBlockBlob has exception on InvalidKeyException " + e.getMessage());
		} catch (URISyntaxException e) {
			log.error("create CloudBlockBlob has exception on URISyntaxException " + e.getMessage());
		} catch (StorageException e) {
			log.error("create CloudBlockBlob has exception on StorageException " + e.getMessage());
		}
		return blob;
	}

	public static String getHttpsUrl(GetAccessPropertiesFactory factory) {
		log.info(" to get https url");

		String accountname = factory.getStorageAccountName();
		String containerName = factory.getStorageContainerName();
		String fileName = factory.getFirmwareFileName();
		
		String url = "https://" + accountname + ".blob.core.windows.net/"+ containerName + "/" + fileName;
		
		return url;
	}

}
