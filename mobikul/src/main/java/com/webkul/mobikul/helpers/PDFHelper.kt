package com.webkul.mobikul.helpers

import android.content.Context
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.html.simpleparser.TableWrapper
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.webkul.mobikul.R
import com.webkul.mobikul.models.user.InvoiceDetailsData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 * Created by aman.gupta on 13/1/18. @Webkul Software Private limited
 */

open class PDFHelper {

    fun generateInvoice(context: Context, data: InvoiceDetailsData, invoiceId: String): File? {
        val file: File

        val invoiceFolder = Environment.getExternalStorageDirectory().path + "/" + context.getString(R.string.app_name) + "-Invoices"
        val invoiceNO = "order-$invoiceId"
        val invoiceFolderFile = File(invoiceFolder)
        if (!invoiceFolderFile.exists())
            invoiceFolderFile.mkdirs()
        file = File(invoiceFolderFile.absolutePath, "/$invoiceNO.pdf")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return if (fatchInvoiceList(data, file, context))
            file
        else
            null
    }

    private fun fatchInvoiceList(orderEntity: InvoiceDetailsData, file: File, context: Context): Boolean {
        try {
            // To customise the text of the pdf
            // we can use FontFamily

            val mFBold14 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14f, Font.BOLD)
            val mF12 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12f)
            val mFBold12 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12f, Font.BOLD)
            // create an instance of itext document
            val document = Document()

            PdfWriter.getInstance(document,
                    FileOutputStream(file.absoluteFile))
            document.open()
            document.add(getParagraph(context.getString(R.string.x_items, orderEntity.items.size), mFBold12))
            document.add(Paragraph("\n"))
            var ls = LineSeparator()
            document.add(Chunk(ls))

            for (index in 0 until orderEntity.items.size) {
                document.add(getParagraph(context.getString(R.string.name_x, orderEntity.items[index].name), mF12))
                document.add(Paragraph("\n"))

                document.add(getParagraph(context.getString(R.string.qty_x, orderEntity.items[index].qty), mF12))
                document.add(Paragraph("\n"))

                document.add(getParagraph(context.getString(R.string.price_x, orderEntity.items[index].price), mF12))
                document.add(Paragraph("\n"))

                document.add(getParagraph(context.getString(R.string.subtotal_x, orderEntity.items[index].subTotal), mF12))
                document.add(Paragraph("\n"))
                ls = LineSeparator()
                document.add(Chunk(ls))
            }

            var val1: MutableList<PdfPCell> = ArrayList()
            val tableWrapper = TableWrapper(HashMap())
            document.add(getParagraph(context.getString(R.string.price_details), mFBold14))
            document.add(Paragraph("\n"))
            for (index in 0 until orderEntity.totals.size) {
                val1 = ArrayList()
                if (orderEntity.totals[index].code == "grand_total") {
                    orderEntity.totals[index].formattedValue?.let { getParagraph(it, mFBold14, PDF_ALIGNMENT_RIGHT) }?.let { getPdfCell(it) }?.let { val1.add(it) }
                    orderEntity.totals[index].label?.let { getParagraph(it, mFBold14, 0) }?.let { getPdfCell(it) }?.let { val1.add(it) }
                    tableWrapper.addRow(val1)
                } else {
                    orderEntity.totals[index].formattedValue?.let { getParagraph(it, mF12, PDF_ALIGNMENT_RIGHT) }?.let { getPdfCell(it) }?.let { val1.add(it) }
                    orderEntity.totals[index].label?.let { getParagraph(it, mFBold12, 0) }?.let { getPdfCell(it) }?.let { val1.add(it) }
                    tableWrapper.addRow(val1)
                }
            }

            val pTable = tableWrapper.createTable()
            document.add(pTable)
            // close document
            document.close()
            ToastHelper.showToast(context, context.getString(R.string.invoice_successfully_saved))
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (e: DocumentException) {
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    internal fun getPdfCell(elements: Paragraph): PdfPCell {
        val pCell = PdfPCell()
        pCell.setPadding(5f)
        pCell.addElement(elements)
        return pCell
    }

    internal fun getParagraph(name: String, font: Font, alignment: Int): Paragraph {

        val paragraph = Paragraph(name, font)
        if (alignment == PDF_ALIGNMENT_RIGHT)
            paragraph.alignment = Element.ALIGN_RIGHT
        else if (alignment == PDF_ALIGNMENT_CENTER)
            paragraph.alignment = Element.ALIGN_CENTER
        else
            paragraph.alignment = Element.ALIGN_LEFT
        return paragraph
    }

    internal fun getParagraph(name: String, font: Font): Paragraph {
        return Paragraph(name, font)
    }

    companion object {

        private var pdfHelper: PDFHelper? = null
        val FONT1 = "resources/fonts/PlayfairDisplay-Regular.ttf"
        val PDF_ALIGNMENT_RIGHT = 2
        val PDF_ALIGNMENT_CENTER = 1

        val instanse: PDFHelper
            @Synchronized get() {
                if (pdfHelper == null)
                    pdfHelper = PDFHelper()
                return pdfHelper!!
            }
    }
}