package com.clienttrack.api.service;

import com.clienttrack.api.core.Client;
import com.clienttrack.api.core.Proposal;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class ProposalPdfService {
    public byte[] build(Proposal proposal) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document doc = new Document(PageSize.A4, 40, 40, 50, 40);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Color primary = new Color(40, 80, 200);
            Color dark = new Color(55, 55, 55);
            Color light = new Color(238, 238, 238);

            Font title = new Font(Font.HELVETICA, 18, Font.BOLD, dark);
            Font section = new Font(Font.HELVETICA, 14, Font.BOLD, dark);
            Font body = new Font(Font.HELVETICA, 11, Font.NORMAL, dark);
            Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD, primary);
            Font footerFont = new Font(Font.HELVETICA, 9, Font.NORMAL, dark);

            // HEADER
            PdfPCell head = new PdfPCell(new Phrase("Proposal Document", new Font(Font.HELVETICA, 16, Font.BOLD, Color.WHITE)));
            head.setBackgroundColor(primary);
            head.setHorizontalAlignment(Element.ALIGN_CENTER);
            head.setPadding(10);
            head.setBorder(Rectangle.NO_BORDER);

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            headerTable.addCell(head);
            doc.add(headerTable);

            doc.add(Chunk.NEWLINE);

            // CLIENT + META
            PdfPTable meta = new PdfPTable(2);
            meta.setWidthPercentage(100);
            meta.setWidths(new float[]{3, 2});

            Client client = proposal.getDeal().getClient();

            PdfPCell left = new PdfPCell(new Phrase(
                    client.getName() + "\n" +
                            (client.getCompany() != null ? client.getCompany() + "\n" : "") +
                            (client.getEmail() != null ? client.getEmail() + "\n" : "") +
                            (client.getPhone() != null ? client.getPhone() : ""),
                    body
            ));
            left.setBorder(Rectangle.NO_BORDER);

            PdfPCell right = new PdfPCell(new Phrase(
                    "Proposal: " + proposal.getTitle() + "\n" +
                            "Date: " + LocalDate.now(),
                    body
            ));
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);
            right.setBorder(Rectangle.NO_BORDER);

            meta.addCell(left);
            meta.addCell(right);
            doc.add(meta);

            doc.add(Chunk.NEWLINE);

            // PROPOSAL TITLE
            doc.add(new Paragraph(proposal.getTitle(), title));
            doc.add(Chunk.NEWLINE);

            // SUMMARY
            doc.add(new Paragraph("Summary", section));
            doc.add(new Paragraph(
                    (proposal.getDescription() == null || proposal.getDescription().isBlank())
                            ? "A structured proposal outlining the agreed scope and pricing."
                            : proposal.getDescription(),
                    body
            ));
            doc.add(Chunk.NEWLINE);

            // PRICING TABLE
            doc.add(new Paragraph("Pricing", section));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{5, 1, 2});

            PdfPCell h1 = new PdfPCell(new Phrase("Item", headerFont));
            PdfPCell h2 = new PdfPCell(new Phrase("Qty", headerFont));
            PdfPCell h3 = new PdfPCell(new Phrase("Price", headerFont));

            h1.setBackgroundColor(primary); h1.setPadding(5);
            h2.setBackgroundColor(primary); h2.setPadding(5);
            h3.setBackgroundColor(primary); h3.setPadding(5);

            table.addCell(h1);
            table.addCell(h2);
            table.addCell(h3);

            boolean even = true;
            double total = 0;

            for (var item : proposal.getPricingItems()) {
                Color bg = even ? Color.WHITE : light;
                even = !even;

                PdfPCell c1 = new PdfPCell(new Phrase(item.description(), body));
                PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(item.quantity()), body));
                PdfPCell c3 = new PdfPCell(new Phrase("€" + item.price(), body));

                c1.setBackgroundColor(bg); c1.setPadding(4);
                c2.setBackgroundColor(bg); c2.setPadding(4);
                c3.setBackgroundColor(bg); c3.setPadding(4);

                table.addCell(c1);
                table.addCell(c2);
                table.addCell(c3);

                total += item.quantity() * item.price().doubleValue();
            }

            doc.add(table);
            doc.add(Chunk.NEWLINE);

            Paragraph t = new Paragraph("Total: €" + String.format("%.2f", total), totalFont);
            t.setAlignment(Element.ALIGN_RIGHT);
            doc.add(t);

            doc.add(Chunk.NEWLINE);

            // ACCEPTANCE
            doc.add(new Paragraph("Acceptance", section));
            doc.add(new Paragraph(
                    "If approved, please reply with confirmation to proceed.",
                    body
            ));
            doc.add(new Paragraph("Signature: __________________________", body));
            doc.add(new Paragraph("Date: __________________________", body));

            doc.add(Chunk.NEWLINE);

            // FOOTER
            PdfPCell fc = new PdfPCell(new Phrase("Generated by ClientTrack — CodeBShift.dev", footerFont));
            fc.setHorizontalAlignment(Element.ALIGN_CENTER);
            fc.setBorder(Rectangle.NO_BORDER);
            fc.setPadding(5);

            PdfPTable footer = new PdfPTable(1);
            footer.setWidthPercentage(100);
            footer.addCell(fc);
            doc.add(footer);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF build failed", e);
        }
    }

}
