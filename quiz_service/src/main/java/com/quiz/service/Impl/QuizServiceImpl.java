package com.quiz.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.entities.Quiz;
import com.quiz.repository.QuizRepository;
import com.quiz.service.QuizService;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    public QuizServiceImpl (QuizRepository quizRepository){
        this.quizRepository = quizRepository;
    }
    


    @Override
    public Quiz add(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> get() {
        return quizRepository.findAll();
    }
       

    @Override
    public Quiz get(Long id) {
       return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }



}
