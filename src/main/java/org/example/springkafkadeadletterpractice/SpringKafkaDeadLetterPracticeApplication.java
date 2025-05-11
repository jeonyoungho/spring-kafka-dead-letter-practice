package org.example.springkafkadeadletterpractice;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.springkafkadeadletterpractice.domain.stock.Stock;
import org.example.springkafkadeadletterpractice.domain.stock.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringKafkaDeadLetterPracticeApplication implements CommandLineRunner {

    private final StockRepository stockRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaDeadLetterPracticeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<Stock> stocks = Lists.newArrayList();
        for (long i = 1; i <= 700; i++) {
            stocks.add(Stock.create(i, 10));
        }

        stockRepository.saveAll(stocks);
    }
}
