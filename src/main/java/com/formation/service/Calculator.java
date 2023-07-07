package com.formation.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
@Service
public class Calculator {
	
	public int sum(int a, int b) {
		return a+b;
	}

	public int mutiply(int a, int b) {
		return a*b;
	}
}
