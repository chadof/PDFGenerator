package dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pdfdto {
    private String name;
    private String contractNumber;
    private String empName;
    private String productName;
    private String productType;
    private String cardType;
    private String paymentSystem;
    private String accountNumber;
    private String dateOfStart;
    private String dateOfEnd;
}
