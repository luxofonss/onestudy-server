CREATE TABLE IF NOT EXISTS saved_quizzes (
    user_id UUID NOT NULL,
    quiz_id UUID NOT NULL,
    saved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, quiz_id)
    );

CREATE INDEX IF NOT EXISTS idx_saved_quizzes_user_id ON saved_quizzes (user_id);
CREATE INDEX IF NOT EXISTS idx_saved_quizzes_quiz_id ON saved_quizzes (quiz_id);