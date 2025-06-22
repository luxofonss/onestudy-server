package com.edu.onestudy.mapper;

import com.edu.onestudy.dto.quiz.CreateQuizDto;
import com.edu.onestudy.entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    Quiz createQuizDtoToQuiz(CreateQuizDto request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateQuizFromDto(CreateQuizDto dto, @MappingTarget Quiz entity);

}
