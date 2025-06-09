package br.com.comigo.assistencia.adapter.aggregate.cliente.storage;

//@Service
//@RequiredArgsConstructor
//@Slf4j
public class ImageUploadAdapter implements ImageUploadPort {

  // @Value("${aws.bucket.name}")
  // private String bucketName;

  // private final S3Client s3Client;

  /*
   * public String uploadImage(MultipartFile multipartFile) {
   * String filename = UUID.randomUUID() + "-" +
   * multipartFile.getOriginalFilename();
   * 
   * try {
   * PutObjectRequest putOb = PutObjectRequest.builder()
   * .bucket(bucketName)
   * .key(filename)
   * .build();
   * s3Client.putObject(putOb,
   * RequestBody.fromByteBuffer(ByteBuffer.wrap(multipartFile.getBytes())));
   * GetUrlRequest request = GetUrlRequest.builder()
   * .bucket(bucketName)
   * .key(filename)
   * .build();
   * return s3Client.utilities().getUrl(request).toString();
   * } catch (Exception e) {
   * log.error("erro ao subir arquivo: {}", e.getMessage());
   * return "";
   * }
   * }
   */
}
