package reading.excel

import jxl.Workbook
import jxl.read.biff.BiffException
import java.io.File
import java.io.IOException

object ExcelReader {
    @Throws(IOException::class, BiffException::class)
    fun readJExcel(fileLocation: String): Map<Int, MutableList<String>> {
        val data: MutableMap<Int, MutableList<String>> = HashMap()
        val workbook = Workbook.getWorkbook(File(fileLocation))
        val sheet = workbook.getSheet(0)
        val rows = sheet.rows
        val columns = sheet.columns
        for (i in 0 until rows) {
            data[i] = ArrayList()
            for (j in 0 until columns) {
                data[i]
                    ?.add(
                        sheet.getCell(j, i)
                            .contents
                    )
            }
        }
        return data
    }
}
