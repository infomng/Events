package com.events.common.supabase.service;

import com.events.common.supabase.config.SupabaseProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Objects;

@Service
public class SupabaseStorageService {

    private final SupabaseProperties properties;
    private final WebClient webClient;

    public SupabaseStorageService(SupabaseProperties properties) {
        this.properties = properties;

        this.webClient = WebClient.builder()
                .baseUrl(properties.getProjectUrl() + "/storage/v1/object/")
                .defaultHeader("Authorization", "Bearer " + properties.getServiceRoleKey())
                .build();
    }

    private static String cleanFileName(MultipartFile file) {
        return Normalizer.normalize(Objects.requireNonNull(file.getOriginalFilename()), Normalizer.Form.NFD)
                .replaceAll("[^a-zA-Z0-9.\\-_]", "_")
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[%?!@#]", "");
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String cleanedFileName = cleanFileName(file);

        webClient.put()
                .uri(properties.getBucketName() + "/" + cleanedFileName)
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())))
                .bodyValue(file.getBytes())
                .retrieve()
                .toBodilessEntity()
                .block();

        return properties.getProjectUrl() + "/storage/v1/object/public/" + properties.getBucketName() + "/" + cleanedFileName;
    }
}
