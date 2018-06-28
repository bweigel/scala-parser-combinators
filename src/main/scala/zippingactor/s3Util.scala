package zippingactor

import io.atlassian.aws.AmazonClient.S3Client
import io.atlassian.aws.AmazonClientConnectionDef

object s3Util {
  val defaultClient = S3Client

  val config: AmazonClientConnectionDef = AmazonClientConnectionDef.default


}
