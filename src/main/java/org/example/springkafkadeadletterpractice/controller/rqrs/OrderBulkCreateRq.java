package org.example.springkafkadeadletterpractice.controller.rqrs;

import java.util.List;

public record OrderBulkCreateRq(List<OrderCreateRq> orders) {}
