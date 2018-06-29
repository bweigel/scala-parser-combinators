package zippingactor

import org.scalatest.FunSuite
import zippingactor.s3Util.s3ObjectExists
import zippingactor.types.S3Location

class s3UtilTest extends FunSuite {

  test("testS3ObjectExists") {
    val loc1 = S3Location("eigelb.projects", "tests")
    val loc2 = S3Location("eigelb.projects", "false")
    val res = s3ObjectExists(loc1)
    val res2 = s3ObjectExists(loc2)
    assert(res)
    assert(!res2)
  }

}
