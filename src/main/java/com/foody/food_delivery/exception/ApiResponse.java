package com.foody.food_delivery.exception;

public record ApiResponse<T>(T data, String message, int status) {}
