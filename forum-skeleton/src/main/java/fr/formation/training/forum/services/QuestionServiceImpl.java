package fr.formation.training.forum.services;

import java.time.LocalDateTime;
import java.util.Optional;

import fr.formation.training.forum.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import fr.formation.training.forum.dtos.*;
import fr.formation.training.forum.entities.*;
import fr.formation.training.forum.repositories.*;

@Service
public class QuestionServiceImpl extends AbstractService
	implements QuestionService {

    private final QuestionJpaRepository questions;

    private final TechnologyJpaRepository technologies;

    public QuestionServiceImpl(QuestionJpaRepository questions,
	    TechnologyJpaRepository technologies) {
	this.questions = questions;
	this.technologies = technologies;
    }

    @Override
    public IdentifierDto add(QuestionAddDto dto) {
	Question question = getMapper().map(dto, Question.class);
	question.setQuestionDate(LocalDateTime.now());
	setTechnology(question, dto.getTechnologyId());
	questions.save(question);
	return new IdentifierDto(question.getId());
    }

    @Override
    public void update(Long id, QuestionUpdateDto dto) {
		Optional<Question> optional = questions.findById(id);
		if (optional.isEmpty()) {
			throw new ResourceNotFoundException();
		} else {
			Question question = optional.get();
			getMapper().map(dto, question);
			setTechnology(question, dto.getTechnologyId());
			questions.save(question);
		}
    }

    @Override
    public DiscussionViewDto getDiscussion(Long id) {
	QuestionViewDto questionView = questions.findProjectedById(id);
	return new DiscussionViewDto(questionView);
    }

    private void setTechnology(Question question, Long technologyId) {
	Technology technology = technologies.getOne(technologyId);
	question.setTechnology(technology);
    }
}
