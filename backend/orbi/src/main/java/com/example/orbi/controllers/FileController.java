package com.example.orbi.controllers;

import org.springframework.web.bind.annotation.*;

import com.example.orbi.dto.SasTokenResponseDTO;
import com.example.orbi.services.AzureBlobService;

@RestController
@RequestMapping("/files")
public class FileController {

    private final AzureBlobService azureBlobService;

    public FileController(AzureBlobService azureBlobService) {
        this.azureBlobService = azureBlobService;
    }

    @GetMapping("/sas-token")
    public SasTokenResponseDTO getSasToken(@RequestParam String filename) {
        return azureBlobService.generateSasToken(filename);
    }

    @GetMapping("/generate-read-url/{fileName}")
        public SasTokenResponseDTO generateReadUrl(@PathVariable String fileName) {
            return azureBlobService.generateReadSas(fileName);
        }

}

