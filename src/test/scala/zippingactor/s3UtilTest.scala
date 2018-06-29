package zippingactor

import org.scalatest.FunSuite
import zippingactor.s3Util.{s3ObjectAccessible, getObjectContentString}
import zippingactor.types.S3Location

class s3UtilTest extends FunSuite {

  test("testS3ObjectExists") {
    val loc1 = S3Location("eigelb.projects", "tests")
    val loc2 = S3Location("eigelb.projects", "false")
    val res = s3ObjectAccessible(loc1)
    val res2 = s3ObjectAccessible(loc2)
    assert(res)
    assert(!res2)
  }

  test("testgetObjectContentString") {
    val loc1 = S3Location("eigelb.projects", "tests")
    val loc2 = S3Location("eigelb.projects", "false")
    val res1 = getObjectContentString(loc1)
    val res2 = getObjectContentString(loc2)
    assert(res1.isDefined)
    assert(res2.isEmpty)
    assert(res1.get == "{\"test\": \"this\"}")
  }

}
