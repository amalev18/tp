import com.github.polomarcus.utils.ClimateService
import com.github.polomarcus.model.CO2Record
import org.scalatest.funsuite.AnyFunSuite

//@See https://www.scalatest.org/scaladoc/3.1.2/org/scalatest/funsuite/AnyFunSuite.html
class ClimateServiceTest extends AnyFunSuite {
  test("containsWordGlobalWarming - non climate related words should return false") {
    assert( ClimateService.isClimateRelated("pizza") == false)
    assert( ClimateService.isClimateRelated("The sun is warming my skin") == false)
  }

  test("isClimateRelated - climate related words should return true") {
    assert(ClimateService.isClimateRelated("climate change") == true)
    assert(ClimateService.isClimateRelated("IPCC"))
    assert(ClimateService.isClimateRelated("global warming") == true)
  }

  test("parseRawData") {
    // our inputs
    val firstRecord = (2003, 1, 355.2)     //help: to acces 2003 of this tuple, you can do firstRecord._1
    val secondRecord = (2004, 1, 375.2)
    val list1 = List(firstRecord, secondRecord)

    // our output of our method "parseRawData"
    val co2RecordWithType = CO2Record(firstRecord._1, firstRecord._2, firstRecord._3)
    val co2RecordWithType2 = CO2Record(secondRecord._1, secondRecord._2, secondRecord._3)
    val output = List(Some(co2RecordWithType), Some(co2RecordWithType2))

    // we call our function here to test our input and output
    assert(ClimateService.parseRawData(list1) == output)
  }

  test("parseRawDataNegativeValue") {
    // our inputs
    val firstRecord = (2003, 1, -355.2)
    val secondRecord = (2004, 1, -375.2)
    val list1 = List(firstRecord, secondRecord)

    // our output of our method "parseRawData"
    val co2RecordWithType = CO2Record(firstRecord._1, firstRecord._2, firstRecord._3)
    val co2RecordWithType2 = CO2Record(secondRecord._1, secondRecord._2, secondRecord._3)
    val output = List(None, None)

    // we call our function here to test our input and output
    assert(ClimateService.parseRawData(list1) == output)
  }

  test("findMinMax") {
    val min = CO2Record(2022, 4, -99.99)
    val max = CO2Record(1964, 2, 320.03)
    val mid = CO2Record(1958, 4, 317.29)
    val list1 = List(mid, max, min)

    val output = ClimateService.getMinMax(list1)

    assert(output == (-99.99,320.03))
  }

  test("findMinMaxDuplicateValue") {
    val min = CO2Record(1961, 9, 314.91)
    val max = CO2Record(1961, 10, 314.91)
    val mid = CO2Record(1962, 2, 318.85)
    val list1 = List(mid, max, min)

    val output = ClimateService.getMinMax(list1)

    assert(output == (314.91, 318.85))
  }

  test("minMaxByYear") {
    val min = CO2Record(1960, 11, -99.99)
    val record1 = CO2Record(1960, 12, 316.39)
    val max = CO2Record(1960, 1, 317.27)
    val record2 = CO2Record(1961, 2, 317.92)

    val list1 = List(min, record1, max, record2)
    val output = ClimateService.getMinMaxByYear(list1, 1960)

    assert(output == (-99.99, 317.27))
  }

  test("diffMinMax") {
    val output = ClimateService.diffMinMax(315.67, 317.59)
    assert(output == 1.92)
  }

  test("diffMinMaxNegativeValue") {
    val output = ClimateService.diffMinMax(-99.99, 317.27)
    assert(output == 417.26)
  }


  test("filterDecemberData") {

    val record1 = CO2Record(1958, 12, 314.76)
    val record2 = CO2Record(1960, 10, 314.08)
    val record3 = CO2Record(1961, 12, 317.3)
    val record4 = CO2Record(1970, 12, 325.18)
    val record5 = CO2Record(2000, 8, 368.02)

    val list1 = List(Option(record1), Option(record2), Option(record3), Option(record4), Option(record5))
    val noDecemberData = ClimateService.filterDecemberData(list1)

    assert(noDecemberData == List(record2, record5))
  }
}
