package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.AwsBucketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AppAwsS3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name.verification}")
    private String verificationBucket;

    @Value("${aws.s3.bucket-name.photos}")
    private String photosBucket;

    private final S3TransferManager s3TransferManager;

    public AppAwsS3Service(S3Client s3Client,S3TransferManager s3TransferManager,
                           S3AsyncClient s3AsyncClient) {
        this.s3Client = s3Client;
        this.s3TransferManager = s3TransferManager;
    }

    public void upload(List<MultipartFile> files, String userId, AwsBucketType awsBucketType) {
        List<CompletableFuture<String>> futures =files.stream().map(file-> {
            String bucketName = photosBucket;
            if(AwsBucketType.VERIFICATION.equals(awsBucketType)){
                bucketName = verificationBucket;
            }
            String s3Key =  userId + "/" + file.getOriginalFilename();
            try {
                UploadRequest uploadRequest = UploadRequest.builder()
                        .putObjectRequest(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(s3Key)
                                .contentType(file.getContentType())
                                .build()
                        ).requestBody(AsyncRequestBody.fromBytes(file.getBytes()))
                        .build();
                CompletableFuture<CompletedUpload> uploadFuture = s3TransferManager.upload(uploadRequest).completionFuture();
                return uploadFuture.thenApply(result -> s3Key);
            } catch (Exception e) {
                CompletableFuture<String> failed = new CompletableFuture<>();
                failed.completeExceptionally(e);
                return failed;
            }
        }).toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            // Block once for the entire batch with a global timeout (e.g., 60 seconds)
            allFutures.orTimeout(60, TimeUnit.SECONDS).join();
        } catch (CompletionException e) {
            throw new RuntimeException("S3 upload failed or timed out", e.getCause());
        }

    }

    public List<Resource> getFilesByUserId(String userId,AwsBucketType awsBucketType) {
        String bucketName = photosBucket;
        String prefix = userId+"/";
        if(AwsBucketType.VERIFICATION.equals(awsBucketType)){
            bucketName = verificationBucket;
        }
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
        final String finalBucketName = bucketName;
        return listResponse.contents().stream()
                .map(s3Object -> downloadFile(s3Object.key(),finalBucketName))
                .collect(Collectors.toList());
    }

    public Resource downloadFile(String key,String bucketName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> s3InputStream = s3Client.getObject(getObjectRequest);

        // Wrap the S3 input stream into an InputStreamResource
        return new InputStreamResource(s3InputStream);
    }
}
