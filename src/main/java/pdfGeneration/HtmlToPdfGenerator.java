package pdfGeneration;

import com.itextpdf.html2pdf.HtmlConverter;
import dto.Pdfdto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HtmlToPdfGenerator {
    public String processTemplate(Pdfdto dto) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Times New Roman; font-size: 12}
                        .header { color: #2c3e50; text-align: center; font-size: 12 ;font-family: Arial }
                        .content { margin: 20px; }
                        .field { margin-bottom: 5px; text-indent: 30px }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h4>Договор №${contractNumber} на выпуск и обслуживание карты</h4>
                    </div>
                    <div class="content">
                        <div class="field">
                           Коммерческий банк "A-Sber" именуемый в дальнейшем БАНК, в лице ${empName} действующего на основании Устава,
                           с одной стороны и именуемый в дальнейшем КЛИЕНТ, в лице ${name} заключили
                           настоящий договор о нижеследующем:
                           </div>
                        <div class="field">
                            Номер счета к которому привязана крата: ${accountNumber}
                        </div>
                        <div class="field">
                            Класс карты: ${cardType}
                        </div>
                        <div class="field">
                            Название карты: ${productName}
                        </div>
                        <div class="field">
                            Дата заключения договора: ${dateOfStart}
                        </div>
                         <div class="field">
                            Дата окончания действия договора: ${dateOfEnd}
                        </div>
                    </div>
                </body>
                </html>
                """;
        Map<String, Object> data = getMap(dto);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}",
                    entry.getValue() != null ? entry.getValue().toString() : "");
        }

        return template;
    }

    private static Map<String, Object> getMap(Pdfdto dto) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = dto.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(dto));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return map;
    }

    public void generatePdf(Pdfdto data, String outputPath) throws IOException {
        String htmlContent = processTemplate(data);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            HtmlConverter.convertToPdf(htmlContent, fos);
        }
    }
    public static void main(String[] args) {
        HtmlToPdfGenerator generator = new HtmlToPdfGenerator();
        Pdfdto pdfdto = new Pdfdto();
        pdfdto.setName("Иванов Иван Павлович");
        pdfdto.setAccountNumber("3456789012831");
        pdfdto.setDateOfStart("23.10.2025");
        pdfdto.setDateOfEnd("23.10.2030");
        pdfdto.setProductName("A-sber");
        pdfdto.setCardType("Платиновая");
        pdfdto.setContractNumber("1231124");
        pdfdto.setEmpName("Иванов Антон Игоревич");

        try {
            generator.generatePdf(pdfdto, pdfdto.getContractNumber()+".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
