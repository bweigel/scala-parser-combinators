package zippingactor

import com.amazonaws.services.s3.AmazonS3Client
import io.atlassian.aws.AmazonClient.S3Client
import io.atlassian.aws.{AmazonClient, AmazonClientConnectionDef}

object s3Util {
  val client: AmazonS3Client  = AmazonClient.default(S3Client)

  client.

}
