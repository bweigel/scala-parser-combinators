package zippingactor

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.{S3Object, S3ObjectInputStream}
import zippingactor.types.S3Location

import scala.io.Source
import scala.util.{Failure, Success, Try}

object s3Util {
  private val client = AmazonS3ClientBuilder.standard()
    .withRegion(Regions.EU_CENTRAL_1)
    .build()

  def getObject(s3Location: S3Location): Try[S3Object] = Try(client.getObject(s3Location.s3Bucket, s3Location.s3Key))

  def getObjectContent(s3Location: S3Location): Try[S3ObjectInputStream] = {
    getObject(s3Location).map(_.getObjectContent)
  }

  def getObjectContentString(s3Location: S3Location): Option[String] = {
    val is = getObjectContent(s3Location)
    is match {
      case Success(value) =>
        val res = Some(Source.fromInputStream(value, enc = "utf-8").mkString)
        value.close()
        res
      case Failure(_) => None
    }

  }


  def s3ObjectAccessible(s3Location: S3Location): Boolean = {
    val s3Object = getObject(s3Location)
    s3Object match {
      case Success(_) => true
      case Failure(_) => false

    }
  }

}
