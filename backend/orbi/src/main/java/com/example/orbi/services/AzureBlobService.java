package com.example.orbi.services;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.*;
import com.azure.storage.common.sas.*;
import com.example.orbi.dto.SasTokenResponseDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class AzureBlobService {

    @Value("${azure.storage-account-name}")
    private String storageAccountName;

    private final BlobServiceClient blobServiceClient;

    public AzureBlobService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public SasTokenResponseDTO generateSasToken(String containerName, String fileName) {
        BlobContainerClient container = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = container.getBlobClient(fileName);
        OffsetDateTime expiry = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5);

        BlobSasPermission permission = new BlobSasPermission()
                .setCreatePermission(true)
                .setWritePermission(true)
                .setReadPermission(true)
                .setListPermission(true);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiry, permission)
                .setContentDisposition("inline");

        String sas = blobClient.generateSas(values);
        String uploadUrl = blobClient.getBlobUrl() + "?" + sas;

        return new SasTokenResponseDTO(
                uploadUrl,
                blobClient.getBlobUrl(),
                fileName
        );
    }

    

    public SasTokenResponseDTO generateReadSas(String containerName, String fileName) {
        System.out.println("Generating read SAS for container: " + containerName + ", file: " + fileName);
        BlobContainerClient container = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = container.getBlobClient(fileName);
        System.out.println("Generating read SAS for container: " + containerName + ", file: " + fileName);
        OffsetDateTime expiry = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5);

        BlobSasPermission permission = new BlobSasPermission()
                .setReadPermission(true);
        System.out.println("Generating read SAS for container: " + containerName + ", file: " + fileName);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiry, permission);

        String sas = blobClient.generateSas(values);

        String readUrl = blobClient.getBlobUrl() + "?" + sas;

        return new SasTokenResponseDTO(
                readUrl,
                blobClient.getBlobUrl(),
                fileName
        );
    }
}
