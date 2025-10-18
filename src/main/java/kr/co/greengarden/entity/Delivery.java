package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "TB_DELIVERY")
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DELIVERY_ID")
    private int deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderNo")
    private Order order;

    @Column(name="INVOICE_NO") // 송장
    private String invoiceNo;

    @Column
    private String status;

    @CreationTimestamp
    @Column
    private LocalDate cratedAt;

    @Column
    private String note;
}
