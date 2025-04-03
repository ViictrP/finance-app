package com.victor.financeapp.backend.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreditCardController {

    @QueryMapping
    public List<Integer> findCreditCards() {
        return new ArrayList<>();
    }
}
