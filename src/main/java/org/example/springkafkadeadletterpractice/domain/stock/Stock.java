package org.example.springkafkadeadletterpractice.domain.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "amount", nullable = false)
    private long amount;

    public static Stock create(Long productId, long amount) {
        Stock stock = new Stock();
        stock.productId = productId;
        stock.amount = amount;
        return stock;
    }

    public void decreaseStock() {
        if (amount <= 0) {
            return;
        }
        amount--;
    }
}
