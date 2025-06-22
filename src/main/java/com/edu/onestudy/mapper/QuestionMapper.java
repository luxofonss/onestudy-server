package com.edu.onestudy.mapper;

import com.edu.onestudy.dto.quiz.OptionDto;
import com.edu.onestudy.dto.quiz.QuestionDto;
import com.edu.onestudy.entity.Question;
import com.edu.onestudy.entity.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question questionDtoToQuestion(QuestionDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateQuestionFromDto(QuestionDto dto, @MappingTarget Question entity);

    QuestionOption optionDtoToOption(OptionDto dto);

    void updateOptionFromDto(OptionDto dto, @MappingTarget QuestionOption entity);

}
