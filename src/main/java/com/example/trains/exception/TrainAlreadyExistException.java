package com.example.trains.exception;

public class TrainAlreadyExistException extends Exception {
    public TrainAlreadyExistException(String s) {
        super(s);
    }
}