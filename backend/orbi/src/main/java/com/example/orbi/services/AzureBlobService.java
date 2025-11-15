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

    @Value("${azure.container-name}")
    private String containerName;

    @Value("${azure.storage-account-name}")
    private String storageAccountName;

    private final BlobServiceClient blobServiceClient;

    public AzureBlobService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public SasTokenResponseDTO generateSasToken(String fileName) {

        BlobContainerClient container = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = container.getBlobClient(fileName);

        // TOKEN VALENDO POR 5 MINUTOS
        OffsetDateTime expiry = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(1440);

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

    public SasTokenResponseDTO generateReadSas(String fileName) {

        BlobContainerClient container = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = container.getBlobClient(fileName);

        OffsetDateTime expiry = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5);

        BlobSasPermission permission = new BlobSasPermission()
                .setReadPermission(true);

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

