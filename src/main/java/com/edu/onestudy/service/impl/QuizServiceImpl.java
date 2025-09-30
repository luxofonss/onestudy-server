package com.edu.onestudy.service.impl;

import com.edu.onestudy.constant.ErrorConstant;
import com.edu.onestudy.constant.QuestionType;
import com.edu.onestudy.constant.QuizStatus;
import com.edu.onestudy.constant.UrlConstant;
import com.edu.onestudy.dto.BaseCreateUpdateResponse;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyResponseDto;
import com.edu.onestudy.dto.quiz.CreateQuizDto;
import com.edu.onestudy.dto.quiz.QuestionDto;
import com.edu.onestudy.dto.quiz.SubmitAnswerRequest;
import com.edu.onestudy.entity.*;
import com.edu.onestudy.exception.BusinessException;
import com.edu.onestudy.mapper.QuestionMapper;
import com.edu.onestudy.mapper.QuizMapper;
import com.edu.onestudy.repository.*;
import com.edu.onestudy.service.QuizService;
import com.edu.onestudy.service.ResourceService;
import com.edu.onestudy.thirdparty.pronunciation_svc.PronunciationServiceImpl;
import com.edu.onestudy.utils.FileUtils;
import com.edu.onestudy.utils.JsonUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.edu.onestudy.constant.UrlConstant.TransactionLogClientServiceApi.GET_LIST_TRANSACTION;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizMapper quizMapper;

    private final QuestionMapper questionMapper;

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final QuizAttemptRepository quizAttemptRepository;

    private final QuizAnswerRepository quizAnswerRepository;

    private final UserRepository userRepository;

    private final SavedQuizRepository savedQuizRepository;

    private final PronunciationServiceImpl pronunciationService;

    private final ResourceService resourceService;

    public QuizServiceImpl(QuizMapper quizMapper,
                           QuestionMapper questionMapper,
                           QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           QuizAttemptRepository quizAttemptRepository,
                           QuizAnswerRepository quizAnswerRepository,
                           UserRepository userRepository,
                           SavedQuizRepository savedQuizRepository,
                           PronunciationServiceImpl pronunciationService,
                           ResourceService resourceService) {
        this.quizMapper = quizMapper;
        this.questionMapper = questionMapper;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.userRepository = userRepository;
        this.savedQuizRepository = savedQuizRepository;
        this.pronunciationService = pronunciationService;
        this.resourceService = resourceService;
    }

    @Override
    public BaseCreateUpdateResponse createQuiz(CreateQuizDto request, UUID authorId) {
        Quiz q = quizMapper.createQuizDtoToQuiz(request);
        q.setAuthorId(authorId);
        quizRepository.save(q);
        return new BaseCreateUpdateResponse(q.getId().toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseCreateUpdateResponse updateQuiz(CreateQuizDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Quiz ID must be provided for update operation.");
        }
        // TODO: remove
        log.info("Updating quiz with ID: {}", UrlConstant.TransactionLogClientServiceApi.GET_TRANSACTION);
        log.info("Updating quiz with ID: {}", GET_LIST_TRANSACTION);
        UUID quizId = UUID.fromString(request.getId());
        Quiz existingQuiz = quizRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Quiz with ID " + request.getId() + " not found."));

        quizMapper.updateQuizFromDto(request, existingQuiz);
        existingQuiz.setUpdatedAt(LocalDateTime.now());
        if (request.getIsPublic()) {
            existingQuiz.setStatus(QuizStatus.PUBLIC);
        } else {
            existingQuiz.setStatus(QuizStatus.DRAFT);
        }

        List<Question> existingQuestions = questionRepository.findByQuizId(request.getId());
        Map<UUID, Question> existingQuestionMap = existingQuestions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<Question> questionsToSaveOrUpdate = new ArrayList<>();
        Set<UUID> questionIdsInDto = request.getQuestions().stream()
                .filter(qDto -> qDto.getId() != null)
                .map(qDto -> UUID.fromString(qDto.getId()))
                .collect(Collectors.toSet());

        for (QuestionDto qDto : request.getQuestions()) {
            if (qDto.getId() == null) {
                Question newQuestion = questionMapper.questionDtoToQuestion(qDto);
                newQuestion.setQuizId(quizId);
                newQuestion.setCreatedAt(LocalDateTime.now());
                newQuestion.setUpdatedAt(LocalDateTime.now());
                if (!CollectionUtils.isEmpty(newQuestion.getOptions())) {
                    for (QuestionOption option : newQuestion.getOptions()) {
                        option.setId(UUID.randomUUID());
                    }
                }
                questionsToSaveOrUpdate.add(newQuestion);
            } else {
                UUID qId = UUID.fromString(qDto.getId());
                Question questionToUpdate = existingQuestionMap.get(qId);
                if (questionToUpdate != null) {
                    questionMapper.updateQuestionFromDto(qDto, questionToUpdate);
                    questionToUpdate.setUpdatedAt(LocalDateTime.now());
                    // Ensure options are updated correctly
                    if (!CollectionUtils.isEmpty(questionToUpdate.getOptions())) {
                        for (QuestionOption option : questionToUpdate.getOptions()) {
                            if (option.getId() == null) {
                                option.setId(UUID.randomUUID());
                            }
                        }
                    }
                    questionsToSaveOrUpdate.add(questionToUpdate);
                }
            }
        }

        List<Question> questionsToDelete = existingQuestions.stream()
                .filter(eq -> !questionIdsInDto.contains(eq.getId()))
                .collect(Collectors.toList());

        if (!questionsToDelete.isEmpty()) {
            questionRepository.deleteAll(questionsToDelete);
        }
        if (!questionsToSaveOrUpdate.isEmpty()) {
            questionRepository.saveAll(questionsToSaveOrUpdate);
        }

        existingQuiz.setQuestionCount(questionRepository.countByQuizId(quizId.toString()));

        quizRepository.save(existingQuiz);
        return new BaseCreateUpdateResponse(existingQuiz.getId().toString());
    }

    @Override
    public List<Quiz> getAllPublicQuizzes() {
        List<Quiz> quizzes = quizRepository.findAllPublic();
        getQuizAuthor(quizzes);
        return quizzes;
    }

    @Override
    public List<Quiz> getMyQuizzes(UUID id) {
        List<Quiz> quizzes = quizRepository.findByUserId(id);
        getQuizAuthor(quizzes);
        return quizzes;
    }

    @Override
    public Quiz getById(String id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_FOUND));
        Set<Question> questions = new HashSet<>(questionRepository.findByQuizId(id));
        quiz.setQuestions(questions);
        getQuizAuthor(Collections.singletonList(quiz));
        return quiz;
    }

    @Override
    public void deleteQuiz(String id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_FOUND));
        quiz.setDeletedAt(LocalDateTime.now());
        quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public BaseCreateUpdateResponse startQuiz(String quizId, UUID userId) {
        quizRepository.findById(quizId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_FOUND));

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuizId(UUID.fromString(quizId));
        quizAttempt.setUserId(userId);
        quizAttempt.setScore(BigDecimal.ZERO);
        quizAttempt.setCorrectAnswers(0);
        quizAttempt.setTimeSpent(0L);
        quizAttempt.setCompletedAt(null);
        quizAttempt.setPassed(false);

        QuizAttempt savedAttempt = quizAttemptRepository.save(quizAttempt);
        return new BaseCreateUpdateResponse(savedAttempt.getId().toString());
    }

    @Override
    @Transactional
    public BaseCreateUpdateResponse submitQuizQuestion(String attemptId, SubmitAnswerRequest request, UUID id) {
        UUID questionId = UUID.fromString(request.getQuestionId());

        QuizAttempt quizAttempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_ATTEMPT_NOT_FOUND));

        // Ensure the quiz attempt is not already completed
        if (quizAttempt.getCompletedAt() != null) {
            throw new BusinessException(ErrorConstant.QUIZ_ATTEMPT_SUBMITTED);
        }

        Quiz quiz = quizRepository.findById(quizAttempt.getQuizId().toString()).orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_FOUND));

        // calculate if time is exceeded by field created_at and quiz.getTimeLimit()
        if (quiz.getTimeLimit() != null && quizAttempt.getCreatedAt() != null) {
            LocalDateTime start = quizAttempt.getCreatedAt();
            LocalDateTime end = LocalDateTime.now();
            long timeSpent = Duration.between(start, end).toMinutes();
            if (timeSpent > quiz.getTimeLimit()) {
                quizAttempt.setTimeSpent(timeSpent);
                quizAttempt.setCompletedAt(quizAttempt.getCreatedAt().plusMinutes(timeSpent));
                quizAttemptRepository.save(quizAttempt);
                throw new BusinessException(ErrorConstant.QUIZ_TIME_LIMIT_EXCEEDED);
            }
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUESTION_NOT_FOUND));

        if (!Objects.equals(question.getQuizId(), quizAttempt.getQuizId())) {
            throw new BusinessException(ErrorConstant.QUESTION_DOES_NOT_BELONG_TO_QUIZ);
        }

        // Prepare QuizAnswer entity
        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setQuestionId(questionId);

        if (QuestionType.MULTIPLE_CHOICE.name().equals(question.getType()) && !CollectionUtils.isEmpty(question.getOptions())) {
            List<QuestionOption> options = question.getOptions().stream()
                    .filter(option -> option.getId() != null && request.getSelectedOptions().contains(option.getId().toString()))
                    .collect(Collectors.toList());
            quizAnswer.setSelectedAnswers(options);
        }
        quizAnswer.setFillInBlanksAnswers(request.getFillInBlanksAnswers());
        quizAnswer.setAnswerText(request.getAnswerText());
        quizAnswer.setTimeTaken(request.getTimeTaken());
        quizAnswer.setAudioUrl(request.getAudioUrl());

        boolean isCorrect = false;
        Integer scoreAchieved = 0;

        // Determine if the answer is correct based on question type
        switch (question.getType().toLowerCase()) {
            case "multiple_choice":
                List<String> userSelectedValues = request.getSelectedOptions() != null ?
                        request.getSelectedOptions().stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()) : new ArrayList<>();

                List<String> correctOptionValues = question.getCorrectAnswer() != null ?
                        question.getOptions().stream().filter(QuestionOption::getIsCorrect).map(i -> i.getId().toString()).sorted().toList() : new ArrayList<>();
                userSelectedValues.sort(String::compareTo);

                isCorrect = userSelectedValues.equals(correctOptionValues);
                break;
            case "true_false":
                isCorrect = (request.getUserAnswerTrueFalse() != null &&
                        request.getUserAnswerTrueFalse().equals(question.getTrueFalseAnswer()));
                break;
            case "fill_in_the_blank":
                List<String> userFillInBlanks = request.getFillInBlanksAnswers() != null ?
                        request.getFillInBlanksAnswers().stream()
                                .filter(Objects::nonNull)
                                .map(i -> i.toLowerCase().trim())
                                .collect(Collectors.toList()) : new ArrayList<>();

                List<String> correctBlanks = question.getCorrectBlanks() != null ?
                        question.getCorrectBlanks().stream()
                                .filter(Objects::nonNull)
                                .map(i -> i.toLowerCase().trim())
                                .collect(Collectors.toList()) : new ArrayList<>();

                Collections.sort(userFillInBlanks);
                Collections.sort(correctBlanks);

                isCorrect = userFillInBlanks.equals(correctBlanks);
                break;
            case "short_answer":
                isCorrect = (request.getAnswerText() != null &&
                        question.getCorrectAnswer() != null &&
                        !question.getCorrectAnswer().isEmpty() &&
                        question.getCorrectAnswer().getFirst().equalsIgnoreCase(request.getAnswerText().trim()));
                break;
            case "pronunciation":
                if (request.getAudioUrl() == null || request.getAudioUrl().isEmpty()) {
                    break;
                }
                File audioFile = resourceService.downloadResourceFromUrl(request.getAudioUrl());
                if (audioFile == null || !audioFile.exists()) {
                    break;
                }
                String base64File = "data:audio/ogg;;base64," + FileUtils.convertToBase64(audioFile);
                if (base64File == null) {
                    break;
                }
                try {
                    PronunciationAccuracyResponseDto pronunAccuracy = pronunciationService.getAccuracy(
                            PronunciationAccuracyRequestDto.builder()
                                    .base64Audio(base64File)
                                    .text(question.getPronunciationText())
                                    .build()
                    );
                    isCorrect = pronunAccuracy.getPronunciationAccuracy() >= question.getAcceptRate();
                    quizAnswer.setAnswerText(JsonUtils.toJsonString(pronunAccuracy));
                } catch (Exception e) {
                    log.error("Error while checking pronunciation accuracy: {}", e.getMessage());
                }
                break;

            default:
                System.err.println("Unknown question type: " + question.getType());
        }

        if (isCorrect) {
            scoreAchieved = question.getPoints() != null ? question.getPoints() : 0;
        }

        quizAnswer.setCorrect(isCorrect);
        quizAnswer.setScoreAchieved(scoreAchieved);

        List<QuizAnswer> currentAnswers = quizAnswerRepository.findByQuizAttemptId(attemptId);
        logger.info(currentAnswers);

        for (int i = 0; i < currentAnswers.size(); i++) {
            if (currentAnswers.get(i).getQuestionId().equals(questionId)) {
                QuizAnswer oldAnswer = currentAnswers.get(i);
                quizAttempt.setScore(quizAttempt.getScore().subtract(BigDecimal.valueOf(oldAnswer.getScoreAchieved())));
                if (oldAnswer.isCorrect()) {
                    quizAttempt.setCorrectAnswers(quizAttempt.getCorrectAnswers() - 1);
                }
                quizAnswerRepository.delete(oldAnswer);
                break;
            }
        }

        quizAnswer.setQuizAttemptId(UUID.fromString(attemptId));
        // check if question submitted before
        quizAnswerRepository.save(quizAnswer);

        quizAttempt.setScore(quizAttempt.getScore().add(BigDecimal.valueOf(scoreAchieved)));
        if (isCorrect) {
            quizAttempt.setCorrectAnswers(quizAttempt.getCorrectAnswers() + 1);
        }

        // update total score
        quizAttemptRepository.save(quizAttempt);

        return new BaseCreateUpdateResponse(quizAnswer.getId().toString());
    }

    @Override
    public List<Quiz> getMyQuizAttempts(String userId) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByUserId(UUID.fromString(userId));
        if (CollectionUtils.isEmpty(attempts)) {
            return Collections.emptyList();
        }

        Set<String> quizId = attempts.stream()
                .map(QuizAttempt::getQuizId)
                .map(UUID::toString)
                .collect(Collectors.toSet());

        List<Quiz> quizzes = quizRepository.findByIdIn(quizId);
        if (CollectionUtils.isEmpty(quizzes)) {
            return Collections.emptyList();
        }

        getQuizAuthor(quizzes);

        Map<UUID, List<QuizAttempt>> attemptMap = attempts.stream()
                .collect(Collectors.groupingBy(QuizAttempt::getQuizId));
        for (Quiz quiz : quizzes) {
            quiz.setQuizAttempts(new HashSet<>(attemptMap.get(quiz.getId())));
        }

        return quizzes;
    }

    @Override
    public QuizAttempt getAttempt(String attemptId, UUID userId) {
        // TODO: check permission
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId).orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_ATTEMPT_NOT_FOUND));
        Quiz quiz = this.getById(attempt.getQuizId().toString());
        getQuizAuthor(Collections.singletonList(quiz));
        attempt.setQuiz(quiz);
        List<QuizAnswer> answers = quizAnswerRepository.findByQuizAttemptId(attemptId);
        attempt.setAnswers(answers);

        return attempt;
    }

    @Override
    public void submitQuizComplete(String attemptId, UUID id) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId).orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_ATTEMPT_NOT_FOUND));
        if (!id.equals(attempt.getUserId())) {
            throw new BusinessException(ErrorConstant.QUIZ_ATTEMPT_NOT_BELONG_TO_USER);
        }

        if (attempt.getCompletedAt() != null) {
            throw new BusinessException(ErrorConstant.QUIZ_ATTEMPT_SUBMITTED);
        }
        attempt.setCompletedAt(LocalDateTime.now());
        quizAttemptRepository.save(attempt);
        log.info("saved quiz attempt with id: {}", attemptId);
    }

    @Override
    public Quiz getQuizStatsById(String id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_FOUND));
        getQuizAuthor(Collections.singletonList(quiz));
        List<Question> questions = questionRepository.findByQuizId(id);
        quiz.setQuestions(new HashSet<>(questions));
        List<QuizAttempt> attempts = quizAttemptRepository.findByQuizId(quiz.getId().toString());

        attempts.forEach(attempt -> {
            userRepository.getUserById(attempt.getUserId().toString()).ifPresent(attempt::setUser);
        });

        quiz.setQuizAttempts(new HashSet<>(attempts));
        List<User> savedUsers = savedQuizRepository.findAllByQuizId(id)
                .stream()
                .map(savedQuiz -> userRepository.getUserById(savedQuiz.getUserId().toString()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
        quiz.setSavedByUsers(new HashSet<>(savedUsers));

        return quiz;
    }

    @Override
    public void saveQuiz(String quizId, UUID userId) {
        savedQuizRepository.saveQuiz(quizId, userId.toString());
    }

    @Override
    public List<Quiz> getSavedQuizzes(UUID userId) {
        List<UserSavedQuiz> savedQuizzes = savedQuizRepository.findAllByUserId(userId.toString());
        if (CollectionUtils.isEmpty(savedQuizzes)) {
            return Collections.emptyList();
        }

        Set<String> quizIds = savedQuizzes.stream()
                .map(UserSavedQuiz::getQuizId)
                .map(UUID::toString)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(quizIds)) {
            return Collections.emptyList();
        }

        List<Quiz> quizzes = quizRepository.findByIdIn(quizIds);
        if (CollectionUtils.isEmpty(quizzes)) {
            return Collections.emptyList();
        }

        getQuizAuthor(quizzes);

        return quizzes;
    }

    @Override
    public void unsaveQuiz(String quizId, UUID userId) {
        UserSavedQuiz savedQuiz = savedQuizRepository.findAllByQuizId(quizId)
                .stream()
                .filter(q -> q.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorConstant.QUIZ_NOT_SAVED));

        savedQuiz.setSavedAt(LocalDateTime.now());
        savedQuiz.setDeletedAt(null);
        savedQuizRepository.update(savedQuiz);
    }

    @Override
    public List<UserSavedQuiz> getUserSavedQuiz(String quizId, UUID userId) {
        List<UserSavedQuiz> savedQuizzes = savedQuizRepository.findAllByUserId(userId.toString());
        savedQuizzes.forEach(savedQuiz -> {
            userRepository.getUserById(savedQuiz.getUserId().toString()).ifPresent(savedQuiz::setUser);
        });
        return savedQuizzes;
    }

    private void getQuizAuthor(List<Quiz> quizzes) {
        quizzes.forEach(q -> {
            if (q.getAuthorId() != null) {
                q.setAuthor(userRepository.getUserById(q.getAuthorId().toString()).orElse(null));
            }
        });
    }

}
