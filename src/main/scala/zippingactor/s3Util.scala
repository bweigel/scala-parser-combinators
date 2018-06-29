package zippingactor

import com.amazonaws.services.s3.AmazonS3Client
import io.atlassian.aws.AmazonClient.S3Client
import io.atlassian.aws.AmazonClient
import zippingactor.types.S3Location

import scala.util.{Failure, Success, Try}

object s3Util {
  private val client: AmazonS3Client = AmazonClient.default(S3Client)

  def s3ObjectExists(s3Location: S3Location): Boolean = {
    val s3Object = Try(client.getObject(s3Location.s3Bucket, s3Location.s3Key))
    s3Object match {
      case Success(value) => true
      case Failure(exception) => false

    }
  }

}
