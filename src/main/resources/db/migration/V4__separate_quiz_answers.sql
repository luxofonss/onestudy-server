ALTER TABLE quiz_attempts DROP COLUMN answers;

-- New quiz_answers table
CREATE TABLE IF NOT EXISTS quiz_answers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_attempt_id UUID NOT NULL,
    question_id UUID NOT NULL,
    selected_answers JSONB,
    fill_in_blanks_answers JSONB,
    answer_text TEXT,
    is_correct BOOLEAN,
    score_achieved INTEGER,
    time_taken INTEGER,
    audio_url TEXT,
    answered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_quiz_attempts_quiz_id ON quiz_attempts (quiz_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_user_id ON quiz_attempts (user_id);
CREATE INDEX IF NOT EXISTS idx_quiz_answers_quiz_attempt_id ON quiz_answers (quiz_attempt_id);