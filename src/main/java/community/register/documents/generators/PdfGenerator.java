package community.register.documents.generators;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import community.register.config.Config;
import community.register.dao.implementations.FamilyDAO;
import community.register.model.Family;
import community.register.model.FamilyMember;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PdfGenerator implements DocumentGenerator{

    public PdfGenerator() {

    }

    @Override
    public void generateDocument() throws IOException {
        
        FamilyDAO dao = FamilyDAO.getInstance();
        
        String pdfPath = Config.resourcePath + "/pdfs/families-"+ LocalDateTime.now().toString().replace(":",".")+".pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

         String FONT = Config.resourcePath + "/fonts/DejaVuSans.ttf";
         PdfFont font = PdfFontFactory.createFont(FONT, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
         document.setFont(font);

        Paragraph title = new Paragraph("ДОМОВНИК")
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFixedPosition(0, 400, 550);
        document.add(title);

        document.add(new AreaBreak());


        List<Family> families = dao.getSortedFamilyListByOrderNumber();

        for (Family family : families) {

            document.add(new Paragraph("Број: " + family.getOrderNumber()).setFontSize(10));
            document.add(new Paragraph("\n"));


            float[] infoColumnWidths = {500F};
            Table infoTable = new Table(infoColumnWidths);
            infoTable.addCell(new Cell().add(new Paragraph("Име и презиме: " + family.getHost().getName() + " " + family.getHost().getLastName())).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Мјесто: " + family.getPlace() + " ул. " + family.getStreet())).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Датум рођења: " + family.getHost().getBirthday())).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Да ли је крштен? " + (family.getHost().isBaptized() ? "Да" : "Не"))).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Да ли је црквено вјенчан? " + (family.getHost().isMarried() ? "Да" : "Не"))).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Крсна слава: " + family.getPatronSaint())).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Преслава: " + (family.getSecondPatronSaint() == null ? "" : family.getSecondPatronSaint()))).setFontSize(10));
            infoTable.addCell(new Cell().add(new Paragraph("Број телефона: " + family.getPhoneNumber())).setFontSize(10));

            document.add(infoTable);
            document.add(new Paragraph("\n"));

            float[] memberColumnWidths = {30F, 110F, 80F, 60F, 50F, 100F, 70F};
            Table memberTable = new Table(memberColumnWidths);
            memberTable.addCell(new Cell().add(new Paragraph("Р/Б")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Имена осталих укућана")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Датум рођења")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Да ли је крштен-а")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Да ли је вјенчан")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Сродство са домаћином")).setFontSize(10));
            memberTable.addCell(new Cell().add(new Paragraph("Дом освећен")).setFontSize(10));

            List<FamilyMember> members = family.getFamilyMembersWithoutHost();
            int count = 1;
            for (int i = 0; i < members.size(); i++) {
                FamilyMember member = members.get(i);
                if (member.isHost()) {
                    continue;
                }
                if (i >= 12) break; // Limit to 12 rows
                memberTable.addCell(new Cell().add(new Paragraph(count++ + ".")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(member.getName())).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(member.getBirthday().toString())).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(member.isBaptized() ? "Да" : "Не")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(member.isMarried() ? "Да" : "Не")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(member.getRelationshipWithHost())).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph(i == 0 && family.isBlessedHome() ? "Да" : "")).setFontSize(10));
            }

            int m = 15 - members.size();
            for (int i = 0; i < m; i++) {
                memberTable.addCell(new Cell().add(new Paragraph(count++ + ".")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
                memberTable.addCell(new Cell().add(new Paragraph("")).setFontSize(10));
            }

            document.add(memberTable);
            document.add(new Paragraph("\n"));

            float[] noteColumnWidths = {500F};
            Table noteTable = new Table(noteColumnWidths);

            String noteText = "Напомена: " + family.getNote();
            String[] noteLines = splitTextIntoLines(noteText, 85);

            for (String line : noteLines) {
                noteTable.addCell(new Cell().add(new Paragraph(line)).setFontSize(10));
            }

            while (noteLines.length < 5) {
                noteTable.addCell(new Cell().setHeight(20).add(new Paragraph("")));
                noteLines = appendEmptyLine(noteLines);
            }

            document.add(noteTable);
            document.add(new AreaBreak());
        }

        document.close();

    }
    private static String[] splitTextIntoLines(String text, int width) {
        ArrayList<String> resultList = new ArrayList<>();
        int startIndex = 0;

        while (startIndex < text.length()) {

            int endIndex = Math.min(startIndex + width, text.length());

            if (endIndex < text.length() && !Character.isWhitespace(text.charAt(endIndex))) {
                int lastSpace = text.lastIndexOf(' ', endIndex);
                if (lastSpace > startIndex) {
                    endIndex = lastSpace;
                }
            }

            resultList.add(text.substring(startIndex, endIndex).trim());

            startIndex = endIndex;

            while (startIndex < text.length() && Character.isWhitespace(text.charAt(startIndex))) {
                startIndex++;
            }
        }

        String[] resultArray = resultList.toArray(new String[0]);
        return resultArray;
    }

    private static String[] appendEmptyLine(String[] lines) {
        String[] newLines = new String[lines.length + 1];

        System.arraycopy(lines, 0, newLines, 0, lines.length);
        newLines[lines.length] = "";

        return newLines;
    }
}
